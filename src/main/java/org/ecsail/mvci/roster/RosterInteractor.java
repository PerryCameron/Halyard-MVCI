package org.ecsail.mvci.roster;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.ecsail.fx.*;
import org.ecsail.mvci.roster.export.Xls_roster;
import org.ecsail.static_tools.POJOtoFxConverter;
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
        Platform.runLater(() -> {
            logger.debug("Rosters is in search mode: {}", rosterModel.isSearchMode());
            if (rosterModel.isSearchMode())
                rosterModel.setNumberOfRecords(String.valueOf(rosterModel.getSearchedRosters().size()));
            else
                rosterModel.setNumberOfRecords(String.valueOf(rosterModel.getRosters().size()));
        });
    }

    protected void clearSearchBox() {
        if (!rosterModel.getTextFieldString().isEmpty())
            rosterModel.setTextFieldString("");
    }

    protected void clearMainRoster() {
        rosterModel.getRosters().clear();
    }

    protected void sortRoster(List<RosterFx> updatedRoster) {
        updatedRoster.sort(Comparator.comparing(RosterFx::getId));
    }

    public void getRadioChoices() throws Exception {
        String endpoint = "radioChoices";
        String jsonResponse = rosterModel.getHttpClient().fetchDataFromGybe(endpoint);
        logger.debug("Radio choices response: {}", jsonResponse);
        List<MembershipListRadioDTO> choices = rosterModel.getHttpClient().getObjectMapper().readValue(
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
        String jsonResponse = rosterModel.getHttpClient().fetchDataFromGybe(endpoint);
        logger.debug("Roster Settings response: {}", jsonResponse);
        List<DbRosterSettingsDTO> choices = rosterModel.getHttpClient().getObjectMapper().readValue(
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

    protected List<RosterFx> updateRoster() throws Exception {
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
        try {
            String jsonResponse = rosterModel.getHttpClient().fetchDataFromGybe(endpoint.toString());
            logger.debug("Roster response: {}", jsonResponse);
            List<Roster> roster = rosterModel.getHttpClient().getObjectMapper().readValue(
                    jsonResponse,
                    new TypeReference<>() {
                    }
            );
            ObservableList<RosterFx> rosterFx = POJOtoFxConverter.copyRoster(roster);
            return rosterFx;
        } catch (Exception e) {
            logger.error("Could not perform search: {}", e.getMessage());
            return List.of();
        }
    }

    protected void exportRoster() {
        try {
            new Xls_roster(rosterModel);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    protected void fillTableView() {
        // do we have search terms in the box?
        if (!rosterModel.getTextFieldString().isEmpty()) fillWithSearchResults();
        else fillWithResults(); // search box cleared
    }

    private void fillWithResults() { // this is where we stick
        logger.debug("TableView is set to display normal results");
        Platform.runLater(() -> {
            rosterModel.getRosterTableView().setItems(rosterModel.getRosters());
            rosterModel.setIsSearchMode(false);
            rosterModel.getSearchedRosters().clear();
        });
    }

    private void fillWithSearchResults() {
        ObservableList<RosterFx> list = searchString(rosterModel.getTextFieldString());
        System.out.println("Search list is: " + list);
        Platform.runLater(() -> {
            rosterModel.getSearchedRosters().clear();
            rosterModel.getSearchedRosters().addAll(list);
            rosterModel.getRosterTableView().setItems(rosterModel.getSearchedRosters());
            rosterModel.setIsSearchMode(true);
        });
    }

    private ObservableList<RosterFx> searchString(String searchTerm) {
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

    public RosterFx getMembership() {
        return rosterModel.getSelectedMembershipList();
    }

    public void setRoster(List<RosterFx> updatedRoster) {
        // should already be in FX thread, but this is to make sure it doesn't stick.
        Platform.runLater(() -> {
            rosterModel.getRosters().addAll(updatedRoster);
            rosterModel.getRosterTableView().refresh();
        });
    }

    public void setNumberOfRecords(int size) {
        rosterModel.numberOfRecordsProperty().set(String.valueOf(size));
    }

//    public void search() {
//    logger.info("making search");
//        logger.info("Search made: {}", rosterModel.getTextFieldString());
//    }

    public int getSelectedYear() {
        return rosterModel.getSelectedYear();
    }

    public TableView<RosterFx> getRosterTableView() {
        return rosterModel.getRosterTableView();
    }
}
