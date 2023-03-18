package org.ecsail.MvciConnect;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.HashMap;

public class ConnectModel {

    private ObservableList<String> choices = FXCollections.observableArrayList();
    private StringProperty user = new SimpleStringProperty();
    private StringProperty pass = new SimpleStringProperty();
    private StringProperty host = new SimpleStringProperty();
    private StringProperty localSqlPort = new SimpleStringProperty();
    private BooleanProperty defaultCheckProperty = new SimpleBooleanProperty(false);
    private BooleanProperty useSSHCheckProperty = new SimpleBooleanProperty(false);





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
