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
        rosterView = new RosterView(rosterModel, this::changeState);
        getRosterOnLaunch();
    }

    private void changeState() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                rosterInteractor.changeState();
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
            rosterView.setRadioListener(); // set last, so it doesn't fire, when radios are created.
        });
        new Thread(task).start();
    }

    @Override
    public Region getView() { return rosterView.build(); }
}
