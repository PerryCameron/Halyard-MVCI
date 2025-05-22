package org.ecsail.repository.interfaces;

import org.ecsail.fx.BoardPositionDTO;

import java.util.List;

public interface BoardPositionsRepository {
    List<BoardPositionDTO> getPositions();
    String getByIdentifier(String code);
    String getByName(String name);
}
