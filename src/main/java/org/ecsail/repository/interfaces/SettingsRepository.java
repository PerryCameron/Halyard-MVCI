package org.ecsail.repository.interfaces;



import org.ecsail.fx.BoatListRadioDTO;
import org.ecsail.fx.DbBoatSettingsDTO;
import org.ecsail.fx.DbRosterSettingsDTO;
import org.ecsail.fx.MembershipListRadioDTO;

import java.util.List;

public interface SettingsRepository {

    List<DbRosterSettingsDTO> getSearchableListItems();
    List<MembershipListRadioDTO> getRadioChoices();
    List<DbBoatSettingsDTO> getBoatSettings();
    List<BoatListRadioDTO> getBoatRadioChoices();
}
