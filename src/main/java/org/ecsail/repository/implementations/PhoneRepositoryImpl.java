package org.ecsail.repository.implementations;

import org.ecsail.dto.PersonDTO;
import org.ecsail.dto.PhoneDTO;
import org.ecsail.repository.interfaces.PhoneRepository;
import org.ecsail.repository.rowmappers.PhoneRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.util.List;

public class PhoneRepositoryImpl implements PhoneRepository {

    private final JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public PhoneRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<PhoneDTO> getPhoneByPid(int pId) {
        String query = "SELECT * FROM phone";
        if(pId != 0)
            query += " WHERE p_id=" + pId;
        return template.query(query, new PhoneRowMapper());
    }

    @Override
    public List<PhoneDTO> getPhoneByPerson(PersonDTO personDTO) {
        String query = "SELECT * FROM phone WHERE p_id=" + personDTO.getP_id();
        return template.query(query, new PhoneRowMapper());
    }

    @Override
    public PhoneDTO getListedPhoneByType(PersonDTO p, String phoneType) {
        String query = "SELECT * FROM phone WHERE p_id=" + p.getP_id() + " AND phone_listed=true AND phone_type='" + phoneType + "'";
        return template.queryForObject(query, new PhoneRowMapper());
    }

    @Override
    public PhoneDTO getPhoneByType(String pId, String type) {
        String query = "SELECT * FROM phone WHERE p_id=" + pId + " AND phone_type='" + type + "'";
        return template.queryForObject(query, new PhoneRowMapper());
    }

    @Override
    public int updatePhone(PhoneDTO phoneDTO) {
        String query = "UPDATE phone SET " +
                "PHONE_ID = :phoneId, " +
                "P_ID = :pId," +
                "PHONE = :phone, " +
                "PHONE_TYPE = :phoneType, " +
                "PHONE_LISTED = :phoneListed " +
                "WHERE PHONE_ID = :phoneId";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(phoneDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }

    @Override
    public int delete(PhoneDTO phoneDTO) {
        String deleteSql = "DELETE FROM phone WHERE PHONE_ID = ?";
        return template.update(deleteSql, phoneDTO.getPhoneId());
    }

    @Override
    public int insert(PhoneDTO phoneDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO phone (P_ID, PHONE, PHONE_TYPE, PHONE_LISTED) " +
                "VALUES (:pId, :phone, :phoneType, :phoneListed)";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(phoneDTO);
        int affectedRows = namedParameterJdbcTemplate.update(query, namedParameters, keyHolder);
        phoneDTO.setPhoneId(keyHolder.getKey().intValue());
        return affectedRows;
    }
}
