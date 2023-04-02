package org.ecsail.repository.implementations;

import com.ecsail.BaseApplication;
import com.ecsail.dto.DbBoatSettingsDTO;
import com.ecsail.dto.DbRosterSettingsDTO;
import com.ecsail.dto.MembershipListRadioDTO;
import com.ecsail.repository.interfaces.SettingsRepository;
import com.ecsail.repository.rowmappers.BoatListRadioRowMapper;
import com.ecsail.repository.rowmappers.DbBoatSettingsRowMapper;
import com.ecsail.repository.rowmappers.DbMembershipListRadioRowMapper;
import com.ecsail.repository.rowmappers.DbRosterSettingsRowMapper;
import com.ecsail.views.tabs.boatlist.BoatListRadioDTO;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class SettingsRepositoryImpl implements SettingsRepository {

    private JdbcTemplate template;

    public SettingsRepositoryImpl() {
        this.template = new JdbcTemplate(BaseApplication.getDataSource());
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
