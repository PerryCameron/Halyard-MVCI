package org.ecsail.repository.interfaces;


import org.ecsail.dto.PersonDTO;

import java.util.List;

public interface PersonRepository {

//    List<PersonDTO> getPeople(int ms_id);

    List<PersonDTO> getActivePeopleByMsId(int ms_id);

    int update(PersonDTO personDTO);

    int insert(PersonDTO personDTO);

    int delete(PersonDTO personDTO);

    PersonDTO insertPerson(PersonDTO person);

    List<PersonDTO> getPeople(int ms_id);

    int deletePerson(int p_id);

    int deleteUserAuthRequest(int pId);
}
