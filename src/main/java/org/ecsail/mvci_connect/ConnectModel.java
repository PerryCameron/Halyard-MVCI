package org.ecsail.mvci_connect;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import org.ecsail.dto.LoginDTO;

public class ConnectModel {

    private ObservableList<LoginDTO> loginDTOS = FXCollections.observableArrayList();
    private final ObjectProperty<LoginDTO> selectedLogin = new SimpleObjectProperty<>();
    private final DoubleProperty titleBarHeight = new SimpleDoubleProperty();
    private final BooleanProperty rotateShipWheel = new SimpleBooleanProperty(false);
    private final DoubleProperty bottomPaneHeight = new SimpleDoubleProperty();
    private final DoubleProperty centerPaneHeight = new SimpleDoubleProperty();
    private final BooleanProperty editMode = new SimpleBooleanProperty(false);
    private final BooleanProperty newMode = new SimpleBooleanProperty(false);
    private final ObjectProperty<HBox> containerBox = new SimpleObjectProperty<>();
    private final ObjectProperty<HBox> buttonBox = new SimpleObjectProperty<>();


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

    public HBox getContainerBox() {
        return containerBox.get();
    }

    public ObjectProperty<HBox> containerBoxProperty() {
        return containerBox;
    }

    public void setContainerBox(HBox containerBox) {
        this.containerBox.set(containerBox);
    }

    public HBox getButtonBox() {
        return buttonBox.get();
    }

    public ObjectProperty<HBox> buttonBoxProperty() {
        return buttonBox;
    }

    public void setButtonBox(HBox buttonBox) {
        this.buttonBox.set(buttonBox);
    }
}
