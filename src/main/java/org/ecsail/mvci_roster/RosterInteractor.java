package org.ecsail.mvci_roster;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.connection.Connections;
import org.ecsail.dto.DbRosterSettingsDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.MembershipListRadioDTO;
import org.ecsail.mvci_roster.export.Xls_roster;
import org.ecsail.repository.implementations.MembershipRepositoryImpl;
import org.ecsail.repository.implementations.SettingsRepositoryImpl;
import org.ecsail.repository.interfaces.MembershipRepository;
import org.ecsail.repository.interfaces.SettingsRepository;
import org.ecsail.static_tools.HandlingTools;
import org.ecsail.static_tools.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    protected ObservableList<MembershipListDTO> setSlipsForRoster(ObservableList<MembershipListDTO> updatedRoster) {
        for(MembershipListDTO m: updatedRoster) {
            if(m.getSubLeaser() > 0) { // let's mark Sublease Owners
                    m.setSlip("O" + m.getSlip()); // put a 0 in front of owner
                    setSublease(m.getSlip(), m.getSubLeaser(), updatedRoster);
            }
        }
        return updatedRoster;
    }

    private ObservableList<MembershipListDTO> setSublease(String slip, int subLeaser, ObservableList<MembershipListDTO> updatedRoster) {
        for(MembershipListDTO m: updatedRoster)
            if(m.getMsId() == subLeaser) m.setSlip("S" + slip);
        return updatedRoster;
    }

    protected void setRosterToTableview() {
        Platform.runLater(() -> {
            logger.info("Setting memberships to roster on change.........");
            rosterModel.getRosterTableView().setItems(rosterModel.getRosters());
        });
        changeState();
    }

    private void changeState() {
        Platform.runLater(() -> {
            logger.debug("Rosters is in search mode: " + rosterModel.isSearchMode());
            if (rosterModel.isSearchMode())
                rosterModel.setNumberOfRecords(String.valueOf(rosterModel.getSearchedRosters().size()));
            else
                rosterModel.setNumberOfRecords(String.valueOf(rosterModel.getRosters().size()));
        });
    }

    protected void changeListType() {
        clearMainRoster();
            Method method;
            try {
                method = membershipRepo.getClass().getMethod(rosterModel.getSelectedRadioBox().getMethod(), Integer.class);
                ObservableList<MembershipListDTO> updatedRoster
                        = FXCollections.observableArrayList((List<MembershipListDTO>) method.invoke(membershipRepo, rosterModel.getSelectedYear()));
                updateRoster(updatedRoster);
                fillTableView();
            } catch (Exception e) {
                e.printStackTrace();
            }
            changeState();
            clearSearchBox();
            sortRoster();
    }

    private void clearSearchBox() {
        if(!rosterModel.getTextFieldString().equals(""))
            Platform.runLater(() -> rosterModel.setTextFieldString(""));
    }

    private void clearMainRoster() {
        Platform.runLater(() -> rosterModel.getRosters().clear());
    }

    private void sortRoster() {
        Platform.runLater(() -> rosterModel.getRosters().sort(Comparator.comparing(MembershipListDTO::getMembershipId)));
    }

    private void updateRoster(ObservableList<MembershipListDTO> updatedRoster) {
        Platform.runLater(() -> rosterModel.getRosters().setAll(setSlipsForRoster(updatedRoster)));
    }

    protected void getRadioChoices() {
        HandlingTools.queryForList(() -> {
            ObservableList<MembershipListRadioDTO> list = FXCollections.observableArrayList(settingsRepo.getRadioChoices());
            Platform.runLater(() -> rosterModel.getRadioChoices().addAll(list));
        }, rosterModel.getMainModel(), logger);
    }
    protected void exportRoster() {
        try {
            new Xls_roster(rosterModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void fillTableView() {
            if (!rosterModel.getTextFieldString().equals("")) fillWithSearchResults();
            else fillWithResults(); // search box cleared
        changeState();
    }

    private void fillWithResults() {
        Platform.runLater(() -> {
            logger.debug("TableView is set to display normal results");
            rosterModel.getRosterTableView().setItems(rosterModel.getRosters());
            rosterModel.setIsSearchMode(false);
            rosterModel.getSearchedRosters().clear();
        });
    }

    private void fillWithSearchResults() {
        ObservableList<MembershipListDTO> list = searchString(rosterModel.getTextFieldString());
        Platform.runLater(() -> {
            rosterModel.getSearchedRosters().clear();
            rosterModel.getSearchedRosters().addAll(list);
            rosterModel.getRosterTableView().setItems(rosterModel.getSearchedRosters());
            rosterModel.setIsSearchMode(true);
        });
    }

    private ObservableList<MembershipListDTO> searchString(String searchTerm) {
        String searchTermToLowerCase = searchTerm.toLowerCase();
        return rosterModel.getRosters().stream()
                .filter(membershipListDTO -> Arrays.stream(membershipListDTO.getClass().getDeclaredFields())
                        .filter(field -> fieldIsSearchable(field.getName()))
                        .peek(field -> field.setAccessible(true))
                        .anyMatch(field -> {
                            String value = StringTools.returnFieldValueAsString(field, membershipListDTO).toLowerCase();
                            return value.contains(searchTermToLowerCase);
                        }))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    private boolean fieldIsSearchable(String fieldName) {
        return rosterModel.getCheckBoxes().stream()
                .filter(dto -> dto.getDTOFieldName().equals(fieldName))
                .findFirst()
                .map(RosterSettingsCheckBox::isSearchable)
                .orElse(false);
    }

    protected void getRosterSettings() {
        HandlingTools.queryForList(() -> {
        ObservableList<DbRosterSettingsDTO> list = FXCollections.observableArrayList(settingsRepo.getSearchableListItems());
        Platform.runLater(() -> rosterModel.setRosterSettings(list));
        }, rosterModel.getMainModel(), logger);
    }

    protected void setListsLoaded(boolean isLoaded) {
        Platform.runLater(() -> rosterModel.setListsLoaded(isLoaded));
    }

    public MembershipListDTO getMembership() {
        return rosterModel.getSelectedMembershipList();
    }
}
