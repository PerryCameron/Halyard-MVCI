package org.ecsail.mvci_roster;

import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class RosterController extends Controller {

    RosterInteractor rosterInteractor;
    RosterView rosterView;

    public RosterController(MainController mainController) {
        RosterModel rosterModel = new RosterModel();
        rosterView = new RosterView(rosterModel);
    }

    @Override
    public Region getView() { return rosterView.build(); }
}
