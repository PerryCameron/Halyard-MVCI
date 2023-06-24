package org.ecsail.mvci_boat;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.dto.BoatListDTO;
import org.ecsail.interfaces.Controller;
import org.ecsail.interfaces.ListCallBack;
import org.ecsail.mvci_main.MainController;

public class BoatController extends Controller implements ListCallBack {
    MainController mainController;
    BoatInteractor boatInteractor;
    BoatView boatView;

    public BoatController(MainController mc, BoatListDTO bl) {
        mainController = mc;
        BoatModel boatModel = new BoatModel(mainController.getMainModel());
        boatModel.setBoatListDTO(bl);
        boatInteractor = new BoatInteractor(boatModel, mainController.getConnections());
        boatView = new BoatView(boatModel, this::action);
        getBoatData();
    }

    private void getBoatData() {
            mainController.setSpinnerOffset(225,25);
            mainController.showLoadingSpinner(true);
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    boatInteractor.getBoatOwners();
                    boatInteractor.getBoatSettings();
                    boatInteractor.getBoatNotes();
                    return null;
                }
            };
            task.setOnSucceeded(e -> {
                mainController.showLoadingSpinner(false);
                boatInteractor.setDataLoaded(true);
            });
            new Thread(task).start();
    }

    private void action(Mode mode) {
        switch (mode) {
            case SEARCH -> System.out.println("");
        }
    }

    @Override
    public Region getView() { return boatView.build(); }
}
