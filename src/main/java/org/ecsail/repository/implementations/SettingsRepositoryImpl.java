package org.ecsail.repository.implementations;


import org.ecsail.dto.BoatListRadioDTO;
import org.ecsail.dto.DbBoatSettingsDTO;
import org.ecsail.dto.DbRosterSettingsDTO;
import org.ecsail.dto.MembershipListRadioDTO;
import org.ecsail.repository.interfaces.SettingsRepository;
import org.ecsail.repository.rowmappers.BoatListRadioRowMapper;
import org.ecsail.repository.rowmappers.DbBoatSettingsRowMapper;
import org.ecsail.repository.rowmappers.DbMembershipListRadioRowMapper;
import org.ecsail.repository.rowmappers.DbRosterSettingsRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class SettingsRepositoryImpl implements SettingsRepository {

    private JdbcTemplate template;

    public SettingsRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<DbRosterSettingsDTO> getSearchableListItems() {
        String query = "SELECT * FROM db_roster_settings";
        List<DbRosterSettingsDTO> dbMembershipListDTOS
                = template.query(query, new DbRosterSettingsRowMapper());
        return dbMembershipListDTOS;
    }

    @Override
    public List<MembershipListRadioDTO> getRadioChoices() {
        String query = "SELECT * FROM db_roster_radio_selection";
        List<MembershipListRadioDTO> dbMembershipListDTOS
                = template.query(query, new DbMembershipListRadioRowMapper());
        return dbMembershipListDTOS;
    }

    @Override
    public List<DbBoatSettingsDTO> getBoatSettings() {
        String query = "SELECT * from db_boat_list_settings";
        List<DbBoatSettingsDTO> dbBoatSettingsDTOS
                = template.query(query,new DbBoatSettingsRowMapper());
        return dbBoatSettingsDTOS;
    }

    @Override
    public List<BoatListRadioDTO> getBoatRadioChoices() {
        String query = "select * from db_boat_list_radio_selection";
        List<BoatListRadioDTO> dbBoatSettingsDTOS
                = template.query(query,new BoatListRadioRowMapper());
        return dbBoatSettingsDTOS;
    }
}
