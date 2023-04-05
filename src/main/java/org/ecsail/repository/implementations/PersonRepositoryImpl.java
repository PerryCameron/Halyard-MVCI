package org.ecsail.repository.implementations;


import org.ecsail.dto.PersonDTO;
import org.ecsail.repository.interfaces.PersonRepository;
import org.ecsail.repository.rowmappers.PersonRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class PersonRepositoryImpl implements PersonRepository {
    private JdbcTemplate template;

    public PersonRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<PersonDTO> getActivePeopleByMsId(int msId) {
        String query = "SELECT * FROM person WHERE IS_ACTIVE=true AND ms_id=?";
        return template.query(query, new PersonRowMapper(), new Object[]{msId});
    }

    public  void deletePerson(PersonDTO p) {
        String query = "DELETE FROM person WHERE p_id = ?";
        try {
            template.update(query, p.getP_id());
        } catch (DataAccessException e) {
//            new Dialogue_ErrorSQL(e,"Unable to DELETE","See below for details");
            System.out.println(e);
        }
    }



}
