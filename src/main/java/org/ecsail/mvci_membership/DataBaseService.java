package org.ecsail.mvci_membership;

import org.ecsail.dto.PersonDTO;
import org.ecsail.interfaces.Messages;
import org.ecsail.repository.implementations.*;
import org.ecsail.repository.interfaces.*;

import javax.sql.DataSource;

public class DataBaseService {

    private PersonRepository peopleRepo;
    private PhoneRepository phoneRepo;
    private EmailRepository emailRepo;
    private AwardRepository awardRepo;
    private OfficerRepository officerRepo;
    private MembershipIdRepository membershipIdRepo;

    public DataBaseService(DataSource dataSource) {
        peopleRepo = new PersonRepositoryImpl(dataSource);
        phoneRepo = new PhoneRepositoryImpl(dataSource);
        emailRepo = new EmailRepositoryImpl(dataSource);
        awardRepo = new AwardRepositoryImpl(dataSource);
        officerRepo = new OfficerRepositoryImpl(dataSource);
        membershipIdRepo = new MembershipIdRepositoryImpl(dataSource);
    }

    public int receiveMessage(Messages.MessageType messages, Object o) {
        switch (messages) {
            case INSERT -> {
            }
            case DELETE -> {
            }
            case UPDATE -> {
                return updateObject(o);
            }
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

        return 0;
    }

    private int updateObject(Object o) {
        if(o instanceof PersonDTO) return peopleRepo.updatePerson((PersonDTO) o);
        else return 0;
    }


}
