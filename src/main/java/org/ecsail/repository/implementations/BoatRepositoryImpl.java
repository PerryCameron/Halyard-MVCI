package org.ecsail.repository.implementations;


import org.ecsail.dto.*;
import org.ecsail.repository.interfaces.BoatRepository;
import org.ecsail.repository.rowmappers.BoatListRowMapper;
import org.ecsail.repository.rowmappers.BoatOwnerRowMapper;
import org.ecsail.repository.rowmappers.BoatPhotosRowMapper;
import org.ecsail.repository.rowmappers.BoatRowMapper;
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
import java.util.Objects;

public class BoatRepositoryImpl implements BoatRepository {
    private final JdbcTemplate template;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(BoatRepositoryImpl.class);


    public BoatRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<BoatListDTO> getActiveSailBoats() {
        String query = """
                SELECT id.membership_id,id.ms_id, p.l_name, p.f_name,b.*,nb.boat_count\s
                FROM (SELECT * FROM membership_id WHERE FISCAL_YEAR=year(now()) and RENEW=true) id\s
                LEFT JOIN (SELECT * FROM person WHERE MEMBER_TYPE=1) p on id.MS_ID=p.MS_ID\s
                INNER JOIN boat_owner bo on id.MS_ID=bo.MS_ID\s
                INNER JOIN (SELECT * FROM boat WHERE AUX=false) b on bo.BOAT_ID=b.BOAT_ID\s
                LEFT JOIN (SELECT BOAT_ID,count(BOAT_ID) AS boat_count\s
                FROM boat_photos group by BOAT_ID having count(BOAT_ID) > 0) nb on b.BOAT_ID=nb.BOAT_ID
                """;
        return template.query(query, new BoatListRowMapper());
    }

    @Override
    public List<BoatListDTO> getActiveAuxBoats() {
        String query = """
                SELECT id.membership_id,id.ms_id, p.l_name, p.f_name,b.*,nb.boat_count
                FROM (SELECT * FROM membership_id WHERE FISCAL_YEAR=year(now()) and RENEW=true) id
                LEFT JOIN (SELECT * FROM person WHERE MEMBER_TYPE=1) p on id.MS_ID=p.MS_ID
                INNER JOIN boat_owner bo on id.MS_ID=bo.MS_ID
                INNER JOIN (SELECT * FROM boat WHERE AUX=true) b on bo.BOAT_ID=b.BOAT_ID
                LEFT JOIN (SELECT BOAT_ID,count(BOAT_ID) AS boat_count
                FROM boat_photos group by BOAT_ID having count(BOAT_ID) > 0) nb on b.BOAT_ID=nb.BOAT_ID;
                """;
        return template.query(query, new BoatListRowMapper());
    }

    @Override
    public List<BoatListDTO> getAllSailBoats() {
        String query = """
                SELECT id.membership_id,id.ms_id, p.l_name, p.f_name,b.*,nb.boat_count
                FROM (select * from boat where AUX=false) b
                LEFT JOIN boat_owner bo on b.BOAT_ID = bo.BOAT_ID
                LEFT JOIN membership m on bo.MS_ID = m.MS_ID
                LEFT JOIN (SELECT * FROM membership_id where FISCAL_YEAR=(select year(now()))) id on bo.MS_ID=id.MS_ID
                LEFT JOIN person p on m.P_ID = p.P_ID
                LEFT JOIN (select BOAT_ID,count(BOAT_ID)
                AS boat_count from boat_photos group by BOAT_ID having count(BOAT_ID) > 0) nb
                ON b.BOAT_ID=nb.BOAT_ID;
                """;
        return template.query(query, new BoatListRowMapper());
    }

    @Override
    public List<BoatListDTO> getAllAuxBoats() {
        String query = """
                SELECT id.membership_id,id.ms_id, p.l_name, p.f_name,b.*,nb.boat_count
                FROM (select * from boat where AUX=true) b
                LEFT JOIN boat_owner bo on b.BOAT_ID = bo.BOAT_ID
                LEFT JOIN membership m on bo.MS_ID = m.MS_ID
                LEFT JOIN (SELECT * FROM membership_id where FISCAL_YEAR=(select year(now()))) id on bo.MS_ID=id.MS_ID
                LEFT JOIN person p on m.P_ID = p.P_ID LEFT JOIN (select BOAT_ID,count(BOAT_ID)
                AS boat_count from boat_photos group by BOAT_ID having count(BOAT_ID) > 0) nb on b.BOAT_ID=nb.BOAT_ID;
                """;
        return template.query(query, new BoatListRowMapper());
    }

