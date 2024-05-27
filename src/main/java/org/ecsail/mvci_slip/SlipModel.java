package org.ecsail.mvci_slip;


import javafx.collections.ObservableList;
import org.ecsail.dto.DockPlacementDTO;
import org.ecsail.dto.SlipInfoDTO;
import org.ecsail.dto.SlipStructureDTO;
import org.ecsail.mvci_main.MainModel;

import java.util.ArrayList;

public class SlipModel {

    private final MainModel mainModel;
    private ArrayList<DockPlacementDTO> dockPlacement;
    private ObservableList<SlipInfoDTO> slipInfoDTOS;
    private ObservableList<SlipStructureDTO> slipStructureDTOS;




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
}
