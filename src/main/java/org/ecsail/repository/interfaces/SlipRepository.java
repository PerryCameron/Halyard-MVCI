package org.ecsail.repository.interfaces;

import org.ecsail.dto.SlipDTO;

public interface SlipRepository {
    SlipDTO getSlip(int msId);
}
