package org.ecsail.mvci_membership;

import javafx.application.Platform;
import org.ecsail.connection.Connections;
import org.ecsail.dto.PersonDTO;
import org.ecsail.interfaces.SlipUser;
import org.ecsail.pdf.PDF_Envelope;
import org.ecsail.widgetfx.DialogueFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MembershipInteractor implements SlipUser {
    private final MembershipModel membershipModel;
    private static final Logger logger = LoggerFactory.getLogger(MembershipInteractor.class);
    private final DataBaseService dataBaseService;

    public MembershipInteractor(MembershipModel membershipModel, Connections connections) {
        this.membershipModel = membershipModel;
        this.dataBaseService = new DataBaseService(connections.getDataSource(), membershipModel);
    }

    protected void setListsLoaded() {
        Platform.runLater(() -> {
            logger.info("Data for MembershipView is now loaded");
            membershipModel.setReturnMessage(MembershipMessage.DATA_LOAD_SUCCEED);
        });
    }

    public DataBaseService getDataBaseService() {
        return dataBaseService;
    }

    public void uploadMemberPhoto() {
        System.out.println("uploading photo");
    }

    public void loadInvoices() {
    }

    public void printEnvelope() {
        try {
            new PDF_Envelope(true, membershipModel, dataBaseService);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    public void deleteMembership() {
        logger.info("Deleting Membership MSID: " + membershipModel.getMembership().getMsId());
        int success[] = membershipModel.getSuccess();
        try {
            if (dataBaseService.existsSlipWithMsId()) {
                Platform.runLater(() -> {
                    DialogueFx.errorAlert("Looks like we have a problem", "You must re-assign slip before deleting this membership");
                });
            } else {
                success[0] = dataBaseService.deleteBoatOwner(membershipModel.getMembership().getMsId());
                success[1] = dataBaseService.deleteNotes(membershipModel.getMembership().getMsId());
                int addIns[] = dataBaseService.deleteAllPaymentsAndInvoicesByMsId(membershipModel.getMembership().getMsId()); // invoiceRepo
                success[2] = addIns[0];
                success[3] = addIns[1];
                success[4] = addIns[2];
                success[5] = dataBaseService.deleteWaitList(membershipModel.getMembership().getMsId()); // slipRepo
                success[6] = dataBaseService.deleteFormMsIdHash(membershipModel.getMembership().getMsId()); // membershipRepo
                success[7] = dataBaseService.deleteMembershipId(membershipModel.getMembership().getMsId()); // membershipIdRepo
                List<PersonDTO> people = dataBaseService.getPeople(membershipModel.getMembership().getMsId()); // personRepo
                success[8] = people.size();
                for (PersonDTO p : people) {
                    success[10] += dataBaseService.deletePhones(p.getpId()); // phoneRepo
                    success[11] += dataBaseService.deleteEmail(p.getpId()); // emailRepo
                    success[12] += dataBaseService.deleteOfficer(p.getpId()); // officerRepo
                    success[13] += dataBaseService.deleteUserAuthRequest(p.getpId());
                    success[14] += dataBaseService.deleteAwards(p.getpId());
                    success[15] += dataBaseService.deletePerson(p.getpId()); // personRepo
                }
                success[9] = dataBaseService.deleteMembership(membershipModel.getMembership().getMsId());  //membershipRepo

            }
            Platform.runLater(() -> {
                membershipModel.setReturnMessage(MembershipMessage.DELETE_MEMBERSHIP_FROM_DATABASE_SUCCEED);
            });
        } catch (Exception e) {
            membershipModel.setReturnMessage(MembershipMessage.DELETE_MEMBERSHIP_FROM_DATABASE_FAIL);
            logger.error(e.getMessage());
        }
    }
}