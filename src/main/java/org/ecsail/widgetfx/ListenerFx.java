package org.ecsail.widgetfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;

import java.util.ArrayList;
import java.util.List;

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
        List<ChangeListener<Boolean>> listenerHolder = new ArrayList<>();
        ChangeListener<Boolean> singleUseListener = (observable, oldValue, newValue) -> {
            action.run();
            // Remove the listener after it has been triggered once
            booleanProperty.removeListener(listenerHolder.get(0));
        };
        listenerHolder.add(singleUseListener);
        return singleUseListener;
    }


    public static <T extends Enum<T>> ChangeListener<T> createEnumListener(Runnable action) {
        return (observable, oldValue, newValue) -> action.run();
    }

    public static <T extends Enum<T>> ChangeListener<T> createSingleUseEnumListener(Runnable action) {
        return new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                // Execute the action
                action.run();
                // Remove this listener from the observable
                observable.removeListener(this);
            }
        };
    }

//    public static ChangeListener<String> createMultipleUseChangeListener(Runnable action) {
//        return (observable, oldValue, newValue) -> {
//            // This code will run whenever the value changes
//            action.run();
//        };
//    }
}
