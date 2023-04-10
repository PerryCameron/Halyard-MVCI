package org.ecsail.mvci_roster;

import javafx.scene.layout.Region;
import javafx.util.Builder;

public class RosterView implements Builder<Region> {

    RosterModel rosterModel;
    public RosterView(RosterModel rm) {
        rosterModel = rm;
    }

    @Override
    public Region build() {
        return null;
    }
}
