package org.ecsail.mvci_membership;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.dto.PersonDTO;
import org.ecsail.widgetfx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class MembershipView implements Builder<Region> {

    private final MembershipModel membershipModel;
    private final Consumer<MembershipMessage> action;
    private static final Logger logger = LoggerFactory.getLogger(MembershipView.class);

    protected MembershipView(MembershipModel mm, Consumer<MembershipMessage> a) {
        membershipModel = mm;
        this.action = a;
    }

    @Override
    public Region build() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,10,0,10));
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createHeader());
        borderPane.setLeft(createPeopleTabPane());
        borderPane.setRight(createInfoTabPane());
        borderPane.setCenter(creteDivider());
        borderPane.setBottom(createExtrasTabPane());
        vBox.getChildren().add(borderPane);
        setViewListener();
        return vBox;
    }

    private Node creteDivider() {
        Region region = new Region();
        region.setPrefWidth(10);
        return region;
    }

    private void setViewListener() {
        ChangeListener<MembershipMessage> viewListener = ListenerFx.createEnumListener(() ->
                viewMessaging(membershipModel.returnMessageProperty().get()).run());
        membershipModel.returnMessageProperty().addListener(viewListener);
    }

    private Runnable viewMessaging(MembershipMessage message) { // when database updates, this makes UI reflect.
        return () -> {
            switch (message) {
                case DATA_LOAD_SUCCEED -> launchDataDependentUI();
                case DELETE_MEMBER_FROM_DATABASE_SUCCEED -> removePersonTab();
                case INSERT_PERSON_SUCCEED -> addPerson();
            }
        };
    }

    private void addPerson() {
        membershipModel.getPeople().add(membershipModel.getSelectedPerson());
        Tab newTab = new PersonTabView(this, new PersonDTO(membershipModel.getSelectedPerson())).build();
        membershipModel.getPeopleTabPane().getTabs().add(newTab);
        // Select the newly added tab
        membershipModel.getPeopleTabPane().getSelectionModel().select(newTab);
        getAddPersonTab().clearPersonDTO(); // clears PersonDTO that is part of this tab.
        selectExtraTabByName("Notes"); // open notes tab after creating entry
    }

    private void removePersonTab() {
        int selectedIndex = membershipModel.getPeopleTabPane().getSelectionModel().getSelectedIndex();
        membershipModel.getPeopleTabPane().getTabs().remove(selectedIndex);
    }

    private void launchDataDependentUI() {
            membershipModel.getPeople().forEach(personDTO -> membershipModel.getPeopleTabPane().getTabs()
                    .add(new PersonTabView(this, personDTO).build()));
            membershipModel.getPeopleTabPane().getTabs().add(new AddPersonTabView(this).build());
            // right tabPane
            membershipModel.getInfoTabPane().getTabs().add(new SlipTabView(this).build());
            membershipModel.getInfoTabPane().getTabs().add(new MembershipIdView(this).build());
            membershipModel.getInfoTabPane().getTabs().add(new InvoiceListView(this).build());
            // bottom tabPane
            membershipModel.getExtraTabPane().getTabs().add(new BoatTabView(this).build());
            membershipModel.getExtraTabPane().getTabs().add(new NotesTabView(this).build());
            membershipModel.getExtraTabPane().getTabs().add(new PropertiesTabView(this).build());
            membershipModel.getExtraTabPane().getTabs().add(new AttachmentsTabView(this).build());
            membershipModel.getExtraTabPane().getTabs().add(new AddressTabView(this).build());
    }

    private Node createExtrasTabPane() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(10,0,0,0)); // provides space between
        TabPane tabPane = TabPaneFx.tabPaneOf(TabPane.TabClosingPolicy.UNAVAILABLE,"custom-tab-pane");
        HBox.setHgrow(tabPane,Priority.ALWAYS);
        membershipModel.setExtraTabPane(tabPane);
        hBox.getChildren().add(tabPane);
        return hBox;
    }

    private Node createInfoTabPane() {
        TabPane tabPane = TabPaneFx.tabPaneOf(TabPane.TabClosingPolicy.UNAVAILABLE, 498,"custom-tab-pane");
        VBox.setVgrow(tabPane,Priority.ALWAYS);  // this works in combo with Vgrow in SlapTabView
        membershipModel.setInfoTabPane(tabPane);
        return tabPane;
    }

    private Node createPeopleTabPane() {
        TabPane tabPane = TabPaneFx.tabPaneOf(TabPane.TabClosingPolicy.UNAVAILABLE, 498,"custom-tab-pane");
        membershipModel.setPeopleTabPane(tabPane);
        membershipModel.getPeopleTabPane().getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if(newTab.getText().equals("Add")) {
                AddPersonTabView addPersonTabView = (AddPersonTabView) newTab.getUserData();
                membershipModel.setSelectedPerson(addPersonTabView.getPersonDTO());
                logger.debug("Showing Add tab: " + membershipModel.getSelectedPerson());
            } else {
                PersonTabView personTabView = (PersonTabView) newTab.getUserData();// Get the associated PersonTabView object
                membershipModel.setSelectedPerson(personTabView.getPersonDTO());
                logger.debug("Showing Person tab: " + membershipModel.getSelectedPerson());
            }
        });
        return tabPane;
    }

    private Node createHeader() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(15,15,10,15), Pos.TOP_CENTER, 100);
        hBox.getChildren().add(newBox("Record Year: ", membershipModel.getMembership().selectedYearProperty()));
        hBox.getChildren().add(newBox("Membership ID: ", membershipModel.getMembership().membershipIdProperty()));
        hBox.getChildren().add(newBox("Membership Type: ", membershipModel.getMembership().memTypeProperty()));
        hBox.getChildren().add(newBox("Join Date: ", membershipModel.getMembership().joinDateProperty()));
        return hBox;
    }

    private Node newBox(String s, Property<?> property) {
        HBox hBox = HBoxFx.hBoxOf(5.0, Pos.CENTER_LEFT);
        Text labelText = TextFx.textOf(s,"invoice-text-label");
        StringBinding valueBinding = Bindings.createStringBinding(() -> String.valueOf(property.getValue()), property);
        Text valueText = TextFx.textOf("", "invoice-text-number");
        valueText.textProperty().bind(valueBinding);
        hBox.getChildren().addAll(labelText,valueText);
        return hBox;
    }

    protected void selectExtraTabByName(String name) {
        for (Tab tab : membershipModel.getExtraTabPane().getTabs()) {
            if (tab.getText().equals(name)) {
                membershipModel.getExtraTabPane().getSelectionModel().select(tab);
                break;
            }
        }
    }

    private AddPersonTabView getAddPersonTab() {
        for (Tab tab : membershipModel.getPeopleTabPane().getTabs()) {
            if (tab.getText().equals("Add")) {
                return (AddPersonTabView) tab;
            }
        }
        return null;
    }

    protected MembershipModel getMembershipModel() {
        return membershipModel;
    }

    protected Consumer<MembershipMessage> sendMessage() {
        return action;
    }
}
