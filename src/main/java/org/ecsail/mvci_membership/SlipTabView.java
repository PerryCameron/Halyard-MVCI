package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.interfaces.Messages;
import org.ecsail.interfaces.SlipUser;
import org.ecsail.widgetfx.*;

import java.util.Objects;

public class SlipTabView implements Builder<Tab>, SlipUser, Messages {

    private final MembershipView membershipView;
    private final MembershipModel membershipModel;

    public SlipTabView(MembershipView membershipView) {
        this.membershipView = membershipView;
        this.membershipModel = membershipView.getMembershipModel();
    }

    @Override
    public Tab build() {
        Tab tab = new Tab();
        tab.setText("Slip");
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,5,5)); // makes outer border
        vBox.setId("custom-tap-pane-frame");
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(setSlipInfo());
        borderPane.setCenter(setSlipImage());
        borderPane.setId("box-background-light");
        VBox.setVgrow(borderPane,Priority.ALWAYS); // causes slip tab to grow to fit vertical space
        vBox.getChildren().add(borderPane);
        tab.setContent(vBox);
        setSlipStatus();
        return tab;
    }

    private Node setSlipInfo() {
        VBox vBox = VBoxFx.vBoxOf(10.0, new Insets(10,20,0,10));
        ToggleGroup tg = new ToggleGroup();
        vBox.getChildren().add(setSlipNumber());
        vBox.getChildren().add(setSubleaseInfo());
        vBox.getChildren().add(addWaitList());
        vBox.getChildren().add(newRadioButton(tg, "Sublease Slip"));
        vBox.getChildren().add(newRadioButton(tg, "Reassign Slip"));
        vBox.getChildren().add(newRadioButton(tg, "Release Sublease"));
        vBox.getChildren().add(setSubmissionButton());
        return vBox;
    }

    private Node addWaitList() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(10,0,0,0),Pos.CENTER_LEFT, 10);
        CheckBox checkBox = new CheckBox("Slip Wait list");
        membershipModel.getSlipControls().put("waitList", checkBox);
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) membershipView.sendMessage().apply(MessageType.SET_WAIT_LIST, "yes");
            else membershipView.sendMessage().apply(MessageType.SET_WAIT_LIST, "no");
        });
        hBox.getChildren().add(checkBox);
        return hBox;
    }

    private Node setSubmissionButton() {
        HBox hBox = HBoxFx.hBoxOf( new Insets(10,0,0,0),Pos.CENTER_LEFT, 10);
        TextField textField = TextFieldFx.textFieldOf(40, "ID");
        Button button = ButtonFx.buttonOf("Submit", 70);
        membershipModel.getSlipControls().put("button", button);
        membershipModel.getSlipControls().put("textField", textField);
        button.setOnAction(event -> makeSlipChange());
        hBox.getChildren().addAll(textField,button);
        return hBox;
    }

    private void makeSlipChange() {
        TextField textField = (TextField) membershipModel.getSlipControls().get("textField");
        switch (membershipModel.getSlipRelationStatus()) {
            case subLeaser, ownAndSublease -> {
                RadioButton rb = (RadioButton) membershipModel.getSlipControls().get("Release Sublease");
                if(rb.isSelected()) membershipView.sendMessage().apply(MessageType.RELEASE_SUBLEASE, null);
            }
            case owner -> {
                RadioButton rb1 = (RadioButton) membershipModel.getSlipControls().get("Sublease Slip");
                RadioButton rb2 = (RadioButton) membershipModel.getSlipControls().get("Reassign Slip");
                if(rb1.isSelected()) {
                    membershipView.sendMessage().apply(MessageType.SUBLEASE_SLIP, textField.getText());
                }
                if(rb2.isSelected()) {
                    membershipView.sendMessage().apply(MessageType.REASSIGN_SLIP, textField.getText());
                }
            }
        }
    }

    private Node newRadioButton(ToggleGroup tg, String rbType) {
        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER_LEFT,new Insets(0,0,0,0));
        RadioButton radioButton = new RadioButton(rbType);
        radioButton.setToggleGroup(tg);
        membershipModel.getSlipControls().put(rbType, radioButton);
        hBox.getChildren().add(radioButton);
        return hBox;
    }

    private Node setSubleaseInfo() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(0,0,0,0),Pos.CENTER_LEFT,10.0);
        Text label = TextFx.textOf("","text-white",membershipModel.subleaseProperty());
        Text membership = TextFx.textOf("","text-blue", membershipModel.membershipIdProperty());
        hBox.getChildren().addAll(label, membership);
        membershipModel.slipRelationStatusProperty().addListener(observable -> setSlipStatus());
        return hBox;
    }

    private Node setSlipNumber() {
        HBox hBox = HBoxFx.hBoxOf(10, Pos.CENTER_LEFT);
        Text label = TextFx.textOf("Slip Number:","text-white");
        Text slip = TextFx.textOf(membershipModel.getSlip().getSlipNumber(),"text-bold");
        hBox.getChildren().addAll(label,slip);
        return hBox;
    }

    private Node setSlipImage() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(15,0,0,15));
        Image slipImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/slips/" + getSlip() + "_icon.png")));
        ImageView imageView = new ImageView(slipImage);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(240);
        vBox.getChildren().add(imageView);
        return vBox;
    }

    private String getSlip() {
        switch (membershipModel.getSlipRelationStatus()) {
            case owner, subLeaser, ownAndSublease -> { return membershipModel.getSlip().getSlipNumber(); }
            default -> { return "none"; }
        }
    }

    private void setSlipStatus() {
        switch (membershipModel.getSlipRelationStatus()) {
            case subLeaser -> {
                membershipModel.setSublease("Subleased From: ");
                setControls(false,false,true,false,true,false);
            }
            case ownAndSublease -> {
                membershipModel.setSublease("Subleasing To: ");
                setControls(false,false,true,false,true,false);
            }
            case noSlip -> {
                membershipModel.setSublease("");
                setControls(false,false,false,true,false,false);
            }
            case owner -> {
                membershipModel.setSublease("");
                setControls(true,true,false,false,true,true);
            }
        }
    }

    private void setControls(boolean rb1, boolean rb2, boolean rb3, boolean check, boolean button, boolean textField) {
        membershipModel.getSlipControls().get("Sublease Slip").setVisible(rb1);
        membershipModel.getSlipControls().get("Reassign Slip").setVisible(rb2);
        membershipModel.getSlipControls().get("Release Sublease").setVisible(rb3);
        membershipModel.getSlipControls().get("waitList").setVisible(check);
        membershipModel.getSlipControls().get("button").setVisible(button);
        membershipModel.getSlipControls().get("textField").setVisible(textField);
    }
}
