package org.ecsail.mvci_membership;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class MembershipController extends Controller {

    MembershipInteractor membershipInteractor;
    MembershipView membershipView;
    MainController mainController;

    public MembershipController(MainController mc, MembershipListDTO ml) {
        this.mainController = mc;
        MembershipModel membershipModel = new MembershipModel(ml , mainController.getMainModel());
        this.membershipInteractor = new MembershipInteractor(membershipModel,mainController.getConnections());
        getDataForMembership();
        membershipView = new MembershipView(membershipModel, this::editRow);
    }

    private void editRow(MembershipMessage type) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                switch (type) {
                    case UPDATE_MEMBERSHIP_LIST -> membershipInteractor.getDataBaseService().updateMembershipList();
                    case UPDATE_MEMBERSHIP_ID -> membershipInteractor.getDataBaseService().updateMembershipId();
                    case UPDATE_AWARD -> membershipInteractor.getDataBaseService().updateAward();
                    case UPDATE_EMAIL -> membershipInteractor.getDataBaseService().updateEmail();
                    case UPDATE_BOAT -> membershipInteractor.getDataBaseService().updateBoat();
                    case UPDATE_NOTE -> membershipInteractor.getDataBaseService().updateNote();
                    case UPDATE_PHONE -> membershipInteractor.getDataBaseService().updatePhone();
                    case UPDATE_OFFICER -> membershipInteractor.getDataBaseService().updateOfficer();
                    case UPDATE_PERSON -> membershipInteractor.getDataBaseService().updatePerson();
                    case INSERT_BOAT -> membershipInteractor.getDataBaseService().insertBoat();
                    case INSERT_AWARD -> membershipInteractor.getDataBaseService().insertAward();
                    case INSERT_EMAIL -> membershipInteractor.getDataBaseService().insertEmail();
                    case INSERT_MEMBERSHIP_ID -> membershipInteractor.getDataBaseService().insertMembershipId();
                    case INSERT_NOTE -> membershipInteractor.getDataBaseService().insertNote("");
                    case INSERT_OFFICER -> membershipInteractor.getDataBaseService().insertOfficer();
                    case INSERT_PERSON -> membershipInteractor.getDataBaseService().insertPerson();
                    case INSERT_PHONE -> membershipInteractor.getDataBaseService().insertPhone();
                    case DELETE_BOAT -> membershipInteractor.getDataBaseService().deleteBoat();
                    case DELETE_AWARD -> membershipInteractor.getDataBaseService().deleteAward();
                    case DELETE_EMAIL -> membershipInteractor.getDataBaseService().deleteEmail();
                    case DELETE_MEMBERSHIP_ID -> membershipInteractor.getDataBaseService().deleteMembershipId();
                    case DELETE_NOTE -> membershipInteractor.getDataBaseService().deleteNote();
                    case DELETE_OFFICER -> membershipInteractor.getDataBaseService().deleteOfficer();
                    case DELETE_PHONE -> membershipInteractor.getDataBaseService().deletePhone();
                    case CHANGE_MEMBER_TYPE -> membershipInteractor.getDataBaseService().changeMemberType();
                    case DETACH_MEMBER_FROM_MEMBERSHIP -> membershipInteractor.getDataBaseService().detachMemberFromMembership();
                    case DETACH_PRIMARY_MEMBER_FROM_MEMBERSHIP -> membershipInteractor.getDataBaseService().detachPrimaryMemberFromMembership();
                    case MOVE_SECONDARY_TO_PRIMARY -> membershipInteractor.getDataBaseService().swapSecondaryToPrimary();
                    case DELETE_MEMBER_FROM_DATABASE -> membershipInteractor.getDataBaseService().deletePerson();
                    case MOVE_MEMBER_TO_MEMBERSHIP -> membershipInteractor.getDataBaseService().movePerson();
                    case UPLOAD_MEMBER_PHOTO -> membershipInteractor.uploadMemberPhoto();
                    case LOAD_INVOICES -> loadSmallTab(MembershipMessage.LOAD_INVOICES);
                    case LOAD_IDS -> loadSmallTab(MembershipMessage.LOAD_IDS);
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void loadSmallTab(MembershipMessage type) {
        mainController.setSpinnerOffset(-400,150);
        mainController.showLoadingSpinner(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                switch (type) {
                   case LOAD_INVOICES -> membershipInteractor.getDataBaseService().getInvoices();
                   case LOAD_IDS -> membershipInteractor.getDataBaseService().getIds();
                }
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            mainController.showLoadingSpinner(false);
        });
        new Thread(task).start();
    }

    private void getDataForMembership() {
        mainController.setSpinnerOffset(50,50);
        mainController.showLoadingSpinner(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                membershipInteractor.getDataBaseService().getPersonLists();
                membershipInteractor.getDataBaseService().getSlipInfo();
                membershipInteractor.getDataBaseService().getBoats();
                membershipInteractor.getDataBaseService().getNotes();
//                membershipInteractor.getDataBaseService().getIds(ml);
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
