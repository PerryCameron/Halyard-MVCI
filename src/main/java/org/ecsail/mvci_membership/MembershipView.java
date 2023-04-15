package org.ecsail.mvci_membership;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.TabPaneFx;
import org.ecsail.widgetfx.VBoxFx;

public class MembershipView implements Builder<Region> {

    MembershipModel membershipModel;
    public MembershipView(MembershipModel mm) {
        membershipModel = mm;
    }

    @Override
    public Region build() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,5,0,10));
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createHeader());
        borderPane.setLeft(createPeopleTabPane());
        vBox.getChildren().add(borderPane);
        return vBox;
    }

    private Node createPeopleTabPane() {
        TabPane tabPane = TabPaneFx.tabPaneOf(TabPane.TabClosingPolicy.UNAVAILABLE, 498);
        tabPane.setId("custom-tab-pane");
        membershipModel.listsLoadedProperty().addListener((observable, oldValue, newValue) -> {
            membershipModel.getPeople().forEach(personDTO
                    -> tabPane.getTabs().add(new PersonTabView(this, personDTO).build()));
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
}
