package org.ecsail.mvci_membership;

import javafx.scene.layout.Region;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class MembershipController extends Controller {

    MembershipInteractor rosterInteractor;
    MembershipView membershipView;

    public MembershipController(MainController mainController, MembershipListDTO membershipListDTO) {
        MembershipModel membershipModel = new MembershipModel(membershipListDTO);
        membershipView = new MembershipView(membershipModel);
    }

    @Override
    public Region getView() { return membershipView.build(); }
}
