package org.ecsail.mvci_welcome;

import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import org.ecsail.connection.Connections;
import org.ecsail.dto.StatsDTO;
import org.ecsail.repository.implementations.StatRepositoryImpl;
import org.ecsail.repository.interfaces.StatRepository;
import org.ecsail.static_tools.HandlingTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Year;
import java.util.ArrayList;

public class WelcomeInteractor {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeInteractor.class);
    private WelcomeModel welcomeModel;
    private Connections connections;
    private StatRepository statRepository;

    public WelcomeInteractor(WelcomeModel welcomeModel, Connections connections) {
        this.welcomeModel = welcomeModel;
        this.connections = connections;
        this.statRepository = new StatRepositoryImpl(connections.getDataSource());
    }

    protected void setStatistics() {
        int endYear = welcomeModel.getDefaultStartYear() + welcomeModel.getYearSpan();
        HandlingTools.queryForList(() -> {
        ArrayList<StatsDTO> statsDTOS = (ArrayList<StatsDTO>) statRepository.getStatistics(welcomeModel.getDefaultStartYear(), endYear);
        Platform.runLater(() -> welcomeModel.setStats(statsDTOS));
        }, welcomeModel.getMainModel(), logger);
    }

    protected void reloadStats() {
        logger.info("Reloading stats...");
        HandlingTools.queryForList(() -> {
            int endYear = welcomeModel.getDefaultStartYear() + welcomeModel.getYearSpan();
            if (endYear > welcomeModel.getSelectedYear()) endYear = welcomeModel.getSelectedYear();
            ArrayList<StatsDTO> statsDTOS = (ArrayList<StatsDTO>) statRepository.getStatistics(welcomeModel.getDefaultStartYear(), endYear);
            Platform.runLater(() -> {
                welcomeModel.getStats().clear();
                welcomeModel.getStats().addAll(statsDTOS);
                welcomeModel.setRefreshCharts(!welcomeModel.isRefreshCharts());
            });
        }, welcomeModel.getMainModel(), logger);
    }

    protected void updateProgressBar() {
        statRepository.deleteAllStats();
        final double numberOfYears = Year.now().getValue() - welcomeModel.getStartYear() + 1;
        final double increment = 100 / numberOfYears;
        for (int i = 0; i < numberOfYears; i++) {
            StatsDTO statsDTO = statRepository.createStatDTO(welcomeModel.getStartYear());
            statRepository.addStatRecord(statsDTO);
            welcomeModel.incrementStartYear();
            double currentProgress = welcomeModel.getProgress();
            double nextProgress = currentProgress + increment;
            welcomeModel.setProgress(nextProgress);
        }
        welcomeModel.setStartYear(1970);
    }

    protected void setStatUpdateSucceeded() {
        reloadStats();
        welcomeModel.getMembershipBarChart().refreshChart();
        welcomeModel.getMembershipStackedBarChart().refreshChart();
        welcomeModel.setDataBaseStatisticsRefreshed(true);
        logger.info("Finished updating Statistics");
    }

    protected void setStatSucceeded() {
        welcomeModel.getMembershipBarChart().refreshChart();
        welcomeModel.getMembershipStackedBarChart().refreshChart();
        logger.info("Statistics have been loaded");
    }

    public void taskOnFailed(WorkerStateEvent e) {
        logger.error(e.toString());
    }

    public String getTab() {
        return welcomeModel.getTabName();
    }
}
