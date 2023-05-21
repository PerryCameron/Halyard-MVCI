package org.ecsail.repository.implementations;


import org.ecsail.dto.InvoiceWithMemberInfoDTO;
import org.ecsail.dto.Memo2DTO;
import org.ecsail.dto.NotesDTO;
import org.ecsail.repository.interfaces.NotesRepository;
import org.ecsail.repository.rowmappers.Memo2RowMapper;
import org.ecsail.repository.rowmappers.MemoRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class NotesRepositoryImpl implements NotesRepository {
    private JdbcTemplate template;

    public NotesRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<NotesDTO> getMemosByMsId(int msId) {
        String query = "SELECT * FROM memo";
        if (msId != 0) query += " WHERE ms_id=?";
        List<NotesDTO> notesDTOS =
                template.query(query, new MemoRowMapper(), new Object[]{msId});
        return notesDTOS;
    }

    @Override
    public List<NotesDTO> getMemosByBoatId(int boatId) {
        String query = "SELECT * FROM memo WHERE boat_id=?";
        List<NotesDTO> notesDTOS =
                template.query(query, new MemoRowMapper(), new Object[]{boatId});
        return notesDTOS;
    }

    @Override
    public NotesDTO getMemoByInvoiceIdAndCategory(InvoiceWithMemberInfoDTO invoice, String category) {
                String query = "SELECT * FROM memo WHERE INVOICE_ID=" + invoice.getId() + " AND category='" + category + "'";

//        String query = "SELECT * FROM memo WHERE INVOICE_ID=:invoiceId AND category=:category";
//        SqlParameterSource params = new MapSqlParameterSource()
//                .addValue("invoiceId", invoice.getId())
//                .addValue("category", category);
        NotesDTO notesDTO =
                template.queryForObject(query, new MemoRowMapper());
        return notesDTO;
    }

//    @Override
//    public List<Memo2DTO> getAllMemosForTabNotes(String year, String category) {
//        String fiscalYear = year;
//        String query = "SELECT * FROM memo "
//                + "LEFT JOIN membership_id id on memo.ms_id=id.ms_id "
//                + "WHERE YEAR(memo_date)=:year and id.fiscal_year=:fiscalYear and memo.CATEGORY IN(:category)";
//        SqlParameterSource namedParameters = new MapSqlParameterSource()
//                .addValue("year", year)
//                .addValue("fiscalYear", fiscalYear)
//                .addValue("category", category);
//        List<Memo2DTO> memoDTOs =
//                template.query(query, new Memo2RowMapper(), namedParameters);
//        return memoDTOs;
//    }

    @Override
    public List<Memo2DTO> getAllMemosForTabNotes(String year, String category) {
        String query = "SELECT * FROM memo "
                + "LEFT JOIN membership_id id on memo.ms_id=id.ms_id "
                + "WHERE YEAR(memo_date)='"+year+"' and id.fiscal_year='"+year+"' and memo.CATEGORY IN("+category+")";
        List<Memo2DTO> memoDTOs =
                template.query(query, new Memo2RowMapper());
        return memoDTOs;
    }
}
