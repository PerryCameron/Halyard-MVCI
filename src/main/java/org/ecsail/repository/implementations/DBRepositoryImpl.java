package org.ecsail.repository.implementations;

import org.ecsail.dto.SchemaDTO;
import org.ecsail.repository.interfaces.DBRepository;
import org.ecsail.repository.rowmappers.SchemaDTORowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class DBRepositoryImpl implements DBRepository {
    private final JdbcTemplate template;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DBRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<SchemaDTO> getSchema(String dbName) {
        String query = """
                SELECT
                    TABLE_NAME AS `Table`,
                    COLUMN_NAME AS `Column`,
                    DATA_TYPE AS `Data Type`,
                    COLUMN_TYPE AS `Column Type`,
                    IS_NULLABLE AS `Is Nullable`,
                    COLUMN_KEY AS `Key`,
                    EXTRA AS `Extra`,
                    COLUMN_DEFAULT AS `Default Value`,
                    COLUMN_COMMENT AS `Comment`
                FROM
                    information_schema.COLUMNS
                WHERE
                    TABLE_SCHEMA = ?;
                                
                """;
        return template.query(query, new Object[]{dbName}, new SchemaDTORowMapper());
    }
}
