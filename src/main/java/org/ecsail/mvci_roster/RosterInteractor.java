package org.ecsail.mvci_roster;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.dto.*;
import org.ecsail.mvci_roster.export.Xls_roster;
import org.ecsail.static_tools.CopyPOJOtoFx;
import org.ecsail.static_tools.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RosterInteractor {

    private static final Logger logger = LoggerFactory.getLogger(RosterInteractor.class);
    private final RosterModel rosterModel;

public RosterInteractor(RosterModel rm) {
    rosterModel = rm;

}

//    protected List<MembershipListDTO> setSlipsForRoster(List<MembershipListDTO> roster) {
//        List<MembershipListDTO> updatedRoster = new ArrayList<>(roster); // Create a new list to avoid modifying the input directly
//        for (MembershipListDTO m : updatedRoster) {
//            if (m.getSubLeaser() > 0) { // Mark sublease owners
//                m.setSlip("O" + m.getSlip()); // Put an "O" in front of owner
//                setSublease(m.getSlip(), m.getSubLeaser(), updatedRoster);
//            }
//        }
//        return updatedRoster;
//    }

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

    protected void clearSearchBox() {
        if (!rosterModel.getTextFieldString().isEmpty())
            rosterModel.setTextFieldString("");
    }

    protected void clearMainRoster() {
        rosterModel.getRosters().clear();
    }

    protected void sortRoster() {
        rosterModel.getRosters().sort(Comparator.comparing(RosterDTOFx::getId));
        rosterModel.getRosterTableView().refresh();
    }

    public void getRadioChoices() throws Exception {
        String endpoint = "radioChoices";
        String jsonResponse = rosterModel.getHttpClient().fetchDataFromHalyard(endpoint);
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
        String jsonResponse = rosterModel.getHttpClient().fetchDataFromHalyard(endpoint);
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


    // for some reason it is properly downloading the data as I noticed in the logs, but it is not filling the table view
    // below it is the old method I used to use when using jdbcTemplate
    protected List<RosterDTOFx> updateRoster() throws Exception {
        // Build the endpoint path and query parameters manually
        StringBuilder endpoint = new StringBuilder("roster");
        endpoint.append("?year=").append(rosterModel.getSelectedYear());
        endpoint.append("&rosterType=").append(URLEncoder.encode(rosterModel.getSelectedRadioBox().getMethod(), StandardCharsets.UTF_8.name()));
        if (rosterModel.textFieldStringProperty().get() != null) {
            String[] searchParams = rosterModel.textFieldStringProperty().get().split(",");
            for (String param : searchParams) {
                endpoint.append("&searchParams=").append(URLEncoder.encode(param, StandardCharsets.UTF_8.name()));
            }
        }
        System.out.println(endpoint);
        // Fetch data using the constructed endpoint
        String jsonResponse = rosterModel.getHttpClient().fetchDataFromHalyard(endpoint.toString());
//        logger.debug("Roster response: {}", jsonResponse);
        List<RosterDTO> roster = rosterModel.getObjectMapper().readValue(
                jsonResponse,
                new TypeReference<>() {
                }
        );
        return CopyPOJOtoFx.copyRoster(roster);
    }

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
        ObservableList<RosterDTOFx> list = searchString(rosterModel.getTextFieldString());
        Platform.runLater(() -> {
            rosterModel.getSearchedRosters().clear();
            rosterModel.getSearchedRosters().addAll(list);
            rosterModel.getRosterTableView().setItems(rosterModel.getSearchedRosters());
            rosterModel.setIsSearchMode(true);
        });
    }

    private ObservableList<RosterDTOFx> searchString(String searchTerm) {
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

    public RosterDTOFx getMembership() {
        return rosterModel.getSelectedMembershipList();
    }

    public void setRoster(List<RosterDTOFx> updatedRoster) {
        rosterModel.getRosters().addAll(updatedRoster);
    }

    public void setNumberOfRecords(int size) {
        rosterModel.numberOfRecordsProperty().set(String.valueOf(size));
    }

    public void logSearch() {
    logger.info("Search made: {}", rosterModel.getTextFieldString());
    }
}
