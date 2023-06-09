package org.ecsail.mvci_membership;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import org.ecsail.dto.*;
import org.ecsail.interfaces.SlipUser;
import org.ecsail.repository.implementations.*;
import org.ecsail.repository.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import javax.sql.DataSource;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DataBaseService {
    private final MembershipModel membershipModel;
    private final PersonRepository peopleRepo;
    private final PhoneRepository phoneRepo;
    private final EmailRepository emailRepo;
    private final AwardRepository awardRepo;
    private final OfficerRepository officerRepo;
    private final MembershipIdRepository membershipIdRepo;
    private final BoatRepository boatRepo;
    private static final Logger logger = LoggerFactory.getLogger(DataBaseService.class);
    private final SlipRepository slipRepo;
    private final NotesRepository notesRepo;
    private final MembershipRepository membershipRepo;


    public DataBaseService(DataSource dataSource, MembershipModel membershipModel) {
        this.membershipModel = membershipModel;
        peopleRepo = new PersonRepositoryImpl(dataSource);
        phoneRepo = new PhoneRepositoryImpl(dataSource);
        emailRepo = new EmailRepositoryImpl(dataSource);
        awardRepo = new AwardRepositoryImpl(dataSource);
        officerRepo = new OfficerRepositoryImpl(dataSource);
        membershipIdRepo = new MembershipIdRepositoryImpl(dataSource);
        boatRepo = new BoatRepositoryImpl(dataSource);
        slipRepo = new SlipRepositoryImpl(dataSource);
        notesRepo = new NotesRepositoryImpl(dataSource);
        membershipRepo = new MembershipRepositoryImpl(dataSource);
    }

    public void queryForList(Runnable task) {
        try {
            task.run();
            retrievedFromIndicator(true);
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            retrievedFromIndicator(false);
            logger.error("DataAccessException: " + dae.getMessage());
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            retrievedFromIndicator(false);
            logger.error("NullPointerException: " + npe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retrievedFromIndicator(false);
            logger.error("Exception: " + e.getMessage());
        }
    }

    public void getPersonLists(MembershipListDTO ml) { // not on FX thread because lists added before UI is launched
        queryForList(() -> {
            List<PersonDTO> personDTOS = peopleRepo.getActivePeopleByMsId(ml.getMsId());
            for (PersonDTO person : personDTOS) {
                person.setPhones(FXCollections.observableArrayList(phoneRepo.getPhoneByPid(person.getpId())));
                person.setEmail(FXCollections.observableArrayList(emailRepo.getEmail(person.getpId())));
                person.setAwards(FXCollections.observableArrayList(awardRepo.getAwards(person)));
                person.setOfficer(FXCollections.observableArrayList(officerRepo.getOfficer(person)));
            }
            membershipModel.setPeople(FXCollections.observableArrayList(personDTOS));
        });
    }

    public void getIds(MembershipListDTO ml) {
        queryForList(() -> {
            List<MembershipIdDTO> membershipIdDTOS = membershipIdRepo.getIds(ml.getMsId());
            Platform.runLater(() -> {
                membershipModel.getMembership().setMembershipIdDTOS(FXCollections.observableArrayList(membershipIdDTOS));
            });
        });
    }

    public void getBoats(MembershipListDTO ml) {
        queryForList(() -> {
            List<BoatDTO> boats = boatRepo.getBoatsByMsId(ml.getMsId());
            Platform.runLater(() -> {
                membershipModel.getMembership()
                        .setBoatDTOS(FXCollections.observableArrayList(boats));
                retrievedFromIndicator(true);
            });
        });
    }

    public void getNotes(MembershipListDTO ml) {
        queryForList(() -> {
            List<NotesDTO> notesDTOS = notesRepo.getMemosByMsId(ml.getMsId());
            Platform.runLater(() -> membershipModel.getMembership()
                    .setNotesDTOS(FXCollections.observableArrayList(notesDTOS)));
            retrievedFromIndicator(true);
        });
    }

    public void getSlipInfo(MembershipListDTO ml) {
        SlipDTO slip = null;
        try {
            slip = slipRepo.getSlip(ml.getMsId());
            retrievedFromIndicator(true);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            retrievedFromIndicator(false);
        }
        SlipDTO finalSlip = slip;
        logger.info("Slip is loaded");
        Platform.runLater(() -> {
            membershipModel.setSlip(finalSlip);
            // member does not own a slip
            if (membershipModel.getSlip().getMs_id() == 0) membershipModel.setSlipRelationStatus(SlipUser.slip.noSlip);
                // member owns a slip
            else if (membershipModel.getSlip().getMs_id() == membershipModel.getMembership().getMsId()) {
                // member owns slip and is not subleasing
                if(membershipModel.getSlip().getSubleased_to() == 0) membershipModel.setSlipRelationStatus(SlipUser.slip.owner);
                    // member owns slip but is subleasing
                else setOwnAndSublease();
                // member does not own but is subleasing
            } else setSubLeaser();
        });
    }

    private void setSubLeaser() {
        membershipModel.setSlipRelationStatus(SlipUser.slip.subLeaser);
        // gets the current id of the slip owner
        membershipModel.setMembershipId(String.valueOf(membershipIdRepo.getCurrentId(membershipModel.getSlip().getMs_id()).getMembership_id()));
    }

    private void setOwnAndSublease() {
        membershipModel.setSlipRelationStatus(SlipUser.slip.ownAndSublease);
        // gets the id of the subLeaser for the current year
        membershipModel.setMembershipId(String.valueOf(membershipIdRepo.getCurrentId(membershipModel.getSlip().getSubleased_to()).getMembership_id()));
    }

    protected void savedToIndicator(boolean returnOk) { // updates status lights
        if(returnOk) membershipModel.getMainModel().getLightAnimationMap().get("receiveSuccess").playFromStart();
        else membershipModel.getMainModel().getLightAnimationMap().get("receiveError").playFromStart();
    }

    protected void retrievedFromIndicator(boolean returnOk) { // updates status lights
        if(returnOk) membershipModel.getMainModel().getLightAnimationMap().get("transmitSuccess").playFromStart();
        else membershipModel.getMainModel().getLightAnimationMap().get("transmitError").playFromStart();
    }

    public void executeQuery(Supplier<Integer> operation) {
        try {
            int rowsUpdated = operation.get();
            savedToIndicator(rowsUpdated == 1);
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            savedToIndicator(false);
            logger.error("DataAccessException: " + dae.getMessage());
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            savedToIndicator(false);
            logger.error("NullPointerException: " + npe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            savedToIndicator(false);
            logger.error("Exception: " + e.getMessage());
        }
    }

    protected void updateMembershipList() {
        System.out.println("Update MembershipLlist");
        executeQuery(() -> membershipRepo.update(membershipModel.getMembership()));
    }

    protected void updateAward() {
        System.out.println("Update Award");
        executeQuery(() -> awardRepo.update(membershipModel.getSelectedAward()));
    }

    protected void updateBoat() {
        System.out.println("Update Boat");
        executeQuery(() -> boatRepo.update(membershipModel.getSelectedBoat()));
    }

    protected void insertBoat() {
        System.out.println("Insert Boat");
        executeQuery(() -> boatRepo.insert(membershipModel.getSelectedBoat()));
    }

    protected void deleteBoat() {
        System.out.println("Delete Boat");
//        executeQuery(() -> boatRepo.delete(membershipModel.getSelectedBoat()));
    }

    protected void updateEmail() {
        System.out.println("Update Email");
        executeQuery(() -> emailRepo.update(membershipModel.getSelectedEmail()));
    }

    protected void updateMembershipId() {
        System.out.println("Update MembershipId");
        executeQuery(() -> membershipIdRepo.update(membershipModel.getSelectedMembershipId()));
    }

    protected void updateNote() {
        System.out.println("updateNote:"); // TODO this is triggering when you scroll the tableview (wierd)
        System.out.println(membershipModel.getSelectedNote());
//        executeQuery(() -> notesRepo.update(membershipModel.getSelectedNote()));
    }

    protected void changeMemberType() {
        System.out.println("changeMemberType");
    }

    protected void removeMemberFromMembership() {
        System.out.println("remove from membership");
    }

    public void updatePhone() {
        System.out.println("Update Phone");
        executeQuery(() -> phoneRepo.update(membershipModel.getSelectedPhone()));
    }

    public void insertAward() {
        System.out.println("Insert Award");
        executeQuery(() -> awardRepo.insert(membershipModel.getSelectedAward()));
    }

    public void deletePerson() {
        System.out.println("Delete Person");
    }

    public void movePerson() {
        System.out.println("Move Person");
    }

    public void deleteAward() {
        System.out.println("Delete Award");
    }

    public void deleteEmail() {
        System.out.println("Delete Email");
    }

    public void deleteMembershipId() {
        System.out.println("Delete Membership");
    }

    public void deleteNote() {
        System.out.println("Delete Note");
    }

    public void deleteOfficer() {
        System.out.println("Delete Officer");
    }

    public void deletePhone() {
        System.out.println("Delete Phone");
    }

    public void updateOfficer() {
        System.out.println("Update Officer");
    }

    public void updatePerson() {
        System.out.println("Update Person");
    }

    public void insertEmail() {
        System.out.println("Insert Email");
    }

    public void insertMembershipId() {
        System.out.println("Insert MembershipId");
    }

    public void insertNote() {
        System.out.println("Insert Note");
    }

    public void insertOfficer() {
        System.out.println("Insert Officer");
    }

    public void insertPerson() {
        System.out.println("Insert Person");
    }

    public void insertPhone() {
        System.out.println("Insert Phone");
    }
}
