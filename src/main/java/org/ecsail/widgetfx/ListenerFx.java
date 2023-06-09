package org.ecsail.widgetfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;

public class ListenerFx {
    public static ChangeListener<Boolean> createSingleUseListener(BooleanProperty booleanProperty, Runnable action) {
        ChangeListener<Boolean>[] listener = new ChangeListener[1];
        listener[0] = (observable, oldValue, newValue) -> {
            action.run();
            // Remove the listener after it has been triggered once
            booleanProperty.removeListener(listener[0]);
        };
        return listener[0];
    }
}
