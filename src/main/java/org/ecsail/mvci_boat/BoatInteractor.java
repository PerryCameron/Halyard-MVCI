package org.ecsail.mvci_boat;

import javafx.application.Platform;
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
import org.ecsail.static_calls.HandlingTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Supplier;

public class BoatInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(BoatInteractor.class);
    private final BoatModel boatModel;
    private final SettingsRepository settingsRepo;
    private final NotesRepository noteRepo;
    private final BoatRepository boatRepo;
    private final MembershipRepository membershipRepo;
    private final Sftp scp;

    public BoatInteractor(BoatModel boatModel, Connections connections) {
        this.boatModel = boatModel;
        settingsRepo = new SettingsRepositoryImpl(connections.getDataSource());
        boatRepo = new BoatRepositoryImpl(connections.getDataSource());
        noteRepo = new NotesRepositoryImpl(connections.getDataSource());
        membershipRepo = new MembershipRepositoryImpl(connections.getDataSource());
        scp = connections.getScp();
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
        Platform.runLater(() ->
                boatModel.setImages((ArrayList<BoatPhotosDTO>) boatRepo.getImagesByBoatId(boatModel.getBoatListDTO().getBoatId()))
        );
    }

    public boolean executeWithHandling(Supplier<Integer> supplier) {
        try {
            HandlingTools.savedToIndicator(supplier.get() == 1, boatModel.getMainModel());
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
            logger.info("Sending " + boatPhotosDTO.getFilename() + " to remote server");
            scp.changeGroup(IMAGE_REMOTE_PATH + boatPhotosDTO.getFilename(), 1006);
            // update SQL
            executeWithHandling(() -> boatRepo.insert(boatPhotosDTO));
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
        logger.info("Setting " + boatModel.getSelectedImage().getFilename() + " to default");
        executeWithHandling(() -> boatRepo.setAllDefaultImagesToFalse(boatModel.getBoatListDTO().getBoatId()));
        executeWithHandling(() -> boatRepo.setDefaultImageTrue(boatModel.getSelectedImage().getId()));
    }

    public void deleteImage() {
        // find if current image is default
        boolean oldImageWasDefault = boatModel.getSelectedImage().isDefault();
        // delete remote
        scp.deleteFile(IMAGE_REMOTE_PATH + boatModel.getSelectedImage().getFilename());
        // delete local
        FileIO.deleteFile(IMAGE_LOCAL_PATH + boatModel.getSelectedImage().getFilename());
        // remove database entry
        BoatPhotosDTO boatPhotosDTO = boatModel.getSelectedImage();
        // delete entry in database, return true if successful
        if(executeWithHandling(() -> boatRepo.delete(boatPhotosDTO))) {
            // move to last image in array if any exist
            if(!boatModel.getImages().isEmpty()) moveToLastImageInArray();
            // if old image was default set the new one as default
            if(oldImageWasDefault) updateSelectedImageAsDefault();
            // remove from array
            boatModel.getImages().remove(boatPhotosDTO);
        }
    }

    private void updateSelectedImageAsDefault() {
        boatModel.getSelectedImage().setDefault(true);
        executeWithHandling(() -> boatRepo.update(boatModel.getSelectedImage()));
    }

    private void moveToLastImageInArray() {
        boatModel.setSelectedImage(boatModel.getImages().get(boatModel.getImages().size() - 1));
        String localFile = BOAT_LOCAL_PATH + boatModel.getSelectedImage().getFilename();
        Image image = new Image("file:" + localFile);
        boatModel.getImageView().setImage(image);
    }

    public void downloadImage() { // if it exists remote but not local
        logger.info(boatModel.getSelectedImage().getFilename() + " does not exist locally, downloading...");
        scp.getFile(IMAGE_REMOTE_PATH + boatModel.getSelectedImage().getFilename(),
                IMAGE_LOCAL_PATH + boatModel.getSelectedImage().getFilename());
        Image image = new Image("file:" + IMAGE_LOCAL_PATH + boatModel.getSelectedImage().getFilename());
        Platform.runLater(() -> boatModel.getImageView().setImage(image));
    }
}
