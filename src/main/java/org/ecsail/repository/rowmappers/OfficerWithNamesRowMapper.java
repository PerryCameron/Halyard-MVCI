package org.ecsail.repository.rowmappers;

import org.ecsail.dto.OfficerWithNameDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OfficerWithNamesRowMapper implements RowMapper<OfficerWithNameDTO> {

    @Override
    public OfficerWithNameDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new OfficerWithNameDTO(
                rs.getString("L_NAME"),
                rs.getString("f_name"),
                rs.getString("off_year")
        );
    }
}
