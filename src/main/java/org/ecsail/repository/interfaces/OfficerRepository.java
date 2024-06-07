package org.ecsail.repository.interfaces;

import org.ecsail.dto.OfficerDTO;
import org.ecsail.dto.OfficerWithNameDTO;
import org.ecsail.dto.PersonDTO;

import java.util.List;

public interface OfficerRepository {
    int deleteOfficer(int pId);

    List<OfficerDTO> getOfficers();
//    List<PDF_Object_Officer> getOfficersByYear(String selectedYear);
    List<OfficerDTO> getOfficer(String field, int attribute);
    List<OfficerDTO> getOfficer(PersonDTO person);
    List<OfficerWithNameDTO> getOfficersWithNames(String type);
    int update(OfficerDTO o);
    int insert(OfficerDTO o);
    int delete(OfficerDTO o);
}
