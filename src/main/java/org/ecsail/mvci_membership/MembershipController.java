package org.ecsail.mvci_membership;

import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class MembershipController extends Controller {

    MembershipInteractor rosterInteractor;
    MembershipView membershipView;

    public MembershipController(MainController mainController) {
        MembershipModel boatListModel = new MembershipModel();
        membershipView = new MembershipView(boatListModel);
    }

    @Override
    public Region getView() { return membershipView.build(); }
}
