package org.ecsail.mvci_roster;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import okhttp3.Response;
import org.ecsail.dto.DbRosterSettingsDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.MembershipListRadioDTO;
import org.ecsail.mvci_roster.export.Xls_roster;
import org.ecsail.static_tools.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RosterInteractor {

    private static final Logger logger = LoggerFactory.getLogger(RosterInteractor.class);
    private final RosterModel rosterModel;
//    private final MembershipRepository membershipRepo;
//    private final SettingsRepository settingsRepo;


//    public RosterInteractor(RosterModel rm, Connections connections) {
//        rosterModel = rm;
//        membershipRepo = new MembershipRepositoryImpl(connections.getDataSource());
//        settingsRepo = new SettingsRepositoryImpl(connections.getDataSource());
//    }
public RosterInteractor(RosterModel rm) {
    rosterModel = rm;

}

    protected ObservableList<MembershipListDTO> setSlipsForRoster(ObservableList<MembershipListDTO> updatedRoster) {
        for (MembershipListDTO m : updatedRoster) {
            if (m.getSubLeaser() > 0) { // let's mark Sublease Owners
                m.setSlip("O" + m.getSlip()); // put a 0 in front of owner
                setSublease(m.getSlip(), m.getSubLeaser(), updatedRoster);
            }
        }
        return updatedRoster;
    }

    private ObservableList<MembershipListDTO> setSublease(String slip, int subLeaser, ObservableList<MembershipListDTO> updatedRoster) {
        for (MembershipListDTO m : updatedRoster)
            if (m.getMsId() == subLeaser) m.setSlip("S" + slip);
        return updatedRoster;
    }

    protected void setRosterToTableview() {
            logger.info("Setting memberships to roster on change.........");
            rosterModel.getRosterTableView().setItems(rosterModel.getRosters());
    }

    protected void changeState() {
        logger.debug("Rosters is in search mode: {}", rosterModel.isSearchMode());
            if (rosterModel.isSearchMode())
                rosterModel.setNumberOfRecords(String.valueOf(rosterModel.getSearchedRosters().size()));
            else
                rosterModel.setNumberOfRecords(String.valueOf(rosterModel.getRosters().size()));
    }

//    protected void updateRoster() {
//        System.out.println("updateRoster()");
//        // I believe problem with tableview not refreshing on first open is in here
//        Method method;
//        try {
//            method = membershipRepo.getClass().getMethod(rosterModel.getSelectedRadioBox().getMethod(), Integer.class);
//            logger.info("Getting roster from data base");
//            ObservableList<MembershipListDTO> updatedRoster
//                    = FXCollections.observableArrayList((List<MembershipListDTO>) method.invoke(membershipRepo, rosterModel.getSelectedYear()));
//            Platform.runLater(() -> {
//                logger.info("Adding roster to model");
//                rosterModel.getRosters().setAll(setSlipsForRoster(updatedRoster));
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    // new version of method without unchecked cast
//    protected void updateRoster() {
//        Method method;
//        try {
//            method = membershipRepo.getClass().getMethod(rosterModel.getSelectedRadioBox().getMethod(), Integer.class);
//            logger.info("Getting roster from database");
//            Object result = method.invoke(membershipRepo, rosterModel.getSelectedYear());
//            if (result instanceof List<?> rawList) {
//                List<MembershipListDTO> rosterList = rawList.stream()
//                        .filter(MembershipListDTO.class::isInstance)
//                        .map(MembershipListDTO.class::cast)
//                        .toList();
//                ObservableList<MembershipListDTO> updatedRoster = FXCollections.observableArrayList(rosterList);
//                Platform.runLater(() -> {
//                    logger.info("Adding roster to model");
//                    rosterModel.getRosters().setAll(setSlipsForRoster(updatedRoster));
//                    // Force table view refresh
//                    rosterModel.getRosters().add(null); // Trigger change
//                    rosterModel.getRosters().remove(null); // Remove dummy entry
//                });
//            } else {
//                throw new IllegalStateException("Expected a List from method invocation, got: " + result);
//            }
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            logger.error(e.getMessage());
//        }
//    }


    protected void clearSearchBox() {
        if (!rosterModel.getTextFieldString().isEmpty())
            rosterModel.setTextFieldString("");
    }

    protected void clearMainRoster() {
        rosterModel.getRosters().clear();
    }

    protected void sortRoster() {
        rosterModel.getRosters().sort(Comparator.comparing(MembershipListDTO::getMembershipId));
        rosterModel.getRosterTableView().refresh(); // this is a hack because sometimes it won't refresh (doesn't work)
    }

    public void getRadioChoices() throws Exception {
        String endpoint = "radioChoices";
        String jsonResponse = fetchDataFromHalyard(endpoint);
        logger.debug("Radio choices response: {}", jsonResponse);
        List<MembershipListRadioDTO> choices = rosterModel.getObjectMapper().readValue(
                jsonResponse,
                new TypeReference<>() {
                }
        );
        logger.info("Fetched {} radio choices", choices.size());
        Platform.runLater(() -> {
            rosterModel.getRadioChoices().addAll(choices); // this is saying required type is MembershipListRadioDTO
            logger.info("Radio choices model updated with {} choices", rosterModel.getRadioChoices().size());
        });
    }

    public void getRosterSettings() throws Exception {
        String endpoint = "searchableListItems";
        String jsonResponse = fetchDataFromHalyard(endpoint);
        logger.debug("Roster Settings response: {}", jsonResponse);
        List<DbRosterSettingsDTO> choices = rosterModel.getObjectMapper().readValue(
                jsonResponse,
                new TypeReference<>() {
                }
        );
        logger.info("Fetched {} Roster", choices.size());
        Platform.runLater(() -> {
            rosterModel.getRosterSettings().addAll(choices);
            logger.info("Radio choices model updated with {} choices", rosterModel.getRadioChoices().size());
        });
    }

    private String fetchDataFromHalyard(String endpoint) throws Exception {
        try (Response response = rosterModel.getHttpClient().makeRequest("halyard/" + endpoint)) {
            logger.info("Fetching data from /halyard/{}: Status {}", endpoint, response.code());
            String contentType = response.header("Content-Type", "");
            if (contentType.contains("text/html")) {
                logger.warn("Received HTML response, likely a redirect to login page. Session may be invalid.");
                throw new Exception("Session invalid: Server redirected to login page. Please log in again.");
            }
            if (response.code() == 403) {
                throw new AccessDeniedException("Access Denied: You don’t have the required permissions to access this resource.");
            } else if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new Exception("Failed to fetch data: " + response.code());
            }
        } catch (IOException e) {
            throw new Exception("Failed to fetch data: " + e.getMessage());
        }
    }

//    protected void getRadioChoices() {
//        HandlingTools.queryForList(() -> {
//            ObservableList<MembershipListRadioDTO> list = FXCollections.observableArrayList(settingsRepo.getRadioChoices());
//            Platform.runLater(() -> rosterModel.getRadioChoices().addAll(list));
//        }, rosterModel.getMainModel(), logger);
//    }
//
//    protected void getRosterSettings() {
//        HandlingTools.queryForList(() -> {
//            ObservableList<DbRosterSettingsDTO> list = FXCollections.observableArrayList(settingsRepo.getSearchableListItems());
//            Platform.runLater(() -> rosterModel.setRosterSettings(list));
//        }, rosterModel.getMainModel(), logger);
//    }

    protected void exportRoster() {
        try {
            new Xls_roster(rosterModel);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    protected void fillTableView() {
        if (!rosterModel.getTextFieldString().isEmpty()) fillWithSearchResults();
        else fillWithResults(); // search box cleared
    }

    private void fillWithResults() { // this is where we stick
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

    protected void setListsLoaded() {
        rosterModel.setListsLoaded(!rosterModel.isListsLoaded());
    }

    public MembershipListDTO getMembership() {
        return rosterModel.getSelectedMembershipList();
    }
}
