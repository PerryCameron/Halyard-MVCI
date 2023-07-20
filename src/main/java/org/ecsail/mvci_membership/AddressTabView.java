package org.ecsail.mvci_membership;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.static_tools.StringTools;
import org.ecsail.widgetfx.*;

public class AddressTabView implements Builder<Tab> {
    private final MembershipView membershipView;

    public AddressTabView(MembershipView membershipView) {
        this.membershipView = membershipView;
    }

    @Override
    public Tab build() {
        return TabFx.tabOf("Address", createMainHBox());
    }

    private Node createMainHBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5),"box-background-light",true);
        hBox.getChildren().addAll(createAddress());
        return hBox;
    }

    private Node createAddress() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(10,0,0,20));
        vBox.setSpacing(10);
        vBox.getChildren().add(TextFx.textOf("Address:","text-white"));
        vBox.getChildren().add(createField("Line 1"));
        vBox.getChildren().add(createField("Line 2"));
        vBox.getChildren().add(createCityState());
        vBox.getChildren().add(createField("Postal / Zip Code"));
        return vBox;
    }

    private Node createCityState() {
        ObservableList<String> states = FXCollections.observableArrayList(StringTools.getStateCodes());
        HBox hBox = HBoxFx.hBoxOf(new Insets(0,0,0,0),10);
        TextField textField1 = TextFieldFx.textFieldOf(310, "City");
        setCity(textField1);
        ComboBox<String> stateComboBox = new ComboBox<>(states);
        setState(stateComboBox);
        hBox.getChildren().addAll(textField1,stateComboBox);
        return hBox;
    }

    private Node createField(String prompt) {
        HBox hBox = HBoxFx.hBoxOf(new Insets(0,0,0,0));
        TextField textField = TextFieldFx.textFieldOf(400,prompt);
        switch (prompt) {
            case "Line 1" -> setAddressLine1(textField);
            case "Line 2" -> setAddressLine2(textField);
            case "Postal / Zip Code" -> setZipCode(textField);
        }
        hBox.getChildren().add(textField);
        return hBox;
    }

    private void setAddressLine2(TextField textField) {
        // TODO need to add functionality here
    }

    private void setAddressLine1(TextField textField) {
        textField.setText(membershipView.getMembershipModel().getMembership().getAddress());
        textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            //focus out
            if (oldValue) {  // we have focused and unfocused
                membershipView.getMembershipModel().getMembership().setAddress(textField.getText());
                membershipView.sendMessage().accept(MembershipMessage.UPDATE_MEMBERSHIP_LIST);
            }
        });
    }

    private void setZipCode(TextField textField) {
        textField.setText(membershipView.getMembershipModel().getMembership().getZip());
        textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            //focus out
            if (oldValue) {  // we have focused and unfocused
                membershipView.getMembershipModel().getMembership().setZip(textField.getText());
                membershipView.sendMessage().accept(MembershipMessage.UPDATE_MEMBERSHIP_LIST);
            }
        });
    }

    private void setCity(TextField textField) {
        textField.setText(membershipView.getMembershipModel().getMembership().getCity());
        textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            //focus out
            if (oldValue) {  // we have focused and unfocused
                membershipView.getMembershipModel().getMembership().setCity(textField.getText());
                membershipView.sendMessage().accept(MembershipMessage.UPDATE_MEMBERSHIP_LIST);
            }
        });
    }

    private void setState(ComboBox comboBox) {
        comboBox.setPrefWidth(80);
        comboBox.setValue(membershipView.getMembershipModel().getMembership().getState());
        comboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            membershipView.getMembershipModel().getMembership().setState(newValue.toString());
            membershipView.sendMessage().accept(MembershipMessage.UPDATE_MEMBERSHIP_LIST);
        });
    }

}
