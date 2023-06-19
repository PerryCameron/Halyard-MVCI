package org.ecsail.mvci_boatlist;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.dto.BoatListDTO;
import org.ecsail.interfaces.Controller;
import org.ecsail.interfaces.ListCallBack;
import org.ecsail.mvci_main.MainController;

public class BoatListController extends Controller implements ListCallBack {
    MainController mainController;
    BoatListInteractor boatListInteractor;
    BoatListView boatListView;

    public BoatListController(MainController mc) {
        mainController = mc;
        BoatListModel boatListModel = new BoatListModel();
        boatListInteractor = new BoatListInteractor(boatListModel, mainController.getConnections());
        boatListView = new BoatListView(boatListModel, this::action);
        getBoatListData();
    }

    private void action(ListCallBack.Mode mode) {
        switch (mode) {
            case SEARCH -> search();
            case CHANGE_STATE -> changeListType();
            case EXPORT_XPS -> exportRoster();
            case UPDATE -> updateBoatList();
            case LAUNCH_TAB -> launchTab();
        }
    }

    private void updateBoatList() {
        boatListInteractor.updateBoat();
    }

    private void launchTab() {
        System.out.println("Launch Tab");
    }

    private void exportRoster() {
        System.out.println("Exporting Roster to XLS");
    }

    private void search() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                boatListInteractor.fillTableView();
                return null;
            }
        };
        new Thread(task).start();
    }

    private void changeListType() {
        mainController.setSpinnerOffset(225,25);
        mainController.showLoadingSpinner(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                boatListInteractor.changeListType();
                return null;
            }
        };
        task.setOnSucceeded(e -> mainController.showLoadingSpinner(false));
        new Thread(task).start();
    }

    private void getBoatListData() {
        mainController.setSpinnerOffset(225,25);
        mainController.showLoadingSpinner(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                boatListInteractor.getRadioChoices();
                boatListInteractor.getBoatListSettings();
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            mainController.showLoadingSpinner(false);
            boatListInteractor.setListsLoaded(true);
            boatListInteractor.setBoatListToTableview();
            boatListView.setRadioListener(); // set last, so it doesn't fire, when radios are created.
        });
        new Thread(task).start();
    }

    @Override
    public Region getView() { return boatListView.build(); }
}
