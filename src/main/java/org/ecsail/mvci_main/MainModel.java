package org.ecsail.mvci_main;


import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.TabPane;
import org.ecsail.dto.BoardPositionDTO;
import org.ecsail.interfaces.Status;
import org.ecsail.static_tools.HttpClientUtil;


public class MainModel {

    private ObservableList<BoardPositionDTO> boardPositionDTOS = FXCollections.observableArrayList();
    private final StringProperty statusLabel = new SimpleStringProperty(""); // keeper here
    private final ObjectProperty<TabPane> mainTabPane = new SimpleObjectProperty<>();
    private final ObservableMap<String, Timeline> LightAnimationMap = FXCollections.observableHashMap();
    private final SimpleObjectProperty<Status.light> lightStatusProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<MainMessage> returnMessage = new SimpleObjectProperty<>();
//    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BooleanProperty clientConnectError = new SimpleBooleanProperty(false);
    private final StringProperty errorMessage = new SimpleStringProperty("");
    private final HttpClientUtil httpClient = new HttpClientUtil(this);
    private Integer msId;


    public Integer getMsId() {
        return msId;
    }

    public void setMsId(Integer msId) {
        this.msId = msId;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel.set(statusLabel);
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

//    public ObjectMapper getObjectMapper() {
//        return objectMapper;
//    }

    public HttpClientUtil getHttpClient() {
        return httpClient;
    }

    public BooleanProperty clientConnectErrorProperty() {
        return clientConnectError;
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public void setConnectError(boolean connectError) {
        this.clientConnectError.set(connectError);
    }

    public void toggleClientConnectError() {
        clientConnectError.set(true);
        clientConnectError.set(false);
    }
}
