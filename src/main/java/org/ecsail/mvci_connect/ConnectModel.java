package org.ecsail.mvci_connect;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConnectModel {

    private ObservableList<String> choices = FXCollections.observableArrayList();
    private final StringProperty user = new SimpleStringProperty();
    private final StringProperty pass = new SimpleStringProperty();
    private final StringProperty host = new SimpleStringProperty();
    private final StringProperty localSqlPort = new SimpleStringProperty();
    private final BooleanProperty defaultCheckProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty useSSHCheckProperty = new SimpleBooleanProperty(false);
    private final StringProperty knownHost = new SimpleStringProperty();
    private final DoubleProperty titleBarHeight = new SimpleDoubleProperty();
    private final BooleanProperty rotateShipWheel = new SimpleBooleanProperty(false);
    private final DoubleProperty bottomPaneHeight = new SimpleDoubleProperty();
    private final DoubleProperty centerPaneHeight = new SimpleDoubleProperty();
    private final BooleanProperty editMode = new SimpleBooleanProperty(false);
    private final BooleanProperty newMode = new SimpleBooleanProperty(false);









    public boolean isNewMode() {
        return newMode.get();
    }

    public BooleanProperty newModeProperty() {
        return newMode;
    }

    public void setNewMode(boolean newMode) {
        this.newMode.set(newMode);
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

    public double getCenterPaneHeight() {
        return centerPaneHeight.get();
    }

    public DoubleProperty centerPaneHeightProperty() {
        return centerPaneHeight;
    }

    public void setCenterPaneHeight(double centerPaneHeight) {
        this.centerPaneHeight.set(centerPaneHeight);
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








    public boolean isRotateShipWheel() { return rotateShipWheel.get();}
    public BooleanProperty rotateShipWheelProperty() { return rotateShipWheel; }
    public void setRotateShipWheel(boolean rotateShipWheel) { this.rotateShipWheel.set(rotateShipWheel);}
    public double getTitleBarHeight() { return titleBarHeight.get(); }
    public DoubleProperty titleBarHeightProperty() { return titleBarHeight; }
    public void setTitleBarHeight(double titleBarHeight) { this.titleBarHeight.set(titleBarHeight); }
    public String getKnownHost() { return knownHost.get();}
    public StringProperty knownHostProperty() { return knownHost; }
    public void setKnownHost(String knownHost) { this.knownHost.set(knownHost); }
    public ObservableList<String> getChoices() { return choices; }
    public void setChoices(ObservableList<String> choices) { this.choices = choices; }
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
    public String getLocalSqlPort() {
        return localSqlPort.get();
    }
    public StringProperty localSqlPortProperty() {
        return localSqlPort;
    }
    public void setLocalSqlPort(String localSqlPort) {
        this.localSqlPort.set(localSqlPort);
    }
    public BooleanProperty defaultCheckProperty() {return defaultCheckProperty;}
    public void setDefaultCheckProperty(boolean value) {
        defaultCheckProperty.set(value);
    }
    public boolean isDefaultCheckProperty() {
        return defaultCheckProperty.get();
    }
    public boolean isUseSSHCheckProperty() {
        return useSSHCheckProperty.get();
    }
    public BooleanProperty useSSHCheckPropertyProperty() {
        return useSSHCheckProperty;
    }
    public void setUseSSHCheckProperty(boolean useSSHCheckProperty) {
        this.useSSHCheckProperty.set(useSSHCheckProperty);
    }

}
