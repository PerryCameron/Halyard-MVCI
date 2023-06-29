package org.ecsail.mvci_boat;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
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
    protected SimpleObjectProperty<NotesDTO> selectedNote = new SimpleObjectProperty<>();
    protected SimpleObjectProperty<ImageView> imageView = new SimpleObjectProperty<>();
    private ObservableList<NotesDTO> notesDTOS = FXCollections.observableArrayList();


    public NotesDTO getSelectedNote() {
        return selectedNote.get();
    }

    public SimpleObjectProperty<NotesDTO> selectedNoteProperty() {
        return selectedNote;
    }

    public void setSelectedNote(NotesDTO selectedNote) {
        this.selectedNote.set(selectedNote);
    }

    public ImageView getImageView() {
        return imageView.get();
    }

    public SimpleObjectProperty<ImageView> imageViewProperty() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView.set(imageView);
    }

    public BoatPhotosDTO getSelectedImage() {
        return selectedImage.get();
    }

    public SimpleObjectProperty<BoatPhotosDTO> selectedImageProperty() {
        return selectedImage;
    }

    public void setSelectedImage(BoatPhotosDTO selectedImage) {
        this.selectedImage.set(selectedImage);
    }

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
