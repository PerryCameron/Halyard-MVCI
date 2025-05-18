package org.ecsail.repository.interfaces;

import org.ecsail.dto.PersonDTOFx;
import org.ecsail.dto.PhoneDTOFx;

import java.util.List;

public interface PhoneRepository {
    List<PhoneDTOFx> getPhoneByPid(int p_id);

    List<PhoneDTOFx> getPhoneByPerson(PersonDTOFx p);

    PhoneDTOFx getListedPhoneByType(PersonDTOFx p, String type);

    PhoneDTOFx getPhoneByType(String pid, String type);
    int update(PhoneDTOFx o);
    int delete(PhoneDTOFx o);
    int insert(PhoneDTOFx o);

    int deletePhones(int pId);
}
