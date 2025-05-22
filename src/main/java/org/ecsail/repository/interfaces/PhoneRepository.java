package org.ecsail.repository.interfaces;

import org.ecsail.fx.PersonFx;
import org.ecsail.fx.PhoneFx;

import java.util.List;

public interface PhoneRepository {
    List<PhoneFx> getPhoneByPid(int p_id);

    List<PhoneFx> getPhoneByPerson(PersonFx p);

    PhoneFx getListedPhoneByType(PersonFx p, String type);

    PhoneFx getPhoneByType(String pid, String type);
    int update(PhoneFx o);
    int delete(PhoneFx o);
    int insert(PhoneFx o);

    int deletePhones(int pId);
}
