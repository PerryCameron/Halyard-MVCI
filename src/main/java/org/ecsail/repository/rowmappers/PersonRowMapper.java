package org.ecsail.repository.rowmappers;


import org.ecsail.dto.PersonDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper implements RowMapper<PersonDTO> {

    @Override
    public PersonDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PersonDTO(
                rs.getInt("p_id"),
                rs.getInt("MS_ID"),
                rs.getInt("member_type"),
                rs.getString("F_NAME"),
                rs.getString("L_NAME"),
                rs.getDate("birthday").toLocalDate(),
                rs.getString("OCCUPATION"),
                rs.getString("BUSINESS"),
                rs.getBoolean("IS_ACTIVE"),
                rs.getString("NICK_NAME"),
                rs.getInt("OLD_MSID"));
    }
}