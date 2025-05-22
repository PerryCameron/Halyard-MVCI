package org.ecsail.repository.interfaces;


import org.ecsail.fx.PersonFx;

import java.util.List;

public interface PersonRepository {

//    List<PersonDTO> getPeople(int ms_id);

    List<PersonFx> getActivePeopleByMsId(int ms_id);

    int update(PersonFx personDTO);

    int insert(PersonFx personDTO);

    int delete(PersonFx personDTO);

    PersonFx insertPerson(PersonFx person);

    List<PersonFx> getPeople(int ms_id);

    int deletePerson(int p_id);

    int deleteUserAuthRequest(int pId);
}
