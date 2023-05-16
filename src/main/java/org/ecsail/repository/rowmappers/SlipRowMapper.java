package org.ecsail.repository.rowmappers;

import org.ecsail.dto.SlipDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SlipRowMapper implements RowMapper<SlipDTO> {

    @Override
    public SlipDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new SlipDTO(rs.getInt("SLIP_ID")
                , rs.getInt("ms_id")
                , rs.getString("slip_num")
                , rs.getInt("subleased_to")
                , rs.getString("ALT_TEXT"));
    }
}
