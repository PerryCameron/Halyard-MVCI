package org.ecsail.mvci_connect;

import javafx.concurrent.Task;
import org.ecsail.dto.LoginDTO;
import org.ecsail.mvci_main.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ConnectController {
    MainController mainController;
    ConnectModel connectModel = new ConnectModel();
    ConnectView connectView;
    ConnectInteractor connectInteractor;

    private static final Logger logger = LoggerFactory.getLogger(ConnectController.class);


    public ConnectController(MainController mainController) {
        this.mainController = mainController;
        connectInteractor = new ConnectInteractor(connectModel);
        connectView = new ConnectView(connectModel, this::saveLogins, this::loginSupply, this::connectToServer);
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

    private void connectToServer(Void unused) {
        Task<Boolean> connectTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                // Perform database connection here
                return connectInteractor.getConnections().connect();
            }
        };
        connectTask.setOnSucceeded(event -> {
            boolean connectionSuccessful = connectTask.getValue();
            if (connectionSuccessful) {
                connectModel.setRotateShipWheel(false);
//                primaryStage.setTitle("Halyard");
//                BaseApplication.tabPane.getTabs().remove(BaseApplication.tabPane.getSelectionModel().getSelectedIndex());
//                BaseApplication.tabPane.getTabs().add(new TabWelcome(new HBoxWelcome()));
//                showStatus();
//                logonStage.close();
                // Code to execute after the task completes successfully
            } else {
                // Handle the case where the connection fails
            }
        });
        connectTask.setOnFailed(event -> {
            logger.error(connectTask.getException().getMessage());
        });
        Thread thread = new Thread(connectTask);
        thread.start();
    }
}
