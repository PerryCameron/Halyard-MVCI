package org.ecsail.mvci.connect;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import okhttp3.Response;
import org.ecsail.fx.LoginDTOProperty;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci.main.MainController;
import org.ecsail.widgetfx.DialogueFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class ConnectController extends Controller<ConnectMessage> {
    private static final Logger logger = LoggerFactory.getLogger(ConnectController.class);
    MainController mainController;
    ConnectView connectView;
    ConnectInteractor connectInteractor;

    public ConnectController(MainController mainController) {
        ConnectModel connectModel = new ConnectModel(mainController.getMainModel());
        this.mainController = mainController;
        connectInteractor = new ConnectInteractor(connectModel);
        action(ConnectMessage.SUPPLY_LOGINS);
        connectView = new ConnectView(connectInteractor.getConnectModel(), this::action);
    }

    @Override
    public void action(ConnectMessage actionEnum) {
        switch (actionEnum) {
            case SAVE_LOGINS -> connectInteractor.saveLoginObjects();
            case CONNECT_TO_SERVER -> connectToServer();
            case SUPPLY_LOGINS -> connectInteractor.supplyLogins();
            case COPY_CURRENT_TO_MATCHING -> connectInteractor.copyCurrentLoginToMatchingLoginInList();
            case SET_CURRENT_LOGIN_AS_DEFAULT -> connectInteractor.setCurrentLoginAsDefault();
            case UPDATE_CURRENT_LOGIN -> connectInteractor.updateCurrentLogin();
            case UPDATE_COMBO_BOX -> connectInteractor.updateComboBox();
            case CREATE_NEW_LOGIN -> connectInteractor.createNewLogin();
            case DELETE_CURRENT_LOGIN -> connectInteractor.deleteLogin();
        }
    }

    @Override
    public Region getView() {
        return connectView.build();
    }

    private void connectToServer() {
        Task<Boolean> connectTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                logger.info("Connecting to server...");
                if(connectInteractor.requiresAuthentication())
                    logger.info("Requires authentication");
                return connectInteractor.connectToServer();
            }
        };
        connectTask.setOnSucceeded(event -> {
            connectInteractor.setRotateShipWheel(false);
            if (connectTask.getValue()) {
                mainController.createLoadingController();
                connectInteractor.closeLoginStage();
                mainController.openWelcomeMVCI();
                mainController.setStatus("Connected to " + connectInteractor.server());
                mainController.getGlobalData();
            }
        });
        connectTask.setOnFailed(event -> {
            connectInteractor.setRotateShipWheel(false);
            Throwable e = connectTask.getException();
            logger.error(connectTask.getMessage());
            if (e != null) {
                boolean retry = DialogueFx.showMaxSessionsDialog();
                if (retry) {
                    Task<Void> logoutTask = new Task<>() {
                        @Override
                        protected Void call() throws Exception {
                            try (Response logoutResponse = connectInteractor.logOutOthers()) {
                                if (logoutResponse.isSuccessful()) {
                                    logger.info("Successfully logged out other sessions");
                                } else {
                                    throw new Exception("Failed to log out other sessions");
                                }
                            }
                            return null;
                        }
                    };
                    logoutTask.setOnSucceeded(evt -> connectToServer()); // Retry login
                    logoutTask.setOnFailed(evt -> DialogueFx.errorAlert("Error", "Failed to log out other sessions.")); // this needs to change
                    getExecutor().submit(logoutTask);
                }
            } else {
                DialogueFx.errorAlert("Error", e.getMessage());
            }
        });
        getExecutor().submit(connectTask);
    }

    private ExecutorService getExecutor() {
        return mainController.getExecutorService();
    }

    public LoginDTOProperty getLogin() {
        return connectInteractor.getLoginDTOProperty();
    }

    public Stage getStage() {
        return connectInteractor.getStage();
    }

    public void setStageHeightListener() {  // Not crazy about accessing view here
        connectView.setStageHeightListener();
    }

}
