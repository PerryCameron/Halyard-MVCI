package org.ecsail.repository.interfaces;

import org.ecsail.dto.SlipDTO;
import org.ecsail.dto.SlipInfoDTO;
import org.ecsail.dto.SlipStructureDTO;

import java.util.List;

public interface SlipRepository {
    SlipDTO getSlip(int msId);

    List<SlipInfoDTO> getSlipInfo();

    List<SlipStructureDTO> getSlipStructure();

    boolean existsSlipWithMsId(int msId);

    int deleteWaitList(int msId);
}
