package org.ecsail.repository.interfaces;

import org.ecsail.fx.AwardDTOFx;
import org.ecsail.fx.PersonFx;

import java.util.List;

public interface AwardRepository {
    List<AwardDTOFx> getAwards(PersonFx p);
    List<AwardDTOFx> getAwards();
    int update(AwardDTOFx o);
    int insert(AwardDTOFx o);

    int delete(AwardDTOFx awardDTO);

    int deleteAwards(int pId);
}
