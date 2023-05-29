package org.ecsail.repository.implementations;


import org.ecsail.dto.PersonDTO;
import org.ecsail.repository.interfaces.PersonRepository;
import org.ecsail.repository.rowmappers.PersonRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.List;

public class PersonRepositoryImpl implements PersonRepository {
    private JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PersonRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<PersonDTO> getActivePeopleByMsId(int msId) {
        String query = "SELECT * FROM person WHERE IS_ACTIVE=true AND ms_id=?";
        return template.query(query, new PersonRowMapper(), new Object[]{msId});
    }

    @Override
    public int update(PersonDTO personDTO) {
        String query = "UPDATE person SET " +
                "MS_ID = :msId, " +
                "member_type = :memberType, " +
                "F_NAME = :firstName, " +
                "L_NAME = :lastName, " +
                "OCCUPATION = :occupation, " +
                "BUSINESS = :business, " +
                "birthday = :birthday, " +
                "IS_ACTIVE = :active, " +
                "NICK_NAME = :nickName, " +
                "OLD_MSID = :oldMsid " +
                "WHERE p_id = :pId";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(personDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }

//    public  void deletePerson(PersonDTO p) {
//        String query = "DELETE FROM person WHERE p_id = ?";
//        template.update(query, p.getP_id());
//    }
//
//    public int getPersonAge(PersonDTO person) {
//        String query = "SELECT DATE_FORMAT(FROM_DAYS(DATEDIFF(NOW(),(SELECT birthday FROM person where p_id=?))), '%Y')+0 AS AGE;";
//            Integer age = template.queryForObject(query, Integer.class, new Object[] { person.getP_id() });
//            return age != null ? age : 0;
//    }
}
