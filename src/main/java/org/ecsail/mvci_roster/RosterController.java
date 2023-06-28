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
        rosterView = new RosterView(rosterModel, this::action);
        getRosterData();
    }

    private void action(RosterMessage action) {
        switch(action) {
            case UPDATE -> System.out.println("Update?");
            case LAUNCH_TAB -> launchTab();
            case SEARCH -> search();
            case EXPORT_XPS -> exportRoster();
            case CHANGE_LIST_TYPE -> changeListType();
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
                rosterInteractor.fillTableView();
                return null;
            }
        };
        new Thread(task).start();
    }

    private void changeListType() {
        mainController.setSpinnerOffset(-175,-25);
        mainController.showLoadingSpinner(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                rosterInteractor.changeListType();
                return null;
            }
        };
        task.setOnSucceeded(e -> mainController.showLoadingSpinner(false));
        new Thread(task).start();
    }

    private void getRosterData() {
        mainController.setSpinnerOffset(-175,-25);
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
            rosterInteractor.setListsLoaded(true);
            rosterInteractor.setRosterToTableview();
            rosterView.setRadioListener(); // set last, so it doesn't fire, when radios are created.
        });
        new Thread(task).start();
    }

    @Override
    public Region getView() { return rosterView.build(); }
}
