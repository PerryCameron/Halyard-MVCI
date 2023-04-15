package org.ecsail.mvci_membership;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.connection.Connections;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.mvci_roster.RosterInteractor;
import org.ecsail.repository.implementations.PersonRepositoryImpl;
import org.ecsail.repository.interfaces.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MembershipInteractor {
    private final MembershipModel membershipModel;
    private static final Logger logger = LoggerFactory.getLogger(MembershipInteractor.class);
    private final PersonRepository peopleRepo;

    public MembershipInteractor(MembershipModel membershipModel, Connections connections) {
        this.membershipModel = membershipModel;
        peopleRepo = new PersonRepositoryImpl(connections.getDataSource());
    }

    public void getPeople(MembershipListDTO ml) {
        ObservableList<PersonDTO> people = FXCollections.observableArrayList(peopleRepo.getActivePeopleByMsId(ml.getMsId()));
        Platform.runLater(() -> {
            membershipModel.setPeople(people);
            logger.info("set people, size: " +membershipModel.getPeople().size());
        });
    }

    protected void setListsLoaded(boolean isLoaded) {
        Platform.runLater(() -> membershipModel.setListsLoaded(isLoaded));
    }
}
