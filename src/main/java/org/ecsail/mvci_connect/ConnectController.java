package org.ecsail.mvci_connect;

import com.jcraft.jsch.JSchException;
import javafx.concurrent.Task;
import org.ecsail.BaseApplication;
import org.ecsail.connection.PortForwardingL;
import org.ecsail.dto.LoginDTO;
import org.ecsail.mvci_main.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
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

    private void saveLogins() {
        connectInteractor.saveLoginObjects();
    }

    private ArrayList<LoginDTO> loginSupply () {
        return new ArrayList<>(connectInteractor.supplyLogins());
    }

    public ConnectController getView() {
        connectView.createStage(connectView.build());
        return this;
    }

    private void connectToServer() {
        Task<Boolean> connectTask = new Task<>() {
            @Override
            protected Boolean call() {
                // Perform database connection here
                return connectInteractor.getConnections().connect();
            }
        };
        connectTask.setOnSucceeded(event -> {
            boolean connectionSuccessful = connectTask.getValue();
            if (connectionSuccessful) {
                connectModel.setRotateShipWheel(false);
                mainController.setStatus("(Connected) " + connectModel.getHost());
                BaseApplication.loginStage.close();
                mainController.openWelcomeMVCI();
            } else {
                // Handle the case where the connection fails
            }
        });
        connectTask.setOnFailed(event -> logger.error(connectTask.getException().getMessage()));
        Thread thread = new Thread(connectTask);
        thread.start();
    }

    public Runnable closeConnection() {
        return () -> {
            try {
                connectInteractor.getConnections().getSqlConnection().close();
                logger.info("SQL: Connection closed");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            PortForwardingL sshConnection = connectInteractor.getConnections().getSshConnection();
            // if ssh is connected then disconnect
            if (sshConnection != null && sshConnection.getSession().isConnected()) {
                try {
                    sshConnection.getSession().delPortForwardingL(3306);
                    sshConnection.getSession().disconnect();
                    logger.info("SSH: port forwarding closed");
                } catch (JSchException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public ConnectInteractor getConnectInteractor() {
        return connectInteractor;
    }
}
