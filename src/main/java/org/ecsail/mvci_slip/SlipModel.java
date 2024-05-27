package org.ecsail.mvci_slip;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.dto.DockPlacementDTO;
import org.ecsail.dto.SlipInfoDTO;
import org.ecsail.dto.SlipStructureDTO;
import org.ecsail.mvci_main.MainModel;

import java.util.ArrayList;

public class SlipModel {

    private final MainModel mainModel;
    private ArrayList<DockPlacementDTO> dockPlacement = new ArrayList<>();
    private ObservableList<SlipInfoDTO> slipInfoDTOS = FXCollections.observableArrayList();
    private ObservableList<SlipStructureDTO> slipStructureDTOS = FXCollections.observableArrayList();




    public SlipModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    public ArrayList<DockPlacementDTO> getDockPlacement() {
        return dockPlacement;
    }

    public void setDockPlacement(ArrayList<DockPlacementDTO> dockPlacement) {
        this.dockPlacement = dockPlacement;
    }

    public MainModel getMainModel() {
        return mainModel;
    }

    public ObservableList<SlipInfoDTO> getSlipInfoDTOS() {
        return slipInfoDTOS;
    }

    public void setSlipInfoDTOS(ObservableList<SlipInfoDTO> slipInfoDTOS) {
        this.slipInfoDTOS = slipInfoDTOS;
    }

    public ObservableList<SlipStructureDTO> getSlipStructureDTOS() {
        return slipStructureDTOS;
    }

    public void setSlipStructureDTOS(ObservableList<SlipStructureDTO> slipStructureDTOS) {
        this.slipStructureDTOS = slipStructureDTOS;
    }
}
