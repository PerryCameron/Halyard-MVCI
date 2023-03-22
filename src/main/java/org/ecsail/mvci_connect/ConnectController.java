package org.ecsail.mvci_connect;

import org.ecsail.mvci_main.MainController;

public class ConnectController {
    MainController mainController;
    ConnectModel connectModel = new ConnectModel();
    ConnectView connectView;
    ConnectInteractor connectInteractor;

    public ConnectController(MainController mainController) {
        this.mainController = mainController;
        connectInteractor = new ConnectInteractor(connectModel);
        connectView = new ConnectView(connectModel, this::saveLogins);
    }

    private void saveLogins(Void unused) {
        connectInteractor.saveLoginObjects();
    }

    public ConnectController getView() {
        connectView.createStage(connectView.build());
        return this;
    }

}
