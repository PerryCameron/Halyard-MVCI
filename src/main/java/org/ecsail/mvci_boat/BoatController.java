package org.ecsail.mvci_boat;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.interfaces.ListCallBack;
import org.ecsail.mvci_boatlist.BoatListInteractor;
import org.ecsail.mvci_boatlist.BoatListModel;
import org.ecsail.mvci_boatlist.BoatListView;
import org.ecsail.mvci_main.MainController;

public class BoatController extends Controller implements ListCallBack {
    MainController mainController;
    BoatInteractor boatInteractor;
    BoatView boatView;

    public BoatController(MainController mc) {
        mainController = mc;
        BoatModel boatModel = new BoatModel(mainController.getMainModel());
        boatInteractor = new BoatInteractor(boatModel, mainController.getConnections());
        boatView = new BoatView(boatModel, this::action);
        getBoatData();
    }

    private void getBoatData() {
    }

    private void action(Mode mode) {
        switch (mode) {
            case SEARCH -> System.out.println("");
        }
    }


    @Override
    public Region getView() { return boatView.build(); }
}
