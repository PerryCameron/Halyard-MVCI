package org.ecsail.mvci_main;


import javafx.application.Platform;
import javafx.scene.layout.Region;
import org.ecsail.connection.Connections;
import org.ecsail.mvci_connect.ConnectController;
import org.ecsail.mvci_welcome.WelcomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private final MainInteractor mainInteractor;
    private MainView mainView;
    private ConnectController connectController;
    public MainController() {
        MainModel mainModel = new MainModel();
        mainInteractor = new MainInteractor(mainModel);
        mainView = new MainView(mainModel, this::closeAllConnections);
        connectController = new ConnectController(this).getView();
    }

    public void openTab(String tabName) {
        mainView.addTab(tabName, mainInteractor.returnController(tabName, this));
    }

    public void openWelcomeMVCI() {
        mainView.closeTabs();
        mainView.addTab("Welcome",new WelcomeController(this).getView());
    }

    public Connections getConnections() {
        return connectController.getConnectInteractor().getConnections();
    }

    private void closeAllConnections() {
        Platform.runLater(connectController.closeConnection());
    }

    public Region getView() {
        { return mainView.build(); }
    }

    public void setStatus(String status) { mainView.setStatus(status); }


}
