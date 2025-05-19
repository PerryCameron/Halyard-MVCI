package org.ecsail.repository.interfaces;

import org.ecsail.dto.SlipDTOFx;
import org.ecsail.dto.SlipInfoDTO;
import org.ecsail.dto.SlipStructureDTO;

import java.util.List;

public interface SlipRepository {
    SlipDTOFx getSlip(int msId);

    List<SlipInfoDTO> getSlipInfo();

    List<SlipStructureDTO> getSlipStructure();

    boolean existsSlipWithMsId(int msId);

    int deleteWaitList(int msId);
}
