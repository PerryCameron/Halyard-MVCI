package org.ecsail.repository.rowmappers;

import org.ecsail.dto.MembershipListRadioDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbMembershipListRadioRowMapper implements RowMapper<MembershipListRadioDTO> {

    @Override
    public MembershipListRadioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MembershipListRadioDTO(
        rs.getInt("ID"),
                rs.getString("LABEL"),
                rs.getString("METHOD_NAME"),
                rs.getInt("LIST_ORDER"),
                rs.getInt("LIST"),
                rs.getBoolean("selected"));
    }
}
