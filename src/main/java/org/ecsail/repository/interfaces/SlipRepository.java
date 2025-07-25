package org.ecsail.repository.interfaces;

import org.ecsail.fx.SlipFx;
import org.ecsail.fx.SlipInfoDTO;
import org.ecsail.fx.SlipStructureDTO;

import java.util.List;

public interface SlipRepository {
    SlipFx getSlip(int msId);

    List<SlipInfoDTO> getSlipInfo();

    List<SlipStructureDTO> getSlipStructure();

    boolean existsSlipWithMsId(int msId);

    int deleteWaitList(int msId);
}
