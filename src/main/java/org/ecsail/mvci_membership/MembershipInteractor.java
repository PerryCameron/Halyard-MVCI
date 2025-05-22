package org.ecsail.mvci_membership;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Platform;
import javafx.scene.control.TableView;
import org.ecsail.fx.*;
import org.ecsail.interfaces.SlipUser;
import org.ecsail.pdf.PDF_Envelope;
import org.ecsail.pojo.*;
import org.ecsail.static_tools.POJOtoFxConverter;
import org.ecsail.widgetfx.DialogueFx;
import org.ecsail.wrappers.InsertBoatResponse;
import org.ecsail.wrappers.UpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class MembershipInteractor implements SlipUser {
    private final MembershipModel membershipModel;
    private static final Logger logger = LoggerFactory.getLogger(MembershipInteractor.class);

    public MembershipInteractor(MembershipModel membershipModel) {
        this.membershipModel = membershipModel;
    }

    protected void setDataLoaded() {
        Platform.runLater(() -> {
            logger.info("Data for MembershipView is now loaded");
            membershipModel.dataIsLoadedProperty().set(true);
        });
    }

    public void uploadMemberPhoto() {
        System.out.println("uploading photo");
    }

    public void loadInvoices() {
    }

    public void printEnvelope() {
        try {
            new PDF_Envelope(membershipModel);
        } catch (IOException e1) {
            Platform.runLater(() -> {
                DialogueFx.errorAlert("Unable to print envelope: {}", e1.getMessage());
            });
            logger.error(e1.getMessage());
        } catch (Exception e2) {
            Platform.runLater(() -> {
                DialogueFx.errorAlert("Unable to perform update", e2.getMessage());
            });
            logger.error(e2.getMessage());
        }
    }


    public Membership getMembershiptoPOJO() {
        Logger logger = LoggerFactory.getLogger(getClass());
        StringBuilder endpoint = new StringBuilder("membership");
        try {
            endpoint.append("?year=").append(membershipModel.selectedMembershipYearProperty().getValue());
            endpoint.append("&msId=").append(URLEncoder.encode(String.valueOf(membershipModel.getMembershipFromRosterList().getMsId()), StandardCharsets.UTF_8.name()));
            logger.debug("Constructed endpoint: {}", endpoint);

            String jsonResponse = membershipModel.getHttpClient().fetchDataFromGybe(endpoint.toString());
            if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                logger.warn("Received null or empty JSON response from endpoint: {}", endpoint);
                System.out.println("No JSON response received from endpoint: " + endpoint);
            } else {
                logger.info("JSON Response: {}", jsonResponse);
                // Deserialize JSON to Membership POJO
                try {
                    return membershipModel.getHttpClient().getObjectMapper().readValue(jsonResponse, Membership.class);
                } catch (IOException e) {
                    logger.error("Failed to deserialize JSON: {}", e.getMessage(), e);
                    System.out.println("Deserialization error: " + e.getMessage());
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding error for endpoint: {}", e.getMessage(), e);
            System.out.println("Encoding error: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IO error fetching data from endpoint {}: {}", endpoint, e.getMessage(), e);
            System.out.println("IO error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error fetching data: {}", e.getMessage(), e);
            System.out.println("Unexpected error: " + e.getMessage());
        }
        return null;
    }

    public void convertPOJOsToFXProperties(Membership membership) {
        try {
            MembershipFx membershipDTOFx = new MembershipFx(membership);
            membershipModel.membershipProperty().set(membershipDTOFx);
            membershipDTOFx.slipProperty().set(new SlipDTOFx(membership.getSlip()));
            setSlipStatus();
            membershipDTOFx.getPeople().addAll(POJOtoFxConverter.copyPeople(membership.getPeople()));
            membershipDTOFx.getMembershipIds().addAll(POJOtoFxConverter.copyMembershipIds(membership.getMembershipIds()));
            membershipDTOFx.getInvoices().addAll(POJOtoFxConverter.copyInvoices(membership.getInvoices()));
            membershipDTOFx.getBoats().addAll(POJOtoFxConverter.copyBoats(membership.getBoats()));
            membershipDTOFx.getMemos().addAll(POJOtoFxConverter.copyNotes(membership.getMemos()));
        } catch (Exception e) {
            Platform.runLater(() -> DialogueFx.errorAlert("Conversion failure", "Failed to covert POJO to FX: " + e.getMessage()));
            logger.error("Failed to convert membership to FX: {}", e.getMessage(), e);
        }
    }

    // TODO This is a temp way to do this. I need to probably change the membership to JSON method to also give enough information to find the sublease person
    private void setSlipStatus() {
        // person owns a slip
        if (!membershipModel.membershipProperty().get().slipProperty().get().getSlipNumber().isEmpty())
            membershipModel.setSlipRelationStatus(SlipUser.slip.owner);
            // if(membershipModel.membershipProperty().get().slipProperty().get().subleased_toProperty().get() == 0)
        else
            membershipModel.setSlipRelationStatus(SlipUser.slip.noSlip);
        // we are not looking for subleases yet.
    }


    /**
     * Updates a membership's notes data by sending a POST request to the halyard/update/notes endpoint.
     *
     * @param // NotesDTOFx the position data to update
     * @return the JSON response from the server
     */
    public String updateNotes() {
        logger.debug("Updating position with pId: {}", membershipModel.getSelectedNote().getMemoId());
        Note note = new Note(membershipModel.getSelectedNote());
        try {
            String response = membershipModel.getHttpClient().postDataToGybe("update/notes", note);
            return processUpdateResponse(response);
        } catch (Exception e) {
            logger.error("Failed to update email with pId {}: {}",
                    membershipModel.getSelectedPerson().pIdProperty().get(), e.getMessage(), e);
            e.printStackTrace();
            membershipModel.getMainModel().toggleRxFail(); // Indicate failure
            return null;
        }
    }

    /**
     * Updates a person's position data by sending a POST request to the halyard/update/position endpoint.
     *
     * @param // OfficerDTOFx the position data to update
     * @return the JSON response from the server
     */
    public String updatePosition() {
        logger.debug("Updating position with pId: {}", membershipModel.getSelectedOfficer().getOfficerId());
        Officer officer = new Officer(membershipModel.getSelectedOfficer());
        try {
            String response = membershipModel.getHttpClient().postDataToGybe("update/position", officer);
            return processUpdateResponse(response);
        } catch (Exception e) {
            logger.error("Failed to update email with pId {}: {}",
                    membershipModel.getSelectedPerson().pIdProperty().get(), e.getMessage(), e);
            e.printStackTrace();
            membershipModel.getMainModel().toggleRxFail(); // Indicate failure
            return null;
        }
    }

    /**
     * Updates a person's awards data by sending a POST request to the halyard/update/awards endpoint.
     *
     * @param // awardDTOFx the phone data to update
     * @return the JSON response from the server
     */
    public String updateAward() {
        logger.debug("Updating phone with pId: {}", membershipModel.getSelectedAward().getAwardId());
        Award award = new Award(membershipModel.getSelectedAward());
        try {
            String response = membershipModel.getHttpClient().postDataToGybe("update/award", award);
            return processUpdateResponse(response);
        } catch (Exception e) {
            logger.error("Failed to update email with pId {}: {}",
                    membershipModel.getSelectedPerson().pIdProperty().get(), e.getMessage(), e);
            e.printStackTrace();
            membershipModel.getMainModel().toggleRxFail(); // Indicate failure
            return null;
        }
    }

    /**
     * Updates a person's phone data by sending a POST request to the halyard/update/email endpoint.
     *
     * @param // emailDTOFx the phone data to update
     * @return the JSON response from the server
     */
    public String updateEmail() {
        logger.debug("Updating phone with pId: {}", membershipModel.getSelectedEmail().getEmailId());
        Email email = new Email(membershipModel.getSelectedEmail());
        try {
            String response = membershipModel.getHttpClient().postDataToGybe("update/email", email);
            return processUpdateResponse(response);
        } catch (Exception e) {
            logger.error("Failed to update email with pId {}: {}",
                    membershipModel.getSelectedPerson().pIdProperty().get(), e.getMessage(), e);
            e.printStackTrace();
            membershipModel.getMainModel().toggleRxFail(); // Indicate failure
            return null;
        }
    }

    /**
     * Updates a person's phone data by sending a POST request to the halyard/update/phone endpoint.
     *
     * @param // phoneDTOFx the phone data to update
     * @return the JSON response from the server
     */
    public String updatePhone() {
        logger.debug("Updating phone with pId: {}", membershipModel.getSelectedPerson().pIdProperty().get());
        Phone phone = new Phone(membershipModel.getSelectedPhone());
        try {
            String response = membershipModel.getHttpClient().postDataToGybe("update/phone", phone);
            return processUpdateResponse(response);
        } catch (Exception e) {
            logger.error("Failed to update phone with pId {}: {}",
                    membershipModel.getSelectedPerson().pIdProperty().get(), e.getMessage(), e);
            e.printStackTrace();
            membershipModel.getMainModel().toggleRxFail(); // Indicate failure
            return null;
        }
    }

    /**
     * Updates a person's data by sending a POST request to the halyard/update/person endpoint.
     *
     * @param // personDTOFx the person data to update
     * @return the JSON response from the server
     */
    public String updatePerson() {
        logger.debug("Updating person with pId: {}", membershipModel.getSelectedPerson().pIdProperty().get());
        Person person = new Person(membershipModel.getSelectedPerson());
        try {
            String response = membershipModel.getHttpClient().postDataToGybe("update/person", person);
            return processUpdateResponse(response);
        } catch (Exception e) {
            logger.error("Failed to update person with pId {}: {}",
                    membershipModel.getSelectedPerson().pIdProperty().get(), e.getMessage(), e);
            e.printStackTrace();
            membershipModel.getMainModel().toggleRxFail(); // Indicate failure
            return null;
        }
    }

    /**
     * Updates a boat's data by sending a POST request to the halyard/update/boat endpoint.
     *
     * @param // Boat the boat data to update
     * @return the JSON response from the server
     */
    public String updateBoat() {
        logger.debug("Updating boat with pId: {}", membershipModel.getSelectedBoat().boatIdProperty().get());
        Boat boat = new Boat(membershipModel.getSelectedBoat());
        try {
            String response = membershipModel.getHttpClient().postDataToGybe("update/boat", boat);
            return processUpdateResponse(response);
        } catch (Exception e) {
            logger.error("Failed to update boat  {}: {}", boat.getBoatId(), e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inserts a boat row by sending a POST request to the halyard/update/boat endpoint.
     *
     * @param // Boat the boat data to update
     * @return the JSON response from the server
     */
    public void insertBoat() {
        try {
            BoatOwner boatOwnerDTO = new BoatOwner(membershipModel.membershipProperty().get().getMsId(), 0);
            String response = membershipModel.getHttpClient().postDataToGybe("insert/boat", boatOwnerDTO);
            InsertBoatResponse insertBoatResponse = membershipModel.getHttpClient().getObjectMapper()
                    .readValue(response, InsertBoatResponse.class);
            if (insertBoatResponse.isSuccess()) {
                membershipModel.getBoatTableView().getItems().add(new BoatDTOFx(insertBoatResponse.getBoat()));
                membershipModel.getBoatTableView().refresh();
            } else {
                Platform.runLater(() -> {
                    DialogueFx.errorAlert("Unable add boat: ", insertBoatResponse.getMessage());
                });
                logger.error(insertBoatResponse.getMessage());
            }
        } catch (Exception e) {
            logger.error("Could not create new boat: {}", e.getMessage());
            Platform.runLater(() -> {
                DialogueFx.errorAlert("Unable add boat: ", e.getMessage());
            });
        }
    }

    public MembershipMessage deleteMembership() {
        logger.info("Deleting Membership MSID: {}", membershipModel.membershipProperty().get().msIdProperty().get());
        try {
            String response = membershipModel.getHttpClient().postDataToGybe("delete/membership", new Membership(membershipModel.membershipProperty().get()));
            if (response == null) {
                logger.error("Failed to delete membership {}: Null response from server", membershipModel.membershipProperty().get().getMembershipId());
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Membership Failed", "Null response from server"));
                return MembershipMessage.DELETE_MEMBERSHIP_FROM_DATABASE_FAIL;
            }
            UpdateResponse updateResponse = membershipModel.getHttpClient().getObjectMapper()
                    .readValue(response, UpdateResponse.class);
            if (updateResponse == null) {
                logger.error("Failed to delete membership {}: Invalid response from server", membershipModel.membershipProperty().get().getMembershipId());
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Membership Failed", "Invalid response from server"));
                return MembershipMessage.DELETE_MEMBERSHIP_FROM_DATABASE_FAIL;
            }
            if (updateResponse.isSuccess()) {
                logger.info("Successfully deleted membership {}", membershipModel.membershipProperty().get().getMembershipId());
                return MembershipMessage.DELETE_MEMBERSHIP_FROM_DATABASE_SUCCEED;
            } else {
                String errorMessage = updateResponse.getMessage() != null ? updateResponse.getMessage() : "Unknown error";
                logger.error("Failed to membership boat {}: {}", membershipModel.membershipProperty().get().getMembershipId(), errorMessage);
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Boat Failed", errorMessage));
                return MembershipMessage.DELETE_MEMBERSHIP_FROM_DATABASE_FAIL;
            }
        } catch (Exception e) {
            Integer boatId = membershipModel.getSelectedBoat() != null ?
                    membershipModel.getSelectedBoat().getBoatId() : null;
            logger.error("Failed to delete boat {}: {}", boatId, e.getMessage(), e);
            Platform.runLater(() -> DialogueFx.errorAlert("Delete Boat Failed", e.getMessage()));
            return MembershipMessage.DELETE_MEMBERSHIP_FROM_DATABASE_FAIL;
        }
    }


    public void deleteBoat() {
        try {
            // Validate inputs
            if (membershipModel.getSelectedBoat() == null) {
                logger.error("Failed to delete boat: No boat selected");
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Boat Failed", "No boat selected"));
                return;
            }
            if (membershipModel.membershipProperty().get() == null) {
                logger.error("Failed to delete boat: No membership selected");
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Boat Failed", "No membership selected"));
                return;
            }
            Integer boatId = membershipModel.getSelectedBoat().getBoatId();
            Integer msId = membershipModel.membershipProperty().get().getMsId();
            BoatOwner boatOwnerDTO = new BoatOwner(msId, boatId);
            // Send delete request to server
            String response = membershipModel.getHttpClient().postDataToGybe("delete/boat", boatOwnerDTO);
            if (response == null) {
                logger.error("Failed to delete boat {}: Null response from server", boatId);
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Boat Failed", "Null response from server"));
                return;
            }
            UpdateResponse updateResponse = membershipModel.getHttpClient().getObjectMapper()
                    .readValue(response, UpdateResponse.class);
            if (updateResponse == null) {
                logger.error("Failed to delete boat {}: Invalid response from server", boatId);
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Boat Failed", "Invalid response from server"));
                return;
            }
            if (updateResponse.isSuccess()) {
                logger.info("Successfully deleted boat {}", boatId);
                BoatDTOFx boatDTOFx = membershipModel.getBoatById(boatId);
                if (boatDTOFx != null) {
                    Platform.runLater(() -> {
                        membershipModel.getBoatTableView().getItems().remove(boatDTOFx);
                        // Refresh table only if necessary
                        membershipModel.getBoatTableView().refresh();
                    });
                } else {
                    // Log warning but don’t show error alert, as deletion succeeded
                    logger.warn("Boat {} deleted on server but not found in membership list", boatId);
                }
            } else {
                String errorMessage = updateResponse.getMessage() != null ? updateResponse.getMessage() : "Unknown error";
                logger.error("Failed to delete boat {}: {}", boatId, errorMessage);
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Boat Failed", errorMessage));
            }
        } catch (Exception e) {
            Integer boatId = membershipModel.getSelectedBoat() != null ?
                    membershipModel.getSelectedBoat().getBoatId() : null;
            logger.error("Failed to delete boat {}: {}", boatId, e.getMessage(), e);
            Platform.runLater(() -> DialogueFx.errorAlert("Delete Boat Failed", e.getMessage()));
        }
    }

    private String processUpdateResponse(String response) throws JsonProcessingException {
        logger.debug("Update response: {}", response);
        UpdateResponse updateResponse = membershipModel.getHttpClient()
                .getObjectMapper().readValue(response, UpdateResponse.class);
        // Toggle the appropriate light based on the success field
        if (updateResponse.isSuccess()) {
            membershipModel.getMainModel().toggleRxSuccess();
            logger.info(updateResponse.getMessage());
        } else {
            membershipModel.getMainModel().toggleRxFail();
            Platform.runLater(() -> {
                DialogueFx.errorAlert("Unable to perform update", updateResponse.getMessage());
            });
            logger.error(updateResponse.getMessage());
        }
        return response;
    }

    public void removeMembershipFromList(TableView<RosterFx> rosterTableView) {
        Optional<RosterFx> rosterFx = Optional.ofNullable(rosterTableView.getItems().stream()
                .filter(roster -> roster.getMsId() == membershipModel.membershipProperty()
                        .get().getMsId()).findFirst().orElse(null));
        if(rosterFx.isPresent()) {
            rosterTableView.getItems().remove(rosterFx.get());
            logger.info("Removed membership {} from membership list", rosterFx.get().getId());
        }
    }

    public int getMsId() {
        return membershipModel.membershipProperty().get().getMsId();
    }
}