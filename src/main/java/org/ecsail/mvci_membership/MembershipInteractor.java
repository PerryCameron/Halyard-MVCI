package org.ecsail.mvci_membership;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.connection.Connections;
import org.ecsail.dto.*;
import org.ecsail.interfaces.Messages;
import org.ecsail.interfaces.SlipUser;
import org.ecsail.repository.implementations.*;
import org.ecsail.repository.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class MembershipInteractor implements SlipUser {
    private final MembershipModel membershipModel;
    private static final Logger logger = LoggerFactory.getLogger(MembershipInteractor.class);
    private DataSource dataSource;
    private final PersonRepository peopleRepo;
    private final PhoneRepository phoneRepo;
    private final EmailRepository emailRepo;
    private final AwardRepository awardRepo;
    private final OfficerRepository officerRepo;
    private final MembershipIdRepository membershipIdRepo;


    public MembershipInteractor(MembershipModel membershipModel, Connections connections) {
        this.membershipModel = membershipModel;
        this.dataSource = connections.getDataSource();
        peopleRepo = new PersonRepositoryImpl(connections.getDataSource());
        phoneRepo = new PhoneRepositoryImpl(connections.getDataSource());
        emailRepo = new EmailRepositoryImpl(connections.getDataSource());
        awardRepo = new AwardRepositoryImpl(connections.getDataSource());
        officerRepo = new OfficerRepositoryImpl(connections.getDataSource());
        membershipIdRepo = new MembershipIdRepositoryImpl(connections.getDataSource());
    }

    public void getPersonLists(MembershipListDTO ml) { // not on FX thread because lists added before UI is launched
        ObservableList<PersonDTO> personDTOS = null;
        try {
            personDTOS = FXCollections.observableArrayList(peopleRepo.getActivePeopleByMsId(ml.getMsId()));
            for (PersonDTO person : personDTOS) {
                person.setPhones(FXCollections.observableArrayList(phoneRepo.getPhoneByPid(person.getP_id())));
                person.setEmail(FXCollections.observableArrayList(emailRepo.getEmail(person.getP_id())));
                person.setAwards(FXCollections.observableArrayList(awardRepo.getAwards(person)));
                person.setOfficer(FXCollections.observableArrayList(officerRepo.getOfficer(person)));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
            membershipModel.setPeople(personDTOS);
            logger.info("set people, size: " +membershipModel.getPeople().size());
    }

    public void getBoats(MembershipListDTO ml) {
        BoatRepository boatRepo = new BoatRepositoryImpl(dataSource);
        Platform.runLater(() -> {  // TODO need try catch blocks here
            membershipModel.getMembership().setBoatDTOS(FXCollections.observableArrayList(boatRepo.getBoatsByMsId(ml.getMsId())));
        });
    }

    public void getNotes(MembershipListDTO ml) {
        NotesRepository noteRepo = new NotesRepositoryImpl(dataSource);
        Platform.runLater(() -> {
            membershipModel.getMembership().setNotesDTOS(FXCollections.observableArrayList(noteRepo.getMemosByMsId(ml.getMsId())));
        });
    }

    public void getSlipInfo(MembershipListDTO ml) {
        SlipRepository slipRepository = new SlipRepositoryImpl(dataSource);
        Platform.runLater(() -> {
            logger.info("Slip is loaded");
            membershipModel.setSlip(slipRepository.getSlip(ml.getMsId()));
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

    protected void setListsLoaded(boolean isLoaded) {
        Platform.runLater(() -> {
            logger.debug("Lists are loaded");
            membershipModel.setListsLoaded(isLoaded);
        });
    }

    public void printPersonMessage(Messages.MessageType messages, Object o) {
        System.out.println(messages + " " + o + " " + membershipModel.getSelectedPerson());
    }



}
