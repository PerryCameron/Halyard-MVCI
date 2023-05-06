package org.ecsail.repository.rowmappers;

import org.ecsail.dto.MembershipListDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MembershipListRowMapper implements RowMapper<MembershipListDTO> {

    @Override
    public MembershipListDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MembershipListDTO(
                rs.getInt("ms_id"),
                rs.getInt("p_id"),
                rs.getInt("membership_id"),
                rs.getString("join_date"),
                rs.getString("mem_type"),
                rs.getString("address"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("zip"),
                rs.getString("l_name"),
                rs.getString("f_name"),
                rs.getString("SLIP_NUM"),
                rs.getInt("subleased_to"),
                rs.getInt("fiscal_year"));
    }
}
