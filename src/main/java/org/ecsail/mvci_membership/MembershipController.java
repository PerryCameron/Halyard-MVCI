package org.ecsail.mvci_membership;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.dto.RosterDTOFx;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;
import static org.ecsail.mvci_membership.MembershipMessage.*;

public class MembershipController extends Controller<MembershipMessage> {

    MembershipInteractor membershipInteractor;
    MembershipView membershipView;
    MainController mainController;

    public MembershipController(MainController mc, RosterDTOFx rosterDTOFxDTO, int selectedYear) {
        this.mainController = mc;
        MembershipModel membershipModel = new MembershipModel(rosterDTOFxDTO, selectedYear,  mainController.getMainModel());
        this.membershipInteractor = new MembershipInteractor(membershipModel);
        action(GET_DATA);
        membershipView = new MembershipView(membershipModel, this::action);
    }

    @Override
    public void action(MembershipMessage type) {
        switch (type) {
            case GET_DATA -> getData();
//            case SELECT_INVOICES -> runSpinner(SELECT_INVOICES,-430, 150);
//            case SELECT_IDS -> runSpinner(SELECT_IDS,-430, 150);
            case SELECT_INVOICE -> runSpinner(SELECT_INVOICE,-430, 150);
            case SELECT_FEES -> runSpinner(SELECT_FEES,-430, 150);
            case UPDATE_INVOICE -> runSpinner(UPDATE_INVOICE,-430, 150);
            case UPDATE_INVOICE_ONLY -> runSpinner(UPDATE_INVOICE_ONLY,-430, 150);
            case UPDATE_MEMBERSHIP_LIST -> runTask(UPDATE_MEMBERSHIP_LIST);
            case UPDATE_MEMBERSHIP_ID -> runTask(UPDATE_MEMBERSHIP_ID);
            case UPDATE_AWARD -> runTask(UPDATE_AWARD);
            case UPDATE_EMAIL -> runTask(UPDATE_EMAIL);
            case UPDATE_BOAT -> runTask(UPDATE_BOAT);
            case UPDATE_NOTE -> runTask(UPDATE_NOTE);
            case UPDATE_PHONE -> runTask(UPDATE_PHONE);
            case UPDATE_OFFICER -> runTask(UPDATE_OFFICER);
            case UPDATE_PERSON -> runTask(UPDATE_PERSON);
            case UPDATE_PAYMENT -> runTask(UPDATE_PAYMENT);
            case INSERT_BOAT -> runTask(INSERT_BOAT);
            case INSERT_AWARD -> runTask(INSERT_AWARD);
            case INSERT_EMAIL -> runTask(INSERT_EMAIL);
            case INSERT_MEMBERSHIP_ID -> runTask(INSERT_MEMBERSHIP_ID);
            case INSERT_NOTE -> runTask(INSERT_NOTE);
            case INSERT_INVOICE_NOTE -> runTask(INSERT_INVOICE_NOTE);
            case INSERT_OFFICER -> runTask(INSERT_OFFICER);
            case INSERT_PERSON -> runTask(INSERT_PERSON);
            case INSERT_PHONE -> runTask(INSERT_PHONE);
            case INSERT_INVOICE -> runSpinner(INSERT_INVOICE,-430, 150);
            case INSERT_PAYMENT -> runTask(INSERT_PAYMENT);
            case DELETE_BOAT -> runTask(DELETE_BOAT);
            case DELETE_AWARD -> runTask(DELETE_AWARD);
            case DELETE_EMAIL -> runTask(DELETE_EMAIL);
            case DELETE_MEMBERSHIP_ID -> runTask(DELETE_MEMBERSHIP_ID);
            case DELETE_MEMBERSHIP -> runSpinner(DELETE_MEMBERSHIP,50, 50);
            case DELETE_NOTE -> runTask(DELETE_NOTE);
            case DELETE_OFFICER -> runTask(DELETE_OFFICER);
            case DELETE_PHONE -> runTask(DELETE_PHONE);
            case DELETE_INVOICE -> runTask(DELETE_INVOICE);
            case DELETE_PAYMENT -> runTask(DELETE_PAYMENT);
            case CHANGE_MEMBER_TYPE -> runTask(CHANGE_MEMBER_TYPE);
            case DETACH_MEMBER_FROM_MEMBERSHIP -> runTask(DETACH_MEMBER_FROM_MEMBERSHIP);
            case DETACH_PRIMARY_MEMBER_FROM_MEMBERSHIP -> runTask(DETACH_PRIMARY_MEMBER_FROM_MEMBERSHIP);
            case MOVE_SECONDARY_TO_PRIMARY -> runTask(MOVE_SECONDARY_TO_PRIMARY);
            case DELETE_MEMBER_FROM_DATABASE -> runTask(DELETE_MEMBER_FROM_DATABASE);
            case MOVE_MEMBER_TO_MEMBERSHIP -> runTask(MOVE_MEMBER_TO_MEMBERSHIP);
            case UPLOAD_MEMBER_PHOTO -> runTask(UPLOAD_MEMBER_PHOTO);
            case SAVE_INVOICE -> runSpinner(SAVE_INVOICE,-430, 150);
            case PRINT_ENVELOPE -> runTask(PRINT_ENVELOPE);
        }
    }

