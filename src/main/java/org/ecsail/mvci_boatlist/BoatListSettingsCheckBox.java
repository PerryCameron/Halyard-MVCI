package org.ecsail.mvci_boatlist;


import javafx.scene.control.CheckBox;
import org.ecsail.dto.DbBoatSettingsDTO;

public class BoatListSettingsCheckBox extends CheckBox {
    private DbBoatSettingsDTO db;
    public BoatListSettingsCheckBox(DbBoatSettingsDTO db, String mode) {
        this.db = db;
        this.setText(db.getName());
        setListener(mode);
    }

    private void setListener(String mode) {
        if(mode.equals("searchable")) {
            this.setSelected(db.isSearchable());
            this.selectedProperty().addListener((observable, oldValue, newValue) -> {
                db.setSearchable(newValue);
            });
        }
        if(mode.equals("exportable")) {
            this.setSelected(db.isExportable());
            this.selectedProperty().addListener((observable, oldValue, newValue) -> {
                db.setExportable(newValue);
            });
        }
    }

    public String getDTOFieldName() {
        return db.getFieldName();
    }

    public boolean isSearchable() {
        return db.isSearchable();
    }

    public DbBoatSettingsDTO getDb() {
        return db;
    }
}
