package org.ecsail.mvci_membership;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.custom.CustomDatePicker;
import org.ecsail.dto.PersonDTO;
import org.ecsail.enums.MemberType;
import org.ecsail.widgetfx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;

public class AddPersonTabView extends Tab implements Builder<Tab> {


    private HashMap<String, TextField> textFieldHashMap = new HashMap<>();
    private CustomDatePicker datePicker = new CustomDatePicker();
    private final MembershipView membershipView;
    private final MembershipModel membershipModel;
    private ComboBox<MemberType> comboBox = new ComboBox<>();
    private static final Logger logger = LoggerFactory.getLogger(AddPersonTabView.class);
    private final PersonDTO personDTO;
    public AddPersonTabView(MembershipView membershipView) {
        this.membershipView = membershipView;
        this.membershipModel = membershipView.getMembershipModel();
        this.personDTO = new PersonDTO(membershipModel.getMembership().getMsId());
    }

    @Override
    public Tab build() {
        this.setText("Add");
        this.setUserData(this);
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2)); // makes outer border
        vBox.setId("custom-tap-pane-frame");
        vBox.getChildren().add(createFields());
        this.setContent(vBox);
        return this;
    }

    protected void clearPersonDTO() {
        personDTO.setFirstName("");
        personDTO.setLastName("");
        personDTO.setNickName("");
        personDTO.setOccupation("");
        personDTO.setBusiness("");
        personDTO.setBirthday(null);
        personDTO.setMemberType(1);
        textFieldHashMap.values().forEach(textField -> textField.setText(""));
        comboBox.setValue(MemberType.getByCode(personDTO.getMemberType()));
        datePicker.setValue(null);
    }

    private Node createFields() {
        VBox vBox = VBoxFx.vBoxOf(10.0, new Insets(60,5,5,60));
        vBox.setId("box-background-light");
        VBox.setVgrow(vBox, Priority.ALWAYS); // Don't know why I need this, but ok
        String[] strings = {"First Name","Last Name", "Occupation", "Business", "Birthday", "Member Type", "Button"};
        Arrays.stream(strings).forEach(field -> vBox.getChildren().add(fieldRow(field)));
        return vBox;
    }

    private Node fieldRow(String label) {
        switch (label) {
            case "First Name" -> { return fieldBox(personDTO.firstNameProperty(), label); }
            case "Last Name" ->  { return fieldBox(personDTO.lastNameProperty(), label); }
            case "Occupation" -> { return fieldBox(personDTO.occupationProperty(), label); }
            case "Business" ->  { return fieldBox(personDTO.businessProperty(), label); }
            case "Birthday" -> { return fieldDateBox(personDTO.birthdayProperty(), label); }
            case "Member Type" -> { return returnTypeComboBox(label); }
            case "Button" -> { return buttonBox(); }
        }
        return null;
    }

    private Node returnTypeComboBox(String label) {
        comboBox.getItems().setAll(MemberType.values());
        comboBox.setValue(MemberType.getByCode(1)); // sets to primary
        comboBox.setPrefWidth(230);
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            personDTO.setMemberType(newValue.getCode());
        });
        return labeledField(label, comboBox);
    }

    private Node buttonBox() {
        Button button = ButtonFx.buttonOf("Add", 60);
        button.setOnAction(event -> {
            if (isConsistent()) {
                membershipModel.setSelectedPerson(personDTO); // not sure if they are already selected here.
                // this sends messages to insert
                membershipView.sendMessage().accept(MembershipMessage.INSERT_PERSON);
            }
        });
        return labeledField("", button);
    }

    private boolean isConsistent() {
        if(!memberTypeAcceptable()) {
            DialogueFx.customAlertWithShow("Can not add " + membershipModel.getSelectedPerson().getFullName(),
                    "You cannot have two " + comboBox.getValue()
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
        return !textFieldHashMap.get("Last Name").getText().equals("");
    }

    private boolean firstNamePresent() {
        return !textFieldHashMap.get("First Name").getText().equals("");
    }

    private boolean memberTypeAcceptable() {
        switch (comboBox.getValue()) {
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
        textFieldHashMap.put(label, TextFieldFx.textFieldOf(230, property));
        textFieldHashMap.get(label).focusedProperty()
                    .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                        if (oldValue) {
                            updatePerson(label, textFieldHashMap.get(label).getText());
                        }
                    });
        return labeledField(label, textFieldHashMap.get(label));
    }

    private void updatePerson(String label, String text) {
        switch (label) {
            case "First Name" -> personDTO.setFirstName(text);
            case "Last Name" -> personDTO.setLastName(text);
            case "Occupation" -> personDTO.setOccupation(text);
            case "Business" -> personDTO.setBusiness(text);
        }
    }

    private Node fieldDateBox(Property<?> property, String label) {
        datePicker.setPrefWidth(230);
        datePicker.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (!isFocused){
                datePicker.updateValue();
                personDTO.setBirthday(datePicker.getValue());
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

    public PersonDTO getPersonDTO() {
        return personDTO;
    }
}
