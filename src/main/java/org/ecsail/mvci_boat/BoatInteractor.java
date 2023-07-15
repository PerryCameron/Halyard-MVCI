package org.ecsail.mvci_boat;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.connection.Connections;
import org.ecsail.connection.Sftp;
import org.ecsail.dto.BoatPhotosDTO;
import org.ecsail.dto.DbBoatSettingsDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.NotesDTO;
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
import java.util.ArrayList;
import java.util.function.Supplier;

public class BoatInteractor {

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

    public void updateBoat() {
        executeWithHandling(() -> boatRepo.update(boatModel.getBoatListDTO()));
    }

    public void updateNote() {
        executeWithHandling(() -> noteRepo.update(boatModel.getSelectedNote()));
    }

    public void deleteOwner() {
        if(executeWithHandling(() -> boatRepo.deleteBoatOwner(boatModel.getSelectedOwner())))
            System.out.println("Remove from tableView code goes here");
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

    public void addImage() {
        System.out.println("adding image");
    }

    public void setImageAsDefault() {
        System.out.println("setting image as default");
    }

    public void deleteImage() {
        System.out.println("Deleting Image");
    }

    public void addOwner() {
        System.out.println("adding owner");
    }

    public BooleanProperty getConfirmation() {
        return boatModel.confirmedProperty();
    }



}
