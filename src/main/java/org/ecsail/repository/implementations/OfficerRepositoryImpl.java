package org.ecsail.repository.implementations;

import org.ecsail.dto.OfficerDTO;
import org.ecsail.dto.OfficerWithNameDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.repository.interfaces.OfficerRepository;
import org.ecsail.repository.rowmappers.OfficerRowMapper;
import org.ecsail.repository.rowmappers.OfficerWithNamesRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.List;

public class OfficerRepositoryImpl implements OfficerRepository {
    private final JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public OfficerRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<OfficerDTO> getOfficers() {
        String query = "SELECT * FROM officer";
        return template.query(query,new OfficerRowMapper());
    }

    @Override
    public List<OfficerDTO> getOfficer(String field, int attribute) {
        String query = "SELECT * FROM officer WHERE " + field + "='" + attribute + "'";
        return template.query(query,new OfficerRowMapper());    }

    @Override
    public List<OfficerDTO> getOfficer(PersonDTO person) {
        String query = "SELECT * FROM officer WHERE P_ID='" + person.getP_id() + "'";
        return template.query(query,new OfficerRowMapper());
    }

    @Override
    public List<OfficerWithNameDTO> getOfficersWithNames(String type) {
        String query = "SELECT f_name,L_NAME,off_year FROM officer o LEFT JOIN person p ON o.p_id=p.p_id WHERE off_type='"+type+"'";
        return template.query(query,new OfficerWithNamesRowMapper());
    }

    @Override
    public int updateOfficer(OfficerDTO officerDTO) {
        String query = "UPDATE officer SET " +
                "P_ID = :pId, " +
                "BOARD_YEAR = :board_year, " +
                "OFF_TYPE = :officer_type, " +
                "OFF_YEAR = :fiscal_year " +
                "WHERE O_ID = :officer_id";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(officerDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }
}
