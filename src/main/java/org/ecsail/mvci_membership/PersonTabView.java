package org.ecsail.mvci_membership;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.dto.PersonDTO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.widgetfx.ButtonFx;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.TextFieldFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.Arrays;
import java.util.Objects;

public class PersonTabView extends Tab implements Builder<Tab>, ConfigFilePaths {
    private final PersonDTO person;
    private final MembershipModel membershipModel;
    private final MembershipView membershipView;

    public PersonTabView(MembershipView membershipView, PersonDTO personDTO) {
        this.person = personDTO;
        this.membershipModel = membershipView.getMembershipModel();
        this.membershipView = membershipView;
    }

    @Override
    public Tab build() {
        Tab tab = new Tab();
        tab.setText(getMemberType());
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
        tabPane.getTabs().add(detailsTab("Phone"));
        tabPane.getTabs().add(detailsTab("Email"));
        tabPane.getTabs().add(detailsTab("Awards"));
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
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5),"box-background-light");
            // ADD DElETE
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

        return hBox;
    }

    private Node handlePhoneTab() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(10,10,5,10),"box-background-light");
        hBox.setPrefHeight(130);
        VBox vBox = createButtonBox(createAdd(person), createDelete(person), createCopy(person));
        hBox.getChildren().addAll(new PhoneTableView(person, membershipView).build(),vBox);
        return hBox;
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
            if (oldValue) {
                // TODO send this to controller with consumer<PersonDTO>
                System.out.println("label change to " + property.toString());
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

    private String getMemberType() {
        String memberTypeString = switch (person.getMemberType()) {
            case 1 -> "Primary";
            case 2 -> "Secondary";
            case 3 -> "Dependant";
            default -> "Unknown";
        };
        return memberTypeString;
    }
}
