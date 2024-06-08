package org.ecsail.repository.implementations;

import org.ecsail.dto.MembershipDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.repository.interfaces.MembershipRepository;
import org.ecsail.repository.rowmappers.MembershipListRowMapper;
import org.ecsail.repository.rowmappers.MembershipListRowMapper1;
import org.ecsail.repository.rowmappers.MembershipRowMapper;
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
import java.util.List;

public class MembershipRepositoryImpl implements MembershipRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate template;
    private static final Logger logger = LoggerFactory.getLogger(MembershipIdRepositoryImpl.class);


    public MembershipRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<MembershipListDTO> getActiveRoster(Integer selectedYear) {
        String query = """
                SELECT m.ms_id,m.p_id,id.membership_id,id.fiscal_year,id.fiscal_year,m.join_date,id.mem_type, 
                s.SLIP_NUM,p.l_name,p.f_name,s.subleased_to,m.address,m.city,m.state,m.zip 
                FROM (select * from membership_id where FISCAL_YEAR=? and RENEW=1) id 
                INNER JOIN membership m on m.MS_ID = id.MS_ID 
                LEFT JOIN (select * from person where MEMBER_TYPE=1) p on m.MS_ID= p.MS_ID 
                LEFT JOIN slip s on m.MS_ID = s.MS_ID;
                """;
        List<MembershipListDTO> membershipListDTOS
                = template.query(query, new MembershipListRowMapper(), new Object[]{selectedYear.intValue()});
        return membershipListDTOS;
    }

    @Override
    public List<MembershipListDTO> getInActiveRoster(Integer selectedYear) {
        String query = """
                SELECT m.ms_id,m.p_id,id.membership_id,id.fiscal_year,id.fiscal_year,m.join_date,id.mem_type, 
                s.SLIP_NUM,p.l_name,p.f_name,s.subleased_to,m.address,m.city,m.state,m.zip 
                FROM (select * from membership_id where FISCAL_YEAR=? and RENEW=0) id 
                INNER JOIN membership m on m.MS_ID = id.MS_ID 
                LEFT JOIN (select * from person where MEMBER_TYPE=1) p on m.MS_ID= p.MS_ID  
                LEFT JOIN slip s on m.MS_ID = s.MS_ID;
                """;
        List<MembershipListDTO> membershipListDTOS
                = template.query(query, new MembershipListRowMapper(), new Object[]{selectedYear.intValue()});
        return membershipListDTOS;
    }

    @Override
    public List<MembershipListDTO> getAllRoster(Integer selectedYear) {
        String query = """
                SELECT m.ms_id,m.p_id,id.membership_id,id.fiscal_year,id.fiscal_year,m.join_date,id.mem_type,
                s.SLIP_NUM,p.l_name,p.f_name,s.subleased_to,m.address,m.city,m.state,m.zip 
                FROM (select * from membership_id where FISCAL_YEAR=?) id 
                INNER JOIN membership m on m.MS_ID = id.MS_ID 
                LEFT JOIN (select * from person where MEMBER_TYPE=1) p on m.MS_ID= p.MS_ID 
                LEFT JOIN slip s on m.MS_ID = s.MS_ID;
                """;
        List<MembershipListDTO> membershipListDTOS
                = template.query(query, new MembershipListRowMapper(), new Object[]{selectedYear.intValue()});
        return membershipListDTOS;
    }

    @Override
    public List<MembershipListDTO> getNewMemberRoster(Integer selectedYear) {
        String query = """
                SELECT m.ms_id,m.p_id,id.membership_id,id.fiscal_year,id.fiscal_year,m.join_date,id.mem_type,s.SLIP_NUM,p.l_name,p.f_name,s.subleased_to,m.address,m.city,m.state,m.zip
                FROM (select * from membership where YEAR(JOIN_DATE)=?) m
                INNER JOIN (select * from membership_id where FISCAL_YEAR=? and RENEW=1) id ON m.ms_id=id.ms_id
                INNER JOIN (select * from person where MEMBER_TYPE=1) p ON m.p_id=p.p_id
                LEFT JOIN slip s on m.MS_ID = s.MS_ID
                                """;
        List<MembershipListDTO> membershipListDTOS
                = template.query(query, new MembershipListRowMapper(), new Object[]{selectedYear, selectedYear.intValue()});
        return membershipListDTOS;
    }

    @Override
    public List<MembershipListDTO> getReturnMemberRoster(Integer selectedYear) {
        String query = """
                SELECT m.ms_id,m.p_id,id.membership_id,id.fiscal_year,m.join_date,id.mem_type,s.SLIP_NUM,p.l_name,
                       p.f_name,s.subleased_to,m.address,m.city,m.state,m.zip
                FROM membership_id id
                         LEFT JOIN membership m ON id.ms_id = m.ms_id
                         LEFT JOIN person p ON p.p_id = m.p_id
                         LEFT JOIN slip s ON s.ms_id = m.ms_id
                WHERE fiscal_year = ?
                  AND id.membership_id >
                      (SELECT membership_id
                       FROM membership_id
                       WHERE fiscal_year = ?
                         AND ms_id = (SELECT ms_id
                                      FROM membership_id
                                      WHERE membership_id = (SELECT MAX(membership_id)
                                                             FROM membership_id
                                                             WHERE fiscal_year = (? - 1)
                                                               AND membership_id < 500
                                                               AND renew = 1)
                                        AND fiscal_year = (? - 1)))
                  AND id.membership_id < 500
                  AND YEAR(m.join_date) != ?
                  AND (SELECT NOT EXISTS(SELECT mid
                                         FROM membership_id
                                         WHERE fiscal_year = (? - 1)
                                           AND renew = 1
                                           AND ms_id = id.ms_id));
                                """;
        List<MembershipListDTO> membershipListDTOS
                = template.query(query, new MembershipListRowMapper(), new Object[]{selectedYear.intValue(),
                selectedYear.intValue(), selectedYear.intValue(), selectedYear.intValue(), selectedYear.intValue(),
                selectedYear.intValue()});
        return membershipListDTOS;
    }

    @Override
    public List<MembershipListDTO> getSlipWaitList(Integer selectedYear) {
        String query = """
                SELECT m.ms_id, m.p_id, id.membership_id, id.fiscal_year, id.fiscal_year, m.join_date, id.mem_type,
                COALESCE(s_direct.SLIP_NUM, s_subleased.SLIP_NUM) AS SLIP_NUM,
                p.l_name, p.f_name, s_subleased.subleased_to, m.address, m.city, m.state, m.zip
                FROM (SELECT * FROM wait_list WHERE SLIP_WAIT = true) wl
                INNER JOIN (SELECT * FROM membership_id WHERE FISCAL_YEAR = YEAR(NOW()) AND RENEW = 1) id ON id.MS_ID = wl.MS_ID
                INNER JOIN membership m ON m.MS_ID = wl.MS_ID
                LEFT JOIN (SELECT * FROM person WHERE MEMBER_TYPE = 1) p ON m.MS_ID = p.MS_ID
                LEFT JOIN slip s_direct ON m.MS_ID = s_direct.MS_ID
                LEFT JOIN slip s_subleased ON m.MS_ID = s_subleased.SUBLEASED_TO;
                                """;
        List<MembershipListDTO> membershipListDTOS
                = template.query(query, new MembershipListRowMapper(), new Object[]{selectedYear.intValue()});
        return membershipListDTOS;
    }

    @Override
    public List<MembershipListDTO> getMembershipByBoatId(Integer boatId) {
        String query = """
                select m.ms_id,m.p_id,m.join_date,p.l_name,p.f_name,m.address,m.city,m.state,m.zip from membership m
                join person p on m.P_ID = p.P_ID
                join boat_owner bo on m.MS_ID = bo.MS_ID where BOAT_ID=?;
                """;
        List<MembershipListDTO> membershipListDTOS
                = template.query(query, new MembershipListRowMapper1(), boatId);
        return membershipListDTOS;
    }

    @Override
    public int update(MembershipListDTO membershipListDTO) {
        String query = "UPDATE membership SET " +
                "P_ID = :pId, " +
                "JOIN_DATE = :joinDate, " +
                "MEM_TYPE = :memType, " +
                "ADDRESS = :address, " +
                "CITY = :city, " +
                "STATE = :state, " +
                "ZIP = :zip WHERE MS_ID = :msId";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(membershipListDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }

    @Override
    public int updateJoinDate(MembershipListDTO membershipListDTO) {
        String query = "UPDATE membership SET " +
                "JOIN_DATE = :joinDate WHERE MS_ID=:msId";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(membershipListDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }

    @Override
    public MembershipListDTO getMembershipByMembershipId(int membershipId) {
        String query = """
                SELECT m.ms_id,m.p_id,id.membership_id,id.fiscal_year,m.join_date,
                id.mem_type,p.l_name,p.f_name,m.address,m.city,m.state,m.zip FROM 
                membership m LEFT JOIN person p ON m.p_id=p.p_id LEFT JOIN membership_id 
                id ON m.ms_id=id.ms_id WHERE id.fiscal_year=YEAR(NOW()) AND membership_id=?
                """;
        return template.queryForObject(query, new MembershipListRowMapper1(), membershipId);
    }

    @Override
    public MembershipListDTO getMembershipByMsId(int msId) {
        String query = """
                SELECT m.ms_id,m.p_id,id.membership_id,id.fiscal_year,id.fiscal_year,m.join_date,id.mem_type,
                s.SLIP_NUM,p.l_name,p.f_name,s.subleased_to,m.address,m.city,m.state,m.zip
                FROM (select * from membership_id where FISCAL_YEAR=YEAR(NOW())) id
                INNER JOIN membership m on m.MS_ID = id.MS_ID
                LEFT JOIN (select * from person where MEMBER_TYPE=1) p on m.MS_ID= p.MS_ID
                LEFT JOIN slip s on m.MS_ID = s.MS_ID
                WHERE m.MS_ID=?;
                """;
        return template.queryForObject(query, new MembershipListRowMapper(), msId);
    }

    @Override
    public MembershipListDTO insertMembership(MembershipListDTO nm) {
        System.out.println("trying to insert membership");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
                INSERT INTO membership (p_id, join_date, mem_type, address, city, state, zip)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try {
            template.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, nm.getPId());
                ps.setDate(2, nm.getJoinDate() != null ? java.sql.Date.valueOf(nm.getJoinDate()) : null);
                ps.setString(3, nm.getMemType());
                ps.setString(4, nm.getAddress());
                ps.setString(5, nm.getCity());
                ps.setString(6, nm.getState());
                ps.setString(7, nm.getZip());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                nm.setMsId(key.intValue()); // Update the DTO with the generated key
            }
        } catch (DataAccessException e) {
            logger.error("Unable to insert into membership: " + e.getMessage());
        }
        return nm; // Return the updated DTO
    }

    @Override
    public MembershipListDTO getMembershipListByIdAndYear(int membershipId, int year) {
        String sql = """
                                SELECT m.ms_id, m.p_id, mid.membership_id, mid.fiscal_year, m.join_date, 
                                mid.mem_type, s.SLIP_NUM, p.l_name, p.f_name, s.subleased_to, m.address, 
                                m.city, m.state, m.zip 
                                FROM membership m 
                                LEFT JOIN person p ON m.ms_id = p.ms_id AND p.member_type = 1 
                                LEFT JOIN membership_id mid ON m.ms_id = mid.ms_id AND mid.fiscal_year = ? 
                                LEFT JOIN slip s ON m.ms_id = s.ms_id 
                                WHERE mid.fiscal_year = ? AND mid.membership_id = ?  
                                LIMIT 1;
                """;
        return template.queryForObject(sql, new MembershipListRowMapper(), year, year, membershipId);
    }

    @Override
    public MembershipDTO getCurrentMembershipChair() {
        String sql = """
                SELECT m.* FROM membership m 
                JOIN app_settings a ON m.MS_ID = CAST(a.setting_value AS UNSIGNED) 
                WHERE a.setting_key = 'current_membership_chair'
                """;
        return template.queryForObject(sql, new MembershipRowMapper());
    }

    @Override
    public int deleteFormMsIdHash(int msId) {
        String sql = "DELETE FROM form_msid_hash WHERE ms_id = ?";
        try {
            return template.update(sql, msId);
        } catch (DataAccessException e) {
            logger.error("Unable to DELETE form_msid_hash row: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int deleteMembership(int msId) {
        String sql = "DELETE FROM membership WHERE ms_id = ?";
        try {
            return template.update(sql, msId);
        } catch (DataAccessException e) {
            logger.error("Unable to DELETE membership: " + e.getMessage());
        }
        return 0;
    }
}
