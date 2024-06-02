package org.ecsail.mvci_new_membership;

import javafx.application.Platform;
import org.ecsail.connection.Connections;
import org.ecsail.dto.MembershipIdDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.repository.implementations.MembershipIdRepositoryImpl;
import org.ecsail.repository.implementations.MembershipRepositoryImpl;
import org.ecsail.repository.implementations.PersonRepositoryImpl;
import org.ecsail.repository.interfaces.MembershipIdRepository;
import org.ecsail.repository.interfaces.MembershipRepository;
import org.ecsail.repository.interfaces.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;

public class NewMembershipInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(NewMembershipInteractor.class);
    private final NewMembershipModel newMembershipModel;
    private final MembershipRepository memRepo;
    private final PersonRepository personRepo;
    private final MembershipIdRepository memberIdRepo;

    public NewMembershipInteractor(NewMembershipModel newMembershipModel, Connections connections) {
        this.newMembershipModel = newMembershipModel;
        this.memRepo = new MembershipRepositoryImpl(connections.getDataSource());
        this.personRepo = new PersonRepositoryImpl(connections.getDataSource());
        this.memberIdRepo = new MembershipIdRepositoryImpl(connections.getDataSource());
    }

    public MembershipListDTO getMembershipList() {
        return null;
    }

    public void getNextAvailableId() {
        int nextNumber = memberIdRepo.getMembershipIdForNewestMembership() + 1;
        Platform.runLater(() -> {
        newMembershipModel.setTabMessage("Creating new membership");
        newMembershipModel.setTabMessage("Setting new membership number to: " + nextNumber);
        });
    }

    public void createMembershipObject() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        MembershipListDTO membershipListDTO = memRepo.insertMembership(
                new MembershipListDTO(date, "FM", Year.now().getValue()));
        membershipListDTO.setMembershipId(newMembershipModel.getMembershipId());
        Platform.runLater(() -> {
            newMembershipModel.setTabMessage("Creating Membership object and inserted it into the database");
            newMembershipModel.setMembership(membershipListDTO);
        });
    }

    public void createPrimaryMember() {
        PersonDTO personDTO = personRepo.insertPerson(
                new PersonDTO(newMembershipModel.getMembership().getMsId(), 1, true));
        Platform.runLater(() -> {
            newMembershipModel.setTabMessage("Creating Primary Member for membership");
            newMembershipModel.getMembership().setPId(personDTO.getpId());
        });
    }

    public void updateMembership() {
        int ok = memRepo.update(newMembershipModel.getMembership());
        if(ok == 1)
        Platform.runLater(() -> {
            newMembershipModel.setTabMessage("Updated Membership with primary member");
        });
    }

    public void createMembershipIdRow() {
        MembershipIdDTO membershipIdDTO = new MembershipIdDTO();
        membershipIdDTO.setFiscalYear(Year.now().getValue());
        membershipIdDTO.setMsId(newMembershipModel.getMembership().getMsId());
        membershipIdDTO.setMemType(newMembershipModel.getMembership().getMemType());
        membershipIdDTO.setMembershipId(newMembershipModel.getMembership().getMembershipId());


//                new MembershipIdDTO(String.valueOf(Year.now().getValue()), newMembershipModel.getMembership().getMsId(), newMembershipModel.getMembershipId() + ""));
    }
}
