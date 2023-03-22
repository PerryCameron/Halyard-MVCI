package org.ecsail.mvci_connect;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.ecsail.interfaces.RunState;
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
    @Override
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
        // remove all Nodes from the bottom box
        connectModel.getVBoxMap().get("bottom-box").getChildren ().clear();
        // supply relevant Nodes to the button control box
        connectModel.getHBoxMap().get("button-container-box").getChildren()
                .add(connectModel.getHBoxMap().get("button-box"));
        // remove TextField from the host HBox, and replace with a ComboBox
        connectModel.getHBoxMap().get("host-container-box").getChildren().clear();
        connectModel.getHBoxMap().get("host-container-box").getChildren()
                .add(connectModel.getHBoxMap().get("host-combo-box"));
    }

    private void switchToEditMode() {
        // supply all relevant Nodes to bottom box
        connectModel.getVBoxMap().get("bottom-box").getChildren()
                .add(connectModel.getVBoxMap().get("bottom-container-box"));
        // remove Nodes from button container box
        connectModel.getHBoxMap().get("button-container-box").getChildren().clear();
        // remove ComboBox from the host HBox and replace with TextField
        connectModel.getHBoxMap().get("host-container-box").getChildren().clear();
        connectModel.getHBoxMap().get("host-container-box").getChildren()
                .add(connectModel.getHBoxMap().get("host-text-field"));
    }
}
