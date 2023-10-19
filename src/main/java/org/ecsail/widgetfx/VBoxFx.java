package org.ecsail.widgetfx;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.PaymentDTO;

import java.util.function.Function;
import java.util.function.Supplier;

public class VBoxFx {

    public static VBox vBoxOf(double width, double height) {
        VBox vBox = new VBox();
        vBox.setPrefSize(width, height);
        return vBox;
    }

    public static VBox vBoxOf(double width, double height, boolean setHgrow, boolean setVgrow) {
        VBox vBox = new VBox();
        vBox.setPrefSize(width, height);
        if(setHgrow) HBox.setHgrow(vBox, Priority.ALWAYS);
        if(setVgrow) VBox.setVgrow(vBox, Priority.ALWAYS);
        return vBox;
    }

    public static VBox vBoxOf(double width, Insets insets, String id) {
        VBox vBox = new VBox();
        vBox.setPrefWidth(width);
        vBox.setPadding(insets);
        vBox.setId(id);
        return vBox;
    }

    public static VBox vBoxOf(Insets insets, String id) {
        VBox vBox = new VBox();
        vBox.setPadding(insets);
        vBox.setId(id);
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

    public static VBox vBoxOf(Double width, Pos pos) {
        VBox vBox = new VBox();
        vBox.setAlignment(pos);
        vBox.setPrefWidth(width);
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

    public static VBox vBoxOfCheckBoxes(Supplier<Node> checkBoxes) {
        VBox vBox = vBoxOf(new Insets(0,15,0,57));
        TitledPane titledPane = new TitledPane();
        titledPane.setText("Searchable Fields");
        titledPane.setExpanded(false);
        titledPane.setContent(checkBoxes.get());
        vBox.getChildren().add(titledPane);
        return vBox;
    }

    public static VBox customVBox(InvoiceDTO invoiceDTO) {
        VBox vBox = vBoxOf(new Insets(5,5,5,20));
        Label date = new Label("Payment Date: " + invoiceDTO.getPaymentDTOS().get(0).getPaymentDate());
        date.getStyleClass().add("standard-black-label");
        Label deposit = new Label("Deposit Number: " + invoiceDTO.getBatch());
        deposit.getStyleClass().add("standard-black-label");
        vBox.getChildren().addAll(date, deposit);
        return vBox;
    }

}
