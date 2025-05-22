package org.ecsail.repository.interfaces;

import org.ecsail.fx.OfficerFx;
import org.ecsail.fx.OfficerWithNameDTO;
import org.ecsail.fx.PersonFx;

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
