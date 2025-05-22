package org.ecsail.repository.interfaces;

import org.ecsail.fx.SlipDTOFx;
import org.ecsail.fx.SlipInfoDTO;
import org.ecsail.fx.SlipStructureDTO;

import java.util.List;

public interface SlipRepository {
    SlipDTOFx getSlip(int msId);

    List<SlipInfoDTO> getSlipInfo();

    List<SlipStructureDTO> getSlipStructure();

    boolean existsSlipWithMsId(int msId);

    int deleteWaitList(int msId);
}
