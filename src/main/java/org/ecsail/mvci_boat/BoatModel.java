package org.ecsail.mvci_boat;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.dto.*;
import org.ecsail.mvci_main.MainModel;

import java.util.ArrayList;

public class BoatModel {

    private final MainModel mainModel;
    private final SimpleObjectProperty<BoatListDTO> boatListDTO = new SimpleObjectProperty<>();
    private ObservableList<MembershipListDTO> boatOwners = FXCollections.observableArrayList();
    private ArrayList<DbBoatSettingsDTO> boatSettings = new ArrayList<>();
    private final BooleanProperty dataLoaded = new SimpleBooleanProperty(false);
    private ArrayList<BoatPhotosDTO> images = new ArrayList<BoatPhotosDTO>();
    protected SimpleObjectProperty<BoatPhotosDTO> selectedImage = new SimpleObjectProperty<>();
    private ObservableList<NotesDTO> notesDTOS = FXCollections.observableArrayList();








    public ObservableList<NotesDTO> getNotesDTOS() {
        return notesDTOS;
    }

    public void setNotesDTOS(ObservableList<NotesDTO> notesDTOS) {
        this.notesDTOS = notesDTOS;
    }

    public ObservableList<MembershipListDTO> getBoatOwners() {
        return boatOwners;
    }

    public void setBoatOwners(ObservableList<MembershipListDTO> boatOwners) {
        this.boatOwners = boatOwners;
    }

    public ArrayList<BoatPhotosDTO> getImages() {
        return images;
    }

    public void setImages(ArrayList<BoatPhotosDTO> images) {
        this.images = images;
    }

    public boolean isDataLoaded() {
        return dataLoaded.get();
    }

    public BooleanProperty dataLoadedProperty() {
        return dataLoaded;
    }

    public void setDataLoaded(boolean dataLoaded) {
        this.dataLoaded.set(dataLoaded);
    }

    public ArrayList<DbBoatSettingsDTO> getBoatSettings() {
        return boatSettings;
    }

    public void setBoatSettings(ArrayList<DbBoatSettingsDTO> boatSettings) {
        this.boatSettings = boatSettings;
    }

    public BoatListDTO getBoatListDTO() {
        return boatListDTO.get();
    }

    public SimpleObjectProperty<BoatListDTO> boatListDTOProperty() {
        return boatListDTO;
    }

    public void setBoatListDTO(BoatListDTO boatListDTO) {
        this.boatListDTO.set(boatListDTO);
    }

    public BoatModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }
    public MainModel getMainModel() {
        return mainModel;
    }
}
