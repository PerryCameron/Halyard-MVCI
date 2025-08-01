package org.ecsail.mvci.boat;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import org.ecsail.fx.*;
import org.ecsail.mvci.main.MainModel;

import java.util.ArrayList;

public class BoatModel {

    private final MainModel mainModel;
    private final SimpleObjectProperty<BoatListDTO> boatListDTO = new SimpleObjectProperty<>();
    private ObservableList<MembershipListDTO> boatOwners = FXCollections.observableArrayList();
    private ArrayList<DbBoatSettingsDTO> boatSettings = new ArrayList<>();
    private final BooleanProperty dataLoaded = new SimpleBooleanProperty(false);
    private ArrayList<BoatPhotosDTO> images = new ArrayList<BoatPhotosDTO>();
    protected SimpleObjectProperty<BoatPhotosDTO> selectedImage = new SimpleObjectProperty<>();
    protected SimpleObjectProperty<NoteFx> selectedNote = new SimpleObjectProperty<>();
    protected SimpleObjectProperty<ImageView> imageView = new SimpleObjectProperty<>();
    private SimpleObjectProperty<TableView<MembershipListDTO>> boatOwnerTableView = new SimpleObjectProperty<>();
    private SimpleObjectProperty<TableView<NoteFx>> notesTableView = new SimpleObjectProperty<>();

    private ObservableList<NoteFx> notesDTOS = FXCollections.observableArrayList();
    protected SimpleObjectProperty<MembershipListDTO> selectedOwner = new SimpleObjectProperty<>();
    private BooleanProperty confirmed = new SimpleBooleanProperty(false);
    private IntegerProperty membershipId = new SimpleIntegerProperty();
    private StringProperty selectedPath = new SimpleStringProperty();


    public TableView<NoteFx> getNotesTableView() {
        return notesTableView.get();
    }

    public SimpleObjectProperty<TableView<NoteFx>> notesTableViewProperty() {
        return notesTableView;
    }

    public void setNotesTableView(TableView<NoteFx> notesTableView) {
        this.notesTableView.set(notesTableView);
    }

    public TableView<MembershipListDTO> getBoatOwnerTableView() {
        return boatOwnerTableView.get();
    }

    public SimpleObjectProperty<TableView<MembershipListDTO>> boatOwnerTableViewProperty() {
        return boatOwnerTableView;
    }

    public void setBoatOwnerTableView(TableView<MembershipListDTO> boatOwnerTableView) {
        this.boatOwnerTableView.set(boatOwnerTableView);
    }

    public String getSelectedPath() {
        return selectedPath.get();
    }

    public StringProperty selectedPathProperty() {
        return selectedPath;
    }

    public void setSelectedPath(String selectedPath) {
        this.selectedPath.set(selectedPath);
    }

    public int getMembershipId() {
        return membershipId.get();
    }

    public IntegerProperty membershipIdProperty() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId.set(membershipId);
    }

    public boolean isConfirmed() {
        return confirmed.get();
    }

    public BooleanProperty confirmedProperty() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed.set(confirmed);
    }

    public MembershipListDTO getSelectedOwner() {
        return selectedOwner.get();
    }

    public SimpleObjectProperty<MembershipListDTO> selectedOwnerProperty() {
        return selectedOwner;
    }

    public void setSelectedOwner(MembershipListDTO selectedOwner) {
        this.selectedOwner.set(selectedOwner);
    }

    public NoteFx getSelectedNote() {
        return selectedNote.get();
    }

    public SimpleObjectProperty<NoteFx> selectedNoteProperty() {
        return selectedNote;
    }

    public void setSelectedNote(NoteFx selectedNote) {
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

    public ObservableList<NoteFx> getNotesDTOS() {
        return notesDTOS;
    }

    public void setNotesDTOS(ObservableList<NoteFx> notesDTOS) {
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
