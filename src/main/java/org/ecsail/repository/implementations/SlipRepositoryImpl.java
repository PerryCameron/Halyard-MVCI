package org.ecsail.repository.implementations;

import org.ecsail.dto.SlipDTO;
import org.ecsail.repository.interfaces.SlipRepository;
import org.ecsail.repository.rowmappers.SlipRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class SlipRepositoryImpl implements SlipRepository {

    private final JdbcTemplate template;

    public SlipRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public SlipDTO getSlip(int msId) {
        String sql = "select * from slip where MS_ID = ? or SUBLEASED_TO = ? LIMIT 1";
        return template.queryForObject(sql, new SlipRowMapper(), msId, msId);
    }
}
