package org.ecsail.repository.rowmappers;


import org.ecsail.dto.DbRosterSettingsDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbRosterSettingsRowMapper implements RowMapper<DbRosterSettingsDTO> {

    @Override
    public DbRosterSettingsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DbRosterSettingsDTO(
        rs.getInt("id"),
        rs.getString("name"),
        rs.getString("pojo_name"),
        rs.getString("data_type"),
        rs.getString("field_name"),
        rs.getString("getter"),
        rs.getBoolean("searchable"), rs.getBoolean("exportable"));
    }
}
