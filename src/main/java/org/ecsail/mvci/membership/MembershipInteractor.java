package org.ecsail.mvci.membership;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.control.TableView;
import org.ecsail.fx.*;
import org.ecsail.interfaces.SlipUser;
import org.ecsail.pdf.PDF_Envelope;
import org.ecsail.pojo.*;
import org.ecsail.static_tools.HttpClientUtil;
import org.ecsail.static_tools.POJOtoFxConverter;
import org.ecsail.widgetfx.DialogueFx;
import org.ecsail.wrappers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class MembershipInteractor implements SlipUser {
    private final MembershipModel membershipModel;
    private static final Logger logger = LoggerFactory.getLogger(MembershipInteractor.class);
    private final HttpClientUtil httpClientUtil;

    public MembershipInteractor(MembershipModel membershipModel) {
        this.membershipModel = membershipModel;
        this.httpClientUtil = membershipModel.getHttpClient();
    }

    protected void setDataLoaded() {
        logger.info("Data for MembershipView is now loaded");
        Platform.runLater(() -> membershipModel.dataIsLoadedProperty().set(true));
    }

    public MembershipMessage printEnvelope() {
        try {
            new PDF_Envelope(membershipModel);
            return MembershipMessage.SUCCESS;
        } catch (IOException e1) {
            return setFailMessage("Unable to print envelope",0,e1.getMessage());
        } catch (Exception e2) {
            return setFailMessage("Unable to perform update",0, e2.getMessage());
        }
    }

    public MembershipMessage getMembership() {
        MembershipResponse membershipResponse = getMembershipResponse();
        return convertPOJOsToFXProperties(membershipResponse);
    }

    public MembershipResponse getMembershipResponse() {
        MembershipResponse membershipResponse = new MembershipResponse();
        Logger logger = LoggerFactory.getLogger(getClass());
        StringBuilder endpoint = new StringBuilder("membership");
        ObjectMapper mapper = membershipModel.getHttpClient().getObjectMapper();
        try {
            endpoint.append("?year=").append(membershipModel.selectedMembershipYearProperty().getValue());
            endpoint.append("&msId=").append(URLEncoder.encode(String.valueOf(membershipModel.getMembershipFromRosterList().getMsId()), StandardCharsets.UTF_8));
            logger.debug("Constructed endpoint: {}", endpoint);
            String jsonResponse = membershipModel.getHttpClient().fetchDataFromGybe(endpoint.toString());
            if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                logger.warn("Received null or empty JSON response from endpoint: {}", endpoint);
                membershipResponse.setMessage("Received null or empty JSON response from endpoint: " + endpoint);
                return membershipResponse;
            }
            // Parse the JSON response to check the status
            JsonNode rootNode = mapper.readTree(jsonResponse);
            String status = rootNode.get("status").asText();
            if ("success".equals(status)) {
                // Deserialize the 'membership' field into Membership class
                JsonNode membershipNode = rootNode.get("membership");
                membershipResponse.setMembership(mapper.treeToValue(membershipNode, Membership.class));
                membershipResponse.setSuccess(true);
            } else {
                // Log the error message and return null
                String errorMessage = rootNode.get("message").asText();
                logger.error("Server returned error: {}", errorMessage);
                membershipResponse.setMessage("Server returned error: " + errorMessage);
            }
            return membershipResponse;
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding error for endpoint: {}", e.getMessage(), e);
            membershipResponse.setMessage("Encoding error for endpoint: " + e.getMessage());
            return membershipResponse;
        } catch (IOException e) {
            logger.error("IO error fetching or parsing data from endpoint {}: {}", endpoint, e.getMessage(), e);
            membershipResponse.setMessage("IO error fetching or parsing data from endpoint: " + e.getMessage());
            return membershipResponse;
        } catch (Exception e) {
            logger.error("Unexpected error fetching data: {}", e.getMessage(), e);
            membershipResponse.setMessage("Unexpected error fetching data: " + e.getMessage());
            return membershipResponse;
        }
    }

    public MembershipMessage convertPOJOsToFXProperties(MembershipResponse membershipResponse) {
        logger.info("Seeing what thread this is on");
        Membership membership = membershipResponse.getMembership();
        try {
            MembershipFx membershipDTOFx = new MembershipFx(membership);
            membershipModel.membershipProperty().set(membershipDTOFx);
            membershipDTOFx.slipProperty().set(new SlipFx(membership.getSlip()));
            setSlipStatus();
            membershipDTOFx.getPeople().addAll(POJOtoFxConverter.copyPeople(membership.getPeople()));
            membershipDTOFx.getMembershipIds().addAll(POJOtoFxConverter.copyMembershipIds(membership.getMembershipIds()));
            membershipDTOFx.getInvoices().addAll(POJOtoFxConverter.copyInvoices(membership.getInvoices()));
            membershipDTOFx.getBoats().addAll(POJOtoFxConverter.copyBoats(membership.getBoats()));
            membershipDTOFx.getMemos().addAll(POJOtoFxConverter.copyNotes(membership.getMemos()));
            return MembershipMessage.GET_DATA_SUCCESS;
        } catch (Exception e) {
            Platform.runLater(() -> DialogueFx.errorAlert("Conversion failure", "Failed to covert POJO to FX: " + e.getMessage()));
            logger.error("Failed to convert membership to FX: {}", e.getMessage(), e);
        }
        return MembershipMessage.GET_DATA;
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

    public MembershipMessage updateNote() {
        logger.debug("Updating position with pId: {}", membershipModel.getSelectedNote().getMemoId());
        Note note = new Note(membershipModel.getSelectedNote());
        try {
            String response = membershipModel.getHttpClient().postDataToGybe("update/notes", note);
            NoteResponse noteResponse = httpClientUtil.getObjectMapper().readValue(response, NoteResponse.class);
            if(noteResponse.isSuccess())
                return MembershipMessage.SUCCESS;
            else return setFailMessage("Note update failure", 0, noteResponse.getMessage());
        } catch (Exception e) {
            logger.error("Failed to update note: {}", e.getMessage());
            return setFailMessage("Failed to update note", 0, e.getMessage());
        }
    }

    public MembershipMessage updateBoat() {
        logger.debug("Updating boat with pId: {}", membershipModel.getSelectedBoat().boatIdProperty().get());
        Boat boat = new Boat(membershipModel.getSelectedBoat());
        try {
            String response = membershipModel.getHttpClient().postDataToGybe("update/boat", boat);
            BoatResponse boatResponse = httpClientUtil.getObjectMapper().readValue(response, BoatResponse.class);
            if(boatResponse.isSuccess())
            return MembershipMessage.SUCCESS;
            else {
                return setFailMessage("Boat update failure",0,boatResponse.getMessage());
            }
        } catch (Exception e) {
            logger.error("Failed to update boat  {}: {}", boat.getBoatId(), e.getMessage(), e);
            return setFailMessage("Boat update failure",0,"");
        }
    }

    public MembershipMessage insertBoat() {
        try {
            BoatOwner boatOwnerDTO = new BoatOwner(membershipModel.membershipProperty().get().getMsId(), 0);
            String response = membershipModel.getHttpClient().postDataToGybe("insert/boat", boatOwnerDTO);
            BoatResponse boatResponse = membershipModel.getHttpClient().getObjectMapper()
                    .readValue(response, BoatResponse.class);
            if (boatResponse.isSuccess()) {
                membershipModel.getBoatTableView().getItems().addFirst(new BoatFx(boatResponse.getBoat())); // this correctly inserts a new row at the top
                membershipModel.getBoatTableView().getSelectionModel().selectFirst();  // why is this not selecting the row I just put in?
                return MembershipMessage.SUCCESS;
            } else {
                Platform.runLater(() -> DialogueFx.errorAlert("Unable add boat: ", boatResponse.getMessage()));
                logger.error(boatResponse.getMessage());
                return MembershipMessage.FAIL;
            }
        } catch (Exception e) {
            logger.error("Could not create new boat: {}", e.getMessage());
            Platform.runLater(() -> DialogueFx.errorAlert("Unable add boat: ", e.getMessage()));
            return MembershipMessage.FAIL;
        }
    }

    public MembershipMessage insertNote() {
        try {
            Note note = new Note(membershipModel.membershipProperty().get().getMsId());
            String response = membershipModel.getHttpClient().postDataToGybe("insert/note", note);
            NoteResponse noteResponse = membershipModel.getHttpClient().getObjectMapper()
                    .readValue(response, NoteResponse.class);
            if (noteResponse.isSuccess()) {
                membershipModel.getNotesTableView().getItems().addFirst(new NoteFx(noteResponse.getNote()));
                membershipModel.getNotesTableView().getSelectionModel().selectFirst();
                return MembershipMessage.SUCCESS;
            } else {
                return setFailMessage("Unable add boat",0,noteResponse.getMessage());
            }
        } catch (Exception e) {
            return setFailMessage("Failed to add new boat",0,e.getMessage());
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
                logger.error("Failed to delete membership {}: {}", membershipModel.membershipProperty().get().getMembershipId(), errorMessage);
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Membership Failed", errorMessage));
                return MembershipMessage.DELETE_MEMBERSHIP_FROM_DATABASE_FAIL;
            }
        } catch (Exception e) {
            logger.error("Failed to delete membership {}: {}", membershipModel.membershipProperty().get().getMembershipId(), e.getMessage(), e);
            Platform.runLater(() -> DialogueFx.errorAlert("Delete Membership Failed", e.getMessage()));
            return MembershipMessage.DELETE_MEMBERSHIP_FROM_DATABASE_FAIL;
        }
    }

    public MembershipMessage deleteBoat() {
        try {
            // Validate inputs
            if (membershipModel.getSelectedBoat() == null) {
                return setFailMessage("Delete Boat failed",0,"Selected boat has a null value");
            }
            if (membershipModel.membershipProperty().get() == null) {
                return setFailMessage("Delete Boat failed",0,"No membership selected");
            }
            int boatId = membershipModel.getSelectedBoat().getBoatId();
            Integer msId = membershipModel.membershipProperty().get().getMsId();
            BoatOwner boatOwnerDTO = new BoatOwner(msId, boatId);
            // Send delete request to server
            String response = membershipModel.getHttpClient().postDataToGybe("delete/boat", boatOwnerDTO);
            if (response == null) {
                return setFailMessage("Delete Boat failed",0,"Null response from server");
            }
            BoatResponse boatResponse = httpClientUtil.getObjectMapper().readValue(response, BoatResponse.class);
            if (boatResponse == null) {
                return setFailMessage("Delete Boat failed",0,"Invalid response from server");
            }
            if (boatResponse.isSuccess()) {
                logger.info("Successfully deleted boat {}", boatId);
                //BoatDTOFx boatDTOFx = membershipModel.getBoatById(boatId);
                BoatFx boatFx = membershipModel.membershipProperty().get().getBoatById(boatId);
                if (boatFx != null) {
                    //membershipModel.getBoatTableView().getItems().remove(membershipModel.getSelectedBoat());
                    membershipModel.getBoatTableView().getItems().remove(boatFx);
                    membershipModel.getBoatTableView().refresh();
                    return MembershipMessage.SUCCESS;
                } else {
                    return setFailMessage("Delete Boat failed",boatId," deleted on server but boat not found for membership");  // this happens when I create a boat then delete it
                }
            } else {
                String errorMessage = boatResponse.getMessage() != null ? boatResponse.getMessage() : "Unknown error";
                return setFailMessage("Delete Boat failed",0,errorMessage);
            }
        } catch (Exception e) {
            int boatId = membershipModel.getSelectedBoat() != null ?
                    membershipModel.getSelectedBoat().getBoatId() : 0;
            return setFailMessage("Delete Boat failed for ID: ",boatId,e.getMessage());
        }
    }

    public MembershipMessage deleteNote() {
        try {
            if (membershipModel.selectedNoteProperty().get() == null)
                return setFailMessage("Failed to delete note", 0, "No note selected");
            String response = httpClientUtil.postDataToGybe("delete/note", new Note(membershipModel.selectedNoteProperty().get()));
            if (response == null)
                return setFailMessage("Failed to delete note", membershipModel.selectedNoteProperty().get().getMemoId(), "Null response from server");
            NoteResponse noteResponse = httpClientUtil.getObjectMapper().readValue(response, NoteResponse.class);
            if (noteResponse == null)
                return setFailMessage("Failed to delete note", membershipModel.selectedNoteProperty().get().getMemoId(), "Invalid response from server");
            if (noteResponse.isSuccess()) {
                logger.info("Successfully deleted note {}", membershipModel.selectedNoteProperty().get().getMemoId());
                NoteFx noteFx = membershipModel.selectedNoteProperty().get();
                if (noteFx != null) {
                    Platform.runLater(() -> {
                        membershipModel.notesTableViewProperty().get().getItems().remove(noteFx);
                        membershipModel.notesTableViewProperty().get().refresh();
                    });
                    return MembershipMessage.SUCCESS;
                } else {
                    return setFailMessage("Failed to delete note: ", membershipModel.selectedNoteProperty().get().getMemoId(), "Null response from server");
                }
            } else {
                String errorMessage = noteResponse.getMessage() != null ? noteResponse.getMessage() : "Unknown error";
                return setFailMessage("Failed to delete note", membershipModel.selectedNoteProperty().get().getMemoId(), errorMessage);
            }
        } catch (Exception e) {
            return setFailMessage("Failed to delete note", membershipModel.selectedNoteProperty().get().getMemoId(), e.getMessage());
        }
    }

    protected void signalSuccess() {
        membershipModel.getMainModel().toggleRxSuccess();
    }

    public void signalFail() {
        membershipModel.getMainModel().toggleRxFail();
    }

    public void removeMembershipFromList(TableView<RosterFx> rosterTableView) {
        Optional<RosterFx> rosterFx = rosterTableView.getItems().stream()
                .filter(roster -> roster.getMsId() == membershipModel.membershipProperty()
                        .get().getMsId()).findFirst();
        if (rosterFx.isPresent()) {
            rosterTableView.getItems().remove(rosterFx.get());
            logger.info("Removed membership {} from membership list", rosterFx.get().getId());
        }
    }

    public int getMsId() {
        return membershipModel.membershipProperty().get().getMsId();
    }

    public void setExecutorService(ExecutorService executorService) {
        membershipModel.setExecutorService(executorService);
    }

    public MembershipMessage setFailMessage(String title, int id, String message) {
        membershipModel.errorMessageProperty().set(title + ":" + id + ":" + message);
        return MembershipMessage.FAIL;
    }

    public String[] getFailMessage() {
        return membershipModel.errorMessageProperty().get().split(":");
    }

    public void logError(String message) {
        logger.error(message);
    }
}