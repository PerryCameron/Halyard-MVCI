package org.ecsail.mvci_welcome;

import org.ecsail.connection.Connections;
import org.ecsail.repository.implementations.StatRepositoryImpl;
import org.ecsail.repository.interfaces.StatRepository;

public class WelcomeInteractor {

    private WelcomeModel welcomeModel;
    private Connections connections;
    private StatRepository statRepository;

    public WelcomeInteractor(WelcomeModel welcomeModel, Connections connections) {
        this.welcomeModel = welcomeModel;
        this.connections = connections;
        this.statRepository = new StatRepositoryImpl(connections.getDataSource());
        welcomeModel.setStats(statRepository.getStatistics(welcomeModel.getDefaultStartYear(), we));
    }




}
