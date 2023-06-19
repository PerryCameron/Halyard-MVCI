package org.ecsail.mvci_boat;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.ecsail.dto.BoatListDTO;
import org.ecsail.dto.BoatListRadioDTO;
import org.ecsail.dto.DbBoatSettingsDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.mvci_boatlist.BoatListRadioHBox;
import org.ecsail.mvci_boatlist.BoatListSettingsCheckBox;
import org.ecsail.mvci_main.MainModel;

import java.util.ArrayList;

public class BoatModel {

    private final MainModel mainModel;
    private final SimpleObjectProperty<BoatListDTO> boatList = new SimpleObjectProperty<>();
    private ObservableList<MembershipListDTO> boatOwners = FXCollections.observableArrayList();





    public BoatListDTO getBoatList() {
        return boatList.get();
    }

    public SimpleObjectProperty<BoatListDTO> boatListProperty() {
        return boatList;
    }

    public void setBoatList(BoatListDTO boatList) {
        this.boatList.set(boatList);
    }

    public BoatModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }
    public MainModel getMainModel() {
        return mainModel;
    }
}
