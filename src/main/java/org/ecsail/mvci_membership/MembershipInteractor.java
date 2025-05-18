package org.ecsail.mvci_membership;

import javafx.application.Platform;
import org.ecsail.interfaces.SlipUser;
import org.ecsail.pojo.Membership;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MembershipInteractor implements SlipUser {
    private final MembershipModel membershipModel;
    private static final Logger logger = LoggerFactory.getLogger(MembershipInteractor.class);
//    private final DataBaseService dataBaseService;

    //    public MembershipInteractor(MembershipModel membershipModel, Connections connections) {
//        this.membershipModel = membershipModel;
//        this.dataBaseService = new DataBaseService(connections.getDataSource(), membershipModel);
//    }
    public MembershipInteractor(MembershipModel membershipModel) {
        this.membershipModel = membershipModel;
    }

    protected void setListsLoaded() {
        Platform.runLater(() -> {
            logger.info("Data for MembershipView is now loaded");
            membershipModel.setReturnMessage(MembershipMessage.DATA_LOAD_SUCCEED);
        });
    }

//    public DataBaseService getDataBaseService() {
//        return dataBaseService;
//    }

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
        logger.info("Deleting Membership MSID: " + membershipModel.getMembership().getMsId());
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

    //    public void getMembershiptoPOJO() throws Exception {
//        StringBuilder endpoint = new StringBuilder("membership");
//        endpoint.append("?year=").append(membershipModel.selectedMembershipYearProperty().getValue());
//        endpoint.append("&msid=").append(URLEncoder.encode(String.valueOf(membershipModel.getMembershipFromRosterList().getMsId()), StandardCharsets.UTF_8.name()));
//        try {
//            String jsonResponse = membershipModel.getHttpClient().fetchDataFromHalyard(endpoint.toString());
//
//            System.out.println(jsonResponse);
//        } catch (UnsupportedEncodingException e) {
//            logger.error(e.getMessage());
//            e.printStackTrace();
//        }
//
//    }
    public void getMembershiptoPOJO() {
        Logger logger = LoggerFactory.getLogger(getClass());
        StringBuilder endpoint = new StringBuilder("membership");
        try {
            endpoint.append("?year=").append(membershipModel.selectedMembershipYearProperty().getValue());
            endpoint.append("&msId=").append(URLEncoder.encode(String.valueOf(membershipModel.getMembershipFromRosterList().getMsId()), StandardCharsets.UTF_8.name()));
            logger.debug("Constructed endpoint: {}", endpoint.toString());

            String jsonResponse = membershipModel.getHttpClient().fetchDataFromHalyard(endpoint.toString());
            if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                logger.warn("Received null or empty JSON response from endpoint: {}", endpoint);
                System.out.println("No JSON response received from endpoint: " + endpoint);
            } else {
                logger.info("JSON Response: {}", jsonResponse);
                System.out.println("JSON Response: " + jsonResponse);

                // Deserialize JSON to Membership POJO
                try {
                    Membership membership = membershipModel.getHttpClient().getObjectMapper().readValue(jsonResponse, Membership.class);
                    logger.info("Deserialized Membership: mid={}, msId={}, fiscalYear={}",
                            membership.getMid(), membership.getMsId(), membership.getFiscalYear());
                    // Access fields, e.g., membership.getPeople().get(0).getFirstName()
                } catch (IOException e) {
                    logger.error("Failed to deserialize JSON: {}", e.getMessage(), e);
                    System.out.println("Deserialization error: " + e.getMessage());
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Encoding error for endpoint: {}", e.getMessage(), e);
            System.out.println("Encoding error: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IO error fetching data from endpoint {}: {}", endpoint.toString(), e.getMessage(), e);
            System.out.println("IO error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error fetching data: {}", e.getMessage(), e);
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}