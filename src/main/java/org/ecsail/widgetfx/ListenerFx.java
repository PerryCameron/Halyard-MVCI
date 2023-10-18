package org.ecsail.widgetfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.ecsail.BaseApplication;
import org.ecsail.dto.InvoiceItemDTO;
import org.ecsail.mvci_membership.MembershipMessage;

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
        System.out.println("BooleanProperty@" + booleanProperty.hashCode()
                + " set to: " + booleanProperty.get());
        ChangeListener<Boolean>[] listener = new ChangeListener[1];
        listener[0] = (observable, oldValue, newValue) -> {
            action.run();
            // Remove the listener after it has been triggered once
            booleanProperty.removeListener(listener[0]);
        };
        return listener[0];
    }

    public static <T extends Enum<T>> ChangeListener<T> createEnumListener(Runnable action) {
        ChangeListener<T> listener = (observable, oldValue, newValue) -> action.run();
        return listener;
    }
}
