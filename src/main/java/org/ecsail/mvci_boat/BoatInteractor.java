package org.ecsail.mvci_boat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.connection.Connections;
import org.ecsail.dto.BoatListDTO;
import org.ecsail.dto.BoatListRadioDTO;
import org.ecsail.dto.DbBoatSettingsDTO;
import org.ecsail.mvci_boatlist.BoatListModel;
import org.ecsail.mvci_boatlist.BoatListSettingsCheckBox;
import org.ecsail.repository.implementations.BoatRepositoryImpl;
import org.ecsail.repository.implementations.SettingsRepositoryImpl;
import org.ecsail.repository.interfaces.BoatRepository;
import org.ecsail.repository.interfaces.SettingsRepository;
import org.ecsail.static_calls.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BoatInteractor {

    private static final Logger logger = LoggerFactory.getLogger(BoatInteractor.class);
    private final BoatModel boatModel;
    private final DataSource dataSource;
    private final SettingsRepository settingsRepo;
    private final BoatRepository boatRepo;

    public BoatInteractor(BoatModel boatModel, Connections connections) {
        this.boatModel = boatModel;
        this.dataSource = connections.getDataSource();
        settingsRepo = new SettingsRepositoryImpl(connections.getDataSource());
        boatRepo = new BoatRepositoryImpl(connections.getDataSource());
    }


    protected void savedToIndicator(boolean returnOk) { // updates status lights
        if(returnOk) boatModel.getMainModel().getLightAnimationMap().get("receiveSuccess").playFromStart();
        else boatModel.getMainModel().getLightAnimationMap().get("receiveError").playFromStart();
    }

    protected void retrievedFromIndicator(boolean returnOk) { // updates status lights
        if(returnOk) boatModel.getMainModel().getLightAnimationMap().get("transmitSuccess").playFromStart();
        else boatModel.getMainModel().getLightAnimationMap().get("transmitError").playFromStart();
    }

    public void getBoatSettings() {
        boatModel.setBoatSettings((ArrayList<DbBoatSettingsDTO>) settingsRepo.getBoatSettings());
    }

    public void setDataLoaded(boolean dataLoaded) {
        boatModel.setDataLoaded(dataLoaded);
    }
}
