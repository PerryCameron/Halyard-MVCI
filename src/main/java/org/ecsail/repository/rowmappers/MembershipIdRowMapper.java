package org.ecsail.repository.rowmappers;

import org.ecsail.dto.MembershipIdDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MembershipIdRowMapper implements RowMapper<MembershipIdDTO> {

    @Override
    public MembershipIdDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MembershipIdDTO(
                rs.getInt("MID"),
                rs.getInt("fiscal_year"),
                rs.getInt("ms_id"),
                rs.getInt("membership_id"),
                rs.getBoolean("renew"),
                rs.getString("MEM_TYPE"),
                rs.getBoolean("SELECTED"),
                rs.getBoolean("LATE_RENEW"));
    }
}