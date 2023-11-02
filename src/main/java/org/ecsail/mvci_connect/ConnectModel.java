package org.ecsail.mvci_connect;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.ecsail.dto.LoginDTO;

import java.util.ArrayList;

public class ConnectModel {

    protected Stage connectStage = new Stage();
    private final ObjectProperty<ComboBox<LoginDTO>> comboBox = new SimpleObjectProperty<>();
    private final DoubleProperty titleBarHeight = new SimpleDoubleProperty();
    private final BooleanProperty rotateShipWheel = new SimpleBooleanProperty(false);
    private final DoubleProperty bottomPaneHeight = new SimpleDoubleProperty();
    private final DoubleProperty centerPaneHeight = new SimpleDoubleProperty();
    private final ObservableMap<String, HBox> hBoxMap = FXCollections.observableHashMap();
    private final ObservableMap<String, VBox> vBoxMap = FXCollections.observableHashMap();
    private final StringProperty sqlUser = new SimpleStringProperty();
    private final StringProperty sqlPass = new SimpleStringProperty();
    private final StringProperty host = new SimpleStringProperty();
    private final IntegerProperty localSqlPort = new SimpleIntegerProperty();
    private final IntegerProperty sshPort = new SimpleIntegerProperty();
    private final BooleanProperty sshUsed = new SimpleBooleanProperty();
    private final BooleanProperty isDefault = new SimpleBooleanProperty();
    private final StringProperty sshUser = new SimpleStringProperty();
    private final StringProperty knownHosts = new SimpleStringProperty();
    private final StringProperty privateKey = new SimpleStringProperty();
    private final StringProperty database = new SimpleStringProperty();
    private final StringProperty statusBarText = new SimpleStringProperty();
    private ArrayList<LoginDTO> loginDTOS = new ArrayList<>();


    public int getSshPort() {
        return sshPort.get();
    }

    public IntegerProperty sshPortProperty() {
        return sshPort;
    }

    public void setSshPort(int sshPort) {
        this.sshPort.set(sshPort);
    }

    public String getPrivateKey() {
        return privateKey.get();
    }

    public StringProperty privateKeyProperty() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey.set(privateKey);
    }

    public String getDatabase() {
        return database.get();
    }

    public StringProperty databaseProperty() {
        return database;
    }

    public void setDatabase(String database) {
        this.database.set(database);
    }

    public Stage getConnectStage() {
        return connectStage;
    }

    public void setConnectStage(Stage connectStage) {
        this.connectStage = connectStage;
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

    public ComboBox<LoginDTO> getComboBox() {
        return comboBox.get();
    }

    public void setComboBox(ComboBox<LoginDTO> comboBox) {
        this.comboBox.set(comboBox);
    }

    public String getKnownHosts() {
        return knownHosts.get();
    }

    public StringProperty knownHostsProperty() {
        return knownHosts;
    }

    public void setKnownHosts(String knownHosts) {
        this.knownHosts.set(knownHosts);
    }

    public String getSshUser() {
        return sshUser.get();
    }

    public StringProperty sshUserProperty() {
        return sshUser;
    }

    public void setSshUser(String sshUser) {
        this.sshUser.set(sshUser);
    }

    public boolean sshUsed() {
        return sshUsed.get();
    }

    public BooleanProperty sshUsedProperty() {
        return sshUsed;
    }

    public void setSshUsed(boolean sshUsed) {
        this.sshUsed.set(sshUsed);
    }

    public String getSqlUser() {
        return sqlUser.get();
    }

    public StringProperty sqlUserProperty() {
        return sqlUser;
    }

    public void setSqlUser(String sqlUser) {
        this.sqlUser.set(sqlUser);
    }

    public String getSqlPass() {
        return sqlPass.get();
    }

    public StringProperty sqlPassProperty() {
        return sqlPass;
    }

    public void setSqlPass(String sqlPass) {
        this.sqlPass.set(sqlPass);
    }

    public String getHost() {
        return host.get();
    }

    public StringProperty hostProperty() {
        return host;
    }

    public void setHost(String host) {
        this.host.set(host);
    }

    public int getLocalSqlPort() {
        return localSqlPort.get();
    }

    public IntegerProperty localSqlPortProperty() {
        return localSqlPort;
    }

    public void setLocalSqlPort(int localSqlPort) {
        this.localSqlPort.set(localSqlPort);
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

    public String getStatusBarText() {
        return statusBarText.get();
    }

    public StringProperty statusBarTextProperty() {
        return statusBarText;
    }

    public void setStatusBarText(String statusBarText) {
        this.statusBarText.set(statusBarText);
    }

    public ArrayList<LoginDTO> getLoginDTOS() {
        return loginDTOS;
    }

    public void setLoginDTOS(ArrayList<LoginDTO> loginDTOS) {
        this.loginDTOS = loginDTOS;
    }
}
