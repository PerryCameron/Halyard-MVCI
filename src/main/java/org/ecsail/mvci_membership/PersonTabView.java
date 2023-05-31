package org.ecsail.mvci_membership;

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
import org.ecsail.dto.*;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.interfaces.Messages;
import org.ecsail.interfaces.ObjectType;
import org.ecsail.static_calls.MathTools;
import org.ecsail.widgetfx.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

import static org.ecsail.interfaces.ObjectType.Dto.*;

public class PersonTabView extends Tab implements Builder<Tab>, ConfigFilePaths, Messages, ObjectType {
    private final PersonDTO person;
    private final MembershipModel membershipModel;
    private final MembershipView membershipView;
    private final HashMap<String, HBox> personInfoHBoxMap = new HashMap<>();

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
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2)); // makes outer border
        vBox.setId("custom-tap-pane-frame");
        BorderPane borderPane = new BorderPane();
        borderPane.setId("box-background-light");
        borderPane.setLeft(createFieldDetails());
        borderPane.setCenter(createPictureFrame());
        borderPane.setBottom(createBottomStacks());
        vBox.getChildren().add(borderPane);
        tab.setContent(vBox);
        return tab;
    }

    private Node createBottomStacks() {
        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER_LEFT, new Insets(10,5,5,5), true); // space between borders
        personInfoHBoxMap.put("Properties", setStack(Properties));
        personInfoHBoxMap.put("Phone", setStack(Phone));
        personInfoHBoxMap.put("Email", setStack(Email));
        personInfoHBoxMap.put("Awards", setStack(Award));
        personInfoHBoxMap.put("Officer", setStack(Officer));
        hBox.getChildren().addAll(selectionButtons(), propertiesStackPane());
        return hBox;
    }

    private Node propertiesStackPane() {
        VBox vBoxBorder = VBoxFx.vBoxOf(new Insets(2,2,2,2)); // border for inner hbox
        HBox.setHgrow(vBoxBorder, Priority.ALWAYS);
        vBoxBorder.setId("custom-tap-pane-frame");
        StackPane stackPane = new StackPane();
        for (HBox hbox : personInfoHBoxMap.values()) stackPane.getChildren().add(hbox);
        vBoxBorder.getChildren().add(stackPane);
        return vBoxBorder;
    }

    private void setVisibleHBox(boolean properties, boolean phone, boolean email, boolean award, boolean officer) {
        personInfoHBoxMap.get("Properties").setVisible(properties);
        personInfoHBoxMap.get("Phone").setVisible(phone);
        personInfoHBoxMap.get("Email").setVisible(email);
        personInfoHBoxMap.get("Awards").setVisible(award);
        personInfoHBoxMap.get("Officer").setVisible(officer);
    }

    private Node selectionButtons() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,0,0,0));
        ToggleGroup tg = new ToggleGroup();
        Region region = RegionFx.regionOf(7);
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
                button.setSelected(true);
                button.setOnAction(event -> setVisibleHBox(true,false,false,false,false));
            }
            case "Phone" -> button.setOnAction(event -> setVisibleHBox(false,true,false,false,false));
            case "Email" -> button.setOnAction(event -> setVisibleHBox(false,false,true,false,false));
            case "Awards" -> button.setOnAction(event -> setVisibleHBox(false,false,false,true,false));
            case "Officer" -> button.setOnAction(event -> setVisibleHBox(false,false,false,false,true));
        }
        button.setPrefHeight(30);
        return button;
    }


    private  HBox setStack(ObjectType.Dto type) {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5),"box-background-light",true);
        switch (type) {
            case Officer -> {
                VBox vBox = createButtonBox(createAddButton(Officer), createDeleteButton(Officer));
                membershipModel.getOfficerTableView().put(person, new OfficerTableView(person, membershipView).build());
                hBox.getChildren().addAll(membershipModel.getOfficerTableView().get(person), vBox);
            }
            case Award -> {
                VBox vBox = createButtonBox(createAddButton(Award), createDeleteButton(Award));
                membershipModel.getAwardTableView().put(person, new AwardTableView(person, membershipView).build());
                hBox.getChildren().addAll(membershipModel.getAwardTableView().get(person),vBox);
            }
            case Email -> {
                TableView<EmailDTO> tableView = new EmailTableView(person, membershipView).build();
                membershipModel.getEmailTableView().put(person, tableView);
                VBox vBox = createButtonBox(createAddButton(Email), createDeleteButton(Email), createCopyButton(Email), createEmailButton());
                hBox.getChildren().addAll(membershipModel.getEmailTableView().get(person),vBox);
            }
            case Phone -> {
                VBox vBox = createButtonBox(createAddButton(Phone), createDeleteButton(Phone), createCopyButton(Phone));
                membershipModel.getPhoneTableView().put(person, new PhoneTableView(person, membershipView).build());
                hBox.getChildren().addAll(membershipModel.getPhoneTableView().get(person),vBox);
            }
            case Properties -> hBox.getChildren().addAll(getInfoBox(), getRadioBox());
        }
        return hBox;
    }

    private Node getRadioBox() {
        VBox vBox = VBoxFx.vBoxOf(5.0, new Insets(5,5,5,5));
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
        VBox vBox = VBoxFx.vBoxOf(7.0, new Insets(15,10,0,5));
        vBox.setId("custom-tap-pane-frame");
        vBox.getChildren().addAll(
                new Label("Age: " + MathTools.calculateAge(person.getBirthday())),
                new Label("Person ID: " + person.getpId()),
                new Label("MSID: " + person.getMsId()));
        return vBox;
    }

    private Control createSubmit() {
        Button button = ButtonFx.buttonOf("Submit",60);
        button.setOnAction(event -> {
            // get selected radio button
            RadioButton rb = membershipModel.getSelectedRadioForPerson().get(person);
            Messages.MessageType type = mapStringToEnum(rb.getText());
            membershipView.sendMessage().accept(type,createData(type));
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

    private Button createCopyButton(ObjectType.Dto type) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        Button button = ButtonFx.buttonOf("Copy", 60);
        switch (type) {
            case Email -> button.setOnAction(event -> {
                int selectedIndex = membershipModel.getEmailTableView().get(person).getSelectionModel().getSelectedIndex();
                EmailDTO emailDTO = membershipModel.getEmailTableView().get(person).getItems().get(selectedIndex);
                content.putString(emailDTO.getEmail());
            });
            case Phone -> button.setOnAction(event -> {
                int selectedIndex = membershipModel.getPhoneTableView().get(person).getSelectionModel().getSelectedIndex();
                PhoneDTO phoneDTO = membershipModel.getPhoneTableView().get(person).getItems().get(selectedIndex);
                content.putString(phoneDTO.getPhone());
            });
        }
        clipboard.setContent(content);
        return button;
    }

    private Button createDeleteButton(ObjectType.Dto type) {
        Button button = ButtonFx.buttonOf("Delete", 60);
        switch (type) {
            case Phone -> button.setOnAction(event -> {
                int selectedIndex = membershipModel.getPhoneTableView().get(person).getSelectionModel().getSelectedIndex();
                PhoneDTO phoneDTO = membershipModel.getPhoneTableView().get(person).getItems().get(selectedIndex);
                membershipView.sendMessage().accept(MessageType.DELETE, phoneDTO);
                person.getPhones().remove(phoneDTO);
            });
            case Email -> button.setOnAction(event -> {
                int selectedIndex = membershipModel.getEmailTableView().get(person).getSelectionModel().getSelectedIndex();
                EmailDTO emailDTO = membershipModel.getEmailTableView().get(person).getItems().get(selectedIndex);
                membershipView.sendMessage().accept(MessageType.DELETE, emailDTO);
                person.getEmail().remove(emailDTO);
            });
            case Award -> button.setOnAction(event -> {
                int selectedIndex = membershipModel.getAwardTableView().get(person).getSelectionModel().getSelectedIndex();
                AwardDTO awardDTO = membershipModel.getAwardTableView().get(person).getItems().get(selectedIndex);
                membershipView.sendMessage().accept(MessageType.DELETE, awardDTO);
                person.getAwards().remove(awardDTO);
            });
            case Officer -> button.setOnAction(event -> {
                int selectedIndex = membershipModel.getOfficerTableView().get(person).getSelectionModel().getSelectedIndex();
                OfficerDTO officerDTO = membershipModel.getOfficerTableView().get(person).getItems().get(selectedIndex);
                membershipView.sendMessage().accept(MessageType.DELETE, officerDTO);
                person.getOfficer().remove(officerDTO);
            });
        }
        return button;
    }

    private Button createAddButton(ObjectType.Dto type) {
        Button button = ButtonFx.buttonOf("Add", 60);
            switch (type) {
                case Phone -> button.setOnAction(event -> {
                    PhoneDTO phoneDTO = new PhoneDTO(person.getpId());
                    membershipView.sendMessage().accept(MessageType.INSERT, phoneDTO);
                    person.getPhones().add(phoneDTO);
                    person.getPhones().sort(Comparator.comparing(PhoneDTO::getPhoneId));
                    requestFocusOnTable(membershipModel.getPhoneTableView().get(person));
                });
                case Email -> button.setOnAction(event -> {
                    EmailDTO emailDTO = new EmailDTO(person.getpId());
                    membershipView.sendMessage().accept(MessageType.INSERT, emailDTO);
                    person.getEmail().add(emailDTO);
                    person.getEmail().sort(Comparator.comparing(EmailDTO::getEmail_id));
                    requestFocusOnTable(membershipModel.getEmailTableView().get(person));
                });
                case Award -> button.setOnAction(event -> {
                    AwardDTO awardDTO = new AwardDTO(person.getpId());
                    membershipView.sendMessage().accept(MessageType.INSERT, awardDTO);
                    person.getAwards().add(awardDTO);
                    person.getAwards().sort(Comparator.comparing(AwardDTO::getAwardId));
                    requestFocusOnTable(membershipModel.getAwardTableView().get(person));
                });
                case Officer -> button.setOnAction(event -> {
                    OfficerDTO officerDTO = new OfficerDTO(person.getpId());
                    person.getOfficer().add(officerDTO);
                    person.getOfficer().sort(Comparator.comparing(OfficerDTO::getOfficerId));
                    membershipView.sendMessage().accept(MessageType.INSERT, officerDTO);
                    requestFocusOnTable(membershipModel.getOfficerTableView().get(person));
                });
            }
        return button;
    }

    private void requestFocusOnTable(TableView<?> tableView) {
        TableColumn firstColumn = tableView.getColumns().get(0);
        tableView.layout();
        tableView.requestFocus();
        tableView.getSelectionModel().select(0);
        tableView.getFocusModel().focus(0);
        tableView.edit(0, firstColumn);
    }

    private Button createEmailButton() {
        Button button = ButtonFx.buttonOf("Email", 60);
        button.setOnAction(event -> {
            int selectedIndex = membershipModel.getEmailTableView().get(person).getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {// make sure something is selected
                EmailDTO emailDTO = membershipModel.getEmailTableView().get(person).getItems().get(selectedIndex);
                Mail.composeEmail(emailDTO.getEmail(), "ECSC", "");
            }
        });
        return button;
    }

    private VBox createButtonBox(Button... buttons) {
        VBox vBox = VBoxFx.vBoxOf(7.0, new Insets(5,5,5,5));
        Arrays.stream(buttons).iterator().forEachRemaining(button -> vBox.getChildren().add(button));
        return vBox;
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
        HBox hBox = HBoxFx.hBoxOf(new Insets(0, 0, 10, 0), Pos.CENTER_LEFT, 10.0);
        TextField textField = TextFieldFx.textFieldOf(150, property);
        textField.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    updatePersonDTO(label, textField.getText());
            if (oldValue) {
                membershipView.sendMessage().accept(MessageType.UPDATE,person);
            }
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
        return switch (person.getMemberType()) {
            case 1 -> "Primary";
            case 2 -> "Secondary";
            case 3 -> "Dependant";
            default -> "Unknown";
        };
    }

    public PersonDTO getPerson() {
        return person;
    }
}
