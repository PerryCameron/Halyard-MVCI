package org.ecsail.repository.rowmappers;


import org.ecsail.dto.SlipInfoDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SlipInfoRowMapper implements RowMapper<SlipInfoDTO> {
    @Override
    public SlipInfoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new SlipInfoDTO(
                rs.getInt("owner_id"),
                rs.getString("owner_first_name"),
                rs.getString("owner_last_name"),
                rs.getInt("owner_msid"),
                rs.getString("slip_number"),
                rs.getString("alt_text"),
                rs.getInt("subleaser_id"),
                rs.getInt("subleaser_msid"),
                rs.getString("subleaser_first_name"),
                rs.getString("subleaser_last_name"));
    }
}

