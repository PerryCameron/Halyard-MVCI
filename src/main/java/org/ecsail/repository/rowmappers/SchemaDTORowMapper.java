package org.ecsail.repository.rowmappers;

import org.ecsail.dto.SchemaDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SchemaDTORowMapper implements RowMapper<SchemaDTO> {
        @Override
        public SchemaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            SchemaDTO schema = new SchemaDTO();
            schema.setTable(rs.getString("Table"));
            schema.setColumn(rs.getString("Column"));
            schema.setDataType(rs.getString("Data Type"));
            schema.setColumnType(rs.getString("Column Type"));
            schema.setIsNullable(rs.getString("Is Nullable"));
            schema.setKey(rs.getString("Key"));
            schema.setExtra(rs.getString("Extra"));
            schema.setDefaultValue(rs.getString("Default Value"));
            schema.setComment(rs.getString("Comment"));
            return schema;
        }
}




