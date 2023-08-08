package org.ecsail.mvci_main;


import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.TabPane;
import org.ecsail.dto.BoardPositionDTO;
import org.ecsail.interfaces.Status;


public class MainModel {

    private ObservableList<BoardPositionDTO> boardPositionDTOS = FXCollections.observableArrayList();
    private final StringProperty statusLabel = new SimpleStringProperty(""); // keeper here
    private final ObjectProperty<TabPane> mainTabPane = new SimpleObjectProperty();
//    private final BooleanProperty primaryStageComplete = new SimpleBooleanProperty(false);
    private ObservableMap<String, Timeline> LightAnimationMap = FXCollections.observableHashMap();
    private SimpleObjectProperty<Status.light> lightStatusProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<MainMessage> returnMessage = new SimpleObjectProperty<>();
    private final IntegerProperty msId = new SimpleIntegerProperty();


    public int getMsId() {
        return msId.get();
    }

    public IntegerProperty msIdProperty() {
        return msId;
    }

    public void setMsId(int msId) {
        this.msId.set(msId);
    }

    public MainMessage getReturnMessage() {
        return returnMessage.get();
    }

    public ObjectProperty<MainMessage> returnMessageProperty() {
        return returnMessage;
    }

    public void setReturnMessage(MainMessage returnMessage) {
        this.returnMessage.set(returnMessage);
    }

    public ObservableMap<String, Timeline> getLightAnimationMap() {
        return LightAnimationMap;
    }

    public SimpleObjectProperty<Status.light> lightStatusPropertyProperty() {
        return lightStatusProperty;
    }

    public void setLightStatusProperty(Status.light lightStatusProperty) {
        this.lightStatusProperty.set(lightStatusProperty);
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
