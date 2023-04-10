package org.ecsail.mvci_boats;

import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class BoatListController extends Controller {

    BoatListInteractor rosterInteractor;
    BoatListView boatListView;

    public BoatListController(MainController mainController) {
        BoatListModel boatListModel = new BoatListModel();
        boatListView = new BoatListView(boatListModel);
    }

    @Override
    public Region getView() { return boatListView.build(); }
}