    @Override
    public List<BoatListDTO> getAllBoats() {
        String query = """
                SELECT id.membership_id,id.ms_id, p.l_name, p.f_name,b.*,nb.boat_count FROM boat b
                LEFT JOIN boat_owner bo on b.BOAT_ID = bo.BOAT_ID
                LEFT JOIN membership m on bo.MS_ID = m.MS_ID
                LEFT JOIN (SELECT * FROM membership_id where FISCAL_YEAR=(select year(now()))) id on bo.MS_ID=id.MS_ID
                LEFT JOIN person p on m.P_ID = p.P_ID
                LEFT JOIN (select BOAT_ID,count(BOAT_ID) AS boat_count from boat_photos
                GROUP BY BOAT_ID having count(BOAT_ID) > 0) nb on b.BOAT_ID=nb.BOAT_ID
                """;
        return template.query(query, new BoatListRowMapper());
    }

    @Override
    public List<BoatDTO> getBoatsByMsId(int msId) {
        String query = """
                SELECT b.boat_id, bo.ms_id, b.manufacturer, b.manufacture_year, b.registration_num, b.model,
                b.boat_name, b.sail_number, b.has_trailer, b.length, b.weight, b.keel, b.phrf, b.draft, b.beam,
                b.lwl, b.aux FROM boat b INNER JOIN boat_owner bo USING (boat_id) WHERE ms_id=?
                """;
        return template.query(query, new BoatRowMapper(), msId);
    }

    @Override
    public List<BoatDTO> getOnlySailboatsByMsId(int msId) {
        String query = """
                Select BOAT_ID, MS_ID, ifnull(MANUFACTURER,'') AS MANUFACTURER, ifnull(MANUFACTURE_YEAR,'') AS
                MANUFACTURE_YEAR, ifnull(REGISTRATION_NUM,'') AS REGISTRATION_NUM, ifnull(MODEL,'') AS MODEL,
                ifnull(BOAT_NAME,'') AS BOAT_NAME, ifnull(SAIL_NUMBER,'') AS SAIL_NUMBER, HAS_TRAILER,
                ifnull(LENGTH,'') AS LENGTH, ifnull(WEIGHT,'') AS WEIGHT, KEEL, ifnull(PHRF,'') AS PHRF, ifnull(DRAFT,'') AS DRAFT,
                ifnull(BEAM,'') AS BEAM, ifnull(LWL,'') AS LWL, AUX from boat
                INNER JOIN boat_owner USING (boat_id) WHERE ms_id=? and
                MODEL NOT LIKE 'Kayak' and MODEL NOT LIKE 'Canoe' and MODEL NOT LIKE 'Row Boat' and
                MODEL NOT LIKE 'Paddle Board'
                                """;
        return template.query(query, new BoatRowMapper(), msId);
    }

    @Override
    public List<BoatOwnerDTO> getBoatOwnersByBoatId(int boatId) {
        String query = "SELECT * FROM boat_owner WHERE BOAT_ID=?";
        return template.query(query, new BoatOwnerRowMapper(), boatId);
    }



