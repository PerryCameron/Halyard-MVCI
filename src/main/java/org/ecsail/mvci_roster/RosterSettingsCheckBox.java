package org.ecsail.mvci_roster;

import javafx.scene.control.CheckBox;
import org.ecsail.dto.DbRosterSettingsDTO;

public class RosterSettingsCheckBox extends CheckBox {
    private DbRosterSettingsDTO dbRosterSettingsDTO;

    public RosterSettingsCheckBox(DbRosterSettingsDTO db, String mode) {
        this.dbRosterSettingsDTO = db;
        this.setText(db.getName());
        setListener(mode);
    }

    private void setListener(String mode) {
        if(mode.equals("searchable")) {
            this.setSelected(dbRosterSettingsDTO.isSearchable());
            this.selectedProperty().addListener((observable, oldValue, newValue) -> {
                dbRosterSettingsDTO.setSearchable(newValue);
            });
        }
        if(mode.equals("exportable")) {
            this.setSelected(dbRosterSettingsDTO.isExportable());
            this.selectedProperty().addListener((observable, oldValue, newValue) -> {
                dbRosterSettingsDTO.setExportable(newValue);
            });
        }
    }

    public String getDTOFieldName() {
        return dbRosterSettingsDTO.getPojo_name();
    }

    public boolean isSearchable() {
        return dbRosterSettingsDTO.isSearchable();
    }
}
