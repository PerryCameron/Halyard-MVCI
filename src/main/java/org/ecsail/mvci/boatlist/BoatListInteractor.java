package org.ecsail.mvci.boatlist;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.fx.BoatListDTO;
import org.ecsail.mvci.main.MainController;
import org.ecsail.static_tools.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class BoatListInteractor {

    private static final Logger logger = LoggerFactory.getLogger(BoatListInteractor.class);
    private final BoatListModel boatListModel;
//    private final SettingsRepository settingsRepo;
//    private final BoatRepository boatRepo;

//    public BoatListInteractor(BoatListModel boatListModel, Connections connections) {
//        this.boatListModel = boatListModel;
//        settingsRepo = new SettingsRepositoryImpl(connections.getDataSource());
//        boatRepo = new BoatRepositoryImpl(connections.getDataSource());
//    }

    public BoatListInteractor(BoatListModel boatListModel) {
        this.boatListModel = boatListModel;

    }

//    protected void changeListType() {
//        clearMainBoatList();
//        Method method;
//        try {
//            method = boatRepo.getClass().getMethod(boatListModel.getSelectedRadioBox().getMethod());
//            ObservableList<BoatListDTO> updatedBoatList
//                    = FXCollections.observableArrayList((List<BoatListDTO>) method.invoke(boatRepo));
//            updateBoatList(updatedBoatList);
//            fillTableView();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        changeState();
//        clearSearchBox();
//        sortRoster();
//    }

    // new version of method without unchecked cast warning
    protected void changeListType() {
        clearMainBoatList();
//        try {
//            Method method = boatRepo.getClass().getMethod(boatListModel.getSelectedRadioBox().getMethod());
//            Object result = method.invoke(boatRepo);
//            if (result instanceof List<?> rawList) {
//                List<BoatListDTO> boatList = rawList.stream()
//                        .filter(BoatListDTO.class::isInstance)
//                        .map(BoatListDTO.class::cast)
//                        .toList();
//                ObservableList<BoatListDTO> updatedBoatList = FXCollections.observableArrayList(boatList);
//                updateBoatList(updatedBoatList);
//                fillTableView();
//            } else {
//                throw new IllegalStateException("Expected a List from method invocation, got: " + result);
//            }
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            logger.error(e.getMessage(), e);
//        }
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
        if (!boatListModel.getTextFieldString().isEmpty())
            Platform.runLater(() -> boatListModel.setTextFieldString(""));
    }

    protected void fillTableView() {
        if (!boatListModel.getTextFieldString().isEmpty()) fillWithSearchResults();
        else fillWithResults(); // search box cleared
        changeState();
    }

    private void fillWithSearchResults() {
        ObservableList<BoatListDTO> list = searchString(boatListModel.getTextFieldString());
        Platform.runLater(() -> {
            boatListModel.getSearchedBoats().clear();
            boatListModel.getSearchedBoats().addAll(list);
            boatListModel.getBoatListTableView().setItems(boatListModel.getSearchedBoats());
            boatListModel.setIsActiveSearch(true);
        });
    }

    private ObservableList<BoatListDTO> searchString(String searchTerm) {
        String searchTermToLowerCase = searchTerm.toLowerCase();
        return boatListModel.getBoats().stream()
                .filter(BoatListDTO -> Arrays.stream(BoatListDTO.getClass().getDeclaredFields())
                        .filter(field -> fieldIsSearchable(field.getName()))
                        .peek(field -> field.setAccessible(true))
                        .anyMatch(field -> {
                            String value = StringTools.returnFieldValueAsString(field, BoatListDTO).toLowerCase();
                            return value.contains(searchTermToLowerCase);
                        }))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    private boolean fieldIsSearchable(String fieldName) {
        return boatListModel.getCheckBoxes().stream()
                .filter(dto -> dto.getDb().getPojoName().equals(fieldName))
                .findFirst()
                .map(BoatListSettingsCheckBox::isSearchable)
                .orElse(false);
    }

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
//        HandlingTools.queryForList(() -> {
//            ObservableList<BoatListRadioDTO> list = FXCollections.observableArrayList(settingsRepo.getBoatRadioChoices());
//            Platform.runLater(() -> boatListModel.getRadioChoices().addAll(list));
//        }, boatListModel.getMainModel(), logger);
    }

    protected void setListsLoaded(boolean isLoaded) {
        Platform.runLater(() -> boatListModel.setListsLoaded(isLoaded));
    }

    public void getBoatListSettings() {
//        HandlingTools.queryForList(() -> {
//            ObservableList<DbBoatSettingsDTO> list = FXCollections.observableArrayList(settingsRepo.getBoatSettings());
//            Platform.runLater(() -> boatListModel.setBoatListSettings(list));
//        }, boatListModel.getMainModel(), logger);
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

    public void updateBoat() {
//        HandlingTools.executeQuery(() ->
//                        boatRepo.updateAux(boatListModel.getSelectedBoatList().isAux(), boatListModel.getSelectedBoatList().getBoatId()),
//                boatListModel.getMainModel(),
//                logger);
    }

    protected void savedToIndicator(boolean returnOk) { // updates status lights
        if (returnOk) boatListModel.getMainModel().getLightAnimationMap().get("receiveSuccess").playFromStart();
        else boatListModel.getMainModel().getLightAnimationMap().get("receiveError").playFromStart();
    }

    protected void retrievedFromIndicator(boolean returnOk) { // updates status lights
        if (returnOk) boatListModel.getMainModel().getLightAnimationMap().get("transmitSuccess").playFromStart();
        else boatListModel.getMainModel().getLightAnimationMap().get("transmitError").playFromStart();
    }

    public void launchBoatTab(MainController mainController) {
        mainController.openBoatMVCI(boatListModel.getSelectedBoatList());
    }
}
