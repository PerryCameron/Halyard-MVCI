package org.ecsail.mvci_welcome;

import javafx.scene.control.Tab;
import org.ecsail.mvci_main.MainController;

public class WelcomeController {

    WelcomeView welcomeView;
    WelcomeInteractor welcomeInteractor;
    MainController mainController;
    public WelcomeController(MainController mainController) {
        WelcomeModel welcomeModel = new WelcomeModel();
        welcomeInteractor = new WelcomeInteractor(welcomeModel);
        this.mainController = mainController;
        this.welcomeView = new WelcomeView(welcomeModel);
    }

    public Tab getView() {
       return welcomeView.build();
    }
}
