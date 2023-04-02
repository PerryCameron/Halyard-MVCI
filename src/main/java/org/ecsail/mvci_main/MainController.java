package org.ecsail.mvci_main;


import javafx.application.Platform;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.ecsail.mvci_connect.ConnectController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private final MainInteractor mainInteractor;
    private MainView mainView;
    private ConnectController connectController;
    private Stage loginStage = new Stage();
    public MainController() {
        MainModel mainModel = new MainModel();
        mainInteractor = new MainInteractor(mainModel);
        mainView = new MainView(mainModel, this::closeAllConnections);
        connectController = new ConnectController(this).getView();
    }

    private void closeAllConnections() {
        Platform.runLater(connectController.closeConnection());
    }

    public Region getView() {
        { return mainView.build(); }
    }

    public void setStatus(String status) { mainView.setStatus(status); }

}
