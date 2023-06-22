package org.ecsail.mvci_boat;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.dto.BoatListDTO;
import org.ecsail.dto.DbBoatSettingsDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.mvci_main.MainModel;

import java.util.ArrayList;

public class BoatModel {

    private final MainModel mainModel;
    private final SimpleObjectProperty<BoatListDTO> boatListDTO = new SimpleObjectProperty<>();
    private ObservableList<MembershipListDTO> boatOwners = FXCollections.observableArrayList();
    private ArrayList<DbBoatSettingsDTO> boatSettings = new ArrayList<>();
    private final BooleanProperty dataLoaded = new SimpleBooleanProperty(false);





    public boolean isDataLoaded() {
        return dataLoaded.get();
    }

    public BooleanProperty dataLoadedProperty() {
        return dataLoaded;
    }

    public void setDataLoaded(boolean dataLoaded) {
        this.dataLoaded.set(dataLoaded);
    }

    public ArrayList<DbBoatSettingsDTO> getBoatSettings() {
        return boatSettings;
    }

    public void setBoatSettings(ArrayList<DbBoatSettingsDTO> boatSettings) {
        this.boatSettings = boatSettings;
    }

    public BoatListDTO getBoatListDTO() {
        return boatListDTO.get();
    }

    public SimpleObjectProperty<BoatListDTO> boatListDTOProperty() {
        return boatListDTO;
    }

    public void setBoatListDTO(BoatListDTO boatListDTO) {
        this.boatListDTO.set(boatListDTO);
    }

    public BoatModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }
    public MainModel getMainModel() {
        return mainModel;
    }
}
