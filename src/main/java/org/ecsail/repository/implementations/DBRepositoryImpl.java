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
                          c.TABLE_NAME AS `Table`,
                          c.COLUMN_NAME AS `Column`,
                          c.DATA_TYPE AS `Data Type`,
                          c.COLUMN_TYPE AS `Column Type`,
                          c.IS_NULLABLE AS `Is Nullable`,
                          c.COLUMN_KEY AS `Key`,
                          c.EXTRA AS `Extra`,
                          c.COLUMN_DEFAULT AS `Default Value`,
                          c.COLUMN_COMMENT AS `Comment`,
                          kcu.REFERENCED_TABLE_NAME as `Referenced Table`,
                          kcu.REFERENCED_COLUMN_NAME as `Referenced Column`
                      FROM
                          information_schema.COLUMNS c
                      LEFT JOIN
                          information_schema.KEY_COLUMN_USAGE kcu
                      ON
                          c.TABLE_SCHEMA = kcu.TABLE_SCHEMA AND
                          c.TABLE_NAME = kcu.TABLE_NAME AND
                          c.COLUMN_NAME = kcu.COLUMN_NAME
                      WHERE
                          c.TABLE_SCHEMA = ?;
                                
                """;
        return template.query(query, new Object[]{dbName}, new SchemaDTORowMapper());
    }
}
