package org.ecsail.mvci_connect;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import org.ecsail.dto.LoginDTO;

public class ConnectModel {

    private ObservableList<LoginDTO> items = FXCollections.observableArrayList();
    private final ObjectProperty<LoginDTO> selectedLogin = new SimpleObjectProperty<>();
    private final ObjectProperty<ComboBox<LoginDTO>> comboBox = new SimpleObjectProperty<>();
    private final DoubleProperty titleBarHeight = new SimpleDoubleProperty();
    private final BooleanProperty rotateShipWheel = new SimpleBooleanProperty(false);
    private final DoubleProperty bottomPaneHeight = new SimpleDoubleProperty();
    private final DoubleProperty centerPaneHeight = new SimpleDoubleProperty();
    private final BooleanProperty editMode = new SimpleBooleanProperty(false);
    private final BooleanProperty newMode = new SimpleBooleanProperty(false);
    private final ObservableMap<String, HBox> observableMap = FXCollections.observableHashMap();
    private final StringProperty user = new SimpleStringProperty();
    private final StringProperty pass = new SimpleStringProperty();
    private final StringProperty host = new SimpleStringProperty();
    private final IntegerProperty localSqlPort = new SimpleIntegerProperty();
    private final BooleanProperty sshUsed = new SimpleBooleanProperty();
    private final StringProperty sshUser = new SimpleStringProperty();
    private final StringProperty knownHosts = new SimpleStringProperty();






    public ComboBox<LoginDTO> getComboBox() {
        return comboBox.get();
    }

    public ObjectProperty<ComboBox<LoginDTO>> comboBoxProperty() {
        return comboBox;
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

    public boolean isSshUsed() {
        return sshUsed.get();
    }

    public BooleanProperty sshUsedProperty() {
        return sshUsed;
    }

    public void setSshUsed(boolean sshUsed) {
        this.sshUsed.set(sshUsed);
    }

    public String getUser() {
        return user.get();
    }

    public StringProperty userProperty() {
        return user;
    }

    public void setUser(String user) {
        this.user.set(user);
    }

    public String getPass() {
        return pass.get();
    }

    public StringProperty passProperty() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass.set(pass);
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
    public ObservableMap<String, HBox> getObservableMap() {
        return observableMap;
    }

    public ObservableList<LoginDTO> getItems() {
        return items;
    }

    public void setItems(ObservableList<LoginDTO> items) {
        this.items = items;
    }

    public LoginDTO getSelectedLogin() {
        return selectedLogin.get();
    }

    public ObjectProperty<LoginDTO> selectedLoginProperty() {
        return selectedLogin;
    }

    public void setSelectedLogin(LoginDTO selectedLogin) {
        this.selectedLogin.set(selectedLogin);
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

    public BooleanProperty editModeProperty() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode.set(editMode);
    }

    public BooleanProperty newModeProperty() {
        return newMode;
    }

    public void setNewMode(boolean newMode) {
        this.newMode.set(newMode);
    }
}
