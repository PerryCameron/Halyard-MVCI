package org.ecsail.mvci_connect;

import org.ecsail.mvci_main.MainController;

public class ConnectController {
    MainController mainController;
    ConnectModel connectModel = new ConnectModel();
    ConnectView connectView;

    public ConnectController(MainController mainController) {
        this.mainController = mainController;
        connectView = new ConnectView(connectModel);
    }

    public ConnectController getView() {
        connectView.createStage(connectView.build());
        return this;
    }

}
