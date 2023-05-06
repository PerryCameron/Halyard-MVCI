package org.ecsail.mvci_membership;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.interfaces.Messages;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.TabPaneFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.function.BiConsumer;

public class MembershipView implements Builder<Region> {

    private final MembershipModel membershipModel;
    private final BiConsumer<Messages.MessageType, Object> personEdit;
    protected MembershipView(MembershipModel mm, BiConsumer<Messages.MessageType, Object> personEdit) {
        membershipModel = mm;
        this.personEdit = personEdit;
    }

    @Override
    public Region build() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,10,0,10));
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createHeader());
        borderPane.setLeft(createPeopleTabPane());
        borderPane.setCenter(createInfoTabPane());
        vBox.getChildren().add(borderPane);
        return vBox;
    }

    private Node createInfoTabPane() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,0,0,10)); // gives space between tabPanes
        TabPane tabPane = TabPaneFx.tabPaneOf(TabPane.TabClosingPolicy.UNAVAILABLE, 498);
        tabPane.setId("custom-tab-pane");
        tabPane.getTabs().add(new SlipTabView(this).build());
        vBox.getChildren().add(tabPane);
        return vBox;
    }

    private Node createPeopleTabPane() {
        TabPane tabPane = TabPaneFx.tabPaneOf(TabPane.TabClosingPolicy.UNAVAILABLE, 498);
        tabPane.setId("custom-tab-pane");
        // temp listener used at tab open, to wait for data first.
        membershipModel.listsLoadedProperty().addListener((observable, oldValue, newValue)
                -> membershipModel.getPeople().forEach(personDTO
                -> tabPane.getTabs().add(new PersonTabView(this, personDTO).build())));
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            PersonTabView personTabView = (PersonTabView) newTab.getUserData();// Get the associated PersonTabView object
            membershipModel.setSelectedPerson(personTabView.getPerson());
        });
        return tabPane;
    }

    private Node createHeader() {
        HBox hBox = HBoxFx.hBoxOf(Pos.TOP_CENTER, new Insets(15,15,10,15), 100);
        hBox.getChildren().add(newBox("Record Year: ", membershipModel.getMembership().selectedYearProperty()));
        hBox.getChildren().add(newBox("Membership ID: ", membershipModel.getMembership().membershipIdProperty()));
        hBox.getChildren().add(newBox("Membership Type: ", membershipModel.getMembership().memTypeProperty()));
        hBox.getChildren().add(newBox("Record Year: ", membershipModel.getMembership().joinDateProperty()));
        return hBox;
    }
    private Node newBox(String s, Property<?> property) {
        HBox hBox = HBoxFx.hBoxOf(5.0, Pos.CENTER_LEFT);
        Text labelText = new Text(s);
        labelText.setId("invoice-text-label");
        StringBinding valueBinding = Bindings.createStringBinding(() -> String.valueOf(property.getValue()), property);
        Text valueText = new Text();
        valueText.setId("invoice-text-number");
        valueText.textProperty().bind(valueBinding);
        hBox.getChildren().addAll(labelText,valueText);
        return hBox;
    }

    protected MembershipModel getMembershipModel() {
        return membershipModel;
    }

    public BiConsumer<Messages.MessageType, Object> getPersonEdit() {
        return personEdit;
    }
}
