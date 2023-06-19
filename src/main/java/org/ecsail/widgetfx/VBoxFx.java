package org.ecsail.widgetfx;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VBoxFx {

    public static VBox vBoxOf(double width, double height) {
        VBox vBox = new VBox();
        vBox.setPrefSize(width, height);
        return vBox;
    }

    public static VBox vBoxOf(double width, Insets insets, String style) {
        VBox vBox = new VBox();
        vBox.setPrefWidth(width);
        vBox.setPadding(insets);
        vBox.setId(style);
        return vBox;
    }

    public static VBox vBoxOf(double width, double height, Insets insets, String style) {
        VBox vBox = new VBox();
        vBox.setPrefSize(width,height);
        vBox.setPadding(insets);
        vBox.setId(style);
        return vBox;
    }

    public static VBox vBoxOf(Insets insets) {
        VBox vBox = new VBox();
        vBox.setPadding(insets);
        return vBox;
    }

    public static VBox vBoxOf(Insets padding, String id, boolean setHgrow) {
        VBox vBox = new VBox();
        vBox.setPadding(padding);
        vBox.setId(id);
        if(setHgrow) HBox.setHgrow(vBox, Priority.ALWAYS);
        return vBox;
    }


    public static VBox vBoxOf(Insets insets, Pos pos) {
        VBox vBox = new VBox();
        vBox.setAlignment(pos);
        vBox.setPadding(insets);
        return vBox;
    }

    public static VBox vBoxOf(Insets insets, Pos pos, Double spacing) {
        VBox vBox = new VBox();
        vBox.setAlignment(pos);
        vBox.setSpacing(spacing);
        vBox.setPadding(insets);
        return vBox;
    }

    public static VBox vBoxOf(Insets insets, DoubleProperty doubleProperty) {
        VBox vBox = new VBox();
        vBox.setPadding(insets);
        doubleProperty.bind(vBox.heightProperty());
        return vBox;
    }

    public static VBox vBoxOf(Insets insets, Double width, Double minWidth, Double spacing) {
        VBox vBox = new VBox();
        vBox.setPrefWidth(width);
        vBox.setMinWidth(minWidth);
        vBox.setSpacing(spacing);
        vBox.setPadding(insets);
        return vBox;
    }

    public static VBox vBoxOf(Double spacing, Insets insets) {
        VBox vBox = new VBox();
        vBox.setSpacing(spacing);
        vBox.setPadding(insets);
        return vBox;
    }

    public static VBox vBoxOf(Double width, Double spacing, Insets insets) {
        VBox vBox = new VBox();
        vBox.setPrefWidth(width);
        vBox.setSpacing(spacing);
        vBox.setPadding(insets);
        return vBox;
    }

}
