package org.ecsail.interfaces;

import javafx.beans.property.ObjectProperty;

public interface RunState {
    enum Mode {
        NEW,
        EDIT,
        NORMAL
    }

    void setMode(Mode mode);
    ObjectProperty<Mode> ModeProperty();
    Mode getMode();
}
