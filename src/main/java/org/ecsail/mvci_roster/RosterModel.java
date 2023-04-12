package org.ecsail.mvci_roster;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.ecsail.dto.DbRosterSettingsDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.MembershipListRadioDTO;

import java.time.Year;
import java.util.ArrayList;

public class RosterModel {
    private ObservableList<MembershipListDTO> rosters = FXCollections.observableArrayList();
    private ObservableList<MembershipListDTO> searchedRosters = FXCollections.observableArrayList();
    private final StringProperty numberOfRecords = new SimpleStringProperty("0");
    private final IntegerProperty selectedYear = new SimpleIntegerProperty(Year.now().getValue());
    private final StringProperty textFieldString = new SimpleStringProperty();
    private final BooleanProperty isActiveSearch = new SimpleBooleanProperty(false);
    private final SimpleObjectProperty<TableView<MembershipListDTO>> rosterTableView = new SimpleObjectProperty<>();
    private ObservableList<MembershipListRadioDTO> radioChoices = FXCollections.observableArrayList();
    private final SimpleObjectProperty<RadioHBox> selectedRadioBox = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<VBox> checkVbox = new SimpleObjectProperty<>();
    private ArrayList<SettingsCheckBox> checkBoxes = new ArrayList<>();
    private ObservableList<DbRosterSettingsDTO> rosterSettings = FXCollections.observableArrayList();
    private BooleanProperty listsLoaded = new SimpleBooleanProperty(false);

    public boolean isListsLoaded() {
        return listsLoaded.get();
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

    public void setCheckBoxes(ArrayList<SettingsCheckBox> checkBoxes) {
        this.checkBoxes = checkBoxes;
    }

    public ObservableList<DbRosterSettingsDTO> getRosterSettings() {
        return rosterSettings;
    }

    public void setRosterSettings(ObservableList<DbRosterSettingsDTO> rosterSettings) {
        this.rosterSettings = rosterSettings;
    }

    public boolean isIsActiveSearch() {
        return isActiveSearch.get();
    }

    public BooleanProperty isActiveSearchProperty() {
        return isActiveSearch;
    }

    public void setIsActiveSearch(boolean isActiveSearch) {
        this.isActiveSearch.set(isActiveSearch);
    }

    public ObservableList<MembershipListDTO> getSearchedRosters() {
        return searchedRosters;
    }

    public void setSearchedRosters(ObservableList<MembershipListDTO> searchedRosters) {
        this.searchedRosters = searchedRosters;
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

    public void setRadioChoices(ObservableList<MembershipListRadioDTO> radioChoices) {
        this.radioChoices = radioChoices;
    }

    public TableView<MembershipListDTO> getRosterTableView() {
        return rosterTableView.get();
    }
    public SimpleObjectProperty<TableView<MembershipListDTO>> rosterTableViewProperty() {
        return rosterTableView;
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

    public IntegerProperty selectedYearProperty() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear.set(selectedYear);
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

    public ObservableList<MembershipListDTO> getRosters() {
        return rosters;
    }

    public void setRosters(ObservableList<MembershipListDTO> rosters) {
        this.rosters = rosters;
    }
}
