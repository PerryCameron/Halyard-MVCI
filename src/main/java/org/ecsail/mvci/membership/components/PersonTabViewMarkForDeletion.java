package org.ecsail.mvci.membership.components;

import javafx.scene.control.*;
import javafx.util.Builder;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.interfaces.ObjectType;

// This class is defunct, I am just keeping it for now incase I missed something
// TODO erase this class when I am sure I don't need it anymore


public class PersonTabViewMarkForDeletion extends Tab implements Builder<Tab>, ConfigFilePaths, ObjectType {
//    private final PersonFx personDTO;
//    private final MembershipModel membershipModel;
//    private final MembershipView membershipView;
//    private final HashMap<String, HBox> personInfoHBoxMap = new HashMap<>();
//    private Label ageLabel;
//
//    private ImageView imageView;
//    private static final Logger logger = LoggerFactory.getLogger(PersonTabView.class);
//
//    public PersonTabView(MembershipView membershipView, PersonFx personDTO) {
//        this.personDTO = personDTO;
//        this.membershipModel = membershipView.getMembershipModel();
//        this.membershipView = membershipView;
//        membershipModel.getStackPaneMap().put(personDTO, new StackPane());
//    }
//
    @Override
    public Tab build() {
//        this.setText(getMemberType());
//        this.setUserData(this);
//        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2)); // makes outer border
//        vBox.setId("custom-tap-pane-frame");
//        BorderPane borderPane = new BorderPane();
//        borderPane.setId("box-background-light");
//        borderPane.setLeft(createFieldDetails());
//        borderPane.setCenter(createPictureFrame());
//        borderPane.setBottom(createBottomStacks());
//        vBox.getChildren().add(borderPane);
//        this.setContent(vBox);
        return null;
    }
//
//    private Node createBottomStacks() {
//        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER_LEFT, new Insets(22,5,20,5), true); // space between borders
//        personInfoHBoxMap.put("Properties", setStack(Properties));
//        personInfoHBoxMap.put("Phone", setStack(Phone));
//        personInfoHBoxMap.put("Email", setStack(Email));
//        personInfoHBoxMap.put("Awards", setStack(Award));
//        personInfoHBoxMap.put("Officer", setStack(Officer));
//        hBox.getChildren().addAll(selectionButtons(), propertiesStackPane());
//        return hBox;
//    }
//
//    private Node propertiesStackPane() {
//        VBox vBoxBorder = VBoxFx.vBoxOf(new Insets(2,2,2,2)); // border for inner hbox
//        HBox.setHgrow(vBoxBorder, Priority.ALWAYS);
//        vBoxBorder.setId("custom-tap-pane-frame");
//        StackPane stackPane = new StackPane();
//        for (HBox hbox : personInfoHBoxMap.values()) stackPane.getChildren().add(hbox);
//        vBoxBorder.getChildren().add(stackPane);
//        return vBoxBorder;
//    }
//
//    private void setVisibleHBox(boolean properties, boolean phone, boolean email, boolean award, boolean officer) {
//        personInfoHBoxMap.get("Properties").setVisible(properties);
//        personInfoHBoxMap.get("Phone").setVisible(phone);
//        personInfoHBoxMap.get("Email").setVisible(email);
//        personInfoHBoxMap.get("Awards").setVisible(award);
//        personInfoHBoxMap.get("Officer").setVisible(officer);
//    }
//
//    private Node selectionButtons() {
//        VBox vBox = VBoxFx.vBoxOf(new Insets(0,0,0,0));
//        ToggleGroup tg = new ToggleGroup();
//        Region region = RegionFx.regionHeightOf(7);
//        vBox.getChildren().add(region);
//        vBox.getChildren().add(buttonControl("Properties", tg));
//        vBox.getChildren().add(buttonControl("Phone", tg));
//        vBox.getChildren().add(buttonControl("Email", tg));
//        vBox.getChildren().add(buttonControl("Awards", tg));
//        vBox.getChildren().add(buttonControl("Officer", tg));
//        return vBox;
//    }
//
//    private Node buttonControl(String label, ToggleGroup tg) {
//        ToggleButton button = ButtonFx.toggleButtonOf(label, 100, tg);
//        switch (label) {
//            case "Properties" -> {
//                button.setOnAction(event -> setVisibleHBox(true,false,false,false,false));
//                button.fire();
//            }
//            case "Phone" -> button.setOnAction(event -> setVisibleHBox(false,true,false,false,false));
//            case "Email" -> button.setOnAction(event -> setVisibleHBox(false,false,true,false,false));
//            case "Awards" -> button.setOnAction(event -> setVisibleHBox(false,false,false,true,false));
//            case "Officer" -> button.setOnAction(event -> setVisibleHBox(false,false,false,false,true));
//        }
//        button.setPrefHeight(30);
//        return button;
//    }
//
//    private  HBox setStack(ObjectType.Dto type) {
//        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5),"box-background-light",true);
//        switch (type) {
//            case Officer -> {
//                VBox vBox = createButtonBox(createAddButton(Officer), createDeleteButton(Officer));
//                membershipModel.getOfficerTableView().put(personDTO, new OfficerTableView(personDTO, membershipView).build());
//                hBox.getChildren().addAll(membershipModel.getOfficerTableView().get(personDTO), vBox);
//            }
//            case Award -> {
//                VBox vBox = createButtonBox(createAddButton(Award), createDeleteButton(Award));
//                membershipModel.getAwardTableView().put(personDTO, new AwardTableView(personDTO, membershipView).build());
//                hBox.getChildren().addAll(membershipModel.getAwardTableView().get(personDTO),vBox);
//            }
//            case Email -> {
//                TableView<EmailDTOFx> tableView = new EmailTableView(personDTO, membershipView).build();
//                membershipModel.getEmailTableView().put(personDTO, tableView);
//                VBox vBox = createButtonBox(createAddButton(Email), createDeleteButton(Email), createCopyButton(Email), createEmailButton());
//                hBox.getChildren().addAll(membershipModel.getEmailTableView().get(personDTO),vBox);
//            }
//            case Phone -> {
//                VBox vBox = createButtonBox(createAddButton(Phone), createDeleteButton(Phone), createCopyButton(Phone));
//                membershipModel.getPhoneTableView().put(personDTO, new PhoneTableView(personDTO, membershipView).build());
//                hBox.getChildren().addAll(membershipModel.getPhoneTableView().get(personDTO),vBox);
//            }
//            case Properties -> hBox.getChildren().addAll(getInfoBox(), getRadioBox());
//        }
//        return hBox;
//    }
//
//    private Node getRadioBox() {
//        VBox vBox = VBoxFx.vBoxOf(5.0, new Insets(5,5,5,5));
//        ToggleGroup tg = new ToggleGroup();
//        vBox.getChildren().add(radioButton(tg, "Change " + personDTO.getFirstName() + "'s member type"));
//        vBox.getChildren().add(radioButton(tg, "Remove " + personDTO.getFirstName() + " from this membership"));
//        vBox.getChildren().add(radioButton(tg, "Delete " + personDTO.getFirstName() + " from database"));
//        vBox.getChildren().add(radioButton(tg, "Move " + personDTO.getFirstName() + " to membership ..."));
//        vBox.getChildren().add(bottomControlBox());
//        return vBox;
//    }
//
//    private MembershipMessage mapStringToEnum(String input) {
//        membershipModel.setSelectedPerson(personDTO);
//        switch (input.split(" ")[0]) { // Split the string and get the first word
//            case "Change" -> { return MembershipMessage.CHANGE_MEMBER_TYPE; }
//            case "Remove" -> { return MembershipMessage.DETACH_MEMBER_FROM_MEMBERSHIP; }
//            case "Delete" -> { return MembershipMessage.DELETE_MEMBER_FROM_DATABASE; }
//            case "Move" -> { return MembershipMessage.MOVE_MEMBER_TO_MEMBERSHIP; }
//        }
//        return MembershipMessage.NONE;
//    }
//
//    private Node bottomControlBox() {
//        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5), 30);
//        TextField textField = TextFieldFx.textFieldOf(120, "MSID");
//        membershipModel.getPersonTextField().put(personDTO,textField);
//        membershipModel.getStackPaneMap().get(personDTO).getChildren().addAll(createComboBox(), textField, createRegion());
//        hBox.getChildren().addAll(membershipModel.getStackPaneMap().get(personDTO), createSubmit());
//        return hBox;
//    }
//
//    private Region createRegion() {
//        Region region = new Region();
//        region.setPrefSize(120,10);
//        region.setMinSize(120,10);
//        return region;
//    }
//
//    private Node createComboBox() {
//        final ComboBox<String> comboBox = new ComboBox<>();
//        membershipModel.getPersonComboBox().put(personDTO,comboBox);
//        comboBox.setPrefWidth(120);
//        comboBox.getItems().clear();
//        switch (personDTO.getMemberType()) {
//            case 1 -> comboBox.getItems().addAll("Secondary", "Dependent");
//            case 2 -> comboBox.getItems().addAll("Primary", "Dependent");
//            default -> comboBox.getItems().addAll("Primary", "Secondary");
//        }
//        comboBox.getSelectionModel().selectFirst();
//        return comboBox;
//    }
//
//    private Control radioButton(ToggleGroup tg, String name) {
//        RadioButton radioButton = new RadioButton(name);
//        radioButton.setToggleGroup(tg);
//        radioButton.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
//            if (isNowSelected) {
//                changeStackPane(mapStringToEnum(name));
//                membershipModel.getSelectedRadioForPerson().put(personDTO, radioButton);
//            }
//        });
//        return radioButton;
//    }
//
//    private void changeStackPane(MembershipMessage action) {
//        System.out.println("Change Stack Pane");
//        membershipModel.getStackPaneMap().get(personDTO).getChildren().forEach(child -> {
//            if (child instanceof ComboBox) {
//                child.setVisible(action == MembershipMessage.CHANGE_MEMBER_TYPE);
//                child.setManaged(action == MembershipMessage.CHANGE_MEMBER_TYPE);
//            } else if (child instanceof TextField) {
//                child.setVisible(action == MembershipMessage.MOVE_MEMBER_TO_MEMBERSHIP);
//                child.setManaged(action == MembershipMessage.MOVE_MEMBER_TO_MEMBERSHIP);
//            } else if (child instanceof Region) {
//                child.setVisible(action == MembershipMessage.DELETE_MEMBER_FROM_DATABASE || action == MembershipMessage.DETACH_MEMBER_FROM_MEMBERSHIP);
//                child.setManaged(action == MembershipMessage.DELETE_MEMBER_FROM_DATABASE || action == MembershipMessage.DETACH_MEMBER_FROM_MEMBERSHIP);
//            }
//        });
//    }
//
//    private Node getInfoBox() {
//        VBox vBox = VBoxFx.vBoxOf(7.0, new Insets(15,10,0,5));
//        vBox.setId("custom-tap-pane-frame");
//        this.ageLabel = new Label("Age: unknown");
//        if(personDTO.getBirthday() != null)
//            ageLabel.setText("Age: " + DateTools.calculateAge(personDTO.getBirthday()));
//        vBox.getChildren().add(ageLabel);
//        vBox.getChildren().add(new Label("Person ID: " + personDTO.getpId()));
//        vBox.getChildren().add(new Label("MSID: " + personDTO.getMsId()));
//        return vBox;
//    }
//
//    private Control createSubmit() {
//        Button button = ButtonFx.buttonOf("Submit",60);
//        button.setOnAction(event -> {
//            RadioButton rb = membershipModel.getSelectedRadioForPerson().get(personDTO);
//            switch (rb.getText().split(" ")[0]) { // Split the string and get the first word
//                case "Change" -> changeMemberType();
//                case "Remove" -> { if (userChoosesToRemovePerson()) removeMemberFromMembership(); }
//                case "Delete" -> deletePerson();
//                case "Move" -> movePersonToMembership();
//            }
//        });
//        return button;
//    }
//
//    private void movePersonToMembership() {
////        // TODO move to another msid
////        int oldMsid = person.getMs_id();
////        // set memberType to 3 as default
////        person.setMemberType(3);
////        person.setOldMsid(oldMsid);
////        // TODO make sure it is an integer and that this membership exists
////        person.setMs_id(Integer.parseInt(msidTextField.getText()));
////        SqlUpdate.updatePerson(person);
////        // TODO error check to make sure we are in membership view
////        removeThisTab(personTabPane);
//        //                case "Move" -> { return MembershipMessage.MOVE_MEMBER_TO_MEMBERSHIP; }
//    }
//
//    private void removeMemberFromMembership() {
//        if (isMemberType(MemberType.PRIMARY)) {
//            if (hasMemberType(MemberType.SECONDARY)) {
//                removePersonFromMembership(MembershipMessage.DETACH_PRIMARY_MEMBER_FROM_MEMBERSHIP);
//                // secondary gets changed after return message to membershipView
//            } else
//                DialogueFx.errorAlert("Can not remove " + membershipModel.getSelectedPerson().getFullName()
//                        , "Can not remove primary without secondary to replace them");
//        } else removePersonFromMembership(MembershipMessage.DETACH_MEMBER_FROM_MEMBERSHIP);
//    }
//
//    private void removePersonFromMembership(MembershipMessage message) {
//        System.out.println("removePersonFromMembership(); (PersonTabView)");
//        logger.info("Removing " + personDTO.getFullName());
//        personDTO.setOldMsid(personDTO.getMsId());
//        personDTO.setMsId(0);
//        personDTO.setMemberType(0);
//        membershipModel.setSelectedPerson(personDTO);
//        membershipView.sendMessage().accept(message);
//    }
//
//    private boolean isMemberType(MemberType memberType) {
//        return membershipModel.getSelectedPerson().getMemberType() == MemberType.getCode(memberType);
//    }
//
//    private void changeMemberType() {
//        String type = membershipModel.getPersonComboBox().get(personDTO).getValue();
//        if(isMemberType(MemberType.PRIMARY)) {
//            changePrimaryToType(type);
//        } else if (isMemberType(MemberType.SECONDARY)) {
//            changeSecondaryToType(type);
//        } else {
//            changeDependentToType(type);
//        }
//        //        return MembershipMessage.CHANGE_MEMBER_TYPE;
//    }
//
//    private void changeDependentToType(String type) {
//        switch (type) {
//            case "Primary" -> {
//                if(hasMemberType(MemberType.PRIMARY)) System.out.println("Swap primary and dependent");
//            }
//            case "Secondary" -> {
//                if(hasMemberType(MemberType.SECONDARY)) System.out.println("Swap secondary and dependent");
//                else System.out.println("Make Dependent secondary");
//            }
//        }
//    }
//
//    private void changeSecondaryToType(String type) {
//        switch (type) {
//            case "Primary" -> {
//                if(hasMemberType(MemberType.PRIMARY)) System.out.println("Swap primary and secondary");
//            }
//            case "Dependent" -> {
//                System.out.println("make secondary a dependant");
//            }
//        }
//    }
//
//    private void changePrimaryToType(String type) {
//        switch (type) {
//            case "Secondary" -> {
//                if(hasMemberType(MemberType.SECONDARY)) System.out.println("Swap primary and secondary");
//            }
//            case "Dependent" -> {
//                if(hasMemberType(MemberType.SECONDARY)) {
//                    System.out.println("make primary dependant, make secondary primary");
//                } else {
//                    DialogueFx.errorAlert("Can not move " + membershipModel.getSelectedPerson().getFullName()
//                            , "Can not move primary without secondary to replace them");
//                }
//            }
//        }
//    }
//
//    private boolean hasMemberType(MemberType memberType) {
//        return membershipModel.getPeople().stream()
//                .anyMatch(p -> p.getMemberType() == MemberType.getCode(memberType));
//    }
//
//    private boolean hasOtherMembers() {
//        return membershipModel.getPeople().size() > 1;
//    }
//
//    private Button createCopyButton(ObjectType.Dto type) {
//        Clipboard clipboard = Clipboard.getSystemClipboard();
//        final ClipboardContent content = new ClipboardContent();
//        Button button = ButtonFx.buttonOf("Copy", 60);
//        switch (type) {
//            case Email -> button.setOnAction(event -> {
//                content.putString(membershipModel.getSelectedEmail().getEmail());
//                clipboard.setContent(content);
//            });
//            case Phone -> button.setOnAction(event -> {
//                content.putString(membershipModel.getSelectedPhone().getPhone());
//                clipboard.setContent(content);
//            });
//        }
//        return button;
//    }
//
//    private Button createDeleteButton(ObjectType.Dto type) {
//        Button button = ButtonFx.buttonOf("Delete", 60);
//        switch (type) {
//            case Phone -> button.setOnAction(onClick -> deletePhone());
//            case Email -> button.setOnAction(onClick -> deleteEmail());
//            case Award -> button.setOnAction(onClick -> deleteAward());
//            case Officer -> button.setOnAction(onClick -> deleteOfficer());
//        }
//        return button;
//    }
//
//    private boolean userChoosesToRemovePerson() {
//        String header = "Remove person from membership";
//        String message = "Are you sure you want to remove " + membershipModel.getSelectedPerson().getFullName() +
//                " from this membership?\n\nIt will leave this person's record free floating in the database, " +
//                "however " + membershipModel.getSelectedPerson().getFullName() + " can be looked back up in " +
//                "the People tab and reattached to this or another membership.";
//        Alert alert = DialogueFx.customAlert(header, message, Alert.AlertType.CONFIRMATION);
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) return true;
//        return false;
//    }
//
//    private void deletePerson() {
//        String[] strings = {
//                "Delete Person",
//                "Are you sure you want to delete " + membershipModel.getSelectedPerson().getFullName() + "?",
//                "Missing Selection",
//                "You need to select a person first"};
//        if (DialogueFx.verifyAction(strings, membershipModel.getSelectedPerson()))
//            membershipView.sendMessage().accept(MembershipMessage.DELETE_MEMBER_FROM_DATABASE);
//    }
//
//    private void deleteAward() {
//        String[] strings = {
//                "Delete Award",
//                "Are you sure you want to delete this award entry?",
//                "Missing Selection",
//                "You need to select an award entry first"};
//        if (DialogueFx.verifyAction(strings, membershipModel.getSelectedAward()))
//            membershipView.sendMessage().accept(MembershipMessage.DELETE_AWARD);
//    }
//    private void deleteEmail() {
//        String[] strings = {
//                "Delete Email",
//                "Are you sure you want to delete this email entry?",
//                "Missing Selection",
//                "You need to select an email entry first"};
//        if (DialogueFx.verifyAction(strings, membershipModel.getSelectedEmail()))
//            membershipView.sendMessage().accept(MembershipMessage.DELETE_EMAIL);
//    }
//
//    private void deleteOfficer() {
//        String[] strings = {
//                "Delete Officer",
//                "Are you sure you want to delete this officer entry?",
//                "Missing Selection",
//                "You need to select an officer entry first"};
//        if (DialogueFx.verifyAction(strings, membershipModel.getSelectedOfficer()))
//            membershipView.sendMessage().accept(MembershipMessage.DELETE_OFFICER);
//    }
//
//    private void deletePhone() {
//        String[] strings = {
//                "Delete Phone",
//                "Are you sure you want to delete this phone number?",
//                "Missing Selection",
//                "You need to select a phone first"};
//        if (DialogueFx.verifyAction(strings, membershipModel.getSelectedPhone()))
//            membershipView.sendMessage().accept(MembershipMessage.DELETE_PHONE);
//    }
//
//    private Button createAddButton(ObjectType.Dto type) {
//        Button button = ButtonFx.buttonOf("Add", 60);
//        switch (type) {
//            case Phone -> button.setOnAction(event ->
//                    membershipView.sendMessage().accept(MembershipMessage.INSERT_PHONE));
//            case Email -> button.setOnAction(event ->
//                    membershipView.sendMessage().accept(MembershipMessage.INSERT_EMAIL));
//            case Award -> button.setOnAction(event ->
//                    membershipView.sendMessage().accept(MembershipMessage.INSERT_AWARD));
//            case Officer -> button.setOnAction(event ->
//                membershipView.sendMessage().accept(MembershipMessage.INSERT_OFFICER));
//        }
//        return button;
//    }
//
//    private Button createEmailButton() {
//        Button button = ButtonFx.buttonOf("Email", 60);
//        button.setOnAction(event -> {
//            if (membershipModel.getSelectedEmail() != null) {// make sure something is selected
//                Mail.composeEmail(membershipModel.getSelectedEmail().getEmail(), "ECSC", "");
//            }
//        });
//        return button;
//    }
//
//    private VBox createButtonBox(Button... buttons) {
//        VBox vBox = VBoxFx.vBoxOf(7.0, new Insets(5,5,5,5));
//        Arrays.stream(buttons).iterator().forEachRemaining(button -> vBox.getChildren().add(button));
//        return vBox;
//    }
//
//    private Node createPictureFrame() {
//        VBox vBoxPicture = VBoxFx.vBoxOf(new Insets(12, 10, 0, 7));
//        vBoxPicture.setAlignment(Pos.CENTER);
//        Image memberPhoto = new Image(Objects.requireNonNull(getClass().getResourceAsStream(DEFAULT_PHOTO)));
//        this.imageView = new ImageView(memberPhoto);
//        imageView.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 2) {
//                PictureAlert pictureAlert = new PictureAlert(this, membershipView);
//                Alert alert = pictureAlert.build(); // Call build() to get the configured Alert
//                Optional<ButtonType> result = alert.showAndWait();
////                result.ifPresent(buttonType -> {
////                    // Handle the result if needed
////                    System.out.println("Button clicked: " + buttonType);
////                });
//            }
//        });
//        vBoxPicture.getChildren().add(imageView);
//        return vBoxPicture;
//    }
//
//    private Node createFieldDetails() {
//        VBox vBox = VBoxFx.vBoxOf(new Insets(20,0,0,20));
//        vBox.getChildren().add(fieldBox(personDTO.firstNameProperty(), "First Name"));
//        vBox.getChildren().add(fieldBox(personDTO.lastNameProperty(), "Last Name"));
//        vBox.getChildren().add(fieldBox(personDTO.nickNameProperty(), "Nickname"));
//        vBox.getChildren().add(fieldBox(personDTO.occupationProperty(), "Occupation"));
//        vBox.getChildren().add(fieldBox(personDTO.businessProperty(), "Business"));
//        vBox.getChildren().add(fieldDateBox(personDTO.birthdayProperty(), "Birthday"));
//        return vBox;
//    }
//
//    private Node fieldDateBox(Property<?> property, String label) {
//        CustomDatePicker datePicker = new CustomDatePicker();
//        datePicker.setPrefWidth(150);
//        datePicker.setValue((LocalDate) property.getValue());
//        datePicker.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
//            if (!isFocused){
//                datePicker.updateValue();
//                updatePersonDTO(label, datePicker.getValue().toString());
//                ageLabel.setText("Age: " + DateTools.calculateAge(datePicker.getValue()));
//                membershipModel.setSelectedPerson(personDTO);
//                membershipView.sendMessage().accept(MembershipMessage.UPDATE_PERSON);
//            }
//        });
//        return labeledField(label, datePicker);
//    }
//
//    private Node fieldBox(Property<?> property, String label) {
//        TextField textField = TextFieldFx.textFieldOf(150, property);
//        textField.focusedProperty()
//                .addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                    updatePersonDTO(label, textField.getText());
//                    if (oldValue) {
//                        membershipModel.setSelectedPerson(personDTO);
//                        membershipView.sendMessage().accept(MembershipMessage.UPDATE_PERSON);
//                    }
//                });
//        return labeledField(label, textField);
//    }
//
//    private Node labeledField(String label, Node node) {
//        HBox hBox = HBoxFx.hBoxOf(new Insets(0, 0, 10, 0), Pos.CENTER_LEFT, 10.0);
//        Text text = new Text(label);
//        text.setId("text-white");
//        HBox hBoxLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 80.0);
//        hBoxLabel.getChildren().add(text);
//        HBox hBoxTextField = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 170.0);
//        hBoxTextField.getChildren().add(node);
//        hBox.getChildren().addAll(hBoxLabel, hBoxTextField);
//        return hBox;
//    }
//
//    private void updatePersonDTO(String label, String text) {
//        switch (label) {
//            case "First Name" -> membershipModel.getSelectedPerson().setFirstName(text);
//            case "Last Name" -> membershipModel.getSelectedPerson().setLastName(text);
//            case "Nickname" -> membershipModel.getSelectedPerson().setNickName(text);
//            case "Occupation" -> membershipModel.getSelectedPerson().setOccupation(text);
//            case "Business" -> membershipModel.getSelectedPerson().setBusiness(text);
//            case "Birthday" -> membershipModel.getSelectedPerson().setBirthday(LocalDate.parse(text));
//        }
//    }
//
//    private String getMemberType() {
//        return switch (personDTO.getMemberType()) {
//            case 1 -> "Primary";
//            case 2 -> "Secondary";
//            case 3 -> "Dependant";
//            default -> "Unknown";
//        };
//    }
//
//    public PersonFx getPersonDTO() {
//        return personDTO;
//    }
//
//    public ImageView getImageView() {
//        return imageView;
//    }
}
