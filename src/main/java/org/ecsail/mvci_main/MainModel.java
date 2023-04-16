package org.ecsail.mvci_main;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import org.ecsail.dto.BoardPositionDTO;

public class MainModel {

    private ObservableList<BoardPositionDTO> boardPositionDTOS = FXCollections.observableArrayList();
    private final StringProperty statusLabel = new SimpleStringProperty(""); // keeper here
    private final ObjectProperty<TabPane> mainTabPane = new SimpleObjectProperty();


    public ObservableList<BoardPositionDTO> getBoardPositionDTOS() {
        return boardPositionDTOS;
    }
    public void setBoardPositionDTOS(ObservableList<BoardPositionDTO> boardPositionDTOS) {
        this.boardPositionDTOS = boardPositionDTOS;
    }
    public String getStatusLabel() {
        return statusLabel.get();
    }
    public StringProperty statusLabelProperty() {
        return statusLabel;
    }
    public void setStatusLabel(String statusLabel) {
        this.statusLabel.set(statusLabel);
    }
    public TabPane getMainTabPane() {
        return mainTabPane.get();
    }
    public ObjectProperty<TabPane> mainTabPaneProperty() {
        return mainTabPane;
    }
    public void setMainTabPane(TabPane mainTabPane) {
        this.mainTabPane.set(mainTabPane);
    }
}
