package org.ecsail.mvci_slip;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
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
    private ObjectProperty<BorderPane> slipPane = new SimpleObjectProperty<>();
    private DoubleProperty dockLength = new SimpleDoubleProperty();
    private DoubleProperty dockWidth = new SimpleDoubleProperty();




















    public SlipModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    public double getDockWidth() {
        return dockWidth.get();
    }

    public DoubleProperty dockWidthProperty() {
        return dockWidth;
    }

    public void setDockWidth(double dockWidth) {
        this.dockWidth.set(dockWidth);
    }

    public double getDockLength() {
        return dockLength.get();
    }

    public DoubleProperty dockLengthProperty() {
        return dockLength;
    }

    public void setDockLength(double dockLength) {
        this.dockLength.set(dockLength);
    }

    public BorderPane getSlipPane() {
        return slipPane.get();
    }

    public ObjectProperty<BorderPane> slipPaneProperty() {
        return slipPane;
    }

    public void setSlipPane(BorderPane slipPane) {
        this.slipPane.set(slipPane);
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
