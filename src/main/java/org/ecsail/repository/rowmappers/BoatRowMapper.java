package org.ecsail.repository.rowmappers;


import org.ecsail.dto.BoatDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BoatRowMapper implements RowMapper<BoatDTO> {

    @Override
    public BoatDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BoatDTO(
        rs.getInt("boat_id"),
                0, // this is not in the database table
                rs.getString("manufacturer"),
                rs.getString("manufacture_year"),
                rs.getString("registration_num"),
                rs.getString("model"),
                rs.getString("boat_name"),
                rs.getString("sail_number"),
                rs.getBoolean("has_trailer"),
                rs.getString("length"),
                rs.getString("weight"),
                rs.getString("keel"),
                rs.getString("phrf"),
                rs.getString("draft"),
                rs.getString("beam"),
                rs.getString("lwl"),
                rs.getBoolean("aux"));
    }
}