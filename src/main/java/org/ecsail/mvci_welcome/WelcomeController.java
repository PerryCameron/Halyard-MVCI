package org.ecsail.mvci_welcome;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class WelcomeController extends Controller<WelcomeMessage> {

    WelcomeView welcomeView;
    WelcomeInteractor welcomeInteractor;
    MainController mainController;
    public WelcomeController(MainController mainController) {
        WelcomeModel welcomeModel = new WelcomeModel(mainController.getMainModel());
        this.mainController = mainController;
        welcomeInteractor = new WelcomeInteractor(welcomeModel, mainController.getConnections());
        getStatisticsOnLaunch();
        this.welcomeView = new WelcomeView(welcomeModel, this::action);
    }

    @Override
    public void action(WelcomeMessage message) {
        switch (message) {
            case OPEN_TAB -> mainController.openTab(welcomeInteractor.getTab());
            case RELOAD_STATS -> welcomeInteractor.reloadStats();
            case UPDATE_STATS -> updateStats();
        };
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
