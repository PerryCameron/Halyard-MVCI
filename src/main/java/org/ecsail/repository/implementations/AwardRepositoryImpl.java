package org.ecsail.repository.implementations;

import org.ecsail.dto.AwardDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.repository.interfaces.AwardRepository;
import org.ecsail.repository.rowmappers.AwardsRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.util.List;

public class AwardRepositoryImpl implements AwardRepository {
    private final JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AwardRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
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

    @Override
    public int updateAward(AwardDTO awardDTO) {
        String query = "UPDATE awards SET " +
                "P_ID = :pId, " +
                "AWARD_YEAR = :awardYear, " +
                "AWARD_TYPE = :awardType " +
                "WHERE AWARD_ID = :awardId ";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(awardDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }

    @Override
    public AwardDTO insert(AwardDTO awardDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO awards (P_ID, AWARD_YEAR, AWARD_TYPE) VALUES (:pId, :awardYear, :awardType)";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(awardDTO);
        namedParameterJdbcTemplate.update(query, namedParameters, keyHolder);
        awardDTO.setAwardId(keyHolder.getKey().intValue());
        return awardDTO;
    }
}
