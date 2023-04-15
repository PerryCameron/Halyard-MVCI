package org.ecsail.mvci_membership;

import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.dto.PersonDTO;
import org.ecsail.widgetfx.TextFieldFx;

public class PersonTab extends Tab implements Builder<Tab> {
    private final PersonDTO person;
    private final MembershipModel membershipModel;
    public PersonTab(PersonDTO personDTO, MembershipModel membershipModel) {
        this.person = personDTO;
        this.membershipModel = membershipModel;
    }

    @Override
    public Tab build() {
        Tab tab = new Tab();
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(createFieldDetails());
        tab.setContent(borderPane);
        return tab;
    }

    private Node createFieldDetails() {
        VBox vBox = new VBox();
        vBox.getChildren().add(fieldBox(person.fnameProperty(), "First Name"));
        vBox.getChildren().add(fieldBox(person.lnameProperty(), "Last Name"));
        vBox.getChildren().add(fieldBox(person.nnameProperty(), "Nickname"));
        vBox.getChildren().add(fieldBox(person.occupationProperty(), "Occupation"));
        vBox.getChildren().add(fieldBox(person.businessProperty(), "Business"));
        vBox.getChildren().add(fieldBox(person.birthdayProperty(), "Birthday"));
        return vBox;
    }


    private Node fieldBox(Property<?> property, String label) {
        HBox hBox = new HBox();
        TextField textField = TextFieldFx.textFieldOf(150, property);
        Text text = new Text();
        return hBox;
    }


}