    @Override
    public int update(BoatDTO boatDTO) {
        String query = "UPDATE boat SET " +
                "MANUFACTURER = :manufacturer, " +
                "MANUFACTURE_YEAR = :manufactureYear, " +
                "REGISTRATION_NUM = :registrationNum, " +
                "MODEL = :model, " +
                "BOAT_NAME = :boatName, " +
                "SAIL_NUMBER = :sailNumber, " +
                "HAS_TRAILER = :hasTrailer, " +
                "LENGTH = :loa, " +
                "WEIGHT = :displacement, " +
                "KEEL = :keel, " +
                "PHRF = :phrf, " +
                "DRAFT = :draft, " +
                "BEAM = :beam, " +
                "LWL = :lwl, " +
                "AUX = :aux " +
                "WHERE BOAT_ID = :boatId ";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(boatDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }

    @Override
    public int update(BoatListDTO boatListDTO) {
        String query = "UPDATE boat SET " +
                "MANUFACTURER = :manufacturer, " +
                "MANUFACTURE_YEAR = :manufactureYear, " +
                "REGISTRATION_NUM = :registrationNum, " +
                "MODEL = :model, " +
                "BOAT_NAME = :boatName, " +
                "SAIL_NUMBER = :sailNumber, " +
                "HAS_TRAILER = :hasTrailer, " +
                "LENGTH = :loa, " +
                "WEIGHT = :displacement, " +
                "KEEL = :keel, " +
                "PHRF = :phrf, " +
                "DRAFT = :draft, " +
                "BEAM = :beam, " +
                "LWL = :lwl, " +
                "AUX = :aux " +
                "WHERE BOAT_ID = :boatId ";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(boatListDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }


    @Override
    public int updateAux(boolean aux, int boatId) {
        String query = "UPDATE boat SET " +
                "AUX = ? " +
                "WHERE BOAT_ID = ? ";
        return template.update(query, aux, boatId);
    }

    @Override
    public int delete(BoatDTO boatDTO) {
        String deleteSql = "DELETE FROM boat WHERE BOAT_ID = ?";
        return template.update(deleteSql, boatDTO.getBoatId());
    }

    @Override
    public int insert(BoatDTO boatDTO) {
        validateObject(boatDTO);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO boat (MANUFACTURER, " +
                "MANUFACTURE_YEAR, REGISTRATION_NUM, MODEL, BOAT_NAME, SAIL_NUMBER, HAS_TRAILER, LENGTH, " +
                "WEIGHT, KEEL, PHRF, DRAFT, BEAM, LWL, AUX) " +
                "VALUES (:manufacturer, :manufactureYear, :registrationNum, :model, :boatName, :sailNumber, " +
                ":hasTrailer, :loa, :displacement, :keel, :phrf, :draft, :beam, :lwl, :aux)";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(boatDTO);
        int affectedRows = namedParameterJdbcTemplate.update(query, namedParameters, keyHolder);
        boatDTO.setBoatId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return affectedRows;
    }

    @Override
    public int insert(BoatPhotosDTO boatPhotosDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO boat_photos (BOAT_ID, " +
                "upload_date, filename, file_number, default_image) " +
                "VALUES (:boatId, NOW(), :filename, :fileNumber, :isDefault)";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(boatPhotosDTO);
        int affectedRows = namedParameterJdbcTemplate.update(query, namedParameters, keyHolder);
        boatPhotosDTO.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return affectedRows;
    }

    @Override
    public List<BoatPhotosDTO> getImagesByBoatId(int boatId) {
        String query = "SELECT * FROM boat_photos WHERE BOAT_ID=?";
        return template.query(query, new BoatPhotosRowMapper(), boatId);
    }

    @Override
    public int delete(BoatPhotosDTO boatPhotosDTO) {
        String deleteSql = "DELETE FROM boat_photos WHERE ID = ?";
        return template.update(deleteSql, boatPhotosDTO.getId());
    }

    @Override
    public int update(BoatPhotosDTO boatPhotosDTO) {
        String query = "UPDATE boat_photos SET " +
                "BOAT_ID = :boatId, " +
                "upload_date = :uploadDate, " +
                "filename = :filename, " +
                "file_number = :fileNumber, " +
                "default_image = :isDefault " +
                "WHERE ID = :id ";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(boatPhotosDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }

    private static void validateObject(BoatDTO boatDTO) {
        if (boatDTO.getPhrf() == null || boatDTO.getPhrf().isEmpty()) {
            boatDTO.setPhrf("0");  // default value
        } else {
            try {
                Integer.parseInt(boatDTO.getPhrf());
            } catch (NumberFormatException e) {
                boatDTO.setPhrf("0");  // default value
            }
        }
    }

    @Override
    public int insertOwner(BoatOwnerDTO boatOwnerDTO) {
        String query = "INSERT INTO boat_owner (MS_ID, BOAT_ID) " +
                "VALUES (:msId, :boatId)";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(boatOwnerDTO);
        return namedParameterJdbcTemplate.update(query, namedParameters);
    }

    @Override
    public int deleteBoatOwner(MembershipListDTO membershipListDTO, BoatListDTO boatListDTO) {
        String sql = "DELETE FROM boat_owner WHERE MS_ID = ? and BOAT_ID = ?";
            return template.update(sql, membershipListDTO.getMsId(), boatListDTO.getBoatId());
    }

    @Override
    public int deleteBoatOwner(MembershipListDTO membershipListDTO, BoatDTO boatDTO) {
        String sql = "DELETE FROM boat_owner WHERE MS_ID = ? and BOAT_ID = ?";
        return template.update(sql, membershipListDTO.getMsId(), boatDTO.getBoatId());
    }

    @Override
    public int setAllDefaultImagesToFalse(int boatId) {
        String query = "UPDATE boat_photos SET default_image = false WHERE BOAT_ID = ?";
        return template.update(query, boatId);
    }

    @Override
    public int setDefaultImageTrue(int id) {
        String query = "UPDATE boat_photos SET default_image = true WHERE ID = ?";
        return template.update(query, id);
    }

    @Override
    public int deleteBoatOwner(int msId) {
        String sql = "DELETE FROM boat_owner WHERE MS_ID = ?";
        try {
            template.update(sql, msId);
        } catch (DataAccessException e) {
            logger.error("Unable to DELETE boat owner: " + e.getMessage());
        }
        return msId;
    }
}
