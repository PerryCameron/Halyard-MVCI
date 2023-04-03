package org.ecsail.mvci_welcome;

import javafx.scene.control.Tab;
import javafx.scene.layout.Region;
import org.ecsail.mvci_main.MainController;

public class WelcomeController {

    WelcomeView welcomeView;
    WelcomeInteractor welcomeInteractor;
    MainController mainController;
    public WelcomeController(MainController mainController) {
        WelcomeModel welcomeModel = new WelcomeModel();
        this.mainController = mainController;
        welcomeInteractor = new WelcomeInteractor(welcomeModel, mainController.getConnections());
        this.welcomeView = new WelcomeView(welcomeModel, this::refreshStats);
    }

    private void refreshStats() {
        welcomeInteractor.reloadStats();
    }

    public Region getView() {
       return welcomeView.build();
    }
}
