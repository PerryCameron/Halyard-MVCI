package org.ecsail.repository.interfaces;



import org.ecsail.dto.BoatListRadioDTO;
import org.ecsail.dto.DbBoatSettingsDTO;
import org.ecsail.dto.DbRosterSettingsDTO;
import org.ecsail.dto.MembershipListRadioDTO;

import java.util.List;

public interface SettingsRepository {

    List<DbRosterSettingsDTO> getSearchableListItems();
    List<MembershipListRadioDTO> getRadioChoices();
    List<DbBoatSettingsDTO> getBoatSettings();
    List<BoatListRadioDTO> getBoatRadioChoices();
}
