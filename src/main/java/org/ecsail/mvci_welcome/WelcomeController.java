package org.ecsail.mvci_welcome;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.dto.StatsDTO;
import org.ecsail.mvci_main.MainController;

import java.time.Year;

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

    public void updateStats(WelcomeModel welcomeModel) {
        welcomeInteractor.deleteAllStats();
        int numberOfYears = Year.now().getValue() - welcomeModel.getStartYear() + 1;
        double increment = 100 / numberOfYears;
        System.out.println("made # 1");
        Task<Void> task = new Task<>(){
            @Override
            protected Void call() {
                for (int i = 0; i < numberOfYears; i++) {
                    StatsDTO stat = welcomeInteractor.createStatDTO(welcomeModel.getStartYear());
                    welcomeInteractor.insertStatDTO(stat);
                    welcomeModel.incrementStartYear();
//                    welcomeModel.progressProperty.set((int) (stat.getStatId() * increment));
                }
                welcomeModel.setStartYear(1970);
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            System.out.println("Finished updating Statistics");
            welcomeModel.setDataBaseStatisticsRefreshed(true);
//            this.close();
        });
        task.setOnFailed(e -> System.out.println("Was unable to compile stats"));
        Thread thread = new Thread(task);
        thread.start();
    }

    public Region getView() {
       return welcomeView.build();
    }
}
