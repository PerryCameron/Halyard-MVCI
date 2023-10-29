package org.ecsail.repository.implementations;


import org.ecsail.dto.*;
import org.ecsail.repository.interfaces.InvoiceRepository;
import org.ecsail.repository.rowmappers.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InvoiceRepositoryImpl implements InvoiceRepository {
    private final JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public InvoiceRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
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
    public List<PaymentDTO> getPaymentByInvoiceId(int id) {
        String query = "SELECT * from payment where INVOICE_ID=?";
        return template.query(query, new PaymentRowMapper(), id);
    }

    @Override
    public  int getBatchNumber(String year) {
        String query = "SELECT MAX(batch) FROM invoice WHERE committed=true AND fiscal_year=:year";
        Map<String, Object> params = Collections.singletonMap("year", year);
        return Optional.ofNullable(template.queryForObject(query, Integer.class, params)).orElse(0);
    }

    @Override
    public List<FeeDTO> getFeesFromYear(int year) {
        String query = "SELECT * FROM fee WHERE fee_year=?";
        return template.query(query, new FeeRowMapper(), year);
    }

    @Override
    public List<DbInvoiceDTO> getDbInvoiceByYear(int year) {
        String query = "SELECT * FROM db_invoice WHERE FISCAL_YEAR=?";
        return template.query(query, new DbInvoiceRowMapper(), year);
    }

    @Override
    public int update(InvoiceItemDTO invoiceItemDTO) {
        String query = "UPDATE invoice_item SET " +
                "INVOICE_ID = :invoiceId, " +
                "MS_ID = :msId, " +
                "FISCAL_YEAR = :year, " +
                "FIELD_NAME = :fieldName, " + // added comma
                "IS_CREDIT = :credit, " + // added comma
                "VALUE = :value, " + // added comma
                "QTY = :qty, " + // added comma
                "CATEGORY = :category, " + // added comma
                "CATEGORY_ITEM = :categoryItem " +
                "WHERE ID = :id";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(invoiceItemDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }

    @Override
    public int update(InvoiceDTO invoiceDTO) {
        String query = "UPDATE invoice SET " +
                "MS_ID = :msId, " +
                "FISCAL_YEAR = :year, " +
                "PAID = :paid, " +
                "TOTAL = :total, " +
                "CREDIT = :credit, " +
                "BALANCE = :balance, " +
                "BATCH = :batch, " +
                "COMMITTED = :committed, " +
                "CLOSED = :closed, " +
                "SUPPLEMENTAL = :supplemental, " +
                "MAX_CREDIT = :maxCredit " +
                "WHERE ID = :id";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(invoiceDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }
}
