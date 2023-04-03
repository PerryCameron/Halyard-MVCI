package org.ecsail.repository.interfaces;


import org.ecsail.dto.PersonDTO;

import java.util.List;

public interface PersonRepository {

//    List<PersonDTO> getPeople(int ms_id);

    List<PersonDTO> getActivePeopleByMsId(int ms_id);
//    List<PersonDTO> getActiveAuxBoats();
//    List<PersonDTO> getAllSailBoats();
//    List<PersonDTO> getAllAuxBoats();
//    List<PersonDTO> getAllBoats();
}
