package org.ecsail.mvci_membership;

import org.ecsail.dto.EmailDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.dto.PhoneDTO;
import org.ecsail.interfaces.Messages;
import org.ecsail.repository.implementations.*;
import org.ecsail.repository.interfaces.*;

import javax.sql.DataSource;
import java.util.function.Predicate;

public class DataBaseService {

    private final MembershipModel membershipModel;
    private PersonRepository peopleRepo;
    private PhoneRepository phoneRepo;
    private EmailRepository emailRepo;
    private AwardRepository awardRepo;
    private OfficerRepository officerRepo;
    private MembershipIdRepository membershipIdRepo;

    public DataBaseService(DataSource dataSource, MembershipModel membershipModel) {
        this.membershipModel = membershipModel;
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
            case UPDATE -> { return updateObject(o); }
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
        int rowsUpdated = 0;
        if(o instanceof PersonDTO) rowsUpdated = peopleRepo.updatePerson((PersonDTO) o);
        if(o instanceof PhoneDTO) rowsUpdated = phoneRepo.updatePhone((PhoneDTO) o);
        if(o instanceof EmailDTO) rowsUpdated = emailRepo.updateEmail((EmailDTO) o);
        Predicate<Integer> isOneRow = number -> number == 1;
        checkTheCorrectNumberOfReturnedRows(rowsUpdated, isOneRow);
        return rowsUpdated;
    }

    protected void checkTheCorrectNumberOfReturnedRows(int rowsUpdated, Predicate<Integer> condition) { // updates status lights
        if(condition.test(rowsUpdated)) membershipModel.getMainModel().getLightAnimationMap().get("receiveSuccess").playFromStart();
        else membershipModel.getMainModel().getLightAnimationMap().get("receiveError").playFromStart();
    }


}
