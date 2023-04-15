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
        MembershipModel membershipModel = new MembershipModel(ml);
        this.membershipInteractor = new MembershipInteractor(membershipModel,mainController.getConnections());
        getDataForMembership(ml);
        membershipView = new MembershipView(membershipModel);
    }

    private void getDataForMembership(MembershipListDTO ml) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                membershipInteractor.getPeople(ml);
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            System.out.println("succeeded");
        });
        new Thread(task).start();
    }

    @Override
    public Region getView() { return membershipView.build(); }
}
