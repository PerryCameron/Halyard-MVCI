package org.ecsail.mvci_membership;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.interfaces.Controller;
import org.ecsail.interfaces.Messages;
import org.ecsail.mvci_main.MainController;

public class MembershipController extends Controller {

    MembershipInteractor membershipInteractor;
    MembershipView membershipView;
    MainController mainController;

    public MembershipController(MainController mc, MembershipListDTO ml) {
        this.mainController = mc;
        MembershipModel membershipModel = new MembershipModel(ml , mainController.getMainModel());
        this.membershipInteractor = new MembershipInteractor(membershipModel,mainController.getConnections());
        getDataForMembership(ml);
        membershipView = new MembershipView(membershipModel, this::personEdit);
    }

    private void personEdit(Messages.MessageType type, Object o) {
        membershipInteractor.printPersonMessage(type, o);
    }

    private void getDataForMembership(MembershipListDTO ml) {
        mainController.setSpinnerOffset(50,50);
        mainController.showLoadingSpinner(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                membershipInteractor.getPersonLists(ml);
                membershipInteractor.getSlipInfo(ml);
                membershipInteractor.getBoats(ml);
                membershipInteractor.getNotes(ml);
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            mainController.showLoadingSpinner(false);
            membershipInteractor.setListsLoaded(true);
        });
        new Thread(task).start();
    }

    @Override
    public Region getView() { return membershipView.build(); }
}
