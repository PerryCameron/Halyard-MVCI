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
        getStatisticsOnLaunch();
        this.welcomeView = new WelcomeView(welcomeModel, this::refreshStats, this::updateStats, this::openTab);
    }

    private void openTab(String tab) {
        mainController.openTab(tab);
    }

    private void getStatisticsOnLaunch() {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    welcomeInteractor.setStatistics();
                    return null;
                }
            };
            task.setOnSucceeded(e -> welcomeInteractor.setStatSucceeded());
            new Thread(task).start();
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
        task.setOnSucceeded(e -> welcomeInteractor.setStatUpdateSucceeded());
        task.setOnFailed(e -> welcomeInteractor.taskOnFailed(e));
        new Thread(task).start();
    }

    public Region getView() {
       return welcomeView.build();
    }
}
