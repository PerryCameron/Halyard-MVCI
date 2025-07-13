package org.ecsail.interfaces;

import javafx.scene.control.Tab;

public abstract class TabController<T extends Enum<T>> {

    public abstract Tab getView();
    public abstract void action(T actionEnum);
}
