package org.ecsail.mvci_membership;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class MembershipView implements Builder<Region> {

    MembershipModel membershipModel;
    public MembershipView(MembershipModel mm) {
        membershipModel = mm;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        return borderPane;
    }
}
