package org.ecsail.widgetfx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.InvoiceItemDTO;

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

    public static HBox hBoxOf(Pos alignment, double prefWidth, Node node) {
        HBox box = new HBox();
        box.setAlignment(alignment);
        box.setPrefWidth(prefWidth);
        box.getChildren().add(node);
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

    public static HBox customHBox(InvoiceDTO invoiceDTO) {  // used for committed invoice totals
        HBox hBox = HBoxFx.hBoxOf(new Insets(10, 0, 5, 20));
        VBox labels = new VBox(5);
        VBox amounts = new VBox(5);
        amounts.setAlignment(Pos.CENTER_RIGHT);
        labels.prefWidthProperty().bind(hBox.widthProperty().multiply(0.5));
        amounts.prefWidthProperty().bind(hBox.widthProperty().multiply(0.5));
        labels.getChildren().addAll(
                new Label("Total Fees:"),
                new Label("Total Credit:"),
                new Label("Payment:"),
                new Label("Balance:"));
        amounts.getChildren().addAll(
                LabelFx.labelOf(invoiceDTO.getTotal(), "standard-black-label"),
                LabelFx.labelOf(invoiceDTO.getCredit(), "standard-red-label"),
                LabelFx.labelOf(invoiceDTO.getPaid(), "standard-black-label"),
                LabelFx.labelOf(invoiceDTO.getBalance(), "standard-black-label"));
        hBox.getChildren().addAll(labels, amounts);
        return hBox;
    }

    public static HBox customHBox(InvoiceItemDTO item) {  // used for committed invoice items
        HBox hBox = HBoxFx.hBoxOf(new Insets(0,20,5,0));
        Label p1Label = new Label(item.getFieldName()+":");
        Label p2Label = new Label(String.valueOf(item.getQty()));
        Label p3Label = new Label(item.getValue());
        p2Label.getStyleClass().add("standard-black-label");
        if(item.isCredit()) p3Label.getStyleClass().add("standard-red-label");
        else p3Label.getStyleClass().add("standard-black-label");
        Pane p1 = new Pane(p1Label);
        HBox p2 = new HBox(p2Label);
        HBox p3 = new HBox(p3Label);
        p3.setAlignment(Pos.CENTER_RIGHT);
        p2.setAlignment(Pos.CENTER_RIGHT);
        p1.setPrefWidth(140);
        p2.setPrefWidth(130);
        p3.setPrefWidth(165);
        hBox.getChildren().addAll(p1,p2,p3);
        return hBox;
    }

    public static HBox customHBoxHeader(boolean isCommitted) {
        HBox hBox = hBoxOf(new Insets(0,20,0,0));
        HBox p1 = hBoxOf(Pos.CENTER_LEFT,140.0, LabelFx.labelOf("Fee","standard-bold-label"));
        HBox p2,p3;
        if(isCommitted) {
            p2 = hBoxOf(Pos.CENTER_RIGHT, 135.0, LabelFx.labelOf("Qty", "standard-bold-label"));
            p3 = hBoxOf(Pos.CENTER_RIGHT, 160.0, LabelFx.labelOf("Total", "standard-bold-label"));
        } else {
            p2 = hBoxOf(Pos.CENTER_RIGHT, 175.0, LabelFx.labelOf("Price", "standard-bold-label"));
            p3 = hBoxOf(Pos.CENTER_RIGHT, 120.0, LabelFx.labelOf("Total", "standard-bold-label"));
        }
        hBox.getChildren().addAll(p1,p2,p3);
        return hBox;
    }
}
