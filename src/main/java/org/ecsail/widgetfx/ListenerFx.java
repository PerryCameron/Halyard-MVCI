package org.ecsail.widgetfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;

public class ListenerFx {
    public static void createSingleUseTabListener(Tab tab, Runnable action) {
        // Create a listener for tab selection change
        ChangeListener<Boolean> tabSelectionListener = new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    // Tab has been selected (opened)
                    action.run();
                    // Remove the listener
                    tab.selectedProperty().removeListener(this);
                }
            }
        };
        tab.selectedProperty().addListener(tabSelectionListener);
    }

    public static ChangeListener<Boolean> createSingleUseListener(BooleanProperty booleanProperty, Runnable action) {
        ChangeListener<Boolean>[] listener = new ChangeListener[1];
        listener[0] = (observable, oldValue, newValue) -> {
                action.run();
                // Remove the listener after it has been triggered once
                booleanProperty.removeListener(listener[0]);
        };
        return listener[0];
    }

    public static <T extends Enum<T>> ChangeListener<T> createEnumListener(Runnable action) {
        return (observable, oldValue, newValue) -> action.run();
    }

    public static ChangeListener<String> createMultipleUseChangeListener(Runnable action) {
        return (observable, oldValue, newValue) -> {
            // This code will run whenever the value changes
            action.run();
        };
    }
}
