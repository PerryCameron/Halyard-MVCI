package org.ecsail.mvci_membership;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class MembershipController extends Controller<MembershipMessage> {

    MembershipInteractor membershipInteractor;
    MembershipView membershipView;
    MainController mainController;
    DataBaseService db;

    public MembershipController(MainController mc, MembershipListDTO ml) {
        this.mainController = mc;
        MembershipModel membershipModel = new MembershipModel(ml , mainController.getMainModel());
        this.membershipInteractor = new MembershipInteractor(membershipModel,mainController.getConnections());
        this.db = membershipInteractor.getDataBaseService();
        getData();
        membershipView = new MembershipView(membershipModel, this::action);
    }

    @Override
    public void action(MembershipMessage type) {
        switch (type) {
            case SELECT_INVOICES -> runSpinner(MembershipMessage.SELECT_INVOICES);
            case SELECT_IDS -> runSpinner(MembershipMessage.SELECT_IDS);
            case SELECT_INVOICE -> runSpinner(MembershipMessage.SELECT_INVOICE);
            case SELECT_FEES -> runSpinner(MembershipMessage.SELECT_FEES);
            case UPDATE_INVOICE -> runSpinner(MembershipMessage.UPDATE_INVOICE);
            case UPDATE_INVOICE_ONLY -> runSpinner(MembershipMessage.UPDATE_INVOICE_ONLY);
            case UPDATE_MEMBERSHIP_LIST -> runTask(MembershipMessage.UPDATE_MEMBERSHIP_LIST);
            case UPDATE_MEMBERSHIP_ID -> runTask(MembershipMessage.UPDATE_MEMBERSHIP_ID);
            case UPDATE_AWARD -> runTask(MembershipMessage.UPDATE_AWARD);
            case UPDATE_EMAIL -> runTask(MembershipMessage.UPDATE_EMAIL);
            case UPDATE_BOAT -> runTask(MembershipMessage.UPDATE_BOAT);
            case UPDATE_NOTE -> runTask(MembershipMessage.UPDATE_NOTE);
            case UPDATE_PHONE -> runTask(MembershipMessage.UPDATE_PHONE);
            case UPDATE_OFFICER -> runTask(MembershipMessage.UPDATE_OFFICER);
            case UPDATE_PERSON -> runTask(MembershipMessage.UPDATE_PERSON);
            case UPDATE_PAYMENT -> runTask(MembershipMessage.UPDATE_PAYMENT);
            case INSERT_BOAT -> runTask(MembershipMessage.INSERT_BOAT);
            case INSERT_AWARD -> runTask(MembershipMessage.INSERT_AWARD);
            case INSERT_EMAIL -> runTask(MembershipMessage.INSERT_EMAIL);
            case INSERT_MEMBERSHIP_ID -> runTask(MembershipMessage.INSERT_MEMBERSHIP_ID);
            case INSERT_NOTE -> runTask(MembershipMessage.INSERT_NOTE);
            case INSERT_INVOICE_NOTE -> runTask(MembershipMessage.INSERT_INVOICE_NOTE);
            case INSERT_OFFICER -> runTask(MembershipMessage.INSERT_OFFICER);
            case INSERT_PERSON -> runTask(MembershipMessage.INSERT_PERSON);
            case INSERT_PHONE -> runTask(MembershipMessage.INSERT_PHONE);
            case INSERT_INVOICE -> runSpinner(MembershipMessage.INSERT_INVOICE);
            case INSERT_PAYMENT -> runTask(MembershipMessage.INSERT_PAYMENT);
            case DELETE_BOAT -> runTask(MembershipMessage.DELETE_BOAT);
            case DELETE_AWARD -> runTask(MembershipMessage.DELETE_AWARD);
            case DELETE_EMAIL -> runTask(MembershipMessage.DELETE_EMAIL);
            case DELETE_MEMBERSHIP_ID -> runTask(MembershipMessage.DELETE_MEMBERSHIP_ID);
            case DELETE_NOTE -> runTask(MembershipMessage.DELETE_NOTE);
            case DELETE_OFFICER -> runTask(MembershipMessage.DELETE_OFFICER);
            case DELETE_PHONE -> runTask(MembershipMessage.DELETE_PHONE);
            case DELETE_INVOICE -> runTask(MembershipMessage.DELETE_INVOICE);
            case DELETE_PAYMENT -> runTask(MembershipMessage.DELETE_PAYMENT);
            case CHANGE_MEMBER_TYPE -> runTask(MembershipMessage.CHANGE_MEMBER_TYPE);
            case DETACH_MEMBER_FROM_MEMBERSHIP -> runTask(MembershipMessage.DETACH_MEMBER_FROM_MEMBERSHIP);
            case DETACH_PRIMARY_MEMBER_FROM_MEMBERSHIP -> runTask(MembershipMessage.DETACH_PRIMARY_MEMBER_FROM_MEMBERSHIP);
            case MOVE_SECONDARY_TO_PRIMARY -> runTask(MembershipMessage.MOVE_SECONDARY_TO_PRIMARY);
            case DELETE_MEMBER_FROM_DATABASE -> runTask(MembershipMessage.DELETE_MEMBER_FROM_DATABASE);
            case MOVE_MEMBER_TO_MEMBERSHIP -> runTask(MembershipMessage.MOVE_MEMBER_TO_MEMBERSHIP);
            case UPLOAD_MEMBER_PHOTO -> runTask(MembershipMessage.UPLOAD_MEMBER_PHOTO);
            case SAVE_INVOICE -> runSpinner(MembershipMessage.SAVE_INVOICE);
        }
    }

    private void runTask(MembershipMessage type) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                switch (type) {
                    case UPDATE_MEMBERSHIP_LIST -> db.updateMembershipList();
                    case UPDATE_MEMBERSHIP_ID -> db.updateMembershipId();
                    case UPDATE_AWARD -> db.updateAward();
                    case UPDATE_EMAIL -> db.updateEmail();
                    case UPDATE_BOAT -> db.updateBoat();
                    case UPDATE_NOTE -> db.updateNote();
                    case UPDATE_PHONE -> db.updatePhone();
                    case UPDATE_OFFICER -> db.updateOfficer();
                    case UPDATE_PERSON -> db.updatePerson();
                    case UPDATE_PAYMENT -> db.updatePayment();
                    case INSERT_BOAT -> db.insertBoat();
                    case INSERT_AWARD -> db.insertAward();
                    case INSERT_EMAIL -> db.insertEmail();
                    case INSERT_MEMBERSHIP_ID -> db.insertMembershipId();
                    case INSERT_NOTE -> db.insertNote("");
                    case INSERT_INVOICE_NOTE -> db.insertInvoiceNote();
                    case INSERT_OFFICER -> db.insertOfficer();
                    case INSERT_PERSON -> db.insertPerson();
                    case INSERT_PHONE -> db.insertPhone();
                    case INSERT_PAYMENT -> db.insertPayment();
                    case DELETE_BOAT -> db.deleteBoat();
                    case DELETE_AWARD -> db.deleteAward();
                    case DELETE_EMAIL -> db.deleteEmail();
                    case DELETE_MEMBERSHIP_ID -> db.deleteMembershipId();
                    case DELETE_NOTE -> db.deleteNote();
                    case DELETE_OFFICER -> db.deleteOfficer();
                    case DELETE_PHONE -> db.deletePhone();
                    case DELETE_INVOICE -> db.deleteInvoice();
                    case DELETE_PAYMENT -> db.deletePayment();
                    case CHANGE_MEMBER_TYPE -> db.changeMemberType();
                    case DETACH_MEMBER_FROM_MEMBERSHIP -> db.detachMemberFromMembership();
                    case DETACH_PRIMARY_MEMBER_FROM_MEMBERSHIP -> db.detachPrimaryMemberFromMembership();
                    case MOVE_SECONDARY_TO_PRIMARY -> db.swapSecondaryToPrimary();
                    case DELETE_MEMBER_FROM_DATABASE -> db.deletePerson();
                    case MOVE_MEMBER_TO_MEMBERSHIP -> db.movePerson();
                    case UPLOAD_MEMBER_PHOTO -> membershipInteractor.uploadMemberPhoto();
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void runSpinner(MembershipMessage type) {
        mainController.setSpinnerOffset(-430, 150);
        mainController.showLoadingSpinner(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                switch (type) {
                    case SELECT_INVOICES -> db.selectInvoices();
                    case SELECT_IDS -> db.selectIds();
                    case SELECT_INVOICE -> db.selectInvoice();
                    case SELECT_FEES -> db.selectFees();
                    case UPDATE_INVOICE -> db.updateInvoice();
                    case UPDATE_INVOICE_ONLY -> db.updateInvoiceOnly();
                    case SAVE_INVOICE -> db.saveInvoice();
                    case INSERT_INVOICE -> db.insertInvoice();
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
                db.selectPersons();
                db.selectSlipInfo();
                db.selectBoats();
                db.selectNotes();
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            mainController.showLoadingSpinner(false);
            membershipInteractor.setListsLoaded();
        });
        new Thread(task).start();
    }

    @Override
    public Region getView() { return membershipView.build(); }
}
