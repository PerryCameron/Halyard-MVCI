package org.ecsail.mvci_roster;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class RosterController extends Controller<RosterMessage> {
    MainController mainController;
    RosterInteractor rosterInteractor;
    RosterView rosterView;

    public RosterController(MainController mc) {
        mainController = mc;
        RosterModel rosterModel = new RosterModel(mainController.getMainModel());
        rosterInteractor = new RosterInteractor(rosterModel, mainController.getConnections());
        action(RosterMessage.GET_DATA); // moved this last, we will see if it works
        rosterView = new RosterView(rosterModel, this::action);
    }

    @Override
    public void action(RosterMessage action) {
        switch (action) {
            case LAUNCH_TAB -> launchTab();
            case SEARCH -> search();
            case EXPORT_XPS -> exportRoster();
            case CHANGE_LIST_TYPE, UPDATE_YEAR -> updateRoster();
            case GET_DATA -> getRosterData();
        }
    }

    private void launchTab() {
        mainController.openMembershipMVCI(rosterInteractor.getMembership());
    }

    private void exportRoster() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                rosterInteractor.exportRoster();
                return null;
            }
        };
        new Thread(task).start();
    }

    private void search() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                rosterInteractor.fillTableView(); // not FX
                Platform.runLater(() -> {
                    rosterInteractor.changeState(); // JFX Thread
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    private void updateRoster() {
        mainController.setSpinnerOffset(-175, -25); // default JFX Thread
        mainController.showLoadingSpinner(true); // default JFX Thread
        rosterInteractor.clearMainRoster(); // default JFX Thread
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                rosterInteractor.updateRoster(); // not FX
                rosterInteractor.fillTableView(); // not FX
                Platform.runLater(() -> {
                    rosterInteractor.changeState(); // JFX Thread
                    rosterInteractor.clearSearchBox(); // JFX Thread
                    rosterInteractor.sortRoster(); // JFX Thread
                });
                return null;
            }
        };
        task.setOnSucceeded(e -> mainController.showLoadingSpinner(false));
        new Thread(task).start();
    }

    private void getRosterData() {
        mainController.setSpinnerOffset(50, 50);
        mainController.showLoadingSpinner(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                rosterInteractor.getRadioChoices();
                rosterInteractor.getRosterSettings();
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            mainController.showLoadingSpinner(false);
            Platform.runLater(() -> {
                rosterInteractor.setListsLoaded();
                rosterInteractor.setRosterToTableview();// JFX Thread
                rosterInteractor.changeState(); // JFX Thread
            });
            rosterView.setRadioListener(); // set last, so it doesn't fire, when radios are created.
        });
        new Thread(task).start();
    }

    @Override
    public Region getView() {
        return rosterView.build();
    }
}
