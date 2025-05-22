package org.ecsail.mvci_slip;


import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.ecsail.fx.SlipInfoDTO;
import org.ecsail.fx.SlipStructureDTO;
import org.ecsail.mvci_main.MainModel;
import org.ecsail.static_tools.HttpClientUtil;

import java.util.ArrayList;

public class SlipModel {

    private final MainModel mainModel;
    private ArrayList<SlipStructureDTO> tempList = new ArrayList<>();
    private final ObservableList<SlipInfoDTO> slipInfoDTOS = FXCollections.observableArrayList();
    private final ObservableList<SlipStructureDTO> slipStructureDTOS = FXCollections.observableArrayList();
    private final ObjectProperty<HBox> mainBox = new SimpleObjectProperty<>();
    private final ObjectProperty<BorderPane> borderPane = new SimpleObjectProperty<>();
    private final DoubleProperty dockWidth = new SimpleDoubleProperty();
    private final DoubleProperty dockHeight = new SimpleDoubleProperty();
    private final DoubleProperty dockPadding = new SimpleDoubleProperty();
    private final DoubleProperty dockSpacing = new SimpleDoubleProperty();
    private final BooleanProperty listsLoaded = new SimpleBooleanProperty(false);
    private final IntegerProperty selectedMsId = new SimpleIntegerProperty(0);
    private final HttpClientUtil httpClient;



    public SlipModel(MainModel mainModel) {
        this.httpClient = mainModel.getHttpClient();
        this.mainModel = mainModel;
    }

    public int getSelectedMsId() {
        return selectedMsId.get();
    }

    public void setSelectedMsId(int selectedMsId) {
        this.selectedMsId.set(selectedMsId);
    }

    public ArrayList<SlipStructureDTO> getTempList() {
        return tempList;
    }

    public void setTempList(ArrayList<SlipStructureDTO> tempList) {
        this.tempList = tempList;
    }

    public BooleanProperty listsLoadedProperty() {
        return listsLoaded;
    }

    public void setListsLoaded(boolean listsLoaded) {
        this.listsLoaded.set(listsLoaded);
    }

    public double getDockSpacing() {
        return dockSpacing.get();
    }

    public void setDockTextSpacing(double dockSpacing) {
        this.dockSpacing.set(dockSpacing);
    }

    public double getDockPadding() {
        return dockPadding.get();
    }

    public void setDockPadding(double dockPadding) {
        this.dockPadding.set(dockPadding);
    }

    public BorderPane getBorderPane() {
        return borderPane.get();
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane.set(borderPane);
    }

    public double getDockHeight() {
        return dockHeight.get();
    }

    public void setDockHeight(double dockHeight) {
        this.dockHeight.set(dockHeight);
    }

    public double getDockWidth() {
        return dockWidth.get();
    }

    public void setDockWidth(double dockWidth) {
        this.dockWidth.set(dockWidth);
    }

    public HBox getMainBox() {
        return mainBox.get();
    }

    public void setMainBox(HBox mainBox) {
        this.mainBox.set(mainBox);
    }

    public MainModel getMainModel() {
        return mainModel;
    }

    public ObservableList<SlipInfoDTO> getSlipInfoDTOS() {
        return slipInfoDTOS;
    }

    public ObservableList<SlipStructureDTO> getSlipStructureDTOS() {
        return slipStructureDTOS;
    }

    public HttpClientUtil getHttpClient() {
        return httpClient;
    }
}
