package org.ecsail.mvci.membership;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.fx.PersonFx;
import org.ecsail.enums.MemberType;
import org.ecsail.mvci.membership.components.*;
import org.ecsail.mvci.membership.mvci.person.PersonController;
import org.ecsail.mvci.membership.mvci.person.PersonView;
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
        VBox vBox = VBoxFx.vBoxOf(new Insets(0, 10, 0, 10));
        BorderPane borderPane = new BorderPane();
        vBox.getChildren().add(borderPane);
        setDataLoadedListener(borderPane);
        return vBox;
    }

    private void setDataLoadedListener(BorderPane borderPane) {
        membershipModel.dataIsLoadedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                try {
                    borderPane.setTop(createHeader());
                    borderPane.setLeft(createPeopleTabPane());
                    borderPane.setRight(createInfoTabPane());
                    borderPane.setCenter(creteDivider());
                    borderPane.setBottom(createExtrasTabPane());
                    addPeopleTabs();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void addPeopleTabs() {
        membershipModel.membershipProperty().get().getPeople().forEach(personDTO -> membershipModel.getPeopleTabPane().getTabs()
                      .add(new PersonController(this, personDTO).getView()));
        membershipModel.getPeopleTabPane().getTabs().add(new AddPersonTabView(this).build());
        membershipModel.personAddedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(() -> {
                    Tab tab = new PersonController(this, membershipModel.getSelectedPerson()).getView();
                    membershipModel.getPeopleTabPane().getTabs().add(tab);
                    membershipModel.getPeopleTabPane().getSelectionModel().select(tab);
                });

            }
        });
    }

    private Node creteDivider() {
        Region region = new Region();
        region.setPrefWidth(10);
        return region;
    }

    private Runnable viewMessaging(MembershipMessage message) { // when database updates, this makes UI reflect.
        return () -> {
            switch (message) {
//                case DATA_LOAD_SUCCEED -> launchDataDependentUI();
                case DELETE_MEMBER_FROM_DATABASE_SUCCEED -> removePersonTab();
                case DELETE_PRIMARY_MEMBER_FROM_DATABASE_SUCCEED -> afterPrimaryMemberRemoved();
                case MOVE_SECONDARY_TO_PRIMARY_SUCCEED -> changeTabName("Secondary", "Primary");
//                case INSERT_PERSON_SUCCEED -> addPerson();
                case DELETE_MEMBERSHIP_FROM_DATABASE_SUCCEED, DELETE_MEMBERSHIP_FROM_DATABASE_FAIL ->
                        displayDeleteTab();
            }
        };
    }

    private void displayDeleteTab() {
//        switch (membershipModel.getReturnMessage()) {
//            case DELETE_MEMBERSHIP_FROM_DATABASE_SUCCEED -> {
//                System.out.println("Successfully deleted membership");
//                int success[] = membershipModel.getSuccess();
//                String message =
//                        "Boat Owner entries removed: " + success[0] + "\n" +
//                                "Membership Notes removed: " + success[1] + "\n" +
//                                "Payments Removed: " + success[2] + "\n" +
//                                "Invoice Items Removed: " + success[3] + "\n" +
//                                "Invoices Removed: " + success[4] + "\n" + "Invoices Removed: " + success[4] + "\n" +
//                                "Wait List Removed: " + success[5] + "\n" +
//                                "FormMsIDHash entry removed: " + success[6] + "\n" +
//                                "Membership ID's removed: " + success[7] + "\n" +
//                                "Persons in membership: " + success[8] + "\n" +
//                                "Phones deleted: " + success[10] + "\n" +
//                                "Email deleted: " + success[11] + "\n" +
//                                "Officer Records deleted: " + success[12] + "\n" +
//                                "User Auth Requests deleted: " + success[13] + "\n" +
//                                "People in membership deleted: " + success[14] + "\n" +
//                                "Memberships Deleted: " + success[9] + "\n";
//                System.out.println(message);
//                try {
//                    DialogueFx.customAlert("Membership Successfully Deleted", message, Alert.AlertType.INFORMATION);
//                } catch (Exception e) {
//                    System.out.println(e);
//                }
//            }
//
//            case DELETE_MEMBERSHIP_FROM_DATABASE_FAIL -> {
//                DialogueFx.errorAlert("Unable to delete membership", "");
//            }
//        }
    }

