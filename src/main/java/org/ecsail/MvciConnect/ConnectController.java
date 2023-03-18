package org.ecsail.MvciConnect;

import javafx.scene.layout.Region;
import org.ecsail.MvciMain.MainController;

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
