package org.ecsail.repository.interfaces;

import org.ecsail.dto.BoardPositionDTO;

import java.util.List;

public interface BoardPositionsRepository {
    List<BoardPositionDTO> getPositions();
    String getByIdentifier(String code);
    String getByName(String name);
}
