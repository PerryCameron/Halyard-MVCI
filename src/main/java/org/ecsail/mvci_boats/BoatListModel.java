package org.ecsail.mvci_boats;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.dto.BoatListDTO;
import org.ecsail.dto.BoatListRadioDTO;

public class BoatListModel {

    private ObservableList<BoatListDTO> boats = FXCollections.observableArrayList();
    private final ObservableList<BoatListRadioDTO> radioChoices = FXCollections.observableArrayList();
    private final StringProperty numberOfRecords = new SimpleStringProperty("0");
    private final StringProperty textFieldString = new SimpleStringProperty();
    private final SimpleObjectProperty<BoatListDTO> selectedBoatList = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<BoatListRadioHBox> selectedRadioBox = new SimpleObjectProperty<>();


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
