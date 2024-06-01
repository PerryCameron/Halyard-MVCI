package org.ecsail.mvci_slip;


import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Builder;

import java.util.function.Consumer;

public class SlipView implements Builder<Region> {
    private final SlipModel slipModel;
    Consumer<SlipMessage> action;
    public SlipView(SlipModel slipModel, Consumer<SlipMessage> m) {
        this.slipModel = slipModel;
        action = m;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
//        borderPane.setPrefSize(1000, 800); // Ensure the pane is large enough
//        borderPane.setStyle("-fx-background-color: #000000;"); // Set background color to black for better visibility
//
//        System.out.println("Dock A");
//        for(int i = 10; i < 450; i= i + 45){
//            System.out.println("x: 120, y: " + i );
//            createLeftDock(120, i, borderPane);
//            createRightDock(120, i, borderPane);
//        }
//        System.out.println("Dock F");
//        for(int i = 480; i < 750; i= i + 45){
//            System.out.println("x: 120, y: " + i );
//            createRightDock(120, i, borderPane);
//        }
//        borderPane.getChildren().add(drawLine(120, 480, 120, 750)); // left side of dock
//
//
//        createLeftDock(370,10,borderPane);
//        createRightDock(370,10,borderPane);
//
//        createLeftDock(620,10,borderPane);
//        createRightDock(620,10,borderPane);
//
//        createLeftDock(870,10,borderPane);
//        createRightDock(870,10,borderPane);
//        System.out.println("width: " + borderPane.getWidth());
//        System.out.println("height: " + borderPane.getHeight());
//        borderPane.setCenter();

        slipModel.setSlipPane(borderPane);
        setSizeListener(borderPane);
        return borderPane;
    }

    private void setSizeListener(BorderPane borderPane) {
        ChangeListener<Number> sizeListener = (obs, oldVal, newVal) -> {
            double width = borderPane.getWidth();
            double height = borderPane.getHeight();
            buildDocks(width, height);
        };
        borderPane.widthProperty().addListener(sizeListener);
        borderPane.heightProperty().addListener(sizeListener);
    }

    private void buildDocks(double width, double height) {
        System.out.println("Pane changed size");
        slipModel.getSlipPane().setCenter(null);

    }

    private void createRightDock(double x, double y, Pane pane) {
        double dw = 45; // dock width (includes walkway and dock width)
        double dl = 100; // dock length
        double ww = 40; // walk way width
        double wh = 10; // walk way length (will affect the dock width)
        Rectangle rectangle1 = new Rectangle(x+ww, y+wh, dl ,dw -wh);
        Rectangle rectangle2 = new Rectangle(x, y, ww ,ww + wh);
        Text topText = drawText(x + ww, y + 24, "C24 Testing top", false);
        Text botText = drawText(x + ww, y + 39, "C26 Testing bot", false);
        pane.getChildren().addAll(rectangle1, rectangle2,
                drawLine(x + ww, y, x + ww, y + wh),
                drawLine(x + ww, y + wh, x + dl + ww, y + wh),
                drawLine(x + dl + ww, y + wh, x + dl + ww, y + dw),
                drawLine(x + dl + ww, y + dw, x + ww, y + dw),
                topText,botText);
    }




    private void createLeftDock(double x, double y, Pane pane) {
        double dw = 45; // dock width
        double dl = 100; // dock length
        double wh = 10; // walk way length (will affect the dock width)

        // Create and style the rectangle
        Rectangle rectangle = new Rectangle(x - dl, y + wh, dl, dw - wh);
        rectangle.setFill(Color.TRANSPARENT); // Ensure rectangle is transparent if only used for layout

        // Create the VBox and set its properties
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: #ff0000; -fx-border-color: black;"); // Add border for debugging
        vBox.setLayoutX(x - dl);
        vBox.setLayoutY(y + wh);
        vBox.setPrefWidth(dl);
        vBox.setPrefHeight(dw - wh); // Set preferred height
        vBox.setOpacity(1.0); // Ensure the VBox is fully opaque
        vBox.setAlignment(Pos.CENTER_LEFT);

        // Create the text elements
        Text topText = new Text("Testing Top C23");
        Text botText = new Text("Testing Bot C25");
        topText.setFont(new Font(11)); // Set font size to 10
        botText.setFont(new Font(11)); // Set font size to 10
        topText.setFill(Color.WHITE); // Set text color to white
        botText.setFill(Color.WHITE); // Set text color to white
        botText.setTextAlignment(TextAlignment.RIGHT);

        // Add text to the VBox
        vBox.getChildren().addAll(topText, botText);

        // Add elements to the pane
        pane.getChildren().addAll(
                drawLine(x, y, x, y + wh),
                drawLine(x, y + wh, x - dl, y + wh),
                drawLine(x - dl, y + wh, x - dl, y + dw),
                drawLine(x - dl, y + dw, x, y + dw),
                vBox);

        // Bring VBox to the front
        vBox.toFront();
    }


    private Text drawText(double startX, double startY, String str, boolean invertText) {
        Text text = new Text(startX, startY, str);
        text.setFill(Color.WHITE); // Set text color to white
        text.setFont(new Font(11)); // Set font size to 10
        if(invertText)
        text.setTextAlignment(TextAlignment.RIGHT);
        return text;
    }

    private Line drawLine(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(1);
        return line;
    }

}
