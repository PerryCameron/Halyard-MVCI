package org.ecsail.mvci_roster;

import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import org.ecsail.dto.MembershipListRadioDTO;

public class RosterRadioHBox extends HBox {
    private final MembershipListRadioDTO membershipListRadioDTO;
    private final RadioButton radioButton;
    private final RosterModel rosterModel;

    public RosterRadioHBox(MembershipListRadioDTO r, RosterModel rm) {
        this.membershipListRadioDTO = r;
        rosterModel = rm;
        this.radioButton = new RadioButton(membershipListRadioDTO.getLabel());
        setRadioButtonListener();
        radioButton.setSelected(membershipListRadioDTO.isSelected());
        this.getChildren().add(radioButton);
    }

    private void setRadioButtonListener() {
        radioButton.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                rosterModel.setSelectedRadioBox(this);
                if (rosterModel.getSlipColumn() != null) {
                    if (membershipListRadioDTO.getLabel().equals("Slip Waitlist")) setColumnFourName("Sublease");
                    else setColumnFourName("Slip");
                }
            }
        });
    }

    public RadioButton getRadioButton() {
        return radioButton;
    }

    public String getMethod() {
        return membershipListRadioDTO.getMethodName();
    }

    public String getRadioLabel() {
        return membershipListRadioDTO.getLabel();
    }

    public void setColumnFourName(String newName) {
        rosterModel.getSlipColumn().setText(newName);
    }
}
