package org.ecsail.interfaces;

import javafx.beans.property.ObjectProperty;
import org.ecsail.mvci_connect.ConnectView;

public interface RunState {
    enum Mode {
        NEW,
        EDIT,
        NORMAL
    }

    void setMode(Mode mode, ConnectView connectView);
    ObjectProperty<Mode> ModeProperty();
    Mode getMode();
}
