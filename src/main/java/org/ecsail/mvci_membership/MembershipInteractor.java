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
        System.out.println("Printing envelope");
        try {
            new PDF_Envelope(true, membershipModel, dataBaseService);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    public void deleteMembership() {
        int success[] = new int[20];
        try {
            if (dataBaseService.existsSlipWithMsId()) {
                Platform.runLater(() -> {
                    DialogueFx.errorAlert("Looks like we have a problem", "You must re-assign slip before deleting this membership");
                });
                success[0] = dataBaseService.deleteBoatOwner(membershipModel.getMembership().getMsId());
                success[1] = dataBaseService.deleteNotes(membershipModel.getMembership().getMsId());
                int addIns[] = dataBaseService.deleteAllPaymentsAndInvoicesByMsId(membershipModel.getMembership().getMsId()); // invoiceRepo
                success[2] = addIns[0];
                success[3] = addIns[1];
                success[4] = addIns[2];
//                setMessage("Deleting wait_list entries", dialogue);
                success[5] = dataBaseService.deleteWaitList(membershipModel.getMembership().getMsId()); // slipRepo
//                setMessage("Deleting membership hash", dialogue);
                success[6] = dataBaseService.deleteFormMsIdHash(membershipModel.getMembership().getMsId()); // membershipRepo
//                setMessage("Deleting history",dialogue);
                success[7] = dataBaseService.deleteMembershipId(membershipModel.getMembership().getMsId()); // membershipIdRepo
                List<PersonDTO> people = dataBaseService.getPeople(membershipModel.getMembership().getMsId()); // personRepo
                success[8] = people.size();
//                setMessage("Deleting membership", dialogue);
                int dm = dataBaseService.deleteMembership(membershipModel.getMembership().getMsId());  //membershipRepo
//                setMessage("Deleting people", dialogue);
                for (PersonDTO p : people) {
                    success[9] += dataBaseService.deletePhones(p.getpId()); // phoneRepo
                    success[10] += dataBaseService.deleteEmail(p.getpId()); // emailRepo
                    success[11] += dataBaseService.deleteOfficer(p.getpId()); // officerRepo
                    success[12] += dataBaseService.deletePerson(p.getpId()); // personRepo
                }
            }
            System.out.println("Array elements: " + Arrays.toString(success));
        } catch (Exception e) {
            System.out.println(e);
        }
//        else {
//            dialogue.setTitle("Deleting Membership MSID:" + msId);
//        }

    }
}

//    private void deleteMembership() {
//        Dialogue_CustomErrorMessage dialogue = new Dialogue_CustomErrorMessage(true);
//        if (slipRepository.existsSlipWithMsId(msId)) {
//            dialogue.setTitle("Looks like we have a problem");
//            dialogue.setText("You must re-assign their slip before deleting this membership");
//            return;
//        } else {
//            dialogue.setTitle("Deleting Membership MSID:" + msId);
//        }
//        Task<Object> task = new Task<>() {
//            @Override
//            protected Object call() throws Exception {
//                setMessage("Deleting boats", dialogue);
//                boatRepository.deleteBoatOwner(msId);
//                setMessage("Deleting notes", dialogue);
//                memoRepository.deleteMemos(msId);
//                setMessage("Deleting Invoices and Payments", dialogue);
//                invoiceRepository.deleteAllPaymentsAndInvoicesByMsId(msId);
//                setMessage("Deleting wait_list entries", dialogue);
//                slipRepository.deleteWaitList(msId);
//                setMessage("Deleting membership hash", dialogue);
//                membershipRepository.deleteFormMsIdHash(msId);
//                setMessage("Deleting history",dialogue);
//                membershipIdRepository.deleteMembershipId(msId);
//                List<PersonDTO> people = personRepository.getPeople(msId);
//                setMessage("Deleting membership", dialogue);
//                membershipRepository.deleteMembership(msId);
//                setMessage("Deleting people", dialogue);
//                for (PersonDTO p : people) {
//                    phoneRepository.deletePhones(p.getpId());
//                    emailRepository.deleteEmail(p.getpId());
//                    officerRepository.delete(p.getpId());
//                    personRepository.deletePerson(p.getpId());
//                }
//
//                return null;
//            }
//        };
//        task.setOnSucceeded(succeed -> {
//                    Launcher.removeMembershipRow(msId);
//                    Launcher.closeActiveTab();
//                    BaseApplication.logger.info("Deleted membership msid: " + msId);
//                    dialogue.setText("Sucessfully deleted membership MSID: " + msId);
//                }
//        );
//        new Thread(task).start();
//
//    }