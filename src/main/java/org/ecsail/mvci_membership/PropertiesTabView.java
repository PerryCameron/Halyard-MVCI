package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.LabelDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.static_tools.LabelPrinter;
import org.ecsail.widgetfx.DialogueFx;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.TabFx;

import java.time.Year;
import java.util.ArrayList;

public class PropertiesTabView implements Builder<Tab> {
    private final MembershipView membershipView;

    public PropertiesTabView(MembershipView membershipView) {
        this.membershipView = membershipView;
    }

    @Override
    public Tab build() {
        return TabFx.tabOf("Properties", addControls());
    }

    private Node addControls() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5),"box-background-light",true);
        hBox.getChildren().add(addHBox());
        return hBox;
    }

    private Node addHBox() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 5, 5, 10));
        VBox.setVgrow(hBox, Priority.ALWAYS);
        hBox.setId("box-background-light");
        hBox.getChildren().addAll(createLeftVBox(), createRightVBox());
        return hBox;
    }

    private Node createRightVBox() {
        VBox vBox = new VBox();
        return vBox;
    }

    private Node createLeftVBox() {
        VBox leftVBox = new VBox();
        leftVBox.setSpacing(20);
        leftVBox.getChildren().addAll(createPrintEnvelope(), printCardLabels(), delMembership());
        return leftVBox;
    }

    private Node createPrintEnvelope() {
        HBox hBox = HBoxFx.hBoxOf(5, Pos.CENTER_LEFT);
        RadioButton r1 = new RadioButton("#10 Envelope");
        RadioButton r2 = new RadioButton("#1 Catalog");
        Button button = new Button("Create Envelope");
        ToggleGroup tg = new ToggleGroup();
        r1.setToggleGroup(tg);
        r2.setToggleGroup(tg);
        r1.setSelected(true);
        hBox.getChildren().addAll(new Label("Print Envelope"), button, r1, r2);
        button.setOnAction(e -> {
            membershipView.getMembershipModel().setEnvelopeIsCatalogue(r2.isSelected());
            membershipView.sendMessage().accept(MembershipMessage.PRINT_ENVELOPE);
        });
        return hBox;
    }

    private Node delMembership() {
        HBox hBox = HBoxFx.hBoxOf(5, Pos.CENTER_LEFT);
        hBox.getChildren().addAll(new Label("Delete Membership"), removeMembershipButton());
        return hBox;
    }

    private Node printCardLabels() {
        HBox hBox = HBoxFx.hBoxOf(5, Pos.CENTER_LEFT);
        hBox.getChildren().addAll(new Label("Print Membership Card Labels"), printLabelsButton1(), printLabelsButton2());
        return hBox;
    }

    private Node removeMembershipButton() {
        Button button = new Button("Delete");
        button.setOnAction(e -> {
            String[] strings = {
                    "Delete Membership",
                    "Are you sure you want to delete membership " + membershipView.getMembershipModel().getMembership().getMembershipId() + "?",
                    "",
                    ""};
            if (DialogueFx.verifyAction(strings, membershipView.getMembershipModel().getMembership()))
                membershipView.sendMessage().accept(MembershipMessage.DELETE_MEMBERSHIP);
        });
        return button;
    }

    private Node printLabelsButton2() {
        Button button = new Button("Print Secondary");
        button.setOnAction((actionEvent -> {
            ArrayList<LabelDTO> labels = new ArrayList<>();
            LabelDTO label;
            for (PersonDTO person : membershipView.getMembershipModel().getPeople()) {
                if (person.getMemberType() == 2) {
                    label = new LabelDTO();
                    label.setCity("Indianapolis, Indiana");
                    label.setNameAndMemId(person.getFullName() + " #" + membershipView.getMembershipModel().getMembership().getMembershipId());
                    label.setExpires("Type " + membershipView.getMembershipModel().getMembership().getMemType() + ", Expires: " + "03/01/" + getYear());
                    label.setMember("Member: U.S. Sailing ILYA &YCA");
                    labels.add(label);
                    LabelPrinter.printMembershipLabel(label);
                }
            }
        }));
        return button;
    }

    private Node printLabelsButton1() {
        Button button = new Button("Print Primary");
        button.setOnAction((actionEvent -> {
            ArrayList<LabelDTO> labels = new ArrayList<>();
            LabelDTO label;
            for (PersonDTO person : membershipView.getMembershipModel().getPeople()) {
                if (person.getMemberType() == 1) {
                    label = new LabelDTO();
                    label.setCity("Indianapolis, Indiana");
                    label.setNameAndMemId(person.getFullName() + " #" + String.valueOf(membershipView.getMembershipModel().getMembership().getMembershipId()));
                    label.setExpires("Type " + membershipView.getMembershipModel().getMembership().getMemType() + ", Expires: " + "03/01/" + getYear());
                    label.setMember("Member: U.S. Sailing ILYA &YCA");
                    labels.add(label);
                    LabelPrinter.printMembershipLabel(label);
                }
            }
        }));
        return button;
    }

    private String getYear() {
        int current = Integer.parseInt(String.valueOf(Year.now().getValue()));
        return String.valueOf(current + 1);
    }
}
