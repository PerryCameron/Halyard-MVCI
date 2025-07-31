package org.ecsail.mvci.membership.mvci.person;


import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.connection.Mail;
import org.ecsail.custom.CustomDatePicker;
import org.ecsail.enums.MemberType;
import org.ecsail.fx.EmailFx;
import org.ecsail.fx.PersonFx;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.interfaces.ObjectType;
//import org.ecsail.mvci.membership.MembershipMessage;
import org.ecsail.mvci.membership.components.*;
import org.ecsail.static_tools.DateTools;
import org.ecsail.widgetfx.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static org.ecsail.interfaces.ObjectType.Dto.*;
import static org.ecsail.interfaces.ObjectType.Dto.Phone;

public class PersonView implements Builder<Tab>, ConfigFilePaths, ObjectType {
    private final PersonModel personModel;
    Consumer<PersonMessage> action;

    public PersonView(PersonModel personModel, Consumer<PersonMessage> m) {
        this.personModel = personModel;
        this.action = m;
    }

    @Override
    public Tab build() {
        personModel.getTab().setText(getMemberType());
        personModel.getTab().setUserData(this);
        VBox vBox = VBoxFx.vBoxOf(new Insets(2, 2, 2, 2)); // makes outer border
        vBox.setId("custom-tap-pane-frame");
        BorderPane borderPane = new BorderPane();
        borderPane.setId("box-background-light");
        borderPane.setLeft(createFieldDetails());
        borderPane.setCenter(createPictureFrame());
        borderPane.setBottom(createBottomStacks());
        vBox.getChildren().add(borderPane);
        personModel.getTab().setContent(vBox);
        personModel.getTab().selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if(!personModel.imageLoadedProperty().get())
                action.accept(PersonMessage.GET_IMAGE);
            }
        });
        if(personModel.getPersonDTO().memberTypeProperty().get() == 1) action.accept(PersonMessage.GET_IMAGE);
        return personModel.getTab();
    }

    private Node createBottomStacks() {
        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER_LEFT, new Insets(5, 5, 20, 5), true); // space between borders
        personModel.getPersonInfoHBoxMap().put("Properties", setStack(Properties));
        personModel.getPersonInfoHBoxMap().put("Phone", setStack(Phone));
        personModel.getPersonInfoHBoxMap().put("Email", setStack(Email));
        personModel.getPersonInfoHBoxMap().put("Awards", setStack(Award));
        personModel.getPersonInfoHBoxMap().put("Officer", setStack(Officer));
        hBox.getChildren().addAll(selectionButtons(), propertiesStackPane());
        return hBox;
    }

    private Node propertiesStackPane() {
        VBox vBoxBorder = VBoxFx.vBoxOf(new Insets(2, 2, 2, 2)); // border for inner hbox
        HBox.setHgrow(vBoxBorder, Priority.ALWAYS);
        vBoxBorder.setId("custom-tap-pane-frame");
        StackPane stackPane = new StackPane();
        for (HBox hbox : personModel.getPersonInfoHBoxMap().values()) stackPane.getChildren().add(hbox);
        vBoxBorder.getChildren().add(stackPane);
        return vBoxBorder;
    }

    private void setVisibleHBox(boolean properties, boolean phone, boolean email, boolean award, boolean officer) {
        personModel.getPersonInfoHBoxMap().get("Properties").setVisible(properties);
        personModel.getPersonInfoHBoxMap().get("Phone").setVisible(phone);
        personModel.getPersonInfoHBoxMap().get("Email").setVisible(email);
        personModel.getPersonInfoHBoxMap().get("Awards").setVisible(award);
        personModel.getPersonInfoHBoxMap().get("Officer").setVisible(officer);
    }

    private Node selectionButtons() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0, 0, 0, 0));
        ToggleGroup tg = new ToggleGroup();
        Region region = RegionFx.regionHeightOf(7);
        vBox.getChildren().add(region);
        vBox.getChildren().add(buttonControl("Properties", tg));
        vBox.getChildren().add(buttonControl("Phone", tg));
        vBox.getChildren().add(buttonControl("Email", tg));
        vBox.getChildren().add(buttonControl("Awards", tg));
        vBox.getChildren().add(buttonControl("Officer", tg));
        return vBox;
    }

    private Node buttonControl(String label, ToggleGroup tg) {
        ToggleButton button = ButtonFx.toggleButtonOf(label, 100, tg);
        switch (label) {
            case "Properties" -> {
                button.setOnAction(event -> setVisibleHBox(true, false, false, false, false));
                button.fire();
            }
            case "Phone" -> button.setOnAction(event -> setVisibleHBox(false, true, false, false, false));
            case "Email" -> button.setOnAction(event -> setVisibleHBox(false, false, true, false, false));
            case "Awards" -> button.setOnAction(event -> setVisibleHBox(false, false, false, true, false));
            case "Officer" -> button.setOnAction(event -> setVisibleHBox(false, false, false, false, true));
        }
        button.setPrefHeight(30);
        return button;
    }

    private HBox setStack(ObjectType.Dto type) {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5, 5, 5, 5), "box-background-light", true);
        switch (type) {
            case Officer -> {
                VBox vBox = createButtonBox(createAddButton(Officer), createDeleteButton(Officer));
                personModel.officerTableViewProperty().set(new OfficerTableView(this).build());
                hBox.getChildren().addAll(personModel.officerTableViewProperty().get(), vBox);
            }
            case Award -> {
                VBox vBox = createButtonBox(createAddButton(Award), createDeleteButton(Award));
                personModel.awardTableViewProperty().set(new AwardTableView(this).build());
                hBox.getChildren().addAll(personModel.awardTableViewProperty().get(), vBox);
            }
            case Email -> {
                TableView<EmailFx> tableView = new EmailTableView(this).build();
                personModel.emailTableViewProperty().set(tableView);
                VBox vBox = createButtonBox(createAddButton(Email), createDeleteButton(Email), createCopyButton(Email), createEmailButton());
                hBox.getChildren().addAll(personModel.emailTableViewProperty().get(), vBox);
            }
            case Phone -> {
                VBox vBox = createButtonBox(createAddButton(Phone), createDeleteButton(Phone), createCopyButton(Phone));
                personModel.phoneTableViewProperty().set(new PhoneTableView(this).build());
                hBox.getChildren().addAll(personModel.phoneTableViewProperty().get(), vBox);
            }
            case Properties -> hBox.getChildren().addAll(getInfoBox(), getRadioBox());
        }
        return hBox;
    }

    private Node getRadioBox() {
        VBox vBox = VBoxFx.vBoxOf(5.0, new Insets(5, 5, 5, 5));
        ToggleGroup tg = new ToggleGroup();
        vBox.getChildren().add(radioButton(tg, "Change " + personModel.getPersonDTO().getFirstName() + "'s member type"));
        vBox.getChildren().add(radioButton(tg, "Remove " + personModel.getPersonDTO().getFirstName() + " from this membership"));
        vBox.getChildren().add(radioButton(tg, "Delete " + personModel.getPersonDTO().getFirstName() + " from database"));
        vBox.getChildren().add(radioButton(tg, "Move " + personModel.getPersonDTO().getFirstName() + " to membership ..."));
        vBox.getChildren().add(bottomControlBox());
        return vBox;
    }

    private PersonMessage mapStringToEnum(String input) {
        personModel.getMembershipModel().setSelectedPerson(personModel.getPersonDTO());
        switch (input.split(" ")[0]) { // Split the string and get the first word
            case "Change" -> {
                return PersonMessage.CHANGE_MEMBER_TYPE;
            }
            case "Remove" -> {
                return PersonMessage.DETACH_MEMBER_FROM_MEMBERSHIP;
            }
            case "Delete" -> {
                return PersonMessage.DELETE_MEMBER_FROM_DATABASE;
            }
            case "Move" -> {
                return PersonMessage.MOVE_MEMBER_TO_MEMBERSHIP;
            }
        }
        return PersonMessage.NONE;
    }

    private Node bottomControlBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5, 5, 5, 5), 30);
        TextField textField = TextFieldFx.textFieldOf(120, "MSID");
        if (personModel.stackPaneProperty().get() != null) {
            personModel.stackPaneProperty().get().getChildren().addAll(createComboBox(), textField, createRegion());
        } else {
            System.out.println("Stackpane is null for " + personModel.getPersonDTO());  // Grok the stackPane is null here, can you see why?
        }
        hBox.getChildren().addAll(personModel.stackPaneProperty().get(), createSubmit());
        return hBox;
    }

    private Region createRegion() {
        Region region = new Region();
        region.setPrefSize(120, 10);
        region.setMinSize(120, 10);
        return region;
    }

    private Node createComboBox() {
        personModel.comboBoxProperty().get().setPrefWidth(120);
        personModel.comboBoxProperty().get().getItems().clear();
        switch (personModel.getPersonDTO().getMemberType()) {
            case 1 -> personModel.comboBoxProperty().get().getItems().addAll("Secondary", "Dependent");
            case 2 -> personModel.comboBoxProperty().get().getItems().addAll("Primary", "Dependent");
            default -> personModel.comboBoxProperty().get().getItems().addAll("Primary", "Secondary");
        }
        personModel.comboBoxProperty().get().getSelectionModel().selectFirst();
        return personModel.comboBoxProperty().get();
    }

    private Control radioButton(ToggleGroup tg, String name) {
        RadioButton radioButton = new RadioButton(name);
        radioButton.setToggleGroup(tg);
        radioButton.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                changeStackPane(mapStringToEnum(name));
                personModel.radioButtonProperty().set(radioButton);
            }
        });
        return radioButton;
    }

    private void changeStackPane(PersonMessage action) {
        personModel.stackPaneProperty().get().getChildren().forEach(child -> {
            if (child instanceof ComboBox) {
                child.setVisible(action == PersonMessage.CHANGE_MEMBER_TYPE);
                child.setManaged(action == PersonMessage.CHANGE_MEMBER_TYPE);
            } else if (child instanceof TextField) {
                child.setVisible(action == PersonMessage.MOVE_MEMBER_TO_MEMBERSHIP);
                child.setManaged(action == PersonMessage.MOVE_MEMBER_TO_MEMBERSHIP);
            } else if (child instanceof Region) {
                child.setVisible(action == PersonMessage.DELETE_MEMBER_FROM_DATABASE || action == PersonMessage.DETACH_MEMBER_FROM_MEMBERSHIP);
                child.setManaged(action == PersonMessage.DELETE_MEMBER_FROM_DATABASE || action == PersonMessage.DETACH_MEMBER_FROM_MEMBERSHIP);
            }
        });
    }

    private Node getInfoBox() {
        VBox vBox = VBoxFx.vBoxOf(7.0, new Insets(15, 10, 0, 5));
        vBox.setId("custom-tap-pane-frame");
        if (personModel.getPersonDTO().getBirthday() != null)
            personModel.getAgeLabel().setText("Age: " + DateTools.calculateAge(personModel.getPersonDTO().getBirthday()));
        vBox.getChildren().add(personModel.getAgeLabel());
        vBox.getChildren().add(new Label("Person ID: " + personModel.getPersonDTO().getpId()));
        vBox.getChildren().add(new Label("MSID: " + personModel.getPersonDTO().getMsId()));
        return vBox;
    }

    private Control createSubmit() {
        Button button = ButtonFx.buttonOf("Submit", 60);
        button.setOnAction(event -> {
            switch (personModel.radioButtonProperty().get().getText().split(" ")[0]) { // Split the string and get the first word
                case "Change" -> changeMemberType();
                case "Remove" -> {
                    if (userChoosesToRemovePerson()) removeMemberFromMembership();
                }
                case "Delete" -> deletePerson();
                case "Move" -> movePersonToMembership();
            }
        });
        return button;
    }

    private void movePersonToMembership() {
        action.accept(PersonMessage.MOVE_MEMBER_TO_MEMBERSHIP);
        // need to place MSID
    }

    private void removeMemberFromMembership() {
        if (isMemberType(MemberType.PRIMARY)) {
            if (hasMemberType(MemberType.SECONDARY)) {
                removePersonFromMembership(PersonMessage.DETACH_PRIMARY_MEMBER_FROM_MEMBERSHIP);
                // secondary gets changed after return message to membershipView
            } else
                DialogueFx.errorAlert("Can not remove " + personModel.getMembershipModel().getSelectedPerson().getFullName()
                        , "Can not remove primary without secondary to replace them");
        } else removePersonFromMembership(PersonMessage.DETACH_MEMBER_FROM_MEMBERSHIP);
    }

    private void removePersonFromMembership(PersonMessage message) {
        personModel.getPersonDTO().setOldMsid(personModel.getPersonDTO().getMsId());
        personModel.getPersonDTO().setMsId(0);
        personModel.getPersonDTO().setMemberType(0);
        personModel.getMembershipModel().setSelectedPerson(personModel.getPersonDTO());
        action.accept(message);
    }

    private boolean isMemberType(MemberType memberType) {
        return personModel.getMembershipModel().getSelectedPerson().getMemberType() == MemberType.getCode(memberType);
    }

    private void changeMemberType() {
        String type = personModel.comboBoxProperty().get().getValue();
        if (isMemberType(MemberType.PRIMARY)) {
            changePrimaryToType(type);
        } else if (isMemberType(MemberType.SECONDARY)) {
            changeSecondaryToType(type);
        } else {
            changeDependentToType(type);
        }
    }

    private void changeDependentToType(String type) {
        switch (type) {
            case "Primary" -> {
                if (hasMemberType(MemberType.PRIMARY)) personModel.messageProperty().set(PersonMessage.SWAP_DEPENDENT_WITH_PRIMARY);
            }
            case "Secondary" -> {
                if (hasMemberType(MemberType.SECONDARY)) personModel.messageProperty().set(PersonMessage.SWAP_DEPENDENT_WITH_SECONDARY);
            }
        }
        action.accept(PersonMessage.CHANGE_MEMBER_TYPE);
    }

    private void changeSecondaryToType(String type) {
        switch (type) {
            case "Primary" -> {
                if (hasMemberType(MemberType.PRIMARY)) personModel.messageProperty().set(PersonMessage.SWAP_PRIMARY_AND_SECONDARY);// System.out.println("Swap primary and secondary");
            }
            case "Dependent" -> personModel.messageProperty().set(PersonMessage.MOVE_SECONDARY_TO_DEPENDENT);//System.out.println("make secondary a dependant");
        }
        action.accept(PersonMessage.CHANGE_MEMBER_TYPE);
    }

    private void changePrimaryToType(String type) {
        switch (type) {
            case "Secondary" -> {
                if (hasMemberType(MemberType.SECONDARY)) personModel.messageProperty().set(PersonMessage.SWAP_PRIMARY_AND_SECONDARY); //System.out.println("Swap primary and secondary");
            }
            case "Dependent" -> {
                if (hasMemberType(MemberType.SECONDARY)) {
                    personModel.messageProperty().set(PersonMessage.MAKE_PRIMARY_DEPENDENT_AND_SECONDARY_PRIMARY);
                } else {
                    DialogueFx.errorAlert("Can not move " + personModel.getMembershipModel().getSelectedPerson().getFullName()
                            , "Can not move primary without secondary to replace them");
                }
            }
        }
        action.accept(PersonMessage.CHANGE_MEMBER_TYPE);
    }

    // had to change this when remove people directly from membership model
    private boolean hasMemberType(MemberType memberType) {
        return personModel.getMembershipModel().membershipProperty().get().getPeople().stream()
                .anyMatch(p -> p.getMemberType() == MemberType.getCode(memberType));
    }

    // had to change this when remove people directly from membership model
    private boolean hasOtherMembers() {
        return personModel.getMembershipModel().membershipProperty().get().getPeople().size() > 1;
    }

    private Button createCopyButton(ObjectType.Dto type) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        Button button = ButtonFx.buttonOf("Copy", 60);
        switch (type) {
            case Email -> button.setOnAction(event -> {
                content.putString(personModel.selectedEmailProperty().get().getEmail());
                clipboard.setContent(content);
            });
            case Phone -> button.setOnAction(event -> {
                content.putString(personModel.selectedPhoneProperty().get().getPhone());
                clipboard.setContent(content);
            });
        }
        return button;
    }

    private Button createDeleteButton(ObjectType.Dto type) {
        Button button = ButtonFx.buttonOf("Delete", 60);
        switch (type) {
            case Phone -> button.setOnAction(onClick -> deletePhone());
            case Email -> button.setOnAction(onClick -> deleteEmail());
            case Award -> button.setOnAction(onClick -> deleteAward());
            case Officer -> button.setOnAction(onClick -> deleteOfficer());
        }
        return button;
    }

    private boolean userChoosesToRemovePerson() {
        String header = "Remove person from membership";
        String message = "Are you sure you want to remove " + personModel.getMembershipModel().getSelectedPerson().getFullName() +
                " from this membership?\n\nIt will leave this person's record free floating in the database, " +
                "however " + personModel.getMembershipModel().getSelectedPerson().getFullName() + " can be looked back up in " +
                "the People tab and reattached to this or another membership.";
        Alert alert = DialogueFx.customAlert(header, message, Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void deletePerson() {
        String[] strings = {
                "Delete Person",
                "Are you sure you want to delete " + personModel.getMembershipModel().getSelectedPerson().getFullName() + "?",
                "Missing Selection",
                "You need to select a person first"};
        if (DialogueFx.verifyAction(strings, personModel.getMembershipModel().getSelectedPerson()))
            action.accept(PersonMessage.DELETE_MEMBER_FROM_DATABASE);
    }

    private void deleteAward() {
        String[] strings = {
                "Delete Award",
                "Are you sure you want to delete this award entry?",
                "Missing Selection",
                "You need to select an award entry first"};
        if (DialogueFx.verifyAction(strings, personModel.selectedAwardProperty().get()))
            action.accept(PersonMessage.DELETE_AWARD);
    }

    private void deleteEmail() {
        String[] strings = {
                "Delete Email",
                "Are you sure you want to delete this email entry?",
                "Missing Selection",
                "You need to select an email entry first"};
        if (DialogueFx.verifyAction(strings, personModel.selectedEmailProperty().get()))
            action.accept(PersonMessage.DELETE_EMAIL);
    }

    private void deleteOfficer() {
        String[] strings = {
                "Delete Officer",
                "Are you sure you want to delete this officer entry?",
                "Missing Selection",
                "You need to select an officer entry first"};
        if (DialogueFx.verifyAction(strings, personModel.selectedPositionProperty().get()))
            action.accept(PersonMessage.DELETE_POSITION);
    }

    private void deletePhone() {
        String[] strings = {
                "Delete Phone",
                "Are you sure you want to delete this phone number?",
                "Missing Selection",
                "You need to select a phone first"};
        if (DialogueFx.verifyAction(strings, personModel.selectedPhoneProperty().get()))
            action.accept(PersonMessage.DELETE_PHONE);
    }

    private Button createAddButton(ObjectType.Dto type) {
        Button button = ButtonFx.buttonOf("Add", 60);
        switch (type) {
            case Phone -> button.setOnAction(event ->
                    action.accept(PersonMessage.INSERT_PHONE));
            case Email -> button.setOnAction(event ->
                    action.accept(PersonMessage.INSERT_EMAIL));
            case Award -> button.setOnAction(event ->
                    action.accept(PersonMessage.INSERT_AWARD));
            case Officer -> button.setOnAction(event ->
                    action.accept(PersonMessage.INSERT_POSITION));
        }
        return button;
    }

    private Button createEmailButton() {
        Button button = ButtonFx.buttonOf("Email", 60);
        button.setOnAction(event -> {
            if (personModel.selectedEmailProperty().get() != null) {// make sure something is selected
                Mail.composeEmail(personModel.selectedEmailProperty().get().getEmail(), "ECSC", "");
            }
        });
        return button;
    }

    private VBox createButtonBox(Button... buttons) {
        VBox vBox = VBoxFx.vBoxOf(7.0, new Insets(5, 5, 5, 5));
        Arrays.stream(buttons).iterator().forEachRemaining(button -> vBox.getChildren().add(button));
        return vBox;
    }

    private Node createPictureFrame() {
        VBox vBoxPicture = new VBox();
        vBoxPicture.setPrefHeight(275);
        vBoxPicture.setMinHeight(275);
        vBoxPicture.setAlignment(Pos.CENTER);
        // Load the image
        Image memberPhoto = new Image(Objects.requireNonNull(getClass().getResourceAsStream(DEFAULT_PHOTO)));
        ImageView imageView = new ImageView(memberPhoto);
        personModel.imageViewPropertyProperty().set(imageView);
        // Size the ImageView with maximum bounds, preserving aspect ratio
        imageView.setFitWidth(192); // Maximum width
        imageView.setFitHeight(222); // Maximum height
        imageView.setPreserveRatio(true); // Maintain aspect ratio
        // Create a StackPane to hold the ImageView and apply the border
        StackPane framePane = new StackPane();
        framePane.setStyle(
                "-fx-border-color: black; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-style: solid; " +
                        "-fx-background-color: #ffffff; " +
                        "-fx-padding: 5;" // Space between image and border
        );
        // Prevent StackPane from expanding beyond the ImageView's size
        framePane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        // Add the ImageView to the StackPane
        framePane.getChildren().add(imageView);
        // Dynamically size the StackPane to fit the ImageView's actual bounds
        imageView.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
            double width = newValue.getWidth();
            double height = newValue.getHeight();
            double borderWidth = 2; // Matches -fx-border-width
            // Set preferred and maximum size to match ImageView plus padding and border
            framePane.setPrefSize(width + 2 * borderWidth, height + 2 * borderWidth);
            framePane.setMaxSize(width + 2 * borderWidth, height + 2 * borderWidth);
        });
        // Set the mouse click handler on the ImageView
        imageView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                PictureAlert pictureAlert = new PictureAlert(this, personModel.getMembershipView());
                Alert alert = pictureAlert.build();
                alert.showAndWait();
            }
        });
        // Add the StackPane to the VBox
        vBoxPicture.getChildren().add(framePane);
        return vBoxPicture;
    }

    private Node createFieldDetails() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(20, 0, 0, 20));
        vBox.getChildren().add(fieldBox(personModel.getPersonDTO().firstNameProperty(), "First Name"));
        vBox.getChildren().add(fieldBox(personModel.getPersonDTO().lastNameProperty(), "Last Name"));
        vBox.getChildren().add(fieldBox(personModel.getPersonDTO().nickNameProperty(), "Nickname"));
        vBox.getChildren().add(fieldBox(personModel.getPersonDTO().occupationProperty(), "Occupation"));
        vBox.getChildren().add(fieldBox(personModel.getPersonDTO().businessProperty(), "Business"));
        vBox.getChildren().add(fieldDateBox(personModel.getPersonDTO().birthdayProperty(), "Birthday"));
        return vBox;
    }

    private Node fieldDateBox(Property<?> property, String label) {
        CustomDatePicker datePicker = new CustomDatePicker();
        datePicker.setPrefWidth(150);
        datePicker.setValue((LocalDate) property.getValue());
        datePicker.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (!isFocused) {
                datePicker.updateValue();
                updatePersonDTO(label, datePicker.getValue().toString());
                personModel.getAgeLabel().setText("Age: " + DateTools.calculateAge(datePicker.getValue()));
                personModel.getMembershipModel().setSelectedPerson(personModel.getPersonDTO());
                action.accept(PersonMessage.UPDATE_PERSON);
            }
        });
        return labeledField(label, datePicker);
    }

    private Node fieldBox(Property<?> property, String label) {
        TextField textField = TextFieldFx.textFieldOf(150, property);
        textField.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    updatePersonDTO(label, textField.getText());
                    if (oldValue) {
                        personModel.getMembershipModel().setSelectedPerson(personModel.getPersonDTO());
                        action.accept(PersonMessage.UPDATE_PERSON);
                    }
                });
        return labeledField(label, textField);
    }

    private Node labeledField(String label, Node node) {
        HBox hBox = HBoxFx.hBoxOf(new Insets(0, 0, 10, 0), Pos.CENTER_LEFT, 10.0);
        Text text = new Text(label);
        text.setId("text-white");
        HBox hBoxLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 80.0);
        hBoxLabel.getChildren().add(text);
        HBox hBoxTextField = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 150.0);
        hBoxTextField.getChildren().add(node);
        hBox.getChildren().addAll(hBoxLabel, hBoxTextField);
        return hBox;
    }

    private void updatePersonDTO(String label, String text) {
        switch (label) {
            case "First Name" -> personModel.getMembershipModel().getSelectedPerson().setFirstName(text);
            case "Last Name" -> personModel.getMembershipModel().getSelectedPerson().setLastName(text);
            case "Nickname" -> personModel.getMembershipModel().getSelectedPerson().setNickName(text);
            case "Occupation" -> personModel.getMembershipModel().getSelectedPerson().setOccupation(text);
            case "Business" -> personModel.getMembershipModel().getSelectedPerson().setBusiness(text);
            case "Birthday" -> personModel.getMembershipModel().getSelectedPerson().setBirthday(LocalDate.parse(text));
        }
    }

    private String getMemberType() {
        return switch (personModel.getPersonDTO().getMemberType()) {
            case 1 -> "Primary";
            case 2 -> "Secondary";
            case 3 -> "Dependant";
            default -> "Unknown";
        };
    }

    public PersonFx getPersonDTO() {
        return personModel.getPersonDTO();
    }

    public PersonModel getPersonModel() {
        return personModel;
    }

    public Consumer<PersonMessage> sendMessage() {
        return action;
    }
}
