package org.ecsail.mvci_boats;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.connection.Connections;
import org.ecsail.dto.BoatListDTO;
import org.ecsail.dto.BoatListRadioDTO;
import org.ecsail.dto.DbBoatSettingsDTO;
import org.ecsail.repository.implementations.BoatRepositoryImpl;
import org.ecsail.repository.implementations.SettingsRepositoryImpl;
import org.ecsail.repository.interfaces.BoatRepository;
import org.ecsail.repository.interfaces.SettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

public class BoatListInteractor {

    private static final Logger logger = LoggerFactory.getLogger(BoatListInteractor.class);
    private final BoatListModel boatListModel;
    private final DataSource dataSource;
    private final SettingsRepository settingsRepo;
    private final BoatRepository boatRepo;

    public BoatListInteractor(BoatListModel boatListModel, Connections connections) {
        this.boatListModel = boatListModel;
        this.dataSource = connections.getDataSource();
        settingsRepo = new SettingsRepositoryImpl(connections.getDataSource());
        boatRepo = new BoatRepositoryImpl(connections.getDataSource());
    }

    protected void changeListType() {
        clearMainBoatList();
        Method method;
        try {
            System.out.println("method: " + boatListModel.getSelectedRadioBox().getMethod());
            method = boatRepo.getClass().getMethod(boatListModel.getSelectedRadioBox().getMethod());
            ObservableList<BoatListDTO> updatedBoatList
                    = FXCollections.observableArrayList((List<BoatListDTO>) method.invoke(boatRepo));
            updateBoatList(updatedBoatList);
            fillTableView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        changeState();
        clearSearchBox();
        sortRoster();
    }

    private void updateBoatList(ObservableList<BoatListDTO> updatedBoatList) {
        Platform.runLater(() -> boatListModel.getBoats().setAll(updatedBoatList));
    }


    private void sortRoster() {
        Platform.runLater(() -> boatListModel.getBoats().sort(Comparator.comparing(BoatListDTO::getMembershipId)));
    }

    private void clearSearchBox() {
        if(!boatListModel.getTextFieldString().equals(""))
            Platform.runLater(() -> boatListModel.setTextFieldString(""));
    }

    protected void fillTableView() {
//        if (!boatListModel.getTextFieldString().equals("")) fillWithSearchResults();
//        else fillWithResults(); // search box cleared
        fillWithResults();
        changeState();
    }

//    private void fillWithSearchResults() {
//        ObservableList<MembershipListDTO> list = searchString(rosterModel.getTextFieldString());
//        Platform.runLater(() -> {
//            rosterModel.getSearchedRosters().clear();
//            rosterModel.getSearchedRosters().addAll(list);
//            rosterModel.getRosterTableView().setItems(rosterModel.getSearchedRosters());
//            rosterModel.setIsSearchMode(true);
//        });
//    }

    private void fillWithResults() {
        Platform.runLater(() -> {
            logger.debug("TableView is set to display normal results");
            boatListModel.getBoatListTableView().setItems(boatListModel.getBoats());
            boatListModel.setIsActiveSearch(false);
            boatListModel.getSearchedBoats().clear();
        });
    }

    private void clearMainBoatList() {
        Platform.runLater(() -> boatListModel.getBoats().clear());
    }

    public void getRadioChoices() {
        try {
            ObservableList<BoatListRadioDTO> list = FXCollections.observableArrayList(settingsRepo.getBoatRadioChoices());
            Platform.runLater(() -> boatListModel.getRadioChoices().addAll(list));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    protected void setListsLoaded(boolean isLoaded) {
        Platform.runLater(() -> boatListModel.setListsLoaded(isLoaded));
    }

    public void getBoatListSettings() {
        ObservableList<DbBoatSettingsDTO> list = FXCollections.observableArrayList(settingsRepo.getBoatSettings());
        boatListModel.setBoatListSettings(list);
    }

    protected void setBoatListToTableview() {
        Platform.runLater(() -> boatListModel.getBoatListTableView().setItems(boatListModel.getBoats()));
        changeState();
    }

    private void changeState() {
        Platform.runLater(() -> {
            logger.debug("Rosters is in search mode: " + boatListModel.isSearchMode());
            if (boatListModel.isSearchMode())
                boatListModel.setNumberOfRecords(String.valueOf(boatListModel.getSearchedBoats().size()));
            else
                boatListModel.setNumberOfRecords(String.valueOf(boatListModel.getBoats().size()));
        });
    }

    public void getListSize() {
        logger.info("Size is " + String.valueOf(boatListModel.getBoats().size()));
    }
}