    private void runTask(MembershipMessage type) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                switch (type) {
//                    case UPDATE_MEMBERSHIP_LIST -> db.updateMembershipList();
//                    case UPDATE_MEMBERSHIP_ID -> db.updateMembershipId();
//                    case UPDATE_AWARD -> db.updateAward();
//                    case UPDATE_EMAIL -> db.updateEmail();
                    case UPDATE_BOAT -> membershipInteractor.updateBoat();
//                    case UPDATE_NOTE -> db.updateNote();
//                    case UPDATE_PHONE -> db.updatePhone();
//                    case UPDATE_OFFICER -> db.updateOfficer();
                    case UPDATE_PERSON -> membershipInteractor.updatePerson();
//                    case UPDATE_PAYMENT -> db.updatePayment();
//                    case INSERT_BOAT -> db.insertBoat();
//                    case INSERT_AWARD -> db.insertAward();
//                    case INSERT_EMAIL -> db.insertEmail();
//                    case INSERT_MEMBERSHIP_ID -> db.insertMembershipId();
//                    case INSERT_NOTE -> db.insertNote("");
//                    case INSERT_INVOICE_NOTE -> db.insertInvoiceNote();
//                    case INSERT_OFFICER -> db.insertOfficer();
//                    case INSERT_PERSON -> db.insertPerson();
//                    case INSERT_PHONE -> db.insertPhone();
//                    case INSERT_PAYMENT -> db.insertPayment();
//                    case DELETE_BOAT -> db.deleteBoat();
//                    case DELETE_AWARD -> db.deleteAward();
//                    case DELETE_EMAIL -> db.deleteEmail();
//                    case DELETE_MEMBERSHIP_ID -> db.deleteMembershipId();
//                    case DELETE_NOTE -> db.deleteNote();
//                    case DELETE_OFFICER -> db.deleteOfficer();
//                    case DELETE_PHONE -> db.deletePhone();
//                    case DELETE_INVOICE -> db.deleteInvoice();
//                    case DELETE_PAYMENT -> db.deletePayment();
//                    case CHANGE_MEMBER_TYPE -> db.changeMemberType();
//                    case DETACH_MEMBER_FROM_MEMBERSHIP -> db.detachMemberFromMembership();
//                    case DETACH_PRIMARY_MEMBER_FROM_MEMBERSHIP -> db.detachPrimaryMemberFromMembership();
//                    case MOVE_SECONDARY_TO_PRIMARY -> db.swapSecondaryToPrimary();
//                    case DELETE_MEMBER_FROM_DATABASE -> db.deletePerson();
//                    case MOVE_MEMBER_TO_MEMBERSHIP -> db.movePerson();
                    case UPLOAD_MEMBER_PHOTO -> membershipInteractor.uploadMemberPhoto();
                    case PRINT_ENVELOPE -> membershipInteractor.printEnvelope();
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void runSpinner(MembershipMessage type, double x, double y) {
        mainController.setSpinnerOffset(x, y);
        mainController.showLoadingSpinner(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                switch (type) {
//                    case SELECT_INVOICES -> db.selectInvoices(); <- this has more pieces not in use
//                    case SELECT_IDS -> db.selectIds(); <- this has more pieces not in use
//                    case SELECT_INVOICE -> db.selectInvoice();
//                    case SELECT_FEES -> db.selectFees();
//                    case UPDATE_INVOICE -> db.updateInvoice();
//                    case UPDATE_INVOICE_ONLY -> db.updateInvoiceOnly();
//                    case SAVE_INVOICE -> db.saveInvoice();
//                    case INSERT_INVOICE -> db.insertInvoice();
                    case DELETE_MEMBERSHIP -> membershipInteractor.deleteMembership();
                }
                return null;
            }
        };
        task.setOnSucceeded(e -> mainController.showLoadingSpinner(false));
        new Thread(task).start();
    }

    private void getData() {
        mainController.setSpinnerOffset(50,50);
        mainController.showLoadingSpinner(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                // get membership as JSON, deserialize it and then covert to FX objects and set in model
                membershipInteractor.convertPOJOsToFXProperties(membershipInteractor.getMembershiptoPOJO());
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            mainController.showLoadingSpinner(false);
            membershipInteractor.setDataLoaded();
        });
        new Thread(task).start();
    }

    @Override
    public Region getView() {
        return membershipView.build();
    }
}
