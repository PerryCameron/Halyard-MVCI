package org.ecsail.mvci_boat;


import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.ecsail.dto.DbBoatSettingsDTO;
import org.ecsail.enums.KeelType;

public class Row extends HBox {

    private final BoatModel boatModel;
    private final BoatView boatView;
    DbBoatSettingsDTO dbBoatSettingsDTO;
    public Row(BoatView boatView, DbBoatSettingsDTO dbBoatSettingsDTO) {
        this.boatView = boatView;
        this.boatModel = boatView.getBoatModel();
        this.dbBoatSettingsDTO = dbBoatSettingsDTO;
        if(dbBoatSettingsDTO.isVisible()) {
            VBox labelBox = new VBox();
            VBox dataBox = new VBox();
            labelBox.setPrefWidth(90);
            labelBox.setAlignment(Pos.CENTER_LEFT);
            labelBox.getChildren().add(new Text(dbBoatSettingsDTO.getName()));
            dataBox.getChildren().add(getDataBoxContent(dbBoatSettingsDTO));
            setPadding(new Insets(0, 5, 5, 15));
            getChildren().addAll(labelBox, dataBox);
        }
    }

    private void setTextFieldListener(TextField textField) {
        textField.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    // focus out
                    if (oldValue) { // we have focused and unfocused
                        setPojo(dbBoatSettingsDTO.getFieldName(), textField.getText());
                        boatView.sendMessage().accept(BoatMessage.UPDATE_BOAT);
                        // TODO SQL
                    }
                });
    }

    private void setCheckBoxListener(CheckBox checkBox) {
        checkBox.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            setBooleanValue(dbBoatSettingsDTO.getFieldName());
            boatView.sendMessage().accept(BoatMessage.UPDATE_BOAT);
            // TODO SQL
                });

    }

    private void setComboBoxListener(ComboBox<KeelType> comboBox) {
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
//            SqlUpdate.updateBoat(parent.boatDTO.getBoatId(), newValue.getCode());
//            if(parent.fromList)
            setPojo(dbBoatSettingsDTO.getFieldName(), newValue.getCode());
            boatView.sendMessage().accept(BoatMessage.UPDATE_BOAT);
        });
    }

    private Node getDataBoxContent(DbBoatSettingsDTO dbBoatDTO) {
        if(dbBoatDTO.getControlType().equals("Text")) {
            Text text = new Text(setStringValue(dbBoatDTO.getFieldName()));
            return text;
        } else if(dbBoatDTO.getControlType().equals("TextField")) {
            TextField textField = new TextField(setStringValue(dbBoatDTO.getFieldName()));
            textField.setPrefSize(150, 10);
            setTextFieldListener(textField);
            return textField;
        } else if (dbBoatDTO.getControlType().equals("CheckBox")) {
            CheckBox checkBox = new CheckBox();
            checkBox.setPrefHeight(10);
            checkBox.setSelected(setBooleanValue(dbBoatDTO.getFieldName()));
            setCheckBoxListener(checkBox);
            return checkBox;
        } else if (dbBoatDTO.getControlType().equals("ComboBox")) {
            ComboBox<KeelType> comboBox = new ComboBox<KeelType>();
            comboBox.getItems().setAll(KeelType.values());
            comboBox.setValue(KeelType.getByCode(boatModel.getBoatListDTO().getKeel()));
            comboBox.setPrefSize(150,10);
            setComboBoxListener(comboBox);
            return comboBox;
        }
        return new Text("undefined");
    }

    private boolean setBooleanValue(String fieldName) {
        if(fieldName.equals("HAS_TRAILER")) return boatModel.getBoatListDTO().hasTrailer();
        else if (fieldName.equals("AUX")) return boatModel.getBoatListDTO().isAux();
        return false;
    }

    private String setStringValue(String fieldName) { // this was used when coming from membership view
        if(fieldName.equals("BOAT_ID")) return String.valueOf(boatModel.getBoatListDTO().getBoatId());
        else if(fieldName.equals("BOAT_NAME")) return boatModel.getBoatListDTO().getBoatName();
        else if(fieldName.equals("MANUFACTURER")) return boatModel.getBoatListDTO().getManufacturer();
        else if(fieldName.equals("MANUFACTURE_YEAR")) return boatModel.getBoatListDTO().getManufactureYear();
        else if(fieldName.equals("REGISTRATION_NUM")) return boatModel.getBoatListDTO().getRegistrationNum();
        else if(fieldName.equals("MODEL")) return boatModel.getBoatListDTO().getModel();
        else if(fieldName.equals("PHRF")) return boatModel.getBoatListDTO().getPhrf();
        else if(fieldName.equals("BOAT_NAME")) return boatModel.getBoatListDTO().getBoatName();
        else if(fieldName.equals("SAIL_NUMBER")) return boatModel.getBoatListDTO().getSailNumber();
        else if(fieldName.equals("LENGTH")) return boatModel.getBoatListDTO().getLoa();
        else if(fieldName.equals("WEIGHT")) return boatModel.getBoatListDTO().getDisplacement();
        else if(fieldName.equals("KEEL")) return boatModel.getBoatListDTO().getKeel();
        else if(fieldName.equals("DRAFT")) return boatModel.getBoatListDTO().getDraft();
        else if(fieldName.equals("BEAM")) return boatModel.getBoatListDTO().getBeam();
        else if(fieldName.equals("LWL")) return boatModel.getBoatListDTO().getLwl();
        else
            return "";
    }

    private String setPojo(String fieldName, String value) {
            if (fieldName.equals("BOAT_ID")) boatModel.getBoatListDTO().setBoatId(Integer.parseInt(value));
            else if (fieldName.equals("BOAT_NAME")) boatModel.getBoatListDTO().setBoatName(value);
            else if (fieldName.equals("MANUFACTURER")) boatModel.getBoatListDTO().setManufacturer(value);
            else if (fieldName.equals("MANUFACTURE_YEAR")) boatModel.getBoatListDTO().setManufactureYear(value);
            else if (fieldName.equals("REGISTRATION_NUM")) boatModel.getBoatListDTO().setRegistrationNum(value);
            else if (fieldName.equals("MODEL")) boatModel.getBoatListDTO().setModel(value);
            else if (fieldName.equals("PHRF")) boatModel.getBoatListDTO().setPhrf(value);
            else if (fieldName.equals("BOAT_NAME")) boatModel.getBoatListDTO().setBoatName(value);
            else if (fieldName.equals("SAIL_NUMBER")) boatModel.getBoatListDTO().setSailNumber(value);
            else if (fieldName.equals("LENGTH")) boatModel.getBoatListDTO().setLoa(value);
            else if (fieldName.equals("WEIGHT")) boatModel.getBoatListDTO().setDisplacement(value);
            else if (fieldName.equals("KEEL")) boatModel.getBoatListDTO().setKeel(value);
            else if (fieldName.equals("DRAFT")) boatModel.getBoatListDTO().setDraft(value);
            else if (fieldName.equals("BEAM")) boatModel.getBoatListDTO().setBeam(value);
            else if (fieldName.equals("LWL")) boatModel.getBoatListDTO().setLwl(value);
        return "";
    }

}
