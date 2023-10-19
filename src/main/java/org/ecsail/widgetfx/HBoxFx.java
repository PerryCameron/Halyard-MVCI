package org.ecsail.widgetfx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.ecsail.dto.InvoiceItemDTO;

public class HBoxFx {

    public static HBox customHBox(InvoiceItemDTO item) {
        HBox hBox = HBoxFx.hBoxOf(new Insets(0,20,5,20));
        Label p1Label = new Label(item.getFieldName());
        Label p2Label = new Label(String.valueOf(item.getQty()));
        Label p3Label = new Label(item.getValue());
        p2Label.getStyleClass().add("standard-black-label");
        if(item.isCredit()) p3Label.getStyleClass().add("standard-red-label");
        else p3Label.getStyleClass().add("standard-black-label");
        Pane p1 = new Pane(p1Label);
        Pane p2 = new Pane(p2Label);
        HBox p3 = new HBox(p3Label);
        p3.setAlignment(Pos.CENTER_RIGHT);
        p1.prefWidthProperty().bind(hBox.widthProperty().multiply(0.5));
        p2.prefWidthProperty().bind(hBox.widthProperty().multiply(0.25));
        p3.prefWidthProperty().bind(hBox.widthProperty().multiply(0.25));
        hBox.getChildren().addAll(p1,p2,p3);
        return hBox;
    }

    public static HBox customHBoxHeader() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(0,20,0,20));
        Label p1Label = new Label("Fee");
        Label p2Label = new Label("Qty");
        Label p3Label = new Label("Total");
        p1Label.getStyleClass().add("standard-bold-label");
        p2Label.getStyleClass().add("standard-bold-label");
        p3Label.getStyleClass().add("standard-bold-label");
        Pane p1 = new Pane(p1Label);
        Pane p2 = new Pane(p2Label);
        HBox p3 = new HBox(p3Label);
        p3.setAlignment(Pos.CENTER_RIGHT);
        p1.prefWidthProperty().bind(hBox.widthProperty().multiply(0.45));
        p2.prefWidthProperty().bind(hBox.widthProperty().multiply(0.3));
        p3.prefWidthProperty().bind(hBox.widthProperty().multiply(0.25));
        hBox.getChildren().addAll(p1,p2,p3);
        return hBox;
    }

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

    public static HBox hBoxOf(Pos alignment, Insets padding, boolean vGrow) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        if(vGrow) VBox.setVgrow(box, Priority.ALWAYS);
        box.setPadding(padding);
        return box;
    }

    public static HBox hBoxOf(Insets padding, String id, boolean setVgrow) {
        HBox hBox = new HBox();
        hBox.setPadding(padding);
        hBox.setId(id);
        if(setVgrow) VBox.setVgrow(hBox, Priority.ALWAYS);
        return hBox;
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

    public static HBox hBoxOf(Insets padding, Pos alignment, double spacing) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setSpacing(spacing);
        box.setPadding(padding);
        return box;
    }

    public static HBox hBoxOf(double spacing, Pos alignment) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setSpacing(spacing);
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
