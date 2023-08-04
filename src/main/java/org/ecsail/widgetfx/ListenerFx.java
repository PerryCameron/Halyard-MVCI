package org.ecsail.widgetfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.ecsail.BaseApplication;
import org.ecsail.mvci_membership.MembershipMessage;

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

    public static ChangeListener<MembershipMessage> createSingleUseEnumListener(ObjectProperty<MembershipMessage> membershipMessageProperty, Runnable action) {
        ChangeListener<MembershipMessage>[] listener = new ChangeListener[1];
        listener[0] = (observable, oldValue, newValue) -> {
            action.run();
            // Remove the listener after it has been triggered once
            membershipMessageProperty.removeListener(listener[0]);
        };
        return listener[0];
    }


    public static void createListener(BooleanProperty booleanProperty, Runnable action) {
        ChangeListener<Boolean> listener = (observable, oldValue, newValue) -> {
            if(newValue) action.run();
        };
        booleanProperty.addListener(listener);
    }



}
