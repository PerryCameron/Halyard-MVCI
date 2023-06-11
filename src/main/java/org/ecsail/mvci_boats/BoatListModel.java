package org.ecsail.mvci_boats;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.ecsail.dto.*;
import org.ecsail.mvci_roster.SettingsCheckBox;

import java.util.ArrayList;

public class BoatListModel {

    private ObservableList<BoatListDTO> boats = FXCollections.observableArrayList();
    private final ObservableList<BoatListDTO> searchedBoats = FXCollections.observableArrayList();
    private final ObservableList<BoatListRadioDTO> radioChoices = FXCollections.observableArrayList();
    private ObservableList<DbBoatSettingsDTO> boatListSettings = FXCollections.observableArrayList();
    private final StringProperty numberOfRecords = new SimpleStringProperty("0");
    private final StringProperty textFieldString = new SimpleStringProperty();
    private final SimpleObjectProperty<BoatListDTO> selectedBoatList = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<BoatListRadioHBox> selectedRadioBox = new SimpleObjectProperty<>();
    private final BooleanProperty listsLoaded = new SimpleBooleanProperty(false);
    private final ArrayList<SettingsCheckBox> checkBoxes = new ArrayList<>();
    private final SimpleObjectProperty<TableView<BoatListDTO>> boatListTableView = new SimpleObjectProperty<>();
    private final BooleanProperty isActiveSearch = new SimpleBooleanProperty(false);


    public ObservableList<BoatListDTO> getSearchedBoats() {
        return searchedBoats;
    }

    public boolean isSearchMode() {
        return isActiveSearch.get();
    }

    public BooleanProperty isActiveSearchProperty() {
        return isActiveSearch;
    }

    public void setIsActiveSearch(boolean isActiveSearch) {
        this.isActiveSearch.set(isActiveSearch);
    }

    public TableView<BoatListDTO> getBoatListTableView() {
        return boatListTableView.get();
    }

    public SimpleObjectProperty<TableView<BoatListDTO>> boatListTableViewProperty() {
        return boatListTableView;
    }

    public void setBoatListTableView(TableView<BoatListDTO> boatListTableView) {
        this.boatListTableView.set(boatListTableView);
    }

    public ArrayList<SettingsCheckBox> getCheckBoxes() {
        return checkBoxes;
    }

    public ObservableList<DbBoatSettingsDTO> getBoatListSettings() {
        return boatListSettings;
    }

    public void setBoatListSettings(ObservableList<DbBoatSettingsDTO> boatListSettings) {
        this.boatListSettings = boatListSettings;
    }

    public boolean isListsLoaded() {
        return listsLoaded.get();
    }

    public BooleanProperty listsLoadedProperty() {
        return listsLoaded;
    }

    public void setListsLoaded(boolean listsLoaded) {
        this.listsLoaded.set(listsLoaded);
    }

    public BoatListRadioHBox getSelectedRadioBox() {
        return selectedRadioBox.get();
    }

    public SimpleObjectProperty<BoatListRadioHBox> selectedRadioBoxProperty() {
        return selectedRadioBox;
    }

    public void setSelectedRadioBox(BoatListRadioHBox selectedRadioBox) {
        this.selectedRadioBox.set(selectedRadioBox);
    }

    public ObservableList<BoatListRadioDTO> getRadioChoices() {
        return radioChoices;
    }

    public ObservableList<BoatListDTO> getBoats() {
        return boats;
    }

    public void setBoats(ObservableList<BoatListDTO> boats) {
        this.boats = boats;
    }

    public BoatListDTO getSelectedBoatList() {
        return selectedBoatList.get();
    }

    public SimpleObjectProperty<BoatListDTO> selectedBoatListProperty() {
        return selectedBoatList;
    }

    public void setSelectedBoatList(BoatListDTO selectedBoatList) {
        this.selectedBoatList.set(selectedBoatList);
    }

    public String getTextFieldString() {
        return textFieldString.get();
    }

    public StringProperty textFieldStringProperty() {
        return textFieldString;
    }

    public void setTextFieldString(String textFieldString) {
        this.textFieldString.set(textFieldString);
    }

    public String getNumberOfRecords() {
        return numberOfRecords.get();
    }

    public StringProperty numberOfRecordsProperty() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(String numberOfRecords) {
        this.numberOfRecords.set(numberOfRecords);
    }
}
