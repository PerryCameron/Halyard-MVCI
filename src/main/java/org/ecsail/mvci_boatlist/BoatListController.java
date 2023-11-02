package org.ecsail.mvci_boatlist;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class BoatListController extends Controller<BoatListMessage> {
    MainController mainController;
    BoatListInteractor boatListInteractor;
    BoatListView boatListView;

    public BoatListController(MainController mc) {
        mainController = mc;
        BoatListModel boatListModel = new BoatListModel(mainController.getMainModel());
        boatListInteractor = new BoatListInteractor(boatListModel, mainController.getConnections());
        boatListView = new BoatListView(boatListModel, this::action);
        getBoatListData();
    }
    @Override
    public void action(BoatListMessage action) {
        switch (action) {
            case SEARCH -> search();
            case CHANGE_LIST -> changeList();
            case EXPORT_XPS -> exportRoster();
            case UPDATE -> updateBoatList();
            case LAUNCH_TAB -> launchTab();
        }
    }

    private void updateBoatList() {
        boatListInteractor.updateBoat();
    }

    private void launchTab() { boatListInteractor.launchBoatTab(mainController); }

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

    private void changeList() {
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
        mainController.setSpinnerOffset(50,50);
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
