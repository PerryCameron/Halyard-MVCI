package org.ecsail.mvci_roster;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.connection.Connections;
import org.ecsail.dto.DbRosterSettingsDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.MembershipListRadioDTO;
import org.ecsail.repository.implementations.*;
import org.ecsail.repository.interfaces.*;
import org.ecsail.static_calls.StringTools;
import org.ecsail.mvci_roster.export.Xls_roster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RosterInteractor {

    private static final Logger logger = LoggerFactory.getLogger(RosterInteractor.class);
    private final RosterModel rosterModel;
    private final MembershipRepository membershipRepo;
    private final SettingsRepository settingsRepo;
    private final PhoneRepository phoneRepo;
    private final EmailRepository emailRepo;
    private final MembershipIdRepository membershipIdRepo;


    public RosterInteractor(RosterModel rm, Connections connections) {
        rosterModel = rm;
        membershipRepo = new MembershipRepositoryImpl(connections.getDataSource());
        settingsRepo = new SettingsRepositoryImpl(connections.getDataSource());
        phoneRepo = new PhoneRepositoryImpl(connections.getDataSource());
        emailRepo = new EmailRepositoryImpl(connections.getDataSource());
        membershipIdRepo = new MembershipIdRepositoryImpl(connections.getDataSource());
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
    protected void chooseRoster() { new Xls_roster(rosterModel); }

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
                .filter(membershipListDTO -> Stream.concat(
                                Arrays.stream(membershipListDTO.getClass().getDeclaredFields()),
                                Arrays.stream(membershipListDTO.getClass().getSuperclass().getDeclaredFields())
                        )
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
