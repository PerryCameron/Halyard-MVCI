package org.ecsail.mvci_connect;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.ecsail.iface.RunState;
import org.ecsail.widgetfx.ObjectFx;

public class RunStateImpl implements RunState {

    private ObjectProperty<Mode> modeProperty;
    ConnectModel connectModel;

    public RunStateImpl(ConnectModel model) {
        connectModel = model;
        modeProperty = new SimpleObjectProperty<>(Mode.NORMAL);
    }

    @Override
    public void setMode(Mode mode) {
        switch (mode) {
            case NEW -> {
            connectModel.getComboBox().getItems().add(ObjectFx.createLoginDTO());
            connectModel.getComboBox().getSelectionModel().select(connectModel.getComboBox().getItems().size() - 1);
            switchToEditMode();
            }
            case EDIT -> switchToEditMode();
            case NORMAL -> switchToNormalMode();
        }
        setModeProperty(mode);
    }

    public Mode getMode() {
        return modeProperty.get();
    }

    @Override
    public ObjectProperty<Mode> ModeProperty() {
        return modeProperty;
    }

    private void setModeProperty(Mode modeProperty) {
        this.modeProperty.set(modeProperty);
    }

    private void switchToNormalMode() {
        connectModel.getBottomBox().getChildren ().clear();
        connectModel.getObservableMap().get("button-box-container").getChildren()
                .add(connectModel.getObservableMap().get("button-box"));
        connectModel.getObservableMap().get("host-container").getChildren().clear();
        connectModel.getObservableMap().get("host-container").getChildren()
                .add(connectModel.getObservableMap().get("host-combo-box"));
    }

    private void switchToEditMode() {
        // add to bottom box
        connectModel.getBottomBox().getChildren().addAll(
                connectModel.getObservableMap().get("sql-port-box"),
                connectModel.getObservableMap().get("use-ssh-box"),
                connectModel.getObservableMap().get("ssh-usr-box"),
                connectModel.getObservableMap().get("known-host-box"),
                connectModel.getObservableMap().get("edit-buttons-box"));
        connectModel.getObservableMap().get("button-box-container").getChildren().clear();
        connectModel.getObservableMap().get("host-container").getChildren().clear();
        // add to host container
        connectModel.getObservableMap().get("host-container").getChildren()
                .add(connectModel.getObservableMap().get("host-text-field"));
    }

}
