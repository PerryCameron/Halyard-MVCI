package org.ecsail.interfaces;

import javafx.scene.layout.Region;
import org.apache.poi.ss.formula.functions.T;

public abstract class Controller<T extends Enum<T>> {

    public abstract Region getView();
    public abstract void action(T actionEnum);
}
