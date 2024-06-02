package org.ecsail.interfaces;

import javafx.scene.layout.Region;
import org.ecsail.mvci_new_membership.NewMembershipMessage;

public abstract class Controller<T extends Enum<T>> {

    public abstract Region getView();
    public abstract void action(T actionEnum);
}
