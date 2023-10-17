package org.ecsail.repository.implementations;


import org.ecsail.dto.DepositDTO;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.InvoiceItemDTO;
import org.ecsail.dto.InvoiceWithMemberInfoDTO;
import org.ecsail.repository.interfaces.InvoiceRepository;
import org.ecsail.repository.rowmappers.InvoiceItemRowMapper;
import org.ecsail.repository.rowmappers.InvoiceRowMapper;
import org.ecsail.repository.rowmappers.InvoiceWithMemberInfoRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InvoiceRepositoryImpl implements InvoiceRepository {
    private final JdbcTemplate template;

    public InvoiceRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<InvoiceDTO> getInvoicesByMsid(int msId) {
        String query = "SELECT * FROM invoice WHERE ms_id=?";
        return template.query(query, new InvoiceRowMapper(), msId);
    }

    @Override
    public List<InvoiceDTO> getAllInvoices() {
        String query = "SELECT * FROM invoice";
        return template.query(query, new InvoiceRowMapper());
    }

    @Override
    public List<InvoiceWithMemberInfoDTO> getInvoicesWithMembershipInfoByDeposit(DepositDTO d) {
        String query = "select mi.MEMBERSHIP_ID, p.L_NAME, p.F_NAME, i.* from invoice i " +
                "left join person p on i.MS_ID = p.MS_ID " +
                "left join membership_id mi on i.MS_ID = mi.MS_ID " +
                "where i.FISCAL_YEAR=? and mi.FISCAL_YEAR=? and p.MEMBER_TYPE=1 and i.COMMITTED=true and i.batch=?";
        return template.query(query, new InvoiceWithMemberInfoRowMapper(), new Object[]{d.getFiscalYear(), d.getFiscalYear(), d.getBatch()});
    }

    @Override
    public List<InvoiceWithMemberInfoDTO> getInvoicesWithMembershipInfoByYear(String year) {
        String query = "select mi.MEMBERSHIP_ID, p.L_NAME, p.F_NAME, i.* from invoice i " +
                "left join person p on i.MS_ID = p.MS_ID " +
                "left join membership_id mi on i.MS_ID = mi.MS_ID " +
                "where i.FISCAL_YEAR=? and mi.FISCAL_YEAR=? and p.MEMBER_TYPE=1 and i.COMMITTED=true";
        return template.query(query, new InvoiceWithMemberInfoRowMapper(), new Object[] {year,year});
    }

    @Override
    public List<InvoiceItemDTO> getInvoiceItemsByInvoiceId(int id) {
        String query = "SELECT * FROM invoice_item WHERE invoice_id=?";
        return template.query(query, new InvoiceItemRowMapper(), id);
    }

    @Override
    public  int getBatchNumber(String year) {
        String query = "SELECT MAX(batch) FROM invoice WHERE committed=true AND fiscal_year=:year";
        Map<String, Object> params = Collections.singletonMap("year", year);
        return Optional.ofNullable(template.queryForObject(query, Integer.class, params)).orElse(0);
    }
}
