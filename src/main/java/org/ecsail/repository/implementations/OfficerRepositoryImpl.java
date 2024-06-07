package org.ecsail.repository.implementations;

import org.ecsail.dto.OfficerDTO;
import org.ecsail.dto.OfficerWithNameDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.repository.interfaces.OfficerRepository;
import org.ecsail.repository.rowmappers.OfficerRowMapper;
import org.ecsail.repository.rowmappers.OfficerWithNamesRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.util.List;

public class OfficerRepositoryImpl implements OfficerRepository {
    private final JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(OfficerRepositoryImpl.class);

    public OfficerRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public int deleteOfficer(int pId) {
        String sql = "DELETE FROM officer WHERE p_id = ?";
        try {
           return template.update(sql, pId);
        } catch (DataAccessException e) {
            logger.error("Unable to DELETE officer: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public List<OfficerDTO> getOfficers() {
        String query = "SELECT * FROM officer";
        return template.query(query,new OfficerRowMapper());
    }

    @Override
    public List<OfficerDTO> getOfficer(String field, int attribute) {
        String query = "SELECT * FROM officer WHERE ? = ?";
        return template.query(query,new OfficerRowMapper(),field,attribute);    }

    @Override
    public List<OfficerDTO> getOfficer(PersonDTO person) {
        String query = "SELECT * FROM officer WHERE P_ID = ?";
        return template.query(query,new OfficerRowMapper(), person.getpId());
    }

    @Override
    public List<OfficerWithNameDTO> getOfficersWithNames(String type) {
        String query = "SELECT f_name,L_NAME,off_year FROM officer o LEFT JOIN person p ON o.p_id=p.p_id WHERE off_type= ?";
        return template.query(query,new OfficerWithNamesRowMapper(), type);
    }

    @Override
    public int update(OfficerDTO officerDTO) {
        String query = "UPDATE officer SET " +
                "P_ID = :pId, " +
                "BOARD_YEAR = :boardYear, " +
                "OFF_TYPE = :officerType, " +
                "OFF_YEAR = :fiscalYear " +
                "WHERE O_ID = :officerId";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(officerDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }

    @Override
    public int insert(OfficerDTO officerDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO officer (P_ID, BOARD_YEAR, OFF_TYPE, OFF_YEAR) " +
                "VALUES (:pId, :boardYear, :officerType, :fiscalYear)";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pId", officerDTO.getpId());
        namedParameters.addValue("boardYear", Integer.parseInt(officerDTO.getBoardYear()));
        namedParameters.addValue("officerType", officerDTO.getOfficerType());
        namedParameters.addValue("fiscalYear", Integer.parseInt(officerDTO.getFiscalYear()));
        int affectedRows = namedParameterJdbcTemplate.update(query, namedParameters, keyHolder);
        officerDTO.setOfficerId(keyHolder.getKey().intValue());
        return affectedRows;
    }

    @Override
    public int delete(OfficerDTO officerDTO) {
        String deleteSql = "DELETE FROM officer WHERE O_ID = ?";
        return template.update(deleteSql, officerDTO.getOfficerId());
    }
}
