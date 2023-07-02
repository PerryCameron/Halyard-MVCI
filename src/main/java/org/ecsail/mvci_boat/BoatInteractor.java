package org.ecsail.mvci_boat;

import javafx.application.Platform;
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

import javax.sql.DataSource;
import java.util.ArrayList;

public class BoatInteractor {

    private static final Logger logger = LoggerFactory.getLogger(BoatInteractor.class);
    private final BoatModel boatModel;
    private final DataSource dataSource;
    private final SettingsRepository settingsRepo;
    private final NotesRepository noteRepo;
    private final BoatRepository boatRepo;
    private final MembershipRepository membershipRepository;
    private Sftp scp;

    public BoatInteractor(BoatModel boatModel, Connections connections) {
        this.boatModel = boatModel;
        this.dataSource = connections.getDataSource();
        settingsRepo = new SettingsRepositoryImpl(connections.getDataSource());
        boatRepo = new BoatRepositoryImpl(connections.getDataSource());
        noteRepo = new NotesRepositoryImpl(connections.getDataSource());
        membershipRepository = new MembershipRepositoryImpl(connections.getDataSource());
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
                FXCollections.observableArrayList(membershipRepository.getMembershipByBoatId(boatModel.getBoatListDTO().getBoatId()));
        Platform.runLater(() -> boatModel.setBoatOwners(boatOwnerDTOS));
    }

    public void getBoatPhotos() {
        boatModel.setImages((ArrayList<BoatPhotosDTO>) boatRepo.getImagesByBoatId(boatModel.getBoatListDTO().getBoatId()));
    }


    public void updateBoat() {
        System.out.println("Updating boat");
        System.out.println(boatModel.getBoatListDTO());
    }

    public void updateNote() {
        try {
            noteRepo.update(boatModel.getSelectedNote());
        } catch (Exception e) {
            logger.error(e.getMessage());
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

    public void insertNote() {
        NotesDTO notesDTO = new NotesDTO(boatModel.getBoatListDTO().getBoatId(),"B");
        try {
            noteRepo.insertNote(notesDTO);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        Platform.runLater(() -> boatModel.getNotesDTOS().add(notesDTO));
    }

    public void addOwner() {
        System.out.println("adding owner");
    }

    public void deleteNote() {
        System.out.println("deleting note");
    }

    public void deleteOwner() {
        System.out.println("deleting owner");
    }
}
