package org.ecsail.widgetfx;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;

public class LabelFx {

    public static Label labelOf(String value, String cssClass, StringProperty stringProperty) {
        Label label = new Label(value);
        label.getStyleClass().add(cssClass);
        label.textProperty().bind(stringProperty);
        return label;
    }

    public static Label labelOf(String value, String cssClass) {
        Label label = new Label(value);
        label.getStyleClass().add(cssClass);
        return label;
    }
}
