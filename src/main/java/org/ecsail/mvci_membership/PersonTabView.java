package org.ecsail.mvci_membership;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.dto.PersonDTO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.TextFieldFx;
import org.ecsail.widgetfx.VBoxFx;

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
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,5,5));
        vBox.setId("custom-tap-pane-frame");
        BorderPane borderPane = new BorderPane();
        borderPane.setId("box-background-light");
        borderPane.setLeft(createFieldDetails());
        borderPane.setCenter(createPictureFrame());
        vBox.getChildren().add(borderPane);
        tab.setContent(vBox);
        return tab;
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
        vBox.getChildren().add(fieldBox(person.fnameProperty(), "First Name"));
        vBox.getChildren().add(fieldBox(person.lnameProperty(), "Last Name"));
        vBox.getChildren().add(fieldBox(person.nnameProperty(), "Nickname"));
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
