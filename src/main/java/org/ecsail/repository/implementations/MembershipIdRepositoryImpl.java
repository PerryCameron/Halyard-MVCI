package org.ecsail.repository.implementations;

import org.ecsail.dto.MembershipIdDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.repository.interfaces.MembershipIdRepository;
import org.ecsail.repository.interfaces.MembershipRepository;
import org.ecsail.repository.rowmappers.MembershipIdRowMapper;
import org.ecsail.repository.rowmappers.PhoneRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class MembershipIdRepositoryImpl implements MembershipIdRepository {
    private final JdbcTemplate template;

    public MembershipIdRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<MembershipIdDTO> getIds() {
        return null;
    }

    @Override
    public List<MembershipIdDTO> getIds(int ms_id) {
        return null;
    }

    @Override
    public MembershipIdDTO getId(int ms_id) {
        String query = "SELECT * FROM membership_id WHERE ms_id=" +ms_id;
        return template.queryForObject(query, new MembershipIdRowMapper());
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
}
