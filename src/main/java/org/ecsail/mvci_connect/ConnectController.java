package org.ecsail.mvci_connect;

import com.jcraft.jsch.JSchException;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.ecsail.BaseApplication;
import org.ecsail.connection.PortForwardingL;
import org.ecsail.dto.LoginDTO;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectController extends Controller<ConnectMessage> {
    MainController mainController;
    ConnectView connectView;
    ConnectInteractor connectInteractor;

    public ConnectController(MainController mainController) {
        ConnectModel connectModel = new ConnectModel();
        this.mainController = mainController;
        connectInteractor = new ConnectInteractor(connectModel);
        action(ConnectMessage.SUPPLY_LOGINS);
        connectView = new ConnectView(connectModel, this::action);
    }
    @Override
    public void action(ConnectMessage actionEnum) {
        switch (actionEnum) {
            case SAVE_LOGINS -> connectInteractor.saveLoginObjects();
            case CONNECT_TO_SERVER -> connectToServer();
            case SUPPLY_LOGINS -> connectInteractor.supplyLogins();
        }
    }

    @Override
    public Region getView() {
        return connectView.build();
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
            System.out.println("getting to setOnSucceeded");
                connectInteractor.setRotateShipWheel(false);
                mainController.createLoadingController();
                mainController.setStatus("(Connected) " + connectInteractor.getHost());
                connectInteractor.closeLoginStage();
                mainController.openWelcomeMVCI();
                mainController.loadCommonLists();
        });
        connectTask.setOnFailed(event -> connectInteractor.logError(connectTask.getException().getMessage()));
        Thread thread = new Thread(connectTask);
        thread.start();
    }

    public void closeConnection() {
            try {
                connectInteractor.getConnections().getSqlConnection().close();
                connectInteractor.logInfo("SQL: Connection closed");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            PortForwardingL sshConnection = connectInteractor.getConnections().getSshConnection();
            // if ssh is connected then disconnect
            if (sshConnection != null && sshConnection.getSession().isConnected()) {
                connectInteractor.logInfo("ssh connection is active: " + sshConnection.getSession().isConnected());
                try {
                    sshConnection.getSession().delPortForwardingL(3306);
                    sshConnection.getSession().disconnect();
                    connectInteractor.logInfo("SSH: port forwarding closed");
                } catch (JSchException e) {
                    e.printStackTrace();
                }
            }
    }

    public Stage getStage() {
        return connectInteractor.getStage();
    }

    public void setStageHeightListener() {  // Not crazy about accessing view here
        connectView.setStageHeightListener();
    }

    public ConnectInteractor getConnectInteractor() {
        return connectInteractor;
    }
}
