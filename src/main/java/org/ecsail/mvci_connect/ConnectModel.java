package org.ecsail.mvci_connect;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.ecsail.BaseApplication;
import org.ecsail.pojo.Login;
import org.ecsail.fx.LoginDTOProperty;
import org.ecsail.mvci_main.MainModel;
import org.ecsail.static_tools.HttpClientUtil;

import java.util.ArrayList;

public class ConnectModel {

    protected Stage connectStage = new Stage();
    private final ObjectProperty<ComboBox<String>> comboBox = new SimpleObjectProperty<>(new ComboBox<>());
    private final ObservableList<String> comboValues = FXCollections.observableArrayList();
    private final DoubleProperty titleBarHeight = new SimpleDoubleProperty();
    private final BooleanProperty rotateShipWheel = new SimpleBooleanProperty(false);
    private final DoubleProperty bottomPaneHeight = new SimpleDoubleProperty();
    private final DoubleProperty centerPaneHeight = new SimpleDoubleProperty();
    private final ObservableMap<String, HBox> hBoxMap = FXCollections.observableHashMap();
    private final ObservableMap<String, VBox> vBoxMap = FXCollections.observableHashMap();
    private final BooleanProperty isDefault = new SimpleBooleanProperty();
    private final StringProperty database = new SimpleStringProperty();
    private final StringProperty statusBarText = new SimpleStringProperty();
    private final LoginDTOProperty currentLogin = new LoginDTOProperty(); // not really a property, but has fields that are.
    private final ArrayList<Login> loginDTOS = new ArrayList<>();
    private final HttpClientUtil httpClient;
    private final ObjectMapper objectMapper;
    private final BooleanProperty authenticationRequired = new SimpleBooleanProperty();

    public ConnectModel(MainModel mainModel) {
        this.httpClient = mainModel.getHttpClient(); // Initialize here
        this.objectMapper = mainModel.getHttpClient().getObjectMapper(); // Initialize here
    }

    // Add this method to update the server URL in HttpClientUtil
    public void updateServerUrl() {
        String host = currentLogin.hostProperty().get();
        String port = currentLogin.portProperty().get();
        String serverUrl;
        if (port.equals("0"))
            serverUrl = (BaseApplication.testMode ? "http" : "https") + "://" + host;
        else
            serverUrl = (BaseApplication.testMode ? "http" : "https") + "://" + host + ":" + port;
        httpClient.setServerUrl(serverUrl);
    }

    public HttpClientUtil getHttpClient() {
        return httpClient;
    }

    public LoginDTOProperty currentLoginProperty() {
        return currentLogin;
    }


    public String getDatabase() {
        return database.get();
    }


    public void setDatabase(String database) {
        this.database.set(database);
    }

    public Stage getConnectStage() {
        return connectStage;
    }


    public boolean isDefault() {
        return isDefault.get();
    }

    public BooleanProperty defaultProperty() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault.set(isDefault);
    }

    public ObservableMap<String, VBox> getVBoxMap() {
        return vBoxMap;
    }

    public ComboBox<String> getComboBox() {
        return comboBox.get();
    }

    public void setComboBox(ComboBox<String> comboBox) {
        this.comboBox.set(comboBox);
    }

    public ObservableMap<String, HBox> getHBoxMap() {
        return hBoxMap;
    }

    public double getTitleBarHeight() {
        return titleBarHeight.get();
    }

    public void setTitleBarHeight(double titleBarHeight) {
        this.titleBarHeight.set(titleBarHeight);
    }

    public boolean isRotateShipWheel() {
        return rotateShipWheel.get();
    }

    public BooleanProperty rotateShipWheelProperty() {
        return rotateShipWheel;
    }

    public void setRotateShipWheel(boolean rotateShipWheel) {
        this.rotateShipWheel.set(rotateShipWheel);
    }

    public double getBottomPaneHeight() {
        return bottomPaneHeight.get();
    }

    public DoubleProperty bottomPaneHeightProperty() {
        return bottomPaneHeight;
    }

    public double getCenterPaneHeight() {
        return centerPaneHeight.get();
    }

    public DoubleProperty centerPaneHeightProperty() {
        return centerPaneHeight;
    }

    public StringProperty statusBarTextProperty() {
        return statusBarText;
    }

    public ArrayList<Login> getLoginDTOS() {
        return loginDTOS;
    }

    public ObservableList<String> getComboValues() {
        return comboValues;
    }

    public BooleanProperty isAuthenticationRequired() {
        return authenticationRequired;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
