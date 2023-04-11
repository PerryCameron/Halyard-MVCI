package org.ecsail.mvci_roster;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.ecsail.dto.MembershipListDTO;

import java.time.Year;

public class RosterModel {
    private ObservableList<MembershipListDTO> rosters = FXCollections.observableArrayList();
    private StringProperty numberOfRecords = new SimpleStringProperty("0");
    private IntegerProperty selectedYear = new SimpleIntegerProperty(Year.now().getValue());
    private StringProperty textField = new SimpleStringProperty();
    private SimpleObjectProperty<TableView<MembershipListDTO>> rosterTableView = new SimpleObjectProperty<>();


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
