package org.ecsail.mvci.membership.mvci.person;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.ecsail.fx.PersonFx;
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
}
