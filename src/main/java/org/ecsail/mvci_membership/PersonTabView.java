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
import org.ecsail.custom.CustomDatePicker;
import org.ecsail.dto.*;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.interfaces.ObjectType;
import org.ecsail.static_tools.MathTools;
import org.ecsail.widgetfx.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

import static org.ecsail.interfaces.ObjectType.Dto.*;

public class PersonTabView extends Tab implements Builder<Tab>, ConfigFilePaths, ObjectType {
    private final PersonDTO personDTO;
    private final MembershipModel membershipModel;
    private final MembershipView membershipView;
    private final HashMap<String, HBox> personInfoHBoxMap = new HashMap<>();

    public PersonTabView(MembershipView membershipView, PersonDTO personDTO) {
        this.personDTO = personDTO;
        this.membershipModel = membershipView.getMembershipModel();
        this.membershipView = membershipView;
        membershipModel.getStackPaneMap().put(personDTO, new StackPane());
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
        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER_LEFT, new Insets(22,5,20,5), true); // space between borders
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
                button.setOnAction(event -> setVisibleHBox(true,false,false,false,false));
                button.fire();
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
                membershipModel.getOfficerTableView().put(personDTO, new OfficerTableView(personDTO, membershipView).build());
                hBox.getChildren().addAll(membershipModel.getOfficerTableView().get(personDTO), vBox);
            }
            case Award -> {
                VBox vBox = createButtonBox(createAddButton(Award), createDeleteButton(Award));
                membershipModel.getAwardTableView().put(personDTO, new AwardTableView(personDTO, membershipView).build());
                hBox.getChildren().addAll(membershipModel.getAwardTableView().get(personDTO),vBox);
            }
            case Email -> {
                TableView<EmailDTO> tableView = new EmailTableView(personDTO, membershipView).build();
                membershipModel.getEmailTableView().put(personDTO, tableView);
                VBox vBox = createButtonBox(createAddButton(Email), createDeleteButton(Email), createCopyButton(Email), createEmailButton());
                hBox.getChildren().addAll(membershipModel.getEmailTableView().get(personDTO),vBox);
            }
            case Phone -> {
                VBox vBox = createButtonBox(createAddButton(Phone), createDeleteButton(Phone), createCopyButton(Phone));
                membershipModel.getPhoneTableView().put(personDTO, new PhoneTableView(personDTO, membershipView).build());
                hBox.getChildren().addAll(membershipModel.getPhoneTableView().get(personDTO),vBox);
            }
            case Properties -> hBox.getChildren().addAll(getInfoBox(), getRadioBox());
        }
        return hBox;
    }

    private Node getRadioBox() {
        VBox vBox = VBoxFx.vBoxOf(5.0, new Insets(5,5,5,5));
        ToggleGroup tg = new ToggleGroup();
        vBox.getChildren().add(radioButton(tg, "Change " + personDTO.getFirstName() + "'s member type"));
        vBox.getChildren().add(radioButton(tg, "Remove " + personDTO.getFirstName() + " from this membership"));
        vBox.getChildren().add(radioButton(tg, "Delete " + personDTO.getFirstName() + " from database"));
        vBox.getChildren().add(radioButton(tg, "Move " + personDTO.getFirstName() + " to membership ..."));
        vBox.getChildren().add(bottomControlBox());
        return vBox;
    }

    private MembershipMessage mapStringToEnum(String input) {
        membershipModel.setSelectedPerson(personDTO);
        switch (input.split(" ")[0]) { // Split the string and get the first word
            case "Change" -> { return MembershipMessage.CHANGE_MEMBER_TYPE; }
            case "Remove" -> { return MembershipMessage.REMOVE_MEMBER_FROM_MEMBERSHIP; }
            case "Delete" -> { return MembershipMessage.DELETE_MEMBER_FROM_DATABASE; }
            case "Move" -> { return MembershipMessage.MOVE_MEMBER_TO_MEMBERSHIP; }
        }
        return MembershipMessage.NONE;
    }

    private Node bottomControlBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5), 30);
        TextField textField = TextFieldFx.textFieldOf(120, "MSID");
        membershipModel.getPersonTextField().put(personDTO,textField);
        membershipModel.getStackPaneMap().get(personDTO).getChildren().addAll(createComboBox(), textField, createRegion());
        hBox.getChildren().addAll(membershipModel.getStackPaneMap().get(personDTO), createSubmit());
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
        membershipModel.getPersonComboBox().put(personDTO,comboBox);
        comboBox.setPrefWidth(120);
        comboBox.getItems().clear();
        switch (personDTO.getMemberType()) {
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
                membershipModel.getSelectedRadioForPerson().put(personDTO, radioButton);
            }
        });
        return radioButton;
    }

    private void changeStackPane(MembershipMessage action) {
        membershipModel.getStackPaneMap().get(personDTO).getChildren().forEach(child -> {
            if (child instanceof ComboBox) {
                child.setVisible(action == MembershipMessage.CHANGE_MEMBER_TYPE);
                child.setManaged(action == MembershipMessage.CHANGE_MEMBER_TYPE);
            } else if (child instanceof TextField) {
                child.setVisible(action == MembershipMessage.MOVE_MEMBER_TO_MEMBERSHIP);
                child.setManaged(action == MembershipMessage.MOVE_MEMBER_TO_MEMBERSHIP);
            } else if (child instanceof Region) {
                child.setVisible(action == MembershipMessage.DELETE_MEMBER_FROM_DATABASE || action == MembershipMessage.REMOVE_MEMBER_FROM_MEMBERSHIP);
                child.setManaged(action == MembershipMessage.DELETE_MEMBER_FROM_DATABASE || action == MembershipMessage.REMOVE_MEMBER_FROM_MEMBERSHIP);
            }
        });
    }

    private Node getInfoBox() {
        VBox vBox = VBoxFx.vBoxOf(7.0, new Insets(15,10,0,5));
        vBox.setId("custom-tap-pane-frame");
        vBox.getChildren().addAll(
                new Label("Age: " + MathTools.calculateAge(personDTO.getBirthday())),
                new Label("Person ID: " + personDTO.getpId()),
                new Label("MSID: " + personDTO.getMsId()));
        return vBox;
    }

    private Control createSubmit() {
        Button button = ButtonFx.buttonOf("Submit",60);
        button.setOnAction(event -> {
            // get selected radio button
            RadioButton rb = membershipModel.getSelectedRadioForPerson().get(personDTO);
            MembershipMessage type = mapStringToEnum(rb.getText());
            membershipView.sendMessage().accept(type);
        });
        return button;
    }

    private Object createData(MembershipMessage message) {
        String returnString;
        switch (message) {
            case CHANGE_MEMBER_TYPE -> returnString = membershipModel.getPersonComboBox().get(personDTO).getValue();
            case MOVE_MEMBER_TO_MEMBERSHIP -> returnString = membershipModel.getPersonTextField().get(personDTO).getText();
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
                int selectedIndex = membershipModel.getEmailTableView().get(personDTO).getSelectionModel().getSelectedIndex();
                EmailDTO emailDTO = membershipModel.getEmailTableView().get(personDTO).getItems().get(selectedIndex);
                content.putString(emailDTO.getEmail());
            });
            case Phone -> button.setOnAction(event -> {
                int selectedIndex = membershipModel.getPhoneTableView().get(personDTO).getSelectionModel().getSelectedIndex();
                PhoneDTO phoneDTO = membershipModel.getPhoneTableView().get(personDTO).getItems().get(selectedIndex);
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
                int selectedIndex = membershipModel.getPhoneTableView().get(personDTO).getSelectionModel().getSelectedIndex();
                PhoneDTO phoneDTO = membershipModel.getPhoneTableView().get(personDTO).getItems().get(selectedIndex);
                membershipModel.setSelectedPhone(phoneDTO);
                membershipView.sendMessage().accept(MembershipMessage.DELETE_PHONE);
                personDTO.getPhones().remove(phoneDTO);
            });
            case Email -> button.setOnAction(event -> {
                int selectedIndex = membershipModel.getEmailTableView().get(personDTO).getSelectionModel().getSelectedIndex();
                EmailDTO emailDTO = membershipModel.getEmailTableView().get(personDTO).getItems().get(selectedIndex);
                membershipModel.setSelectedEmail(emailDTO);
                membershipView.sendMessage().accept(MembershipMessage.DELETE_EMAIL);
                personDTO.getEmail().remove(emailDTO);
            });
            case Award -> button.setOnAction(event -> {
                int selectedIndex = membershipModel.getAwardTableView().get(personDTO).getSelectionModel().getSelectedIndex();
                AwardDTO awardDTO = membershipModel.getAwardTableView().get(personDTO).getItems().get(selectedIndex);
                membershipModel.setSelectedAward(awardDTO);
                membershipView.sendMessage().accept(MembershipMessage.DELETE_AWARD);
                personDTO.getAwards().remove(awardDTO);
            });
            case Officer -> button.setOnAction(event -> {
                int selectedIndex = membershipModel.getOfficerTableView().get(personDTO).getSelectionModel().getSelectedIndex();
                OfficerDTO officerDTO = membershipModel.getOfficerTableView().get(personDTO).getItems().get(selectedIndex);
                membershipModel.setSelectedOfficer(officerDTO);
                membershipView.sendMessage().accept(MembershipMessage.DELETE_OFFICER);
                personDTO.getOfficer().remove(officerDTO);
            });
        }
        return button;
    }

    private Button createAddButton(ObjectType.Dto type) {
        Button button = ButtonFx.buttonOf("Add", 60);
            switch (type) {
                case Phone -> button.setOnAction(event -> {
                    PhoneDTO phoneDTO = new PhoneDTO(personDTO.getpId());
                    membershipModel.setSelectedPhone(phoneDTO);
                    membershipView.sendMessage().accept(MembershipMessage.INSERT_PHONE);
                    personDTO.getPhones().add(phoneDTO);
                    personDTO.getPhones().sort(Comparator.comparing(PhoneDTO::getPhoneId));
                    requestFocusOnTable(membershipModel.getPhoneTableView().get(personDTO));
                });
                case Email -> button.setOnAction(event -> {
                    EmailDTO emailDTO = new EmailDTO(personDTO.getpId());
                    membershipModel.setSelectedEmail(emailDTO);
                    membershipView.sendMessage().accept(MembershipMessage.INSERT_EMAIL);
                    personDTO.getEmail().add(emailDTO);
                    personDTO.getEmail().sort(Comparator.comparing(EmailDTO::getEmail_id));
                    requestFocusOnTable(membershipModel.getEmailTableView().get(personDTO));
                });
                case Award -> button.setOnAction(event -> {
                    AwardDTO awardDTO = new AwardDTO(personDTO.getpId());
                    membershipModel.setSelectedAward(awardDTO);
                    membershipView.sendMessage().accept(MembershipMessage.INSERT_AWARD);
                    personDTO.getAwards().add(awardDTO);
                    personDTO.getAwards().sort(Comparator.comparing(AwardDTO::getAwardId));
                    requestFocusOnTable(membershipModel.getAwardTableView().get(personDTO));
                });
                case Officer -> button.setOnAction(event -> {
                    OfficerDTO officerDTO = new OfficerDTO(personDTO.getpId());
                    personDTO.getOfficer().add(officerDTO);
                    personDTO.getOfficer().sort(Comparator.comparing(OfficerDTO::getOfficerId));
                    membershipModel.setSelectedOfficer(officerDTO);
                    membershipView.sendMessage().accept(MembershipMessage.INSERT_OFFICER);
                    requestFocusOnTable(membershipModel.getOfficerTableView().get(personDTO));
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
            int selectedIndex = membershipModel.getEmailTableView().get(personDTO).getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {// make sure something is selected
                EmailDTO emailDTO = membershipModel.getEmailTableView().get(personDTO).getItems().get(selectedIndex);
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
        vBox.getChildren().add(fieldBox(personDTO.firstNameProperty(), "First Name"));
        vBox.getChildren().add(fieldBox(personDTO.lastNameProperty(), "Last Name"));
        vBox.getChildren().add(fieldBox(personDTO.nickNameProperty(), "Nickname"));
        vBox.getChildren().add(fieldBox(personDTO.occupationProperty(), "Occupation"));
        vBox.getChildren().add(fieldBox(personDTO.businessProperty(), "Business"));
        vBox.getChildren().add(fieldDateBox(personDTO.birthdayProperty(), "Birthday"));
        return vBox;
    }

    private Node fieldDateBox(Property<?> property, String label) {
        CustomDatePicker datePicker = new CustomDatePicker();
        datePicker.setPrefWidth(150);
        datePicker.setValue(stringToDate(property.getValue().toString()));
        datePicker.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (!isFocused){
                datePicker.updateValue();
                updatePersonDTO(label, datePicker.getValue().toString());
                membershipModel.setSelectedPerson(personDTO);
                membershipView.sendMessage().accept(MembershipMessage.UPDATE_PERSON);
            }
        });
        return labeledField(label, datePicker);
    }

    private LocalDate stringToDate(String stringDate) {
        LocalDate date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (membershipView.getMembershipModel().getMembership().getJoinDate() != null)
            date = LocalDate.parse(stringDate, formatter);
        else date = LocalDate.parse("1900-01-01", formatter);
        return date;
    }

    private Node fieldBox(Property<?> property, String label) {
        TextField textField = TextFieldFx.textFieldOf(150, property);
        textField.focusedProperty()
                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    updatePersonDTO(label, textField.getText());
            if (oldValue) {
                membershipModel.setSelectedPerson(personDTO);
                membershipView.sendMessage().accept(MembershipMessage.UPDATE_PERSON);
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
        HBox hBoxTextField = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 170.0);
        hBoxTextField.getChildren().add(node);
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
        return switch (personDTO.getMemberType()) {
            case 1 -> "Primary";
            case 2 -> "Secondary";
            case 3 -> "Dependant";
            default -> "Unknown";
        };
    }

    public PersonDTO getPersonDTO() {
        return personDTO;
    }
}
