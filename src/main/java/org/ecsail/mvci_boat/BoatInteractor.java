package org.ecsail.mvci_boat;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.ecsail.connection.Connections;
import org.ecsail.connection.Sftp;
import org.ecsail.dto.*;
import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.repository.implementations.BoatRepositoryImpl;
import org.ecsail.repository.implementations.MembershipRepositoryImpl;
import org.ecsail.repository.implementations.NotesRepositoryImpl;
import org.ecsail.repository.implementations.SettingsRepositoryImpl;
import org.ecsail.repository.interfaces.BoatRepository;
import org.ecsail.repository.interfaces.MembershipRepository;
import org.ecsail.repository.interfaces.NotesRepository;
import org.ecsail.repository.interfaces.SettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Supplier;

public class BoatInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(BoatInteractor.class);
    private final BoatModel boatModel;
    private final DataSource dataSource;
    private final SettingsRepository settingsRepo;
    private final NotesRepository noteRepo;
    private final BoatRepository boatRepo;
    private final MembershipRepository membershipRepo;
    private Sftp scp;

    public BoatInteractor(BoatModel boatModel, Connections connections) {
        this.boatModel = boatModel;
        this.dataSource = connections.getDataSource();
        settingsRepo = new SettingsRepositoryImpl(connections.getDataSource());
        boatRepo = new BoatRepositoryImpl(connections.getDataSource());
        noteRepo = new NotesRepositoryImpl(connections.getDataSource());
        membershipRepo = new MembershipRepositoryImpl(connections.getDataSource());
        scp = connections.getScp();
    }

    protected void savedToIndicator(boolean returnOk) { // updates status lights
        if(returnOk) boatModel.getMainModel().getLightAnimationMap().get("receiveSuccess").playFromStart();
        else boatModel.getMainModel().getLightAnimationMap().get("receiveError").playFromStart();
    }

    protected void retrievedFromIndicator(boolean returnOk) { // updates status lights
        if(returnOk) boatModel.getMainModel().getLightAnimationMap().get("transmitSuccess").playFromStart();
        else boatModel.getMainModel().getLightAnimationMap().get("transmitError").playFromStart();
    }

    public void getBoatSettings() {
        boatModel.setBoatSettings((ArrayList<DbBoatSettingsDTO>) settingsRepo.getBoatSettings());
    }

    public void setDataLoaded(boolean dataLoaded) {
        boatModel.setDataLoaded(dataLoaded);
    }

    public void getBoatNotes() {
        ObservableList<NotesDTO> notesDTOS =
                FXCollections.observableArrayList(noteRepo.getMemosByBoatId(boatModel.getBoatListDTO().getBoatId()));
        Platform.runLater(() -> boatModel.setNotesDTOS(notesDTOS));
    }

    public void getBoatOwners() {
        ObservableList<MembershipListDTO> boatOwnerDTOS =
                FXCollections.observableArrayList(membershipRepo.getMembershipByBoatId(boatModel.getBoatListDTO().getBoatId()));
        Platform.runLater(() -> boatModel.setBoatOwners(boatOwnerDTOS));
    }

    public void getBoatPhotos() {
        boatModel.setImages((ArrayList<BoatPhotosDTO>) boatRepo.getImagesByBoatId(boatModel.getBoatListDTO().getBoatId()));
    }

    public boolean executeWithHandling(Supplier<Integer> supplier) {
        try {
            if(supplier.get() == 1) savedToIndicator(true);
            else savedToIndicator(false);
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.error("IncorrectResultSizeDataAccessException: Expected 1 row but got multiple or none", e);
        } catch (DataAccessException e) {
            logger.error("DataAccessException: A database access error occurred", e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred", e);
        }
        return false;
    }

    public void insertNote() {
        NotesDTO notesDTO = new NotesDTO(boatModel.getBoatListDTO().getBoatId(), "B");
        executeWithHandling(() -> noteRepo.insertNote(notesDTO));
        Platform.runLater(() -> boatModel.getNotesDTOS().add(notesDTO));
    }

    public void insertOwner() {
        BoatOwnerDTO boatOwnerDTO = new BoatOwnerDTO(boatModel.getSelectedOwner().getMsId(), boatModel.getBoatListDTO().getBoatId());
        executeWithHandling(() -> boatRepo.insertOwner(boatOwnerDTO));
    }

    public void updateBoat() {
        executeWithHandling(() -> boatRepo.update(boatModel.getBoatListDTO()));
    }

    public void updateNote() {
        executeWithHandling(() -> noteRepo.update(boatModel.getSelectedNote()));
    }

    public void deleteOwner() {
        if(executeWithHandling(() -> boatRepo.deleteBoatOwner(boatModel.getSelectedOwner(), boatModel.getBoatListDTO())))
            boatModel.getBoatOwners().removeIf(owner -> owner.getMsId() == boatModel.getSelectedOwner().getMsId());
    }

    public void deleteNote() {
        if(executeWithHandling(() -> noteRepo.delete(boatModel.getSelectedNote()))) // if properly deleted from db
            Platform.runLater(() -> boatModel.getNotesDTOS().remove(boatModel.getSelectedNote())); // remove it from the UI
    }

    public void getBoatOwner() {
        try {
            MembershipListDTO membershipListDTO = membershipRepo.getMembershipByMembershipId(boatModel.getMembershipId());
            boatModel.setSelectedOwner(membershipListDTO);
        } catch (IncorrectResultSizeDataAccessException e) {
            boatModel.setSelectedOwner(new MembershipListDTO("Found", "Member Not"));
        } catch (DataAccessException e) {
            logger.error("DataAccessException: A database access error occurred", e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred", e);
        }
    }

    public void insertImage() {
        if(FileIO.isImageType(boatModel.getSelectedPath())) {
            // get number for photo
            int fileNumber = getNextFileNumberAvailable();
            // create filename
            String fileName = boatModel.getBoatListDTO().getBoatId()
                    + "_" + fileNumber + "." + FileIO.getFileExtension(boatModel.getSelectedPath());
            // create new POJO
            BoatPhotosDTO boatPhotosDTO = new BoatPhotosDTO(0,
                    boatModel.getBoatListDTO().getBoatId(), "", fileName, fileNumber, isFirstPic());
            // send file to remote server and change its group
            scp.sendFile(boatModel.getSelectedPath(), IMAGE_REMOTE_PATH + boatPhotosDTO.getFilename());
            scp.changeGroup(IMAGE_REMOTE_PATH + boatPhotosDTO.getFilename(), 1006);
            // update SQL
            Platform.runLater(() -> boatRepo.insert(boatPhotosDTO));
            // move a copy to local HD
            FileIO.copyFile(new File(boatModel.getSelectedPath()), new File(IMAGE_LOCAL_PATH + fileName));
            // resets everything to work correctly in GUI
            Platform.runLater(() -> {
                boatModel.setSelectedImage(resetImages());
                // Show our new image
                Image newImage = new Image("file:" + IMAGE_LOCAL_PATH + fileName);
                boatModel.getImageView().setImage(newImage);
            });
            // to update tableview in TabBoats);
            refreshBoatList();
        }
    }

    private void refreshBoatList() {  // this should update boat view when images are added probably should add all columns
        boatModel.getBoatListDTO().setNumberOfImages(boatModel.getImages().size());
    }

    private BoatPhotosDTO resetImages() {
        boatModel.getImages().clear();
        boatModel.getImages().addAll(boatRepo.getImagesByBoatId(boatModel.getBoatListDTO().getBoatId()));
        // sort them so one just created is last
        boatModel.getImages().sort(Comparator.comparingInt(BoatPhotosDTO::getId));
        // get the last
        return boatModel.getImages().get(boatModel.getImages().size() -1);
    }

    private boolean isFirstPic() {
        return boatModel.getImages().size() == 0;
    }

    private int getNextFileNumberAvailable() {
        if(boatModel.getImages().size() == 0) return 1;
        else boatModel.getImages().sort(Comparator.comparingInt(BoatPhotosDTO::getFileNumber));
        return boatModel.getImages().get(boatModel.getImages().size() - 1).getFileNumber() + 1;
    }

    public void setImageAsDefault() {
        System.out.println("setting image as default");
    }

    public void deleteImage() {
        // find if current image is default
        boolean oldImageWasDefault = boatModel.getSelectedImage().isDefault();
        // get id of selected image
        int id = boatModel.getSelectedImage().getId();
        // delete remote
        scp.deleteFile(IMAGE_REMOTE_PATH + boatModel.getSelectedImage().getFilename());
        // delete local
        FileIO.deleteFile(IMAGE_LOCAL_PATH + boatModel.getSelectedImage().getFilename());
        // remove database entry
        BoatPhotosDTO boatPhotosDTO = boatModel.getSelectedImage();
        if(executeWithHandling(() -> boatRepo.delete(boatPhotosDTO))) {
            // move to last image in array if any exist
            if(!boatModel.getImages().isEmpty()) {
                boatModel.setSelectedImage(boatModel.getImages().get(boatModel.getImages().size() - 1));
                String localFile = BOAT_LOCAL_PATH + boatModel.getSelectedImage().getFilename();
                Image image = new Image("file:" + localFile);
                boatModel.getImageView().setImage(image);
            }
            // if old image was default set the new one as default
            if(oldImageWasDefault) {
                boatModel.getSelectedImage().setDefault(true);
                boatRepo.update(boatModel.getSelectedImage());
            }
            boatModel.getImages().remove(boatPhotosDTO);
        }
    }

    public void addOwner() {
        System.out.println("adding owner");
    }

    public BooleanProperty getConfirmation() {
        return boatModel.confirmedProperty();
    }


}
