package org.ecsail.mvci_boats;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class BoatListController extends Controller {
    MainController mainController;
    BoatListInteractor boatListInteractor;
    BoatListView boatListView;

    public BoatListController(MainController mc) {
        mainController = mc;
        BoatListModel boatListModel = new BoatListModel();
        boatListInteractor = new BoatListInteractor(boatListModel, mainController.getConnections());
        boatListView = new BoatListView(boatListModel, this::changeListType);
        getBoatListData();
    }

    private void changeListType() {
        mainController.setSpinnerOffset(-175,-25);
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
        mainController.setSpinnerOffset(-175,-25);
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
            boatListInteractor.getListSize();
        });
        new Thread(task).start();
    }

    @Override
    public Region getView() { return boatListView.build(); }
}
