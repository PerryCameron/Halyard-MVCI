package org.ecsail.repository.interfaces;

import org.ecsail.dto.PersonDTO;
import org.ecsail.dto.PhoneDTO;

import java.util.List;

public interface PhoneRepository {
    List<PhoneDTO> getPhoneByPid(int p_id);

    List<PhoneDTO> getPhoneByPerson(PersonDTO p);

    PhoneDTO getListedPhoneByType(PersonDTO p, String type);

    PhoneDTO getPhoneByType(String pid, String type);
    int update(PhoneDTO o);
    int delete(PhoneDTO o);
    int insert(PhoneDTO o);

    int deletePhones(int pId);
}
