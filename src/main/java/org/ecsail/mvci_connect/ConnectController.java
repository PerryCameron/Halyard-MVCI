package org.ecsail.mvci_connect;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import okhttp3.Response;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;
import org.ecsail.widgetfx.DialogueFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectController extends Controller<ConnectMessage> {
    private static final Logger logger = LoggerFactory.getLogger(ConnectController.class);
    MainController mainController;
    ConnectView connectView;
    ConnectInteractor connectInteractor;

    public ConnectController(MainController mainController) {
        ConnectModel connectModel = new ConnectModel();
        this.mainController = mainController;
        connectInteractor = new ConnectInteractor(connectModel);
        action(ConnectMessage.SUPPLY_LOGINS);
        checkIfAuthenticationRequired();
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
        return connectInteractor.getAuthenticationRequired() ? connectView.build() : null;
    }

    private void connectToServer() {
        Task<Boolean> connectTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                logger.info("Connecting to server...");
                return connectInteractor.connectToServer();
            }
        };
        connectTask.setOnSucceeded(event -> {
            connectInteractor.setRotateShipWheel(false);
            if (connectTask.getValue()) {
                mainController.createLoadingController();
                connectInteractor.closeLoginStage();
                mainController.openWelcomeMVCI();
            }
        });
        connectTask.setOnFailed(event -> {
            connectInteractor.setRotateShipWheel(false);
            Throwable e = connectTask.getException();
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
                    logoutTask.setOnFailed(evt -> DialogueFx.showAlert("Error", "Failed to log out other sessions."));
                    new Thread(logoutTask).start();
                }
            } else {
                DialogueFx.showAlert("Error", e.getMessage());
            }
        });
        Thread thread = new Thread(connectTask);
        thread.start();
    }

    private void checkIfAuthenticationRequired() {
        connectInteractor.requiresAuthenticationOrDialogue();
        if (!connectInteractor.getAuthenticationRequired()) {
            logger.info("No authentication required, proceeding directly.");
            connectInteractor.setRotateShipWheel(false);
            mainController.createLoadingController();
            connectInteractor.closeLoginStage();
            mainController.openWelcomeMVCI();
        } else {
            connectView = new ConnectView(connectInteractor.getConnectModel(), this::action);
        }
    }

    public HttpClientUtil getHttpClient() {
        return connectInteractor.getHttpClient();
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
