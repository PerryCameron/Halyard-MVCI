package org.ecsail.repository.implementations;

import org.ecsail.dto.MembershipIdDTO;
import org.ecsail.mvci_new_membership.NewMembershipInteractor;
import org.ecsail.repository.interfaces.MembershipIdRepository;
import org.ecsail.repository.rowmappers.MembershipIdRowMapper;
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
import java.util.List;

public class MembershipIdRepositoryImpl implements MembershipIdRepository {
    private final JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(MembershipIdRepositoryImpl.class);

    public MembershipIdRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<MembershipIdDTO> getIds() {
        return null;
    }

    @Override
    public List<MembershipIdDTO> getIds(int msId) {
        String query = "SELECT * FROM membership_id WHERE MS_ID=?";
        return template.query(query, new MembershipIdRowMapper(),msId);
    }

    @Override
    public MembershipIdDTO getId(int ms_id) {
        return null;
    }

    @Override
    public MembershipIdDTO getCurrentId(int msId) {
        String query = "SELECT * FROM membership_id WHERE FISCAL_YEAR=YEAR(CURDATE()) and MS_ID=?";
        return template.queryForObject(query, new MembershipIdRowMapper(),msId);
    }

    @Override
    public MembershipIdDTO getMembershipIdFromMsid(int msid) {
        return null;
    }

    @Override
    public MembershipIdDTO getMsidFromMembershipID(int membership_id) {
        return null;
    }

    @Override
    public MembershipIdDTO getMembershipId(String year, int ms_id) {
        return null;
    }

    @Override
    public MembershipIdDTO getMembershipIdObject(int mid) {
        return null;
    }

    @Override
    public MembershipIdDTO getHighestMembershipId(String year) {
        return null;
    }

    @Override
    public boolean isRenewedByMsidAndYear(int ms_id, String year) {
        return false;
    }

    @Override
    public List<MembershipIdDTO> getAllMembershipIdsByYear(String year) {
        return null;
    }

    @Override
    public List<MembershipIdDTO> getActiveMembershipIdsByYear(String year) {
        return null;
    }

    @Override
    public int getNonRenewNumber(int year) {
        return 0;
    }

    @Override
    public int getMsidFromYearAndMembershipId(int year, String membershipId) {
        return 0;
    }

    @Override
    public int update(MembershipIdDTO membershipIdDTO) {
        String query = "UPDATE membership_id SET " +
                "FISCAL_YEAR = :fiscalYear, " +
                "MS_ID = :msId, " +
                "MEMBERSHIP_ID = :membershipId, " +
                "RENEW = :renew, " +
                "MEM_TYPE = :memType, " +
                "SELECTED = :selected, " +
                "LATE_RENEW = :lateRenew " +
                "WHERE MID = :mId ";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(membershipIdDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }

    @Override
    public int delete(MembershipIdDTO membershipIdDTO) {
        String deleteSql = "DELETE FROM membership_id WHERE MID = ?";
        return template.update(deleteSql, membershipIdDTO.getmId());
    }

    @Override
    public int insert(MembershipIdDTO membershipIdDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO membership_id (FISCAL_YEAR, MS_ID, MEMBERSHIP_ID, RENEW, MEM_TYPE, SELECTED, LATE_RENEW) " +
                "VALUES (:fiscalYear, :msId, :membershipId, :renew, :memType, :selected, :lateRenew)";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(membershipIdDTO);
        int affectedRows = namedParameterJdbcTemplate.update(query, namedParameters, keyHolder);
        membershipIdDTO.setmId(keyHolder.getKey().intValue());
        return affectedRows;
    }

    @Override
    public int getMembershipIdForNewestMembership() {
        String sql = "SELECT MAX(membership_id) FROM membership_id WHERE fiscal_year = YEAR(NOW()) AND membership_id < 500";
        try {
            Integer result = template.queryForObject(sql, Integer.class);
            return (result != null) ? result : 0;
        } catch (DataAccessException e) {
            logger.error("Unable to retrieve highest membership ID: " + e.getMessage());
            // Handle or rethrow the exception as per your application's requirements
            return 0;
        }
    }
}
