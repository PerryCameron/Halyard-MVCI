package org.ecsail.repository.interfaces;

import org.ecsail.dto.AwardDTO;
import org.ecsail.dto.PersonDTOFx;

import java.util.List;

public interface AwardRepository {
    List<AwardDTO> getAwards(PersonDTOFx p);
    List<AwardDTO> getAwards();
    int update(AwardDTO o);
    int insert(AwardDTO o);

    int delete(AwardDTO awardDTO);

    int deleteAwards(int pId);
}
