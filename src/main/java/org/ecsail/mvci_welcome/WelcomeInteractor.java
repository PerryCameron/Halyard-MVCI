package org.ecsail.mvci_welcome;

import org.ecsail.connection.Connections;
import org.ecsail.dto.StatsDTO;
import org.ecsail.repository.implementations.StatRepositoryImpl;
import org.ecsail.repository.interfaces.StatRepository;
import org.ecsail.repository.temp.SqlStats;
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
        setStatistics();
    }

    private void setStatistics() {
        int endYear = welcomeModel.getDefaultStartYear() + welcomeModel.getYearSpan();
        welcomeModel.setStats((ArrayList<StatsDTO>) statRepository.getStatistics(welcomeModel.getDefaultStartYear(), endYear));
    }

    protected void reloadStats() {
        int endYear = welcomeModel.getDefaultStartYear() + welcomeModel.getYearSpan();
        if(endYear > welcomeModel.getSelectedYear()) endYear = welcomeModel.getSelectedYear();
        welcomeModel.getStats().clear();
        welcomeModel.getStats().addAll(statRepository.getStatistics(welcomeModel.getDefaultStartYear(), endYear));
    }

    protected void updateProgressBar() {
        statRepository.deleteAllStats();
        final double numberOfYears = Year.now().getValue() - welcomeModel.getStartYear() + 1;
        final double increment = 100 / numberOfYears;
        for (int i = 0; i < numberOfYears; i++) {
            StatsDTO statsDTO = SqlStats.createStatDTO(welcomeModel.getStartYear(), connections.getDataSource());
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
}
