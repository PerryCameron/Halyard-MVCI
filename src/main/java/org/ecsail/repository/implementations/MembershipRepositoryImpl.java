package org.ecsail.repository.implementations;

import org.ecsail.dto.MembershipListDTO;
import org.ecsail.repository.interfaces.MembershipRepository;
import org.ecsail.repository.rowmappers.MembershipListRowMapper;
import org.ecsail.repository.rowmappers.MembershipListRowMapper1;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.List;

public class MembershipRepositoryImpl implements MembershipRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate template;

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
                = template.query(query, new MembershipListRowMapper(), new Object[] {selectedYear.intValue()});
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
                = template.query(query, new MembershipListRowMapper(), new Object[]{selectedYear,selectedYear.intValue()});
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
                selectedYear.intValue(), selectedYear.intValue(),selectedYear.intValue(),selectedYear.intValue(),
                selectedYear.intValue()});
        return membershipListDTOS;
    }

    @Override
    public List<MembershipListDTO> getSlipWaitList(Integer selectedYear) {
        String query = """
                SELECT m.ms_id,m.p_id,id.membership_id,id.fiscal_year,id.fiscal_year,m.join_date,id.mem_type,
                s.SLIP_NUM,p.l_name,p.f_name,s.subleased_to,m.address,m.city,m.state,m.zip
                FROM (SELECT * from wait_list where SLIP_WAIT=true) wl
                INNER JOIN (select * from membership_id where FISCAL_YEAR=? and RENEW=1) id on id.MS_ID=wl.MS_ID
                INNER JOIN membership m on m.MS_ID=wl.MS_ID
                LEFT JOIN (select * from person where MEMBER_TYPE=1) p on m.MS_ID= p.MS_ID
                LEFT JOIN slip s on m.MS_ID = s.MS_ID;
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
                "MS_ID = :msId, " +
                "P_ID = :pID, " +
                "JOIN_DATE = :joinDate, " +
                "MEM_TYPE = :memType, " +
                "ADDRESS = :address, " +
                "CITY = :city, " +
                "STATE = :state, " +
                "ZIP = :zip";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(membershipListDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }
}
