package org.ecsail.mvci_roster;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.MembershipListRadioDTO;

import java.time.Year;

public class RosterModel {
    private ObservableList<MembershipListDTO> rosters = FXCollections.observableArrayList();
    private StringProperty numberOfRecords = new SimpleStringProperty("0");
    private IntegerProperty selectedYear = new SimpleIntegerProperty(Year.now().getValue());
    private StringProperty textField = new SimpleStringProperty();
    private SimpleObjectProperty<TableView<MembershipListDTO>> rosterTableView = new SimpleObjectProperty<>();
    private ObservableList<MembershipListRadioDTO> radioChoices = FXCollections.observableArrayList();
    private SimpleObjectProperty<RadioHBox> selectedRadioBox = new SimpleObjectProperty<>();






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

    public String getTextField() {
        return textField.get();
    }

    public StringProperty textFieldProperty() {
        return textField;
    }

    public void setTextField(String textField) {
        this.textField.set(textField);
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
