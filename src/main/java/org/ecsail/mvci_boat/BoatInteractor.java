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
import org.ecsail.static_tools.HandlingTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

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
        HandlingTools.queryForList(() -> {
            ObservableList<NotesDTO> notesDTOS = FXCollections.observableArrayList(noteRepo.getMemosByBoatId(boatModel.getBoatListDTO().getBoatId()));
            Platform.runLater(() -> boatModel.setNotesDTOS(notesDTOS));
        }, boatModel.getMainModel(), logger);
    }

    public void getBoatOwners() {
        HandlingTools.queryForList(() -> {
        ObservableList<MembershipListDTO> boatOwnerDTOS = FXCollections.observableArrayList(membershipRepo.getMembershipByBoatId(boatModel.getBoatListDTO().getBoatId()));
        Platform.runLater(() -> boatModel.setBoatOwners(boatOwnerDTOS));
        }, boatModel.getMainModel(), logger);
    }

    public void getBoatPhotos() {
        HandlingTools.queryForList(() -> {
        Platform.runLater(() ->
                boatModel.setImages((ArrayList<BoatPhotosDTO>) boatRepo.getImagesByBoatId(boatModel.getBoatListDTO().getBoatId()))
        );
        }, boatModel.getMainModel(), logger);
    }

    public void insertNote() {
        NotesDTO notesDTO = new NotesDTO(boatModel.getBoatListDTO().getBoatId(), "B");
        HandlingTools.executeQuery(() ->
                noteRepo.insertNote(notesDTO), boatModel.getMainModel(), logger);
        Platform.runLater(() -> boatModel.getNotesDTOS().add(notesDTO));
    }

    public void insertOwner() {
        BoatOwnerDTO boatOwnerDTO =
                new BoatOwnerDTO(boatModel.getSelectedOwner().getMsId(), boatModel.getBoatListDTO().getBoatId());
        HandlingTools.executeQuery(() ->
                boatRepo.insertOwner(boatOwnerDTO), boatModel.getMainModel(), logger);
    }

    public void updateBoat() {
        HandlingTools.executeQuery(() ->
                boatRepo.update(boatModel.getBoatListDTO()), boatModel.getMainModel(), logger);
    }

    public void updateNote() {
        HandlingTools.executeQuery(() ->
                noteRepo.update(boatModel.getSelectedNote()), boatModel.getMainModel(), logger);
    }

    public void deleteOwner() {
        if(HandlingTools.executeQuery(() ->
                boatRepo.deleteBoatOwner(boatModel.getSelectedOwner(), boatModel.getBoatListDTO()),
                boatModel.getMainModel(),
                logger))
            boatModel.getBoatOwners().remove(boatModel.getSelectedOwner());
    }

    public void deleteNote() {
        if(HandlingTools.executeQuery(() -> noteRepo.delete(boatModel.getSelectedNote()),
                boatModel.getMainModel(),
                logger))
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
            HandlingTools.executeQuery(() -> boatRepo.insert(boatPhotosDTO),
                    boatModel.getMainModel(),
                    logger);
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

    private BoatPhotosDTO resetImages() {  // TODO put in static call
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
        HandlingTools.executeQuery(() -> boatRepo.setAllDefaultImagesToFalse(boatModel.getBoatListDTO().getBoatId()),
                boatModel.getMainModel(),
                logger);
        HandlingTools.executeQuery(() -> boatRepo.setDefaultImageTrue(boatModel.getSelectedImage().getId()),
                boatModel.getMainModel(),
                logger);
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
        if(HandlingTools.executeQuery(() -> boatRepo.delete(boatPhotosDTO), boatModel.getMainModel(), logger)) {
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
        HandlingTools.executeQuery(() -> boatRepo.update(boatModel.getSelectedImage()), boatModel.getMainModel(), logger);
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
