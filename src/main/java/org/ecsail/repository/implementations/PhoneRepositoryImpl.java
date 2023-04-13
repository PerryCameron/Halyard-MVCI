package org.ecsail.repository.implementations;

import org.ecsail.dto.PersonDTO;
import org.ecsail.dto.PhoneDTO;
import org.ecsail.repository.interfaces.PhoneRepository;
import org.ecsail.repository.rowmappers.MemoRowMapper;
import org.ecsail.repository.rowmappers.PhoneRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class PhoneRepositoryImpl implements PhoneRepository {

    private final JdbcTemplate template;

    public PhoneRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<PhoneDTO> getPhoneByPid(int p_id) {
        String query = "SELECT * FROM phone";
        if(p_id != 0)
            query += " WHERE p_id=" + p_id;
        return template.query(query, new PhoneRowMapper());
    }

    @Override
    public List<PhoneDTO> getPhoneByPerson(PersonDTO p) {
        String query = "SELECT * FROM phone WHERE p_id=" + p.getP_id();
        return template.query(query, new PhoneRowMapper());
    }

    @Override
    public PhoneDTO getListedPhoneByType(PersonDTO p, String type) {
        String query = "SELECT * FROM phone WHERE p_id=" + p.getP_id() + " AND phone_listed=true AND phone_type='" + type + "'";
        return template.queryForObject(query, new PhoneRowMapper());
    }

    @Override
    public PhoneDTO getPhoneByType(String pid, String type) {
        String query = "SELECT * FROM phone WHERE p_id=" + pid + " AND phone_type='" + type + "'";
        return template.queryForObject(query, new PhoneRowMapper());
    }
}
