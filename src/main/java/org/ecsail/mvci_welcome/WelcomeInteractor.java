package org.ecsail.mvci_welcome;

import org.ecsail.connection.Connections;
import org.ecsail.dto.StatsDTO;
import org.ecsail.repository.implementations.StatRepositoryImpl;
import org.ecsail.repository.interfaces.StatRepository;

import java.util.ArrayList;

public class WelcomeInteractor {

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


}
