package org.ecsail.repository.interfaces;


import org.ecsail.dto.PersonDTOFx;

import java.util.List;

public interface PersonRepository {

//    List<PersonDTO> getPeople(int ms_id);

    List<PersonDTOFx> getActivePeopleByMsId(int ms_id);

    int update(PersonDTOFx personDTO);

    int insert(PersonDTOFx personDTO);

    int delete(PersonDTOFx personDTO);

    PersonDTOFx insertPerson(PersonDTOFx person);

    List<PersonDTOFx> getPeople(int ms_id);

    int deletePerson(int p_id);

    int deleteUserAuthRequest(int pId);
}
