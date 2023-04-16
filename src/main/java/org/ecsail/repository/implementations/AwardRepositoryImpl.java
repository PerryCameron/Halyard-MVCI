package org.ecsail.repository.implementations;

import org.ecsail.dto.AwardDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.repository.interfaces.AwardRepository;
import org.ecsail.repository.rowmappers.AwardsRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class AwardRepositoryImpl implements AwardRepository {
    private final JdbcTemplate template;

    public AwardRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<AwardDTO> getAwards(PersonDTO p) {
        String query = "SELECT * FROM awards WHERE p_id=" + p.getP_id();
        return template.query(query, new AwardsRowMapper());
    }

    @Override
    public List<AwardDTO> getAwards() {
        String query = "SELECT * FROM awards";
        return template.query(query, new AwardsRowMapper());
    }
}
