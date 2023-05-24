package org.ecsail.mvci_main;


import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;
import org.ecsail.dto.BoardPositionDTO;

public class MainModel {

    private ObservableList<BoardPositionDTO> boardPositionDTOS = FXCollections.observableArrayList();
    private final StringProperty statusLabel = new SimpleStringProperty(""); // keeper here
    private final StringProperty changeStatusLabel = new SimpleStringProperty(""); // keeper here
    private final ObjectProperty<TabPane> mainTabPane = new SimpleObjectProperty();
    private final BooleanProperty primaryStageComplete = new SimpleBooleanProperty(false);


    public String getChangeStatusLabel() {
        return changeStatusLabel.get();
    }

    public StringProperty changeStatusLabelProperty() {
        return changeStatusLabel;
    }

    public void setChangeStatusLabel(String changeStatusLabel) {
        this.changeStatusLabel.set(changeStatusLabel);
    }

    public BooleanProperty primaryStageCompleteProperty() {
        return primaryStageComplete;
    }
    public void setPrimaryStageComplete(boolean primaryStageComplete) {
        this.primaryStageComplete.set(primaryStageComplete);
    }
    public ObservableList<BoardPositionDTO> getBoardPositionDTOS() {
        return boardPositionDTOS;
    }
    public void setBoardPositionDTOS(ObservableList<BoardPositionDTO> boardPositionDTOS) {
        this.boardPositionDTOS = boardPositionDTOS;
    }
    public StringProperty statusLabelProperty() {
        return statusLabel;
    }
    public TabPane getMainTabPane() {
        return mainTabPane.get();
    }
    public void setMainTabPane(TabPane mainTabPane) {
        this.mainTabPane.set(mainTabPane);
    }
}
