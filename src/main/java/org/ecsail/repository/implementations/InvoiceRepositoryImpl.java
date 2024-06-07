package org.ecsail.repository.implementations;


import org.ecsail.dto.*;
import org.ecsail.repository.interfaces.InvoiceRepository;
import org.ecsail.repository.rowmappers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.util.*;

public class InvoiceRepositoryImpl implements InvoiceRepository {
    private final JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(InvoiceRepositoryImpl.class);


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
    public int insert(PaymentDTO paymentDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
                String query = "INSERT INTO payment ( " +
                        "INVOICE_ID, " +
                        "CHECK_NUMBER, " +
                        "PAYMENT_TYPE, " +
                        "PAYMENT_DATE, " +
                        "AMOUNT, " +
                        "DEPOSIT_ID) " +
                "VALUES (:invoiceId, :checkNumber, :paymentType, :paymentDate, :PaymentAmount, :depositId)";
                SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(paymentDTO);
                int affectedRows = namedParameterJdbcTemplate.update(query, namedParameters, keyHolder);
        paymentDTO.setPayId(keyHolder.getKey().intValue());
        return affectedRows;
    }

    public int insert(InvoiceDTO invoiceDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO invoice ( " +
                "MS_ID, FISCAL_YEAR, PAID, TOTAL, CREDIT, BALANCE, BATCH, COMMITTED, CLOSED, " +
                "SUPPLEMENTAL, MAX_CREDIT)" +
                "VALUES (:msId, :year, :paid, :total, :credit, :balance, :batch, :committed, :closed, " +
                ":supplemental, :maxCredit)";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(invoiceDTO);
        int affectedRows = namedParameterJdbcTemplate.update(query, namedParameters, keyHolder);
        invoiceDTO.setId(keyHolder.getKey().intValue());
        return affectedRows;
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



    @Override
    public int[] updateBatch(InvoiceDTO invoiceDTO) {
        List<InvoiceItemDTO> invoiceItems = invoiceDTO.getInvoiceItemDTOS();
        String query = "UPDATE invoice_item SET " +
                "INVOICE_ID = :invoiceId, " +
                "MS_ID = :msId, " +
                "FISCAL_YEAR = :year, " +
                "FIELD_NAME = :fieldName, " +
                "IS_CREDIT = :credit, " +
                "VALUE = :value, " +
                "QTY = :qty, " +
                "CATEGORY = :category, " +
                "CATEGORY_ITEM = :categoryItem " +
                "WHERE ID = :id";
        List<SqlParameterSource> parameters = new ArrayList<>();
        for (InvoiceItemDTO invoiceItemDTO : invoiceItems) {
            parameters.add(new BeanPropertySqlParameterSource(invoiceItemDTO));
        }
        int[] updateResults = namedParameterJdbcTemplate.batchUpdate(query, parameters.toArray(new SqlParameterSource[0]));
        int[] number = new int[updateResults.length + 1];
        System.arraycopy(updateResults, 0, number, 0, updateResults.length);
        if(Arrays.stream(updateResults).sum() == invoiceItems.size())
            number[updateResults.length] = update(invoiceDTO);
        return number;
    }

    @Override
    public int[] insertBatch(InvoiceDTO invoiceDTO) {
        List<InvoiceItemDTO> invoiceItems = invoiceDTO.getInvoiceItemDTOS();
        String query = "INSERT INTO invoice_item " +
                "(INVOICE_ID, MS_ID, FISCAL_YEAR, FIELD_NAME, IS_CREDIT, VALUE, QTY, CATEGORY, CATEGORY_ITEM) " +
                "VALUES (:invoiceId, :msId, :year, :fieldName, :credit, :value, :qty, :category, :categoryItem)";

        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(invoiceItems.toArray());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int[] updateResults = new int[invoiceItems.size()];

        for (int i = 0; i < batch.length; i++) {
            namedParameterJdbcTemplate.update(query, batch[i], keyHolder);

            // Retrieve the generated key for the current row
            if (keyHolder.getKeys() != null) {
                Number key = (Number) keyHolder.getKeys().get("ID"); // insert generated number into ID column
                if (key != null) {
                    invoiceItems.get(i).setId(key.intValue());
                }
            }
            updateResults[i] = 1; // Assuming that each insert will affect one row
        }
        return updateResults;
    }


    @Override
    public int update(PaymentDTO paymentDTO) {
        String query = "UPDATE payment set " +
                "INVOICE_ID = :invoiceId, " +
                "CHECK_NUMBER = :checkNumber, " +
                "PAYMENT_TYPE = :paymentType, " +
                "PAYMENT_DATE = :paymentDate, " +
                "AMOUNT = :PaymentAmount, " +
                "DEPOSIT_ID = :depositId " +
                "WHERE PAY_ID = :payId";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(paymentDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }

    @Override
    public int delete(PaymentDTO paymentDTO) {
        String query = "DELETE FROM payment WHERE PAY_ID = ?";
        return template.update(query, paymentDTO.getPayId());
    }

    @Override
    public boolean exists(MembershipListDTO membershipListDTO, int year) {
        String query = "SELECT EXISTS(SELECT * FROM invoice WHERE ms_id=? AND fiscal_year=?)";
        return template.queryForObject(query, Boolean.class, membershipListDTO.getMsId(), year);
    }

    @Override
    public Set<FeeDTO> getRelatedFeesAsInvoiceItems(DbInvoiceDTO dbInvoiceDTO) {
        String query = "select * from fee where db_invoice_id=?";
        List<FeeDTO> feeDTOS = template.query(query, new FeeRowMapper(), dbInvoiceDTO.getId());
        return new HashSet<>(feeDTOS);
    }

    @Override
    public int deletePaymentsByInvoiceID(InvoiceDTO invoiceDTO) {
        String query = "DELETE FROM payment WHERE INVOICE_ID = ?";
        return template.update(query, invoiceDTO.getId());
    }

    @Override
    public int deleteItemsByInvoiceID(InvoiceDTO invoiceDTO) {
        String query = "DELETE FROM invoice_item WHERE INVOICE_ID = ?";
        return template.update(query, invoiceDTO.getId());
    }
    @Override
    public int delete(InvoiceDTO invoiceDTO) {
        String query = "DELETE FROM invoice WHERE ID = ?";
        return template.update(query, invoiceDTO.getId());
    }

    @Override
    public int deletePaymentByInvoiceID(int invoiceId) {
        String sql = "DELETE FROM payment WHERE invoice_id = ?";
        executeDelete(sql, invoiceId);
        return invoiceId;
    }
    @Override
    public int deleteInvoiceItemByInvoiceID(int invoiceId) {
        String sql = "DELETE FROM invoice_item WHERE invoice_id = ?";
        executeDelete(sql, invoiceId);
        return invoiceId;
    }
    @Override
    public int deleteInvoiceByID(int invoiceId) {
        String sql = "DELETE FROM invoice WHERE id = ?";
        return executeDelete(sql, invoiceId);
    }

    @Override
    public int[] deleteAllPaymentsAndInvoicesByMsId(int msId) {
        int d[] = new int[3];
        List<InvoiceDTO> invoices = getInvoicesByMsid(msId);
        invoices.forEach(invoiceDTO -> {
            d[0] = deletePaymentByInvoiceID(invoiceDTO.getId());
            d[1] = deleteInvoiceItemByInvoiceID(invoiceDTO.getId());
            d[2] = deleteInvoiceByID(invoiceDTO.getId());
        });
        return d;
    }

    private int executeDelete(String sql, int id) {
        try {
            return template.update(sql, id);
        } catch (DataAccessException e) {
            logger.error("Unable to DELETE: " + e.getMessage());
        }
        return 0;
    }
}
