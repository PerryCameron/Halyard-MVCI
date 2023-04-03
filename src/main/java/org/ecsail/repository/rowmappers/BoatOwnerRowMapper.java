package org.ecsail.repository.rowmappers;


import org.ecsail.dto.BoatOwnerDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BoatOwnerRowMapper implements RowMapper<BoatOwnerDTO> {

    @Override
    public BoatOwnerDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BoatOwnerDTO(
                rs.getInt("ms_id"),
                rs.getInt("boat_id"));
    }
}