package org.ecsail.widgetfx;

import javafx.animation.PauseTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.ecsail.dto.InvoiceItemDTO;
import org.ecsail.mvci_boatlist.BoatListMessage;

import java.math.BigDecimal;
import java.util.function.Consumer;

public class HBoxFx {

    public static HBox customHBox(InvoiceItemDTO item) {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Label(item.getFieldName()), new Label(String.valueOf(item.getQty())), new Label(item.getValue()));
        return hBox;
    }

    public static HBox customHBoxHeader() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Label("Fee"), new Label("Qty"), new Label("Total"));
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
