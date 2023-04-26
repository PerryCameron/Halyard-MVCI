package org.ecsail.mvci_membership;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.dto.PersonDTO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.interfaces.Messages;
import org.ecsail.static_calls.MathTools;
import org.ecsail.widgetfx.ButtonFx;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.TextFieldFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.Arrays;
import java.util.Objects;

public class PersonTabView extends Tab implements Builder<Tab>, ConfigFilePaths, Messages {
    private final PersonDTO person;
    private final MembershipModel membershipModel;
    private final MembershipView membershipView;


    public PersonTabView(MembershipView membershipView, PersonDTO personDTO) {
        this.person = personDTO;
        this.membershipModel = membershipView.getMembershipModel();
        this.membershipView = membershipView;
        membershipModel.getStackPaneMap().put(person, new StackPane());
    }

    @Override
    public Tab build() {
        Tab tab = new Tab();
        tab.setText(getMemberType());
        tab.setUserData(this);
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,5,5)); // makes outer border
        vBox.setId("custom-tap-pane-frame");
        BorderPane borderPane = new BorderPane();
        borderPane.setId("box-background-light");
        borderPane.setLeft(createFieldDetails());
        borderPane.setCenter(createPictureFrame());
        borderPane.setBottom(createBottomTabs());
        vBox.getChildren().add(borderPane);
        tab.setContent(vBox);
        return tab;
    }

    private Node createBottomTabs() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(10,5,5,5)); // space between borders
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(detailsTab("Properties"));
        tabPane.getTabs().add(detailsTab("Phone"));
        tabPane.getTabs().add(detailsTab("Email"));
        tabPane.getTabs().add(detailsTab("Awards"));
        tabPane.getTabs().add(detailsTab("Officer"));
        vBox.getChildren().add(tabPane);
        return vBox;
    }

    private Tab detailsTab(String tabType) { // common part of tabs
        Tab tab = new Tab();
        tab.setText(tabType);
        VBox vBoxBorder = VBoxFx.vBoxOf(new Insets(5,5,5,5)); // border for inner tabpane
        vBoxBorder.setId("custom-tap-pane-frame");
        switch (tabType) {
            case "Properties" -> vBoxBorder.getChildren().add(handlePropertiesTab());
            case "Phone" -> vBoxBorder.getChildren().add(handlePhoneTab());
            case "Email" -> vBoxBorder.getChildren().add(handleEmailTab());
            case "Officer" -> vBoxBorder.getChildren().add(handleOfficerTab());
            case "Awards" -> vBoxBorder.getChildren().add(handleAwardsTab());
        }
        tab.setContent(vBoxBorder);
        return tab;
    }

    private Node handleAwardsTab() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(10,10,5,10),"box-background-light");
        hBox.setPrefHeight(130);
        VBox vBox = createButtonBox(createAdd(person), createDelete(person));
        hBox.getChildren().addAll(new AwardTableView(person, membershipView).build(),vBox);
        return hBox;
    }

    private Node handleOfficerTab() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(10,10,5,10),"box-background-light");
        hBox.setPrefHeight(130);
        VBox vBox = createButtonBox(createAdd(person), createDelete(person));
        hBox.getChildren().addAll(new OfficerTableView(person, membershipView).build(),vBox);
        return hBox;
    }

    private Node handleEmailTab() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(10,10,5,10),"box-background-light");
        hBox.setPrefHeight(130);
        VBox vBox = createButtonBox(createAdd(person), createDelete(person), createCopy(person), createEmail(person));
        hBox.getChildren().addAll(new EmailTableView(person, membershipView).build(),vBox);
        return hBox;
    }

    private Node handlePropertiesTab() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5),"box-background-light");
        hBox.getChildren().addAll(getInfoBox(), getRadioBox());
        return hBox;
    }

    private Node getRadioBox() {
        VBox vBox = VBoxFx.vBoxOf(5.0, new Insets(5,5,5,15));
        ToggleGroup tg = new ToggleGroup();
        vBox.getChildren().add(radioButton(tg, "Change " + person.getFirstName() + "'s member type"));
        vBox.getChildren().add(radioButton(tg, "Remove " + person.getFirstName() + " from this membership"));
        vBox.getChildren().add(radioButton(tg, "Delete " + person.getFirstName() + " from database"));
        vBox.getChildren().add(radioButton(tg, "Move " + person.getFirstName() + " to membership (MSID)"));
        vBox.getChildren().add(bottomControlBox());
        return vBox;
    }

    private MessageType mapStringToEnum(String input) {
        switch (input.split(" ")[0]) { // Split the string and get the first word
            case "Change" -> { return MessageType.CHANGE_MEMBER_TYPE; }
            case "Remove" -> { return MessageType.REMOVE_MEMBER_FROM_MEMBERSHIP; }
            case "Delete" -> { return MessageType.DELETE_MEMBER_FROM_DATABASE; }
            case "Move" -> { return MessageType.MOVE_MEMBER_TO_MEMBERSHIP; }
        }
        return MessageType.NONE;
    }

    private Node bottomControlBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5), 30);
        TextField textField = TextFieldFx.textFieldOf(120, "MSID");
        membershipModel.getPersonTextField().put(person,textField);
        membershipModel.getStackPaneMap().get(person).getChildren().addAll(createComboBox(), textField, createRegion());
        hBox.getChildren().addAll(membershipModel.getStackPaneMap().get(person), createSubmit());
        return hBox;
    }

    private Region createRegion() {
        Region region = new Region();
        region.setPrefSize(120,10);
        region.setMinSize(120,10);
        return region;
    }

    private Node createComboBox() {
        final ComboBox<String> comboBox = new ComboBox<>();
        membershipModel.getPersonComboBox().put(person,comboBox);
        comboBox.setPrefWidth(120);
        comboBox.getItems().clear();
        switch (person.getMemberType()) {
            case 1 -> comboBox.getItems().addAll("Secondary", "Dependent");
            case 2 -> comboBox.getItems().addAll("Primary", "Dependent");
            default -> comboBox.getItems().addAll("Primary", "Secondary");
        }
        comboBox.getSelectionModel().selectFirst();
        return comboBox;
    }

    private Control radioButton(ToggleGroup tg, String name) {
        RadioButton radioButton = new RadioButton(name);
        radioButton.setToggleGroup(tg);
        radioButton.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                changeStackPane(mapStringToEnum(name));
                membershipModel.getSelectedRadioForPerson().put(person, radioButton);
            }
        });
        return radioButton;
    }

    private void changeStackPane(MessageType action) {
        membershipModel.getStackPaneMap().get(person).getChildren().forEach(child -> {
            if (child instanceof ComboBox) {
                child.setVisible(action == MessageType.CHANGE_MEMBER_TYPE);
                child.setManaged(action == MessageType.CHANGE_MEMBER_TYPE);
            } else if (child instanceof TextField) {
                child.setVisible(action == MessageType.MOVE_MEMBER_TO_MEMBERSHIP);
                child.setManaged(action == MessageType.MOVE_MEMBER_TO_MEMBERSHIP);
            } else if (child instanceof Region) {
                child.setVisible(action == MessageType.DELETE_MEMBER_FROM_DATABASE || action == MessageType.REMOVE_MEMBER_FROM_MEMBERSHIP);
                child.setManaged(action == MessageType.DELETE_MEMBER_FROM_DATABASE || action == MessageType.REMOVE_MEMBER_FROM_MEMBERSHIP);
            }
        });
    }

    private Node getInfoBox() {
        VBox vBox = new VBox(7);
        vBox.getChildren().addAll(
                new Label("Age: " + MathTools.calculateAge(person.getBirthday())),
                new Label("Person ID: " + person.getP_id()),
                new Label("MSID: " + person.getMs_id()));
        return vBox;
    }

    private Node handlePhoneTab() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(10,10,5,10),"box-background-light");
        hBox.setPrefHeight(130);
        VBox vBox = createButtonBox(createAdd(person), createDelete(person), createCopy(person));
        hBox.getChildren().addAll(new PhoneTableView(person, membershipView).build(),vBox);
        return hBox;
    }

    private Control createSubmit() {
        Button button = ButtonFx.buttonOf("Submit",60);
        button.setOnAction(event -> {
            // get selected radio button
            RadioButton rb = membershipModel.getSelectedRadioForPerson().get(person);
            Messages.MessageType type = mapStringToEnum(rb.getText());
            membershipView.getPersonEdit().accept(type,createData(type));
        });
        return button;
    }

    private Object createData(MessageType message) {
        String returnString;
        switch (message) {
            case CHANGE_MEMBER_TYPE -> returnString = membershipModel.getPersonComboBox().get(person).getValue();
            case MOVE_MEMBER_TO_MEMBERSHIP -> returnString = membershipModel.getPersonTextField().get(person).getText();
            default -> returnString = "NONE";
        }
        return returnString;
    }

    private Button createCopy(Object object) {
        Button button = ButtonFx.buttonOf("Copy",60);
        // TODO Add listener
        return button;
    }

    private Button createDelete(Object object) {
        Button button = ButtonFx.buttonOf("Delete",60);
        // TODO Add listener
        return button;
    }

    private Button createAdd(Object object) {
        Button button = ButtonFx.buttonOf("Add",60);
        // TODO Add listener
        return button;
    }

    private Button createEmail(Object object) {
        Button button = ButtonFx.buttonOf("Email",60);
        // TODO Add listener
        return button;
    }

    private VBox createButtonBox(Button... buttons) {
        VBox vBox = VBoxFx.vBoxOf(7.0, new Insets(0,5,5,15));
        Arrays.stream(buttons).iterator().forEachRemaining(button -> vBox.getChildren().add(button));
        return vBox;
    }

    private Node createTabContent(String tabType) { // selects correct content
        HBox hBox = new HBox();
        hBox.setId("box-background-light");
        return hBox;
    }

    private Node createPictureFrame() {
        VBox vBoxPicture = VBoxFx.vBoxOf(new Insets(12, 5, 0, 7));
        VBox vBoxFrame = VBoxFx.vBoxOf(196,226,new Insets(2, 2, 2, 2),"box-frame-dark");
        Image memberPhoto = new Image(Objects.requireNonNull(getClass().getResourceAsStream(DEFAULT_PHOTO)));
        ImageView imageView = new ImageView(memberPhoto);
        imageView.setOnMouseExited(ex -> vBoxFrame.setStyle("-fx-background-color: #010e11;"));
        imageView.setOnMouseEntered(en -> vBoxFrame.setStyle("-fx-background-color: #201ac9;"));
        vBoxFrame.getChildren().add(imageView);
        vBoxPicture.getChildren().add(vBoxFrame);
        return vBoxPicture;
    }

    private Node createFieldDetails() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(20,0,0,20));
        vBox.getChildren().add(fieldBox(person.firstNameProperty(), "First Name"));
        vBox.getChildren().add(fieldBox(person.lastNameProperty(), "Last Name"));
        vBox.getChildren().add(fieldBox(person.nickNameProperty(), "Nickname"));
        vBox.getChildren().add(fieldBox(person.occupationProperty(), "Occupation"));
        vBox.getChildren().add(fieldBox(person.businessProperty(), "Business"));
        vBox.getChildren().add(fieldBox(person.birthdayProperty(), "Birthday"));
        return vBox;
    }

    private Node fieldBox(Property<?> property, String label) {
        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER_LEFT, new Insets(0, 0, 10, 0), 10.0);
        TextField textField = TextFieldFx.textFieldOf(150, property);
        textField.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    updatePersonDTO(label, textField.getText());
            if (oldValue) { membershipView.getPersonEdit().accept(MessageType.UPDATE,label); }
        });
        Text text = new Text(label);
        text.setId("text-white");
        HBox hBoxLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 80.0);
        hBoxLabel.getChildren().add(text);
        HBox hBoxTextField = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 170.0);
        hBoxTextField.getChildren().add(textField);
        hBox.getChildren().addAll(hBoxLabel, hBoxTextField);
        return hBox;
    }

    private void updatePersonDTO(String label, String text) {
        switch (label) {
            case "First Name" -> membershipModel.getSelectedPerson().setFirstName(text);
            case "Last Name" -> membershipModel.getSelectedPerson().setLastName(text);
            case "Nickname" -> membershipModel.getSelectedPerson().setNickName(text);
            case "Occupation" -> membershipModel.getSelectedPerson().setOccupation(text);
            case "Business" -> membershipModel.getSelectedPerson().setBusiness(text);
            case "Birthday" -> membershipModel.getSelectedPerson().setBirthday(text);
        }
    }

    private String getMemberType() {
        String memberTypeString = switch (person.getMemberType()) {
            case 1 -> "Primary";
            case 2 -> "Secondary";
            case 3 -> "Dependant";
            default -> "Unknown";
        };
        return memberTypeString;
    }

    public PersonDTO getPerson() {
        return person;
    }
}
