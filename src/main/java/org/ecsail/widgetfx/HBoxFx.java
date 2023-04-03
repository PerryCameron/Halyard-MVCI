package org.ecsail.widgetfx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class HBoxFx {

    public static HBox boundBoxOf(ObjectProperty<HBox> objectProperty) {
        HBox hBox = new HBox();
        objectProperty.bindBidirectional(new SimpleObjectProperty<>(hBox));
        return hBox;
    }
    public static HBox boundBoxOf(Insets insets, ObjectProperty<HBox> objectProperty) {
        HBox hBox = new HBox();
        hBox.setPadding(insets);
        objectProperty.bindBidirectional(new SimpleObjectProperty<>(hBox));
        return hBox;
    }
    public static HBox hBoxOf(Pos alignment, Insets padding) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setPadding(padding);
        return box;
    }

    public static HBox hBoxOf(Pos alignment, double prefWidth) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setPrefWidth(prefWidth);
        return box;
    }

    public static HBox hBoxOf(Pos alignment, double prefWidth, double spacing) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setSpacing(spacing);
        box.setPrefWidth(prefWidth);
        return box;
    }

    public static HBox hBoxOf(Pos alignment, double prefWidth, Insets padding) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setPrefWidth(prefWidth);
        box.setPadding(padding);
        return box;
    }

    public static HBox hBoxOf(Pos alignment, Insets padding, double spacing) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setSpacing(spacing);
        box.setPadding(padding);
        return box;
    }

    public static HBox hBoxOf(Pos alignment, double prefWidth, Insets padding, double spacing) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setPrefWidth(prefWidth);
        box.setPadding(padding);
        box.setSpacing(spacing);
        return box;
    }
    public static HBox hBoxOf(Insets padding) {
        HBox box = new HBox();
        box.setPadding(padding);
        return box;
    }

    public static HBox hBoxOf(Insets padding, double spacing) {
        HBox box = new HBox();
        box.setPadding(padding);
        box.setSpacing(spacing);
        return box;
    }

    public static HBox hBoxOf(double spacing, Pos position, Node control1, Node control2) {
        HBox hBox = new HBox(spacing, control1,control2);
        hBox.setAlignment(position);
        return hBox;
    }

    public static HBox spacerOf(double height) {
        HBox hBox = new HBox();
        Region region = new Region();
        region.setPrefHeight(height);
        hBox.getChildren().add(region);
        return hBox;
    }

}
