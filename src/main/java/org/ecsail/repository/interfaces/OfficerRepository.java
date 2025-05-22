package org.ecsail.repository.interfaces;

import org.ecsail.dto.OfficerFx;
import org.ecsail.dto.OfficerWithNameDTO;
import org.ecsail.dto.PersonFx;

import java.util.List;

public interface OfficerRepository {
    int deleteOfficer(int pId);

    List<OfficerFx> getOfficers();
//    List<PDF_Object_Officer> getOfficersByYear(String selectedYear);
    List<OfficerFx> getOfficer(String field, int attribute);
    List<OfficerFx> getOfficer(PersonFx person);
    List<OfficerWithNameDTO> getOfficersWithNames(String type);
    int update(OfficerFx o);
    int insert(OfficerFx o);
    int delete(OfficerFx o);
}
