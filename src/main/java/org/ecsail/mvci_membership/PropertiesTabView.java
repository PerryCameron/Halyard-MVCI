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
import org.ecsail.pdf.PDF_Envelope;
import org.ecsail.static_tools.LabelPrinter;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.TabFx;
import org.ecsail.widgetfx.VBoxFx;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Optional;

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
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2), "custom-tap-pane-frame", true); // makes outer border
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5),"box-background-light",true);
        vBox.getChildren().add(hBox);
        hBox.getChildren().add(addHBox());
        return vBox;
    }

    private Node addHBox() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 5, 5, 10));
        HBox.setHgrow(hBox, Priority.ALWAYS);
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
        HBox hBox = new HBox();
        RadioButton r1 = new RadioButton("#10 Envelope");
        RadioButton r2 = new RadioButton("#1 Catalog");
        Button button = new Button("Create Envelope");
        ToggleGroup tg = new ToggleGroup();
        r1.setToggleGroup(tg);
        r2.setToggleGroup(tg);
        r1.setSelected(true);
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(new Label("Print Envelope"), button, r1, r2);
        button.setOnAction(e -> {
            membershipView.getMembershipModel().setEnvelopeIsCatalogue(r2.isSelected());
            membershipView.sendMessage().accept(MembershipMessage.PRINT_ENVELOPE);
        });
        return hBox;
    }

    private Node delMembership() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(new Label("Delete Membership"), removeMembershipButton());
        return hBox;
    }

    private Node printCardLabels() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(new Label("Print Membership Card Labels"), printLabelsButton1(), printLabelsButton2());
        return hBox;
    }

    private Node removeMembershipButton() {
        Button button = new Button("Delete");
        button.setOnAction(e -> {
            Alert conformation = new Alert(Alert.AlertType.CONFIRMATION);
            conformation.setTitle("Delete Membership");
            conformation.setHeaderText("Membership " + membershipView.getMembershipModel().getMembership().getMembershipId());
            conformation.setContentText("Are sure you want to delete this membership?\n\n");
            DialogPane dialogPane = conformation.getDialogPane();
            dialogPane.getStylesheets().add("css/dark/dialogue.css");
            dialogPane.getStyleClass().add("dialog");
            Optional<ButtonType> result = conformation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteMembership(membershipView.getMembershipModel().getMembership().getMsId());
            }
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

    private void deleteMembership(int msId) {
//        Dialogue_CustomErrorMessage dialogue = new Dialogue_CustomErrorMessage(true);
//        if (slipRepository.existsSlipWithMsId(msId)) {
//            dialogue.setTitle("Looks like we have a problem");
//            dialogue.setText("You must re-assign their slip before deleting this membership");
//            return;
//        } else {
//            dialogue.setTitle("Deleting Membership MSID:" + msId);
//        }
//        Task<Object> task = new Task<>() {
//            @Override
//            protected Object call() throws Exception {
//                setMessage("Deleting boats", dialogue);
//                boatRepository.deleteBoatOwner(msId);
//                setMessage("Deleting notes", dialogue);
//                memoRepository.deleteMemos(msId);
//                setMessage("Deleting Invoices and Payments", dialogue);
//                invoiceRepository.deleteAllPaymentsAndInvoicesByMsId(msId);
//                setMessage("Deleting wait_list entries", dialogue);
//                slipRepository.deleteWaitList(msId);
//                setMessage("Deleting membership hash", dialogue);
//                membershipRepository.deleteFormMsIdHash(msId);
//                setMessage("Deleting history",dialogue);
//                membershipIdRepository.deleteMembershipId(msId);
//                List<PersonDTO> people = personRepository.getPeople(msId);
//                setMessage("Deleting membership", dialogue);
//                membershipRepository.deleteMembership(msId);
//                setMessage("Deleting people", dialogue);
//                for (PersonDTO p : people) {
//                    phoneRepository.deletePhones(p.getpId());
//                    emailRepository.deleteEmail(p.getpId());
//                    officerRepository.delete(p.getpId());
//                    personRepository.deletePerson(p.getpId());
//                }
//
//                return null;
//            }
//        };
//        task.setOnSucceeded(succeed -> {
//                    Launcher.removeMembershipRow(msId);
//                    Launcher.closeActiveTab();
//                    BaseApplication.logger.info("Deleted membership msid: " + msId);
//                    dialogue.setText("Sucessfully deleted membership MSID: " + msId);
//                }
//        );
//        new Thread(task).start();
//
    }
//    private void setMessage(String message, Dialogue_CustomErrorMessage dialogue) {
//        Platform.runLater(() -> {
//            dialogue.setText(message);
//        });
//    }


}
