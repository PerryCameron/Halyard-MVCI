package org.ecsail.repository.interfaces;

import org.ecsail.fx.AwardFx;
import org.ecsail.fx.PersonFx;

import java.util.List;

public interface AwardRepository {
    List<AwardFx> getAwards(PersonFx p);
    List<AwardFx> getAwards();
    int update(AwardFx o);
    int insert(AwardFx o);

    int delete(AwardFx awardDTO);

    int deleteAwards(int pId);
}
