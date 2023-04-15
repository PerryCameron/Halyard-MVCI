package org.ecsail.mvci_membership;

import javafx.scene.layout.Region;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class MembershipController extends Controller {

    MembershipInteractor rosterInteractor;
    MembershipView membershipView;
    MainController mainController;

    public MembershipController(MainController mc, MembershipListDTO ml) {
        System.out.println(ml);
        MembershipModel membershipModel = new MembershipModel(ml);
        this.mainController = mc;
        membershipView = new MembershipView(membershipModel);
    }

    @Override
    public Region getView() { return membershipView.build(); }
}
