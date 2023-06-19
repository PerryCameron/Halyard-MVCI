package org.ecsail.mvci_boatlist;

import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import org.ecsail.dto.BoatListRadioDTO;

public class BoatListRadioHBox extends HBox {
    private BoatListRadioDTO boatListRadioDTO;
    private RadioButton radioButton;
    private BoatListModel boatListModel;
//    private ControlBox parent;

    public BoatListRadioHBox(BoatListRadioDTO r, BoatListModel b) {
        this.boatListRadioDTO = r;
        boatListModel = b;
        this.radioButton = new RadioButton(boatListRadioDTO.getLabel());
        setRadioButtonListener();
        radioButton.setSelected(boatListRadioDTO.isSelected());
        this.getChildren().add(radioButton);
    }
    
    private void setRadioButtonListener() {
        radioButton.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                boatListModel.setSelectedRadioBox(this);
            }
        });
    }

    public RadioButton getRadioButton() {
        return radioButton;
    }

    public String getMethod() {
        return boatListRadioDTO.getMethod();
    }

    public String getRadioLabel() { return boatListRadioDTO.getLabel(); }

}
