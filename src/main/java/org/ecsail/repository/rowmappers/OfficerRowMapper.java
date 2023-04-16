package org.ecsail.repository.rowmappers;

import org.ecsail.dto.OfficerDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OfficerRowMapper implements RowMapper<OfficerDTO> {
    @Override
    public OfficerDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new OfficerDTO(
                rs.getInt("O_ID"),
                rs.getInt("p_id"),
                rs.getString("BOARD_YEAR"), // beginning of board term
                rs.getString("off_type"),
                rs.getString("off_year"));
    }
}
