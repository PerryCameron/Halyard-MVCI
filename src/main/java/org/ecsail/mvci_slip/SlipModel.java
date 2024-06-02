package org.ecsail.mvci_slip;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    private ObjectProperty<HBox> mainBox = new SimpleObjectProperty<>();
    private ObjectProperty<BorderPane> borderPane = new SimpleObjectProperty<>();
    private DoubleProperty dockWidth = new SimpleDoubleProperty();
    private DoubleProperty dockHeight = new SimpleDoubleProperty();
    private DoubleProperty dockPadding = new SimpleDoubleProperty();
    private DoubleProperty dockSpacing = new SimpleDoubleProperty();



















    public SlipModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    public double getDockSpacing() {
        return dockSpacing.get();
    }

    public DoubleProperty dockSpacingProperty() {
        return dockSpacing;
    }

    public void setDockSpacing(double dockSpacing) {
        this.dockSpacing.set(dockSpacing);
    }

    public double getDockPadding() {
        return dockPadding.get();
    }

    public DoubleProperty dockPaddingProperty() {
        return dockPadding;
    }

    public void setDockPadding(double dockPadding) {
        this.dockPadding.set(dockPadding);
    }

    public BorderPane getBorderPane() {
        return borderPane.get();
    }

    public ObjectProperty<BorderPane> borderPaneProperty() {
        return borderPane;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane.set(borderPane);
    }

    public double getDockHeight() {
        return dockHeight.get();
    }

    public DoubleProperty dockHeightProperty() {
        return dockHeight;
    }

    public void setDockHeight(double dockHeight) {
        this.dockHeight.set(dockHeight);
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

    public HBox getMainBox() {
        return mainBox.get();
    }

    public ObjectProperty<HBox> mainBoxProperty() {
        return mainBox;
    }

    public void setMainBox(HBox mainBox) {
        this.mainBox.set(mainBox);
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