//    private void addPerson() {
//        membershipModel.getPeople().add(membershipModel.getSelectedPerson());
//        //Tab newTab = new PersonTabView(this, new PersonFx(membershipModel.getSelectedPerson())).build();
//        Tab newTab = new PersonController(this, new PersonFx(membershipModel.getSelectedPerson())).getView();
//        membershipModel.getPeopleTabPane().getTabs().add(newTab);
//        // Select the newly added tab
//        membershipModel.getPeopleTabPane().getSelectionModel().select(newTab);
//        getAddPersonTab().clearPersonDTO(); // clears PersonDTO that is part of this tab.
//        selectExtraTabByName("Notes"); // open notes tab after creating entry
//    }

    private void afterPrimaryMemberRemoved() { // gets called with message after primary removed
        removePersonTab();
        moveSecondaryToPrimary();
    }

    private void moveSecondaryToPrimary() {
        membershipModel.setSelectedPerson(getSecondaryMember());
        logger.info("Moving Secondary to Primary: " + membershipModel.getSelectedPerson().getFullName());
        membershipModel.getSelectedPerson().setMemberType(MemberType.getCode(MemberType.PRIMARY));
        action.accept(MembershipMessage.MOVE_SECONDARY_TO_PRIMARY);
    }

    // had to change this to use people in membership
    private PersonFx getSecondaryMember() {
        System.out.println("getSecondaryMember()  (PersonTabView)");
        return membershipModel.membershipProperty().get().getPeople().stream()
                .filter(p -> p.getMemberType() == MemberType.SECONDARY.getCode())
                .findFirst()
                .orElse(null);
    }

    private void removePersonTab() {
        System.out.println("removePersonTab() membershipView");
        Tab tab = membershipModel.getPeopleTabPane().getSelectionModel().getSelectedItem();
        membershipModel.getPeopleTabPane().getTabs().remove(tab);
    }

    private Node createExtrasTabPane() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(10, 0, 0, 0)); // provides space between
        TabPane tabPane = PaneFx.tabPaneOf(TabPane.TabClosingPolicy.UNAVAILABLE, "custom-tab-pane");
        HBox.setHgrow(tabPane, Priority.ALWAYS);
        membershipModel.setExtraTabPane(tabPane);
        tabPane.getTabs().add(new BoatTabView(this).build());
        tabPane.getTabs().add(new NotesTabView(this).build());
        tabPane.getTabs().add(new PropertiesTabView(this).build());
//        tabPane.getTabs().add(new AttachmentsTabView(this).build());
        tabPane.getTabs().add(new AddressTabView(this).build());
        hBox.getChildren().add(tabPane);
        return hBox;
    }

    private Node createInfoTabPane() {
        TabPane tabPane = PaneFx.tabPaneOf(TabPane.TabClosingPolicy.UNAVAILABLE, 498, "custom-tab-pane");
        VBox.setVgrow(tabPane, Priority.ALWAYS);  // this works in combo with Vgrow in SlapTabView
        membershipModel.setInfoTabPane(tabPane);
        tabPane.getTabs().add(new SlipTabView(this).build());
        tabPane.getTabs().add(new MembershipIdView(this).build());
        tabPane.getTabs().add(new InvoiceListView(this).build());
        return tabPane;
    }

    private Node createPeopleTabPane() {
        TabPane tabPane = PaneFx.tabPaneOf(TabPane.TabClosingPolicy.UNAVAILABLE, 498, "custom-tab-pane");
        membershipModel.setPeopleTabPane(tabPane);

        membershipModel.getPeopleTabPane().getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab.getText().equals("Add")) {
                AddPersonTabView addPersonTabView = (AddPersonTabView) newTab.getUserData();
                membershipModel.setSelectedPerson(addPersonTabView.getPersonFx());
                logger.debug("Showing Add tab: {}", membershipModel.getSelectedPerson());
            } else {
                PersonView personView = (PersonView) newTab.getUserData(); // Cast to PersonView
                membershipModel.setSelectedPerson(personView.getPersonDTO()); // Get PersonFx
                System.out.println(newTab.getUserData());
                logger.debug("Showing Person tab: {}", membershipModel.getSelectedPerson());
            }
        });
        return tabPane;
    }

    private Node createHeader() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(15, 15, 10, 15), Pos.TOP_CENTER, 100);
        hBox.getChildren().add(newBox("Record Year: ", membershipModel.membershipProperty().get().fiscalYearProperty()));
        hBox.getChildren().add(newBox("Membership ID: ", membershipModel.membershipProperty().get().membershipIdProperty()));
        hBox.getChildren().add(newBox("Membership Type: ", membershipModel.membershipProperty().get().memTypeProperty()));
        hBox.getChildren().add(newBox("Join Date: ", membershipModel.membershipProperty().get().joinDateProperty()));
        return hBox;
    }

    private Node newBox(String s, Property<?> property) {
        HBox hBox = HBoxFx.hBoxOf(5.0, Pos.CENTER_LEFT);
        Text labelText = TextFx.textOf(s, "invoice-text-label");
        StringBinding valueBinding = Bindings.createStringBinding(() -> String.valueOf(property.getValue()), property);
        Text valueText = TextFx.textOf("", "invoice-text-number");
        valueText.textProperty().bind(valueBinding);
        hBox.getChildren().addAll(labelText, valueText);
        return hBox;
    }

    protected void selectExtraTabByName(String name) {
        membershipModel.getExtraTabPane().getTabs().stream()
                .filter(tab -> tab.getText().equals(name))
                .findFirst()
                .ifPresent(tab -> membershipModel.getExtraTabPane().getSelectionModel().select(tab));
    }

    protected void changeTabName(String name, String newName) {
        membershipModel.getPeopleTabPane().getTabs().stream()
                .filter(tab -> tab.getText().equals(name))
                .findFirst()
                .ifPresent(tab -> tab.setText(newName));
    }


    private AddPersonTabView getAddPersonTab() {
        return membershipModel.getPeopleTabPane().getTabs().stream()
                .filter(tab -> "Add".equals(tab.getText()))
                .map(tab -> (AddPersonTabView) tab)
                .findFirst()
                .orElse(null);
    }

    public MembershipModel getMembershipModel() {
        return membershipModel;
    }

    public Consumer<MembershipMessage> sendMessage() {
        return action;
    }
}
