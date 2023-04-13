package org.ecsail.mvci_roster;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.connection.Connections;
import org.ecsail.dto.DbRosterSettingsDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.MembershipListRadioDTO;
import org.ecsail.repository.implementations.MembershipRepositoryImpl;
import org.ecsail.repository.implementations.SettingsRepositoryImpl;
import org.ecsail.repository.interfaces.MembershipRepository;
import org.ecsail.repository.interfaces.SettingsRepository;
import org.ecsail.static_calls.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class RosterInteractor {

    private static final Logger logger = LoggerFactory.getLogger(RosterInteractor.class);
    private final RosterModel rosterModel;
    private final MembershipRepository membershipRepo;
    private final SettingsRepository settingsRepo;

    public RosterInteractor(RosterModel rm, Connections connections) {
        rosterModel = rm;
        membershipRepo = new MembershipRepositoryImpl(connections.getDataSource());
        settingsRepo = new SettingsRepositoryImpl(connections.getDataSource());
    }

    protected void getSelectedRoster() {
        try {
            ObservableList<MembershipListDTO> observableList = FXCollections.observableArrayList(membershipRepo.getAllRoster(rosterModel.getSelectedYear()));
            Platform.runLater(() -> rosterModel.setRosters(observableList));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    protected void setRosterToTableview() {
        Platform.runLater(() -> {
            rosterModel.getRosterTableView().setItems(rosterModel.getRosters());
            rosterModel.getRosters().sort(Comparator.comparing(MembershipListDTO::getMembershipId));
        });
        changeState();
    }

    private void changeState() {
        Platform.runLater(() -> {
            if (rosterModel.isSearchMode())
                rosterModel.setNumberOfRecords(String.valueOf(rosterModel.getSearchedRosters().size()));
            else
                rosterModel.setNumberOfRecords(String.valueOf(rosterModel.getRosters().size()));
        });
    }

    @SuppressWarnings("unchecked")
    protected void changeListState() {
            logger.info("changeListState()");
            rosterModel.getRosters().clear();
            Method method;
            try {
                method = membershipRepo.getClass().getMethod(rosterModel.getSelectedRadioBox().getMethod(), Integer.class);
                ObservableList<MembershipListDTO> updatedRoster
                        = FXCollections.observableArrayList((List<MembershipListDTO>) method.invoke(membershipRepo, rosterModel.getSelectedYear()));
                updateRoster(updatedRoster);
            } catch (Exception e) {
                e.printStackTrace();
            }
            changeState();
            rosterModel.getRosters().sort(Comparator.comparing(MembershipListDTO::getMembershipId));
    }

    private void updateRoster(ObservableList<MembershipListDTO> updatedRoster) {
        Platform.runLater(() -> rosterModel.getRosters().setAll(updatedRoster));
    }

    protected void getRadioChoices() {
        try {
            ObservableList<MembershipListRadioDTO> list = FXCollections.observableArrayList(settingsRepo.getRadioChoices());
            Platform.runLater(() -> rosterModel.getRadioChoices().addAll(list));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    // TODO move to interactor
//    private void chooseRoster() { //
//        if (parent.searchedRosters.size() > 0)
//            new Xls_roster(parent.searchedRosters, parent.rosterSettings, parent.selectedRadioBox.getRadioLabel());
//        else
//            new Xls_roster(parent.rosters, parent.rosterSettings, parent.selectedRadioBox.getRadioLabel());
//    }

    protected void fillTableView() {
            if (!rosterModel.getTextFieldString().equals("")) fillWithSearchResults();
            else fillWithResults(); // search box cleared
        changeState();
    }

    private void fillWithResults() {
        Platform.runLater(() -> {
        rosterModel.getRosterTableView().setItems(rosterModel.getRosters());
        rosterModel.setIsSearchMode(false);
        rosterModel.getSearchedRosters().clear();
        logger.info("normal mode");
        });
    }

    private void fillWithSearchResults() {
        ObservableList list = searchString(rosterModel.getTextFieldString());
        Platform.runLater(() -> {
            rosterModel.getSearchedRosters().clear();
            rosterModel.getSearchedRosters().addAll(list);
            rosterModel.getRosterTableView().setItems(rosterModel.getSearchedRosters());
            rosterModel.setIsSearchMode(true);
            logger.info("search mode");
        });
    }

    private ObservableList<MembershipListDTO> searchString(String searchTerm) {
        String text = searchTerm.toLowerCase();
        ObservableList<MembershipListDTO> searchedMemberships = FXCollections.observableArrayList();
        boolean hasMatch = false;
        for(MembershipListDTO membershipListDTO: rosterModel.getRosters()) {
            Field[] fields1 = membershipListDTO.getClass().getDeclaredFields();
            Field[] fields2 = membershipListDTO.getClass().getSuperclass().getDeclaredFields();
            Field[] allFields = new Field[fields1.length + fields2.length];
            Arrays.setAll(allFields, i -> (i < fields1.length ? fields1[i] : fields2[i - fields1.length]));
            for(Field field: allFields) {
                if(fieldIsSearchable(field.getName())) {
                    field.setAccessible(true);
                    String value = StringTools.returnFieldValueAsString(field, membershipListDTO).toLowerCase();
                    if (value.contains(text)) hasMatch = true;
                }
            }  // add boat DTO here
            if(hasMatch)
                searchedMemberships.add(membershipListDTO);
            hasMatch = false;
        }
        return searchedMemberships;
    }

    private boolean fieldIsSearchable(String fieldName) {
        return rosterModel.getCheckBoxes().stream()
                .filter(dto -> dto.getDTOFieldName().equals(fieldName))
                .findFirst()
                .map(SettingsCheckBox::isSearchable)
                .orElse(false);
    }

    protected void getRosterSettings() {
        ObservableList<DbRosterSettingsDTO> list = FXCollections.observableArrayList(settingsRepo.getSearchableListItems());
        Platform.runLater(() -> rosterModel.setRosterSettings(list));
    }

    protected void setListsLoaded() {
        Platform.runLater(() -> rosterModel.setListsLoaded(true));
    }
}
