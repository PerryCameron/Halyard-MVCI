package org.ecsail.mvci_welcome;

import org.ecsail.connection.Connections;
import org.ecsail.dto.StatsDTO;
import org.ecsail.repository.implementations.StatRepositoryImpl;
import org.ecsail.repository.interfaces.StatRepository;
import org.ecsail.repository.temp.SqlStats;

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
    protected int deleteAllStats() { return statRepository.deleteAllStats(); }
    protected StatsDTO createStatDTO(int year) {
//        return statRepository.createStatDTO(year);
        return SqlStats.createStatDTO(year, connections.getDataSource());
    }
    protected int insertStatDTO(StatsDTO statsDTO) { return  statRepository.addStatRecord(statsDTO); }
}
