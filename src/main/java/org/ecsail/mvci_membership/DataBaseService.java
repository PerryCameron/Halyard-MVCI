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
    private final PersonRepository peopleRepo;
    private final PhoneRepository phoneRepo;
    private final EmailRepository emailRepo;
    private final AwardRepository awardRepo;
    private final OfficerRepository officerRepo;
    private final MembershipIdRepository membershipIdRepo;
    private final BoatRepository boatRepo;
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

    public void insert(Object o) {
        int rowsUpdated = 0;
        try {
            if (o instanceof AwardDTO) rowsUpdated = awardRepo.insert((AwardDTO) o);
            if (o instanceof EmailDTO) rowsUpdated = emailRepo.insert((EmailDTO) o);
            if (o instanceof PhoneDTO) rowsUpdated = phoneRepo.insert((PhoneDTO) o);
            if (o instanceof OfficerDTO) rowsUpdated = officerRepo.insert((OfficerDTO) o);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            illuminateStatusLight(false);
        }
        illuminateStatusLight(rowsUpdated == 1);
    }

    public void receiveMessage(Messages.MessageType messages, Object o) {
        switch (messages) {
            case DELETE -> delete(o);
            case UPDATE -> update(o);
            case INSERT -> insert(o);
//            case NONE -> {
//            }
//            case CHANGE_MEMBER_TYPE -> {
//            }
//            case REMOVE_MEMBER_FROM_MEMBERSHIP -> {
//            }
//            case DELETE_MEMBER_FROM_DATABASE -> {
//            }
//            case RELEASE_SUBLEASE -> {
//            }
//            case REASSIGN_SLIP -> {
//            }
//            case SUBLEASE_SLIP -> {
//            }
//            case SET_WAIT_LIST -> {
//            }
        }
    }

    private void delete(Object o) {
        int rowsUpdated = 0;
        try {
            if (o instanceof AwardDTO) rowsUpdated = awardRepo.delete((AwardDTO) o);
            if (o instanceof EmailDTO) rowsUpdated = emailRepo.delete((EmailDTO) o);
            if (o instanceof PhoneDTO) rowsUpdated = phoneRepo.delete((PhoneDTO) o);
            if (o instanceof OfficerDTO) rowsUpdated = officerRepo.delete((OfficerDTO) o);
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            illuminateStatusLight(false);
        }
        illuminateStatusLight(rowsUpdated == 1);
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
            // TODO do more stuff with this
        }
        System.out.println(rowsUpdated);
        illuminateStatusLight(rowsUpdated == 1);
    }

    protected void illuminateStatusLight(boolean returnOk) { // updates status lights
        if(returnOk) membershipModel.getMainModel().getLightAnimationMap().get("receiveSuccess").playFromStart();
        else membershipModel.getMainModel().getLightAnimationMap().get("receiveError").playFromStart();
    }
}
