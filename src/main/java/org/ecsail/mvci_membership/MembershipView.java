package org.ecsail.mvci_membership;

import javafx.scene.layout.Region;
import javafx.util.Builder;

public class MembershipView implements Builder<Region> {

    MembershipModel boatListModel;
    public MembershipView(MembershipModel rm) {
        boatListModel = rm;
    }

    @Override
    public Region build() {
        return null;
    }
}
