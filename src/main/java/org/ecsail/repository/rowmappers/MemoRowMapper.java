package org.ecsail.repository.rowmappers;

import org.ecsail.dto.NotesDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemoRowMapper implements RowMapper<NotesDTO> {

    @Override
    public NotesDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new NotesDTO(
                rs.getInt("MEMO_ID"),
                rs.getInt("MS_ID"),
                rs.getDate("MEMO_DATE").toLocalDate(),
                rs.getString("MEMO"),
                rs.getInt("INVOICE_ID"),
                rs.getString("CATEGORY"),
                rs.getInt("boat_id"));
    }
}