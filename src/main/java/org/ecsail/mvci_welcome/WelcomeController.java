package org.ecsail.mvci_welcome;

import javafx.concurrent.Task;
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
        this.welcomeView = new WelcomeView(welcomeModel, this::refreshStats, this::updateStats);
    }

    private void refreshStats() {
        welcomeInteractor.reloadStats();
    }

    public void updateStats() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                welcomeInteractor.updateProgressBar();
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            welcomeInteractor.setStatUpdateSucceeded();
        });
        task.setOnFailed(e -> System.out.println("Was unable to compile stats"));
        Thread thread = new Thread(task);
        thread.start();
    }

    public Region getView() {
       return welcomeView.build();
    }
}
