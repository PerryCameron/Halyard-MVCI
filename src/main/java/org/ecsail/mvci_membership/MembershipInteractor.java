package org.ecsail.mvci_membership;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Platform;
import org.ecsail.dto.BoatDTOFx;
import org.ecsail.dto.BoatOwnerFx;
import org.ecsail.dto.MembershipDTOFx;
import org.ecsail.dto.SlipDTOFx;
import org.ecsail.interfaces.SlipUser;
import org.ecsail.pojo.*;
import org.ecsail.static_tools.CopyPOJOtoFx;
import org.ecsail.widgetfx.DialogueFx;
import org.ecsail.wrappers.InsertBoatResponse;
import org.ecsail.wrappers.UpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
//        try {
//            new PDF_Envelope(true, membershipModel, dataBaseService);
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        } catch (Exception e2) {
//            e2.printStackTrace();
//        }
    }


    public void deleteMembership() {
        logger.info("Deleting Membership MSID: " + membershipModel.membershipProperty().get().msIdProperty().get());
        int success[] = membershipModel.getSuccess();
//        try {
//            if (dataBaseService.existsSlipWithMsId()) {
//                Platform.runLater(() -> {
//                    DialogueFx.errorAlert("Looks like we have a problem", "You must re-assign slip before deleting this membership");
//                });
//            } else {
//                success[0] = dataBaseService.deleteBoatOwner(membershipModel.getMembership().getMsId());
//                success[1] = dataBaseService.deleteNotes(membershipModel.getMembership().getMsId());
//                int addIns[] = dataBaseService.deleteAllPaymentsAndInvoicesByMsId(membershipModel.getMembership().getMsId()); // invoiceRepo
//                success[2] = addIns[0];
//                success[3] = addIns[1];
//                success[4] = addIns[2];
//                success[5] = dataBaseService.deleteWaitList(membershipModel.getMembership().getMsId()); // slipRepo
//                success[6] = dataBaseService.deleteFormMsIdHash(membershipModel.getMembership().getMsId()); // membershipRepo
//                success[7] = dataBaseService.deleteMembershipId(membershipModel.getMembership().getMsId()); // membershipIdRepo
//                List<PersonDTO> people = dataBaseService.getPeople(membershipModel.getMembership().getMsId()); // personRepo
//                success[8] = people.size();
//                for (PersonDTO p : people) {
//                    success[10] += dataBaseService.deletePhones(p.getpId()); // phoneRepo
//                    success[11] += dataBaseService.deleteEmail(p.getpId()); // emailRepo
//                    success[12] += dataBaseService.deleteOfficer(p.getpId()); // officerRepo
//                    success[13] += dataBaseService.deleteUserAuthRequest(p.getpId());
//                    success[14] += dataBaseService.deleteAwards(p.getpId());
//                    success[15] += dataBaseService.deletePerson(p.getpId()); // personRepo
//                }
//                success[9] = dataBaseService.deleteMembership(membershipModel.getMembership().getMsId());  //membershipRepo
//
//            }
//            Platform.runLater(() -> {
//                membershipModel.setReturnMessage(MembershipMessage.DELETE_MEMBERSHIP_FROM_DATABASE_SUCCEED);
//            });
//        } catch (Exception e) {
//            membershipModel.setReturnMessage(MembershipMessage.DELETE_MEMBERSHIP_FROM_DATABASE_FAIL);
//            logger.error(e.getMessage());
//        }
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
            MembershipDTOFx membershipDTOFx = new MembershipDTOFx(membership);
            membershipModel.membershipProperty().set(membershipDTOFx);
            membershipDTOFx.slipProperty().set(new SlipDTOFx(membership.getSlip()));
            setSlipStatus();
            membershipDTOFx.getPeople().addAll(CopyPOJOtoFx.copyPeople(membership.getPeople()));
            membershipDTOFx.getMembershipIds().addAll(CopyPOJOtoFx.copyMembershipIds(membership.getMembershipIds()));
            membershipDTOFx.getInvoices().addAll(CopyPOJOtoFx.copyInvoices(membership.getInvoices()));
            membershipDTOFx.getBoats().addAll(CopyPOJOtoFx.copyBoats(membership.getBoats()));
            membershipDTOFx.getMemos().addAll(CopyPOJOtoFx.copyNotes(membership.getMemos()));
        } catch (Exception e) {
            logger.error("Failed to convert membership to FX: {}", e.getMessage(), e);
        }
    }

    private void setSlipStatus() {
        // person owns a slip
        if(!membershipModel.membershipProperty().get().slipProperty().get().getSlipNumber().isEmpty())
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
        BoatDTO boat = new BoatDTO(membershipModel.getSelectedBoat());
        try {
            String response = membershipModel.getHttpClient().postDataToGybe("update/boat", boat);
            return processUpdateResponse(response);
        } catch (Exception e) {
            logger.error("Failed to update boat  {}: {}",boat.getBoatId(), e.getMessage(), e);
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
            BoatOwnerDTO boatOwnerDTO = new BoatOwnerDTO(membershipModel.membershipProperty().get().getMsId(), 0);
            String response = membershipModel.getHttpClient().postDataToGybe("insert/boat", boatOwnerDTO);
            InsertBoatResponse insertBoatResponse = membershipModel.getHttpClient().getObjectMapper()
                    .readValue(response, InsertBoatResponse.class);
            if(insertBoatResponse.isSuccess()) {
                membershipModel.membershipProperty().get().getBoats().add(new BoatDTOFx(insertBoatResponse.getBoat()));
            } else {
                Platform.runLater(() -> {
                    DialogueFx.errorAlert("Unable add boat: ", insertBoatResponse.getMessage());
                });
                logger.error(insertBoatResponse.getMessage());
            }
        } catch (Exception e) {
//            logger.error("Failed to update boat  {}: {}", boatOwnerDTO.getBoatId(), e.getMessage(), e);
            e.printStackTrace();
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
}