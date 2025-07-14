package org.ecsail.mvci.membership.mvci.person;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.*;
import org.ecsail.fx.*;
import org.ecsail.mvci.membership.MembershipModel;
import org.ecsail.mvci.membership.MembershipView;

import java.util.HashMap;

public class PersonModel {
    private final PersonFx personDTO;
    private final MembershipModel membershipModel;
    private final MembershipView membershipView;
    private final Tab tab = new Tab();
    private final HashMap<String, HBox> personInfoHBoxMap = new HashMap<>();
    private Label ageLabel = new Label("Age: unknown");
    private final ObjectProperty<ImageView> imageViewProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<PictureDTO> picture = new SimpleObjectProperty<>();
    private final ObjectProperty<TableView<AwardDTOFx>> awardTableViewProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<TableView<EmailDTOFx>> emailTableViewProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<TableView<OfficerFx>> officerTableViewProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<TableView<PhoneFx>> phoneTableViewProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<StackPane> stackPaneProperty = new SimpleObjectProperty<>(new StackPane());
    private final ObjectProperty<RadioButton>  radioButtonProperty = new SimpleObjectProperty<>(new RadioButton());
    private final ObjectProperty<ComboBox<String>> comboBoxProperty = new SimpleObjectProperty<>(new ComboBox<>());
    private final ObjectProperty<AwardDTOFx> selectedAward = new SimpleObjectProperty<>();
    private final ObjectProperty<PhoneFx> selectedPhone = new SimpleObjectProperty<>();

    private final ObjectProperty<HBox> awardHbox = new SimpleObjectProperty<>();

    public PersonModel(MembershipView membershipView, PersonFx personDTO) {
        this.membershipView = membershipView;
        this.membershipModel = membershipView.getMembershipModel();
        this.personDTO = personDTO;
    }

    public PersonFx getPersonDTO() {
        return personDTO;
    }

    public MembershipModel getMembershipModel() {
        return membershipModel;
    }

    public MembershipView getMembershipView() {
        return membershipView;
    }

    public HashMap<String, HBox> getPersonInfoHBoxMap() {
        return personInfoHBoxMap;
    }

    public Label getAgeLabel() {
        return ageLabel;
    }

    public void setAgeLabel(Label ageLabel) {
        this.ageLabel = ageLabel;
    }

    public ImageView getImageViewProperty() {
        return imageViewProperty.get();
    }

    public ObjectProperty<ImageView> imageViewPropertyProperty() {
        return imageViewProperty;
    }

    public Tab getTab() {
        return tab;
    }

    public PictureDTO getPicture() {
        return picture.get();
    }

    public ObjectProperty<PictureDTO> pictureProperty() {
        return picture;
    }

    public ObjectProperty<TableView<AwardDTOFx>> awardTableViewProperty() {
        return awardTableViewProperty;
    }

    public ObjectProperty<TableView<EmailDTOFx>> emailTableViewProperty() {
        return emailTableViewProperty;
    }

    public ObjectProperty<TableView<OfficerFx>> officerTableViewProperty() {
        return officerTableViewProperty;
    }

    public ObjectProperty<TableView<PhoneFx>> phoneTableViewProperty() {
        return phoneTableViewProperty;
    }

    public ObjectProperty<StackPane> stackPaneProperty() {
        return stackPaneProperty;
    }

    public ObjectProperty<RadioButton>  radioButtonProperty() {
        return radioButtonProperty;
    }

    public ObjectProperty<ComboBox<String>> comboBoxProperty() {
        return comboBoxProperty;
    }

    public ObjectProperty<AwardDTOFx> selectedAwardProperty() {
        return selectedAward;
    }

    public ObjectProperty<PhoneFx> selectedPhoneProperty() {
        return selectedPhone;
    }
}
