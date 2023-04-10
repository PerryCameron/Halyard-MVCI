package org.ecsail.mvci_boats;

import javafx.scene.layout.Region;
import javafx.util.Builder;

public class BoatListView implements Builder<Region> {

    BoatListModel boatListModel;
    public BoatListView(BoatListModel rm) {
        boatListModel = rm;
    }

    @Override
    public Region build() {
        return null;
    }
}
