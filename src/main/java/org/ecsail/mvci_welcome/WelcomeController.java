package org.ecsail.mvci_welcome;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;
import org.ecsail.widgetfx.DialogueFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WelcomeController extends Controller<WelcomeMessage> {
    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);
    WelcomeView welcomeView;
    WelcomeInteractor welcomeInteractor;
    MainController mainController;

    public WelcomeController(MainController mainController) {
        WelcomeModel welcomeModel = new WelcomeModel(mainController.getMainModel());
        this.mainController = mainController;
        welcomeInteractor = new WelcomeInteractor(welcomeModel);
        getStatisticsOnLaunch();
        this.welcomeView = new WelcomeView(welcomeModel, this::action);
    }

    @Override
    public void action(WelcomeMessage message) {
        switch (message) {
            case OPEN_TAB -> mainController.openTab(welcomeInteractor.getTab());
            case RELOAD_STATS -> welcomeInteractor.reloadStats();
            case UPDATE_STATS -> updateStats();
        }
    }

    private void getStatisticsOnLaunch() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                welcomeInteractor.fetchStatistics();
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            welcomeInteractor.setStatSucceeded();
            welcomeInteractor.triggerInitialChartUpdate();
        });
        task.setOnFailed(e -> {
            logger.error("Failed to fetch statistics", e.getSource().getException());
            String errorMessage = e.getSource().getException().getMessage();
            if (errorMessage.contains("Session invalid")) {
                logger.warn("Session invalid, redirecting to login dialog");
                redirectToLogin();
            } else {
                DialogueFx.errorAlert("Error", "Failed to fetch statistics: " + errorMessage);
            }
        });
        new Thread(task).start();
    }

    private void redirectToLogin() {
        // Clear any existing session data
        welcomeInteractor.logout();
        // Redirect to login dialog
        mainController.showLoginDialog();
    }

    public void updateStats() {
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                return welcomeInteractor.recompileStatsAndFetchData();
            }
        };
        task.setOnSucceeded(e -> {
            String jsonResponse = task.getValue(); // Get the result from the task
            try {
                welcomeInteractor.setStatUpdateSucceeded(jsonResponse);
            } catch (JsonProcessingException ex) {
                logger.error("Failed to update stats", ex);
            }
        });

        task.setOnFailed(e -> welcomeInteractor.taskOnFailed(e));

        new Thread(task).start();
    }

    public Region getView() {
        return welcomeView.build();
    }
}
