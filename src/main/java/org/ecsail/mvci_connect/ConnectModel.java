package org.ecsail.mvci_connect;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.ecsail.dto.LoginDTO;
import org.ecsail.dto.LoginDTOProperty;

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

    private final IntegerProperty localSqlPort = new SimpleIntegerProperty();
    private final BooleanProperty isDefault = new SimpleBooleanProperty();


    private final StringProperty database = new SimpleStringProperty();
    private final StringProperty statusBarText = new SimpleStringProperty();
    private final ObjectProperty<LoginDTOProperty> currentLogin = new SimpleObjectProperty<>(new LoginDTOProperty());
    private ArrayList<LoginDTO> loginDTOS = new ArrayList<>();



    public ObjectProperty<LoginDTOProperty> currentLoginProperty() {
        System.out.println("Getting current login");
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

    public ComboBox<LoginDTO> getComboBox() {
        return comboBox.get();
    }

    public void setComboBox(ComboBox<LoginDTO> comboBox) {
        this.comboBox.set(comboBox);
    }




    public int getLocalSqlPort() {
        return localSqlPort.get();
    }

    public IntegerProperty localSqlPortProperty() {
        return localSqlPort;
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
