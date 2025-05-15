package org.ecsail.mvci_roster;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import org.ecsail.dto.*;
import org.ecsail.mvci_main.MainModel;
import org.ecsail.static_tools.HttpClientUtil;

import java.io.File;
import java.time.Year;
import java.util.ArrayList;

public class RosterModel {
    private final ObjectMapper objectMapper;
    private final ObservableList<RosterDTOFx> rosters = FXCollections.observableArrayList();
    private final ObservableList<RosterDTOFx> searchedRosters = FXCollections.observableArrayList();
    private final ObservableList<MembershipListRadioDTO> radioChoices = FXCollections.observableArrayList();
    private final HttpClientUtil httpClient;
    private ObservableList<DbRosterSettingsDTO> rosterSettings = FXCollections.observableArrayList();
    private final ArrayList<RosterSettingsCheckBox> checkBoxes = new ArrayList<>();
    private final SimpleObjectProperty<TableView<RosterDTOFx>> rosterTableView = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<RosterRadioHBox> selectedRadioBox = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<File> fileToSave = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<TableColumn<RosterDTOFx, Text>> slipColumn = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<RosterDTOFx> selectedMembershipList = new SimpleObjectProperty<>();
    private final StringProperty numberOfRecords = new SimpleStringProperty("0");
    private final StringProperty textFieldString = new SimpleStringProperty();
    private final IntegerProperty selectedYear = new SimpleIntegerProperty(Year.now().getValue());
    private final BooleanProperty isActiveSearch = new SimpleBooleanProperty(false);
    private final BooleanProperty listsLoaded = new SimpleBooleanProperty(false);




    public RosterModel(MainModel mainModel) {
        this.httpClient = mainModel.getHttpClient();
        this.objectMapper = mainModel.getObjectMapper();
    }


    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public HttpClientUtil getHttpClient() {
        return httpClient;
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
    public ArrayList<RosterSettingsCheckBox> getCheckBoxes() {
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
    public ObservableList<RosterDTOFx> getSearchedRosters() {
        return searchedRosters;
    }
    public RosterRadioHBox getSelectedRadioBox() {
        return selectedRadioBox.get();
    }
    public SimpleObjectProperty<RosterRadioHBox> selectedRadioBoxProperty() {
        return selectedRadioBox;
    }
    public void setSelectedRadioBox(RosterRadioHBox selectedRadioBox) {
        this.selectedRadioBox.set(selectedRadioBox);
    }
    public ObservableList<MembershipListRadioDTO> getRadioChoices() {
        return radioChoices;
    }
    public TableView<RosterDTOFx> getRosterTableView() {
        return rosterTableView.get();
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
    public ObservableList<RosterDTOFx> getRosters() {
        return rosters;
    }

    public boolean isListsLoaded() {
        return listsLoaded.get();
    }

    public SimpleObjectProperty<TableView<RosterDTOFx>> rosterTableViewProperty() {
        return rosterTableView;
    }

    public SimpleObjectProperty<File> fileToSaveProperty() {
        return fileToSave;
    }

    public TableColumn<RosterDTOFx, Text> getSlipColumn() {
        return slipColumn.get();
    }

    public SimpleObjectProperty<TableColumn<RosterDTOFx, Text>> slipColumnProperty() {
        return slipColumn;
    }

    public RosterDTOFx getSelectedMembershipList() {
        return selectedMembershipList.get();
    }

    public SimpleObjectProperty<RosterDTOFx> selectedMembershipListProperty() {
        return selectedMembershipList;
    }

    public String getNumberOfRecords() {
        return numberOfRecords.get();
    }

    public IntegerProperty selectedYearProperty() {
        return selectedYear;
    }

    public boolean isIsActiveSearch() {
        return isActiveSearch.get();
    }

    public BooleanProperty isActiveSearchProperty() {
        return isActiveSearch;
    }
}
