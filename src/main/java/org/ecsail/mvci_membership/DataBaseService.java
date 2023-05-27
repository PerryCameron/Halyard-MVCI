package org.ecsail.mvci_membership;

import org.ecsail.dto.*;
import org.ecsail.interfaces.Messages;
import org.ecsail.repository.implementations.*;
import org.ecsail.repository.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import javax.sql.DataSource;

public class DataBaseService {

    private final MembershipModel membershipModel;
    private PersonRepository peopleRepo;
    private PhoneRepository phoneRepo;
    private EmailRepository emailRepo;
    private AwardRepository awardRepo;
    private OfficerRepository officerRepo;
    private MembershipIdRepository membershipIdRepo;
    private BoatRepository boatRepo;
    private static final Logger logger = LoggerFactory.getLogger(DataBaseService.class);


    public DataBaseService(DataSource dataSource, MembershipModel membershipModel) {
        this.membershipModel = membershipModel;
        peopleRepo = new PersonRepositoryImpl(dataSource);
        phoneRepo = new PhoneRepositoryImpl(dataSource);
        emailRepo = new EmailRepositoryImpl(dataSource);
        awardRepo = new AwardRepositoryImpl(dataSource);
        officerRepo = new OfficerRepositoryImpl(dataSource);
        membershipIdRepo = new MembershipIdRepositoryImpl(dataSource);
        boatRepo = new BoatRepositoryImpl(dataSource);
    }

    public Object insertRow(Object o) {
        boolean noError = true;
        try {
            if (o instanceof AwardDTO) return awardRepo.insert((AwardDTO) o);
            if (o instanceof EmailDTO) return emailRepo.insert((EmailDTO) o);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            noError = false;
        }
        checkTheCorrectNumberOfReturnedRows(noError);
        return null;
    }

    public void receiveMessage(Messages.MessageType messages, Object o) {
        switch (messages) {
            case DELETE -> deleteObject(o);
            case UPDATE -> updateObject(o);
            case NONE -> {
            }
            case CHANGE_MEMBER_TYPE -> {
            }
            case REMOVE_MEMBER_FROM_MEMBERSHIP -> {
            }
            case DELETE_MEMBER_FROM_DATABASE -> {
            }
            case RELEASE_SUBLEASE -> {
            }
            case REASSIGN_SLIP -> {
            }
            case SUBLEASE_SLIP -> {
            }
            case SET_WAIT_LIST -> {
            }
        }
    }

    private void deleteObject(Object o) {
        int rowsUpdated = 0;
        try {
            if (o instanceof AwardDTO) rowsUpdated = awardRepo.delete((AwardDTO) o);
            if (o instanceof EmailDTO) rowsUpdated = emailRepo.delete((EmailDTO) o);

        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            // TODO do more stuff with this
        }
        checkTheCorrectNumberOfReturnedRows(rowsUpdated == 1);
    }

    private int updateObject(Object o) {
        int rowsUpdated = 0;
        try {
            if (o instanceof PersonDTO) rowsUpdated = peopleRepo.updatePerson((PersonDTO) o);
            if (o instanceof PhoneDTO) rowsUpdated = phoneRepo.updatePhone((PhoneDTO) o);
            if (o instanceof EmailDTO) rowsUpdated = emailRepo.updateEmail((EmailDTO) o);
            if (o instanceof AwardDTO) rowsUpdated = awardRepo.updateAward((AwardDTO) o);
            if (o instanceof OfficerDTO) rowsUpdated = officerRepo.updateOfficer((OfficerDTO) o);
            if (o instanceof BoatDTO) rowsUpdated = boatRepo.updateBoat((BoatDTO) o);
            if (o instanceof MembershipIdDTO) rowsUpdated = membershipIdRepo.updateId((MembershipIdDTO) o);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            // TODO do more stuff with this
        }
        checkTheCorrectNumberOfReturnedRows(rowsUpdated == 1);
        return rowsUpdated;
    }

    protected void checkTheCorrectNumberOfReturnedRows(boolean returnOk) { // updates status lights
        if(returnOk) membershipModel.getMainModel().getLightAnimationMap().get("receiveSuccess").playFromStart();
        else membershipModel.getMainModel().getLightAnimationMap().get("receiveError").playFromStart();
    }


}
