package org.ecsail.repository.implementations;


import org.ecsail.dto.PersonDTO;
import org.ecsail.repository.interfaces.PersonRepository;
import org.ecsail.repository.rowmappers.PersonRowMapper;
import org.mariadb.jdbc.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonRepositoryImpl implements PersonRepository {
    private JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(PersonRepositoryImpl.class);


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

    @Override
    public int insert(PersonDTO personDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query =
                """
                    INSERT INTO person (MS_ID, member_type, F_NAME, L_NAME, OCCUPATION, BUSINESS, 
                    birthday, IS_ACTIVE, NICK_NAME, OLD_MSID) VALUES (:msId, :memberType, :firstName,
                    :lastName, :occupation, :business, :birthday, :active, :nickName, :oldMsid)
                """;
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(personDTO);
        int affectedRows = namedParameterJdbcTemplate.update(query, namedParameters, keyHolder);
        personDTO.setpId(keyHolder.getKey().intValue());
        return affectedRows;
    }

    @Override
    public int delete(PersonDTO personDTO) {
        String deleteSql = "DELETE FROM person WHERE P_ID = ?";
        return template.update(deleteSql, personDTO.getpId());
    }

    @Override
    public PersonDTO insertPerson(PersonDTO person) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
        INSERT INTO person (MS_ID, MEMBER_TYPE, F_NAME, L_NAME, BIRTHDAY, OCCUPATION, BUSINESS, IS_ACTIVE, NICK_NAME, OLD_MSID)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try {
            template.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, person.getMsId());
                ps.setInt(2, person.getMemberType());
                ps.setString(3, person.getFirstName());
                ps.setString(4, person.getLastName());

                // Handle birthday conversion with the correctly formatted LocalDate
                LocalDate birthday = person.getBirthday();
                if (birthday != null) {
                    ps.setDate(5, java.sql.Date.valueOf(birthday));
                } else {
                    ps.setDate(5, null); // Set null if the birthday is null
                }

                ps.setString(6, person.getOccupation());
                ps.setString(7, person.getBusiness());
                ps.setBoolean(8, person.isActive());
                ps.setString(9, person.getNickName());
                ps.setInt(10, person.getOldMsid());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                person.setpId(key.intValue()); // Update the DTO with the generated key
            }
        } catch (DataAccessException e) {
            logger.error("Unable to insert into person: " + e.getMessage());
            // Handle or rethrow the exception as per your application's requirements
        }
        return person; // Return the updated DTO
    }

    @Override
    public List<PersonDTO> getPeople(int ms_id) {
        String sql = "SELECT * FROM person WHERE ms_id = ? AND IS_ACTIVE = true";
        try {
            return template.query(sql, new PersonRowMapper(), ms_id);
        } catch (DataAccessException e) {
            logger.error("Unable to retrieve information: " + e.getMessage());
            return new ArrayList<>(); // Return an empty list in case of an exception
        }
    }

    @Override
    public int deletePerson(int pId) {
        String sql = "DELETE FROM person WHERE p_id = ?";
        try {
            return template.update(sql, pId);
        } catch (DataAccessException e) {
            logger.error("Unable to DELETE person: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int deleteUserAuthRequest(int pId) {
        String sql = "DELETE FROM user_auth_request WHERE pid = ?";
        try {
            return template.update(sql, pId);
        } catch (DataAccessException e) {
            logger.error("Unable to DELETE person: " + e.getMessage());
        }
        return 0;
    }

}
