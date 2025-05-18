package org.ecsail.repository.interfaces;

import org.ecsail.dto.AwardDTOFx;
import org.ecsail.dto.PersonDTOFx;

import java.util.List;

public interface AwardRepository {
    List<AwardDTOFx> getAwards(PersonDTOFx p);
    List<AwardDTOFx> getAwards();
    int update(AwardDTOFx o);
    int insert(AwardDTOFx o);

    int delete(AwardDTOFx awardDTO);

    int deleteAwards(int pId);
}
