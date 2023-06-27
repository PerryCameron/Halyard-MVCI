package org.ecsail.mvci_membership;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.dto.*;
import org.ecsail.interfaces.SlipUser;
import org.ecsail.repository.implementations.*;
import org.ecsail.repository.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import javax.sql.DataSource;
import java.util.List;

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
    }

    public void getPersonLists(MembershipListDTO ml) { // not on FX thread because lists added before UI is launched
        System.out.println("getPersonLists()");
        ObservableList<PersonDTO> personDTOS = null;
        try {
            personDTOS = FXCollections.observableArrayList(peopleRepo.getActivePeopleByMsId(ml.getMsId()));
            for (PersonDTO person : personDTOS) {
                person.setPhones(FXCollections.observableArrayList(phoneRepo.getPhoneByPid(person.getpId())));
                person.setEmail(FXCollections.observableArrayList(emailRepo.getEmail(person.getpId())));
                person.setAwards(FXCollections.observableArrayList(awardRepo.getAwards(person)));
                person.setOfficer(FXCollections.observableArrayList(officerRepo.getOfficer(person)));
            }
            retrievedFromIndicator(true);
            logger.info("Blink Green");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            retrievedFromIndicator(false);
        }
        membershipModel.setPeople(personDTOS);
        logger.info("set people, size: " + membershipModel.getPeople().size());
    }

    public void getIds(MembershipListDTO ml) {
        List<MembershipIdDTO> membershipIdDTOS = membershipIdRepo.getIds(ml.getMsId());
        try {
            Platform.runLater(() -> membershipModel.getMembership()
                    .setMembershipIdDTOS(FXCollections.observableArrayList(membershipIdDTOS)));
            retrievedFromIndicator(true);
            logger.info("Blink Green");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            retrievedFromIndicator(false);
        }
    }

    public void getBoats(MembershipListDTO ml) {
        try {
            List<BoatDTO> boats = boatRepo.getBoatsByMsId(ml.getMsId());
            Platform.runLater(() -> {
                membershipModel.getMembership()
                        .setBoatDTOS(FXCollections.observableArrayList(boats));
                retrievedFromIndicator(true);
            });
            logger.info("Blink Green");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            retrievedFromIndicator(false);
        }
    }

    public void getNotes(MembershipListDTO ml) {
        try {
            List<NotesDTO> notesDTOS = notesRepo.getMemosByMsId(ml.getMsId());
            Platform.runLater(() -> membershipModel.getMembership()
                    .setNotesDTOS(FXCollections.observableArrayList(notesDTOS)));
            retrievedFromIndicator(true);
            logger.info("Blink Green");
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            retrievedFromIndicator(false);
        }
    }

    public void getSlipInfo(MembershipListDTO ml) {
        SlipDTO slip = null;
        try {
            slip = slipRepo.getSlip(ml.getMsId());
            logger.info("Blink Green");
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

    public void receiveMessage(MembershipMessages.action messages, Object o) {
        switch (messages) {
            case DELETE -> delete(o);
            case UPDATE -> update(o);
            case INSERT -> insert(o);
            case NONE -> none(o);
            case CHANGE_MEMBER_TYPE -> changeMemberType(o);
            case REMOVE_MEMBER_FROM_MEMBERSHIP -> removeFromMembership(o);
            case DELETE_MEMBER_FROM_DATABASE -> removeFromDatabase(o);
            case RELEASE_SUBLEASE -> releaseSublease(o);
            case REASSIGN_SLIP -> reassignSlip(o);
            case SUBLEASE_SLIP -> subleaseSlip(o);
            case SET_WAIT_LIST -> setWaitList(o);
        }
    }

    private void removeFromDatabase(Object o) {
        System.out.println("Remove from Database " + o);
    }

    private void removeFromMembership(Object o) {
        System.out.println("Remove from membership " + o);
    }

    private void changeMemberType(Object o) {
        System.out.println("changeMemberType " + o);
    }

    private void none(Object o) {
        System.out.println("none " + o);
    }

    private void setWaitList(Object o) {
        System.out.println("Add to wait-list " + o);
    }

    private void subleaseSlip(Object o) {
        System.out.println("Sublease Slip " + o);
    }

    private void reassignSlip(Object o) {
        System.out.println("reassign Slip " + o);
    }

    private void releaseSublease(Object o) {
        System.out.println("release Sublease " + o);
    }


    private void delete(Object o) {
        int rowsUpdated = 0;
        try {
            if (o instanceof AwardDTO) rowsUpdated = awardRepo.delete((AwardDTO) o);
            if (o instanceof EmailDTO) rowsUpdated = emailRepo.delete((EmailDTO) o);
            if (o instanceof PhoneDTO) rowsUpdated = phoneRepo.delete((PhoneDTO) o);
            if (o instanceof OfficerDTO) rowsUpdated = officerRepo.delete((OfficerDTO) o);
            if (o instanceof BoatDTO) rowsUpdated = boatRepo.delete((BoatDTO) o);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            savedToIndicator(false);
        }
        savedToIndicator(rowsUpdated == 1);
    }

    public void insert(Object o) {
        int rowsUpdated = 0;
        try {
            if (o instanceof AwardDTO) rowsUpdated = awardRepo.insert((AwardDTO) o);
            if (o instanceof EmailDTO) rowsUpdated = emailRepo.insert((EmailDTO) o);
            if (o instanceof PhoneDTO) rowsUpdated = phoneRepo.insert((PhoneDTO) o);
            if (o instanceof OfficerDTO) rowsUpdated = officerRepo.insert((OfficerDTO) o);
            if (o instanceof BoatDTO) {
                BoatDTO boatDTO = (BoatDTO) o;
                rowsUpdated = boatRepo.insert(boatDTO);
                BoatOwnerDTO boatOwnerDTO = new BoatOwnerDTO(boatDTO.getMsId(), boatDTO.getBoatId());
                if(rowsUpdated == 1) rowsUpdated = boatRepo.insertOwner(boatOwnerDTO);
                else rowsUpdated = 0;
            }
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            savedToIndicator(false);
        }
        savedToIndicator(rowsUpdated == 1);
    }

    private void update(Object o) {
        int rowsUpdated = 0;
        try {
            if (o instanceof PersonDTO) rowsUpdated = peopleRepo.update((PersonDTO) o);
            if (o instanceof PhoneDTO) rowsUpdated = phoneRepo.update((PhoneDTO) o);
            if (o instanceof EmailDTO) rowsUpdated = emailRepo.update((EmailDTO) o);
            if (o instanceof AwardDTO) rowsUpdated = awardRepo.update((AwardDTO) o);
            if (o instanceof OfficerDTO) rowsUpdated = officerRepo.update((OfficerDTO) o);
            if (o instanceof BoatDTO) rowsUpdated = boatRepo.update((BoatDTO) o);
            if (o instanceof MembershipIdDTO) rowsUpdated = membershipIdRepo.update((MembershipIdDTO) o);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            savedToIndicator(false);
        }
        savedToIndicator(rowsUpdated == 1);
    }

    protected void savedToIndicator(boolean returnOk) { // updates status lights
        if(returnOk) membershipModel.getMainModel().getLightAnimationMap().get("receiveSuccess").playFromStart();
        else membershipModel.getMainModel().getLightAnimationMap().get("receiveError").playFromStart();
    }

    protected void retrievedFromIndicator(boolean returnOk) { // updates status lights
        if(returnOk) membershipModel.getMainModel().getLightAnimationMap().get("transmitSuccess").playFromStart();
        else membershipModel.getMainModel().getLightAnimationMap().get("transmitError").playFromStart();
    }
}
