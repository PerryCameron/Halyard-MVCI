package org.ecsail.widgetfx;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckBox;

public class CheckBoxFx {
    public static CheckBox CheckBoxOf(String text, double width, BooleanProperty booleanProperty) {
        CheckBox checkBox = new CheckBox(text);
        checkBox.setPrefWidth(width);
        booleanProperty.bind(checkBox.selectedProperty());
        return checkBox;
    }
}
