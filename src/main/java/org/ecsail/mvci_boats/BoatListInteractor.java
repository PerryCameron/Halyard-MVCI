package org.ecsail.mvci_boats;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.connection.Connections;
import org.ecsail.dto.BoatListRadioDTO;
import org.ecsail.repository.implementations.SettingsRepositoryImpl;
import org.ecsail.repository.interfaces.SettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class BoatListInteractor {

    private static final Logger logger = LoggerFactory.getLogger(BoatListInteractor.class);
    private final BoatListModel boatListModel;
    private final DataSource dataSource;
    private final SettingsRepository settingsRepo;

    public BoatListInteractor(BoatListModel boatListModel, Connections connections) {
        this.boatListModel = boatListModel;
        this.dataSource = connections.getDataSource();
        settingsRepo = new SettingsRepositoryImpl(connections.getDataSource());
    }

    public void getRadioChoices() {
        try {
            ObservableList<BoatListRadioDTO> list = FXCollections.observableArrayList(settingsRepo.getBoatRadioChoices());
            Platform.runLater(() -> boatListModel.getRadioChoices().addAll(list));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
