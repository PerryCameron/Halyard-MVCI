package org.ecsail.widgetfx;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class VBoxFx {

    public static VBox vBoxOf(double width, Insets insets, String style) {
        VBox vBox = new VBox();
        vBox.setPrefWidth(width);
        vBox.setPadding(insets);
        vBox.setId(style);
        return vBox;
    }

    public static VBox vBoxOf(Insets insets) {
        VBox vBox = new VBox();
        vBox.setPadding(insets);
        return vBox;
    }

    public static VBox vBoxOf(Insets insets, DoubleProperty doubleProperty) {
        VBox vBox = new VBox();
        vBox.setPadding(insets);
        doubleProperty.bind(vBox.heightProperty());
        return vBox;
    }



}
