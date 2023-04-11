package org.ecsail.mvci_roster;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class RosterController extends Controller {
    MainController mainController;
    RosterInteractor rosterInteractor;
    RosterView rosterView;

    public RosterController(MainController mc) {
        mainController = mc;
        RosterModel rosterModel = new RosterModel();
        rosterInteractor = new RosterInteractor(rosterModel,mainController.getConnections());
        rosterView = new RosterView(rosterModel);
        getRosterOnLaunch();
    }

    private void getRosterOnLaunch() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    rosterInteractor.getSelectedRoster();
                } catch (Exception e) {
                }
                return null;
            }
        };
        task.setOnSucceeded(e -> rosterInteractor.setRosterToTableview());
        new Thread(task).start();
    }

    @Override
    public Region getView() { return rosterView.build(); }
}
