package org.ecsail.repository.rowmappers;

import org.ecsail.dto.MembershipListDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MembershipListRowMapper1 implements RowMapper<MembershipListDTO> {
    // Customized Row mapper for List<MembershipListDTO> getMembershipByBoatId(Integer boatId)
    @Override
    public MembershipListDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MembershipListDTO(
                rs.getInt("ms_id"),
                rs.getInt("p_id"),
                0,
                rs.getString("join_date"),
                "",
                rs.getString("address"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("zip"),
                rs.getString("l_name"),
                rs.getString("f_name"),
                "",
                0,
               0);
    }
}
