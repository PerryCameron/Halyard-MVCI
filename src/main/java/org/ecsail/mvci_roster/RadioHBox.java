package org.ecsail.mvci_roster;

import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import org.ecsail.dto.MembershipListRadioDTO;

public class RadioHBox extends HBox {
    private final MembershipListRadioDTO membershipListRadioDTO;
    private final RadioButton radioButton;
    private final RosterModel rosterModel;

    public RadioHBox(MembershipListRadioDTO r, RosterModel rm) {
        this.membershipListRadioDTO = r;
        rosterModel = rm;
        this.radioButton = new RadioButton(membershipListRadioDTO.getLabel());
        setRadioButtonListener();
        System.out.println("radiobutton =" + getRadioLabel());
        radioButton.setSelected(membershipListRadioDTO.isSelected());
        this.getChildren().add(radioButton);
    }

    private void setRadioButtonListener() {
        radioButton.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
             rosterModel.setSelectedRadioBox(this);
//                    parent.makeListByRadioButtonChoice();
            }
        });
    }

    public RadioButton getRadioButton() {
        return radioButton;
    }

    public String getMethod() {
        return membershipListRadioDTO.getMethodName();
    }

    public String getRadioLabel() { return membershipListRadioDTO.getLabel(); }
}
