package org.ecsail.repository.implementations;

import org.ecsail.dto.BoardPositionDTO;
import org.ecsail.repository.interfaces.BoardPositionsRepository;
import org.ecsail.repository.rowmappers.BoardPositionsRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class BoardPositionsRepositoryImpl implements BoardPositionsRepository {
    private final JdbcTemplate template;

    public BoardPositionsRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<BoardPositionDTO> getPositions() {
        String query = "SELECT * FROM board_positions";
        return template.query(query,new BoardPositionsRowMapper());
    }

    @Override
    public String getByIdentifier(String code) {
        return null;
    }

    @Override
    public String getByName(String name) {
        return null;
    }
}
