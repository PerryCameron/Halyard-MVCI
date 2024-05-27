package org.ecsail.repository.implementations;

import org.ecsail.dto.SlipDTO;
import org.ecsail.dto.SlipInfoDTO;
import org.ecsail.dto.SlipStructureDTO;
import org.ecsail.repository.interfaces.SlipRepository;
import org.ecsail.repository.rowmappers.SlipInfoRowMapper;
import org.ecsail.repository.rowmappers.SlipRowMapper;
import org.ecsail.repository.rowmappers.SlipStructureRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class SlipRepositoryImpl implements SlipRepository {

    private final JdbcTemplate template;

    public SlipRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public SlipDTO getSlip(int msId) {
        String sql = "select * from slip where MS_ID = ? or SUBLEASED_TO = ? LIMIT 1";
        try {
            return template.queryForObject(sql, new SlipRowMapper(), msId, msId);
        } catch (EmptyResultDataAccessException e) {
            return new SlipDTO();
        }
    }

    @Override
    public List<SlipInfoDTO> getSlipInfo() {
        String query = """
                select id.membership_id AS owner_id,
                       p.F_NAME AS owner_first_name,
                       p.L_NAME AS owner_last_name,
                       s.MS_ID AS owner_msid,
                       SLIP_NUM AS slip_number,
                       sid.MEMBERSHIP_ID AS subleaser_id,
                       SUBLEASED_TO AS subleaser_msid,
                       po.F_NAME AS subleaser_first_name,
                       po.L_NAME AS subleaser_last_name,
                       ALT_TEXT AS alt_text
                from slip s
                left join membership m on m.MS_ID = s.MS_ID
                left join membership_id id on id.MS_ID=m.MS_ID AND id.FISCAL_YEAR = YEAR(NOW())
                left join person p on p.P_ID=m.P_ID
                left join membership slm on slm.MS_ID=s.SUBLEASED_TO
                left join person po on po.P_ID=slm.P_ID
                left join membership_id sid on sid.MS_ID=SUBLEASED_TO
                group by SLIP_NUM;
                """;
        return template.query(query, new SlipInfoRowMapper());
    }
    @Override
    public List<SlipStructureDTO> getSlipStructure() {
        String query = "SELECT * FROM slip_structure";
        return template.query(query, new SlipStructureRowMapper());
    }
}
