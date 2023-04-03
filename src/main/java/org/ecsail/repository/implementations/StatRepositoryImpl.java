package org.ecsail.repository.implementations;


import org.ecsail.dto.StatsDTO;
import org.ecsail.repository.interfaces.StatRepository;
import org.ecsail.repository.rowmappers.BoatRowMapper;
import org.ecsail.repository.rowmappers.StatsRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class StatRepositoryImpl implements StatRepository {
    private final JdbcTemplate template;

    public StatRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }


    @Override
    public List<StatsDTO> getStatistics(int startYear, int stopYear) {
        String query = "SELECT * FROM stats WHERE fiscal_year > "+(startYear -1)+" AND fiscal_year < " + (stopYear +1);
        return template.query(query, new StatsRowMapper());
    }

    @Override
    public List<StatsDTO> createStatDTO(int year, int statID) {
        return null;
    }

    @Override
    public String getStatQuery(int year) {
        return null;
    }

    @Override
    public int getNumberOfStatYears() {
        return 0;
    }
}
