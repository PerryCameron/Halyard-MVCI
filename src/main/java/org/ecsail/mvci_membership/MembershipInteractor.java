package org.ecsail.mvci_membership;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.connection.Connections;
import org.ecsail.dto.*;
import org.ecsail.interfaces.Messages;
import org.ecsail.repository.implementations.*;
import org.ecsail.repository.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MembershipInteractor {
    private final MembershipModel membershipModel;
    private static final Logger logger = LoggerFactory.getLogger(MembershipInteractor.class);
    private final PersonRepository peopleRepo;
    private final PhoneRepository phoneRepo;
    private final EmailRepository emailRepo;
    private final AwardRepository awardRepo;
    private final OfficerRepository officerRepo;


    public MembershipInteractor(MembershipModel membershipModel, Connections connections) {
        this.membershipModel = membershipModel;
        peopleRepo = new PersonRepositoryImpl(connections.getDataSource());
        phoneRepo = new PhoneRepositoryImpl(connections.getDataSource());
        emailRepo = new EmailRepositoryImpl(connections.getDataSource());
        awardRepo = new AwardRepositoryImpl(connections.getDataSource());
        officerRepo = new OfficerRepositoryImpl(connections.getDataSource());
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

    public void getSlipInfo(MembershipListDTO ml) {
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
