package org.ecsail.repository.interfaces;

import org.ecsail.dto.OfficerDTOFx;
import org.ecsail.dto.OfficerWithNameDTO;
import org.ecsail.dto.PersonDTOFx;

import java.util.List;

public interface OfficerRepository {
    int deleteOfficer(int pId);

    List<OfficerDTOFx> getOfficers();
//    List<PDF_Object_Officer> getOfficersByYear(String selectedYear);
    List<OfficerDTOFx> getOfficer(String field, int attribute);
    List<OfficerDTOFx> getOfficer(PersonDTOFx person);
    List<OfficerWithNameDTO> getOfficersWithNames(String type);
    int update(OfficerDTOFx o);
    int insert(OfficerDTOFx o);
    int delete(OfficerDTOFx o);
}
