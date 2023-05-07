package org.ecsail.mvci_roster;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.ecsail.dto.DbRosterSettingsDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.MembershipListRadioDTO;

import java.io.File;
import java.time.Year;
import java.util.ArrayList;

public class RosterModel {
    private ObservableList<MembershipListDTO> rosters = FXCollections.observableArrayList();
    private final ObservableList<MembershipListDTO> searchedRosters = FXCollections.observableArrayList();
    private final ObservableList<MembershipListRadioDTO> radioChoices = FXCollections.observableArrayList();
    private ObservableList<DbRosterSettingsDTO> rosterSettings = FXCollections.observableArrayList();
    private final ArrayList<SettingsCheckBox> checkBoxes = new ArrayList<>();
    private final SimpleObjectProperty<TableView<MembershipListDTO>> rosterTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<RadioHBox> selectedRadioBox = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<File> fileToSave = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<MembershipListDTO> selectedMembershipList = new SimpleObjectProperty<>();
    private final StringProperty numberOfRecords = new SimpleStringProperty("0");
    private final StringProperty textFieldString = new SimpleStringProperty();
    private final IntegerProperty selectedYear = new SimpleIntegerProperty(Year.now().getValue());
    private final BooleanProperty isActiveSearch = new SimpleBooleanProperty(false);
    private final BooleanProperty listsLoaded = new SimpleBooleanProperty(false);


    public SimpleObjectProperty<MembershipListDTO> selectedMembershipListProperty() {
        return selectedMembershipList;
    }
    public void setSelectedMembershipList(MembershipListDTO selectedMembershipList) {
        this.selectedMembershipList.set(selectedMembershipList);
    }
    public File getFileToSave() {
        return fileToSave.get();
    }
    public void setFileToSave(File fileToSave) {
        this.fileToSave.set(fileToSave);
    }
    public BooleanProperty listsLoadedProperty() {
        return listsLoaded;
    }
    public void setListsLoaded(boolean listsLoaded) {
        this.listsLoaded.set(listsLoaded);
    }
    public ArrayList<SettingsCheckBox> getCheckBoxes() {
        return checkBoxes;
    }
    public ObservableList<DbRosterSettingsDTO> getRosterSettings() {
        return rosterSettings;
    }
    public void setRosterSettings(ObservableList<DbRosterSettingsDTO> rosterSettings) {
        this.rosterSettings = rosterSettings;
    }
    public boolean isSearchMode() {
        return isActiveSearch.get();
    }
    public void setIsSearchMode(boolean isActiveSearch) {
        this.isActiveSearch.set(isActiveSearch);
    }
    public ObservableList<MembershipListDTO> getSearchedRosters() {
        return searchedRosters;
    }
    public RadioHBox getSelectedRadioBox() {
        return selectedRadioBox.get();
    }
    public SimpleObjectProperty<RadioHBox> selectedRadioBoxProperty() {
        return selectedRadioBox;
    }
    public void setSelectedRadioBox(RadioHBox selectedRadioBox) {
        this.selectedRadioBox.set(selectedRadioBox);
    }
    public ObservableList<MembershipListRadioDTO> getRadioChoices() {
        return radioChoices;
    }
    public TableView<MembershipListDTO> getRosterTableView() {
        return rosterTableView.get();
    }

    public void setRosterTableView(TableView<MembershipListDTO> rosterTableView) {
        this.rosterTableView.set(rosterTableView);
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
    public int getSelectedYear() {
        return selectedYear.get();
    }
    public void setSelectedYear(int selectedYear) {
        this.selectedYear.set(selectedYear);
    }
    public StringProperty numberOfRecordsProperty() {
        return numberOfRecords;
    }
    public void setNumberOfRecords(String numberOfRecords) {
        this.numberOfRecords.set(numberOfRecords);
    }
    public ObservableList<MembershipListDTO> getRosters() {
        return rosters;
    }
}
