package org.ecsail.mvci_roster;

import javafx.application.Platform;
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
        rosterView = new RosterView(rosterModel, this::changeYear);
        getRosterOnLaunch();
    }

    private void changeYear() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                rosterInteractor.changeYear();
                return null;
            }
        };
//        task.setOnSucceeded(e -> rosterInteractor.setRosterToTableview());
        new Thread(task).start();
    }

    private void getRosterOnLaunch() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                rosterInteractor.getSelectedRoster();
                Platform.runLater(() -> rosterInteractor.getRadioChoices());
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            rosterInteractor.setRosterToTableview();
            rosterInteractor.getRadioChoicesSize();
        });
        new Thread(task).start();
    }

    @Override
    public Region getView() { return rosterView.build(); }
}
