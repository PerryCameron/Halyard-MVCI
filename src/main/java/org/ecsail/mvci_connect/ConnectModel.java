package org.ecsail.mvci_connect;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.layout.HBox;
import org.ecsail.dto.LoginDTO;

public class ConnectModel {

    private ObservableList<LoginDTO> loginDTOS = FXCollections.observableArrayList();
    // to keep things simple we will repopulate this everytime loginDTOs changes.
    private ObservableList<String> comboBoxItems = FXCollections.observableArrayList();
    private final IntegerProperty selectedIndex = new SimpleIntegerProperty(0);
    private final ObjectProperty<LoginDTO> selectedLogin = new SimpleObjectProperty<>();
    private final DoubleProperty titleBarHeight = new SimpleDoubleProperty();
    private final BooleanProperty rotateShipWheel = new SimpleBooleanProperty(false);
    private final DoubleProperty bottomPaneHeight = new SimpleDoubleProperty();
    private final DoubleProperty centerPaneHeight = new SimpleDoubleProperty();
    private final BooleanProperty editMode = new SimpleBooleanProperty(false);
    private final BooleanProperty newMode = new SimpleBooleanProperty(false);
    private final ObservableMap<String, HBox> observableMap = FXCollections.observableHashMap();

    private StringProperty user = new SimpleStringProperty();
    private StringProperty pass = new SimpleStringProperty();
    private StringProperty host = new SimpleStringProperty();
    private IntegerProperty localSqlPort = new SimpleIntegerProperty();
    private BooleanProperty sshUsed = new SimpleBooleanProperty();
    private StringProperty sshUser = new SimpleStringProperty();
    private StringProperty knownHosts = new SimpleStringProperty();







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

    public ObservableList<String> getComboBoxItems() {
        return comboBoxItems;
    }

    public void setComboBoxItems(ObservableList<String> comboBoxItems) {
        this.comboBoxItems = comboBoxItems;
    }

    public int getSelectedIndex() {
        return selectedIndex.get();
    }

    public IntegerProperty selectedIndexProperty() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex.set(selectedIndex);
    }

    public ObservableMap<String, HBox> getObservableMap() {
        return observableMap;
    }

    public ObservableList<LoginDTO> getLoginDTOS() {
        return loginDTOS;
    }

    public void setLoginDTOS(ObservableList<LoginDTO> loginDTOS) {
        this.loginDTOS = loginDTOS;
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

    public DoubleProperty titleBarHeightProperty() {
        return titleBarHeight;
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

    public void setBottomPaneHeight(double bottomPaneHeight) {
        this.bottomPaneHeight.set(bottomPaneHeight);
    }

    public double getCenterPaneHeight() {
        return centerPaneHeight.get();
    }

    public DoubleProperty centerPaneHeightProperty() {
        return centerPaneHeight;
    }

    public void setCenterPaneHeight(double centerPaneHeight) {
        this.centerPaneHeight.set(centerPaneHeight);
    }

    public boolean isEditMode() {
        return editMode.get();
    }

    public BooleanProperty editModeProperty() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode.set(editMode);
    }

    public boolean isNewMode() {
        return newMode.get();
    }

    public BooleanProperty newModeProperty() {
        return newMode;
    }

    public void setNewMode(boolean newMode) {
        this.newMode.set(newMode);
    }
}
