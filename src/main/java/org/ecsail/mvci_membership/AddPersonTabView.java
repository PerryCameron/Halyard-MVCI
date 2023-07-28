package org.ecsail.mvci_membership;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.custom.CustomDatePicker;
import org.ecsail.dto.PersonDTO;
import org.ecsail.enums.MemberType;
import org.ecsail.widgetfx.*;

import java.util.Arrays;

public class AddPersonTabView extends Tab implements Builder<Tab> {


    private final MembershipView membershipView;
    private final MembershipModel membershipModel;


    public AddPersonTabView(MembershipView membershipView) {
        this.membershipView = membershipView;
        this.membershipModel = membershipView.getMembershipModel();
        membershipModel.setNewPerson(new PersonDTO());
    }

    @Override
    public Tab build() {
        Tab tab = new Tab();
        tab.setText("Add");
        tab.setUserData(this);
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2)); // makes outer border
        vBox.setId("custom-tap-pane-frame");
        vBox.getChildren().add(createFields());
        tab.setContent(vBox);
        // once database has added person, this updates UI
        ListenerFx.createListener(membershipModel.addPersonProperty(), addPerson());
        return tab;
    }

    private Runnable addPerson() {
        return () -> {
            // TODO create new personDTO object
            // clear all fields
            System.out.println("Time to clear fields");
        };
    }

    private Node createFields() {
        VBox vBox = VBoxFx.vBoxOf(10.0, new Insets(20,5,5,60));
        vBox.setId("box-background-light");
        VBox.setVgrow(vBox, Priority.ALWAYS); // Don't know why I need this, but ok
        String[] strings = {"First Name","Last Name", "Occupation", "Business", "Birthday", "Member Type", "Button"};
        Arrays.stream(strings).forEach(field -> vBox.getChildren().add(fieldRow(field)));
        return vBox;
    }

    private Node fieldRow(String label) {
        switch (label) {
            case "First Name" -> { return fieldBox(membershipModel.getNewPerson().firstNameProperty(), label); }
            case "Last Name" ->  { return fieldBox(membershipModel.getNewPerson().lastNameProperty(), label); }
            case "Occupation" -> { return fieldBox(membershipModel.getNewPerson().occupationProperty(), label); }
            case "Business" ->  { return fieldBox(membershipModel.getNewPerson().businessProperty(), label); }
            case "Birthday" -> { return fieldDateBox(membershipModel.getNewPerson().birthdayProperty(), label); }
            case "Member Type" -> { return returnTypeComboBox(label); }
            case "Button" -> { return buttonBox(); }
        }
        return null;
    }

    private Node returnTypeComboBox(String label) {
        membershipModel.getAddPersonComboBox().getItems().setAll(MemberType.values());
        membershipModel.getAddPersonComboBox().setValue(MemberType.getByCode(1)); // sets to primary
        membershipModel.getAddPersonComboBox().setPrefWidth(230);
        return labeledField(label, membershipModel.getAddPersonComboBox());
    }

    private Node buttonBox() {
        Button button = ButtonFx.buttonOf("Add", 60);
        button.setOnAction(event -> {
            if (isConsistent())
                membershipView.sendMessage().accept(MembershipMessage.INSERT_PERSON);
        });
        return labeledField("", button);
    }

    private boolean isConsistent() {
        if(!memberTypeAcceptable()) {
            DialogueFx.customAlertWithShow("Can not add " + membershipModel.getSelectedPerson().getFullName(),
                    "You cannot have two " + membershipModel.getAddPersonComboBox().getValue()
                            + " members for the same membership!",
                    Alert.AlertType.ERROR);
            return false;
        }
        if(!firstNamePresent()) {
            DialogueFx.customAlertWithShow("Can not add this person",
                    "You must fill out the first name!",
                    Alert.AlertType.ERROR);
            return false;
        }
        if(!lastNamePresent()) {
            DialogueFx.customAlertWithShow("Can not add this person",
                    "You must fill out the last name!",
                    Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private boolean lastNamePresent() {
        return !membershipModel.getTextFields().get("Last Name").getText().equals("");
    }

    private boolean firstNamePresent() {
        return !membershipModel.getTextFields().get("First Name").getText().equals("");
    }

    private boolean memberTypeAcceptable() {
        switch (membershipModel.getAddPersonComboBox().getValue()) {
            case PRIMARY -> { return !primaryExists(); }
            case SECONDARY -> { return !secondaryExists(); }
            case DEPENDANT -> { return true; }
        }
        return false;
    }

    private boolean primaryExists() {
        return membershipModel.getPeople().stream().anyMatch(p -> p.getMemberType() == 1);
    }

    private boolean secondaryExists() {
        return membershipModel.getPeople().stream().anyMatch(p -> p.getMemberType() == 2);
    }

    private Node fieldBox(Property<?> property, String label) {
        membershipModel.getTextFields().put(label, TextFieldFx.textFieldOf(230, property));
        membershipModel.getTextFields().get(label).focusedProperty()
                    .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                        if (oldValue) {
                            updatePerson(label, membershipModel.getTextFields().get(label).getText());
                        }
                    });
        return labeledField(label, membershipModel.getTextFields().get(label));
    }

    private void updatePerson(String label, String text) {
        switch (label) {
            case "First Name" -> membershipModel.getNewPerson().setFirstName(text);
            case "Last Name" -> membershipModel.getNewPerson().setLastName(text);
            case "Occupation" -> membershipModel.getNewPerson().setOccupation(text);
            case "Business" -> membershipModel.getNewPerson().setBusiness(text);
        }
    }

    private Node fieldDateBox(Property<?> property, String label) {
        CustomDatePicker datePicker = new CustomDatePicker();
        datePicker.setPrefWidth(230);
        datePicker.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (!isFocused){
                datePicker.updateValue();
                membershipModel.getNewPerson().setBirthday(datePicker.getValue().toString());
            }
        });
        return labeledField(label, datePicker);
    }

    private Node labeledField(String label, Node node) {
        HBox hBox = HBoxFx.hBoxOf(new Insets(0, 0, 10, 0), Pos.CENTER_LEFT, 10.0);
        Text text = new Text(label);
        text.setId("text-white");
        HBox hBoxLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 100);
        hBoxLabel.getChildren().add(text);
        HBox hBoxTextField = HBoxFx.hBoxOf(Pos.CENTER, 230);
        hBoxTextField.getChildren().add(node);
        hBox.getChildren().addAll(hBoxLabel, hBoxTextField);
        return hBox;
    }

}
