package org.ecsail.mvci_connect;

import org.ecsail.dto.LoginDTO;
import org.ecsail.mvci_main.MainController;

import java.util.ArrayList;

public class ConnectController {
    MainController mainController;
    ConnectModel connectModel = new ConnectModel();
    ConnectView connectView;
    ConnectInteractor connectInteractor;

    public ConnectController(MainController mainController) {
        this.mainController = mainController;
        connectInteractor = new ConnectInteractor(connectModel);
        connectView = new ConnectView(connectModel, this::saveLogins, this::loginSupply);
    }

    private void saveLogins(Void unused) {
        connectInteractor.saveLoginObjects();
    }

    private ArrayList<LoginDTO> loginSupply () {
        return new ArrayList<>(connectInteractor.supplyLogins());
    }

    public ConnectController getView() {
        connectView.createStage(connectView.build());
        return this;
    }
}
