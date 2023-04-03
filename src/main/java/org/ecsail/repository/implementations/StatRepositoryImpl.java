package org.ecsail.repository.implementations;


import org.ecsail.dto.StatsDTO;
import org.ecsail.repository.interfaces.StatRepository;
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
        return null;
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
