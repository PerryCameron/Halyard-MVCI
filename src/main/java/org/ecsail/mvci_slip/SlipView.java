package org.ecsail.mvci_slip;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.widgetfx.VBoxFx;

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
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(hBox);
        slipModel.setMainBox(hBox);
        slipModel.setBorderPane(borderPane);
        setSizeListener();
        return borderPane;
    }

    private void setSizeListener() {
        ChangeListener<Number> sizeListener = (obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                buildDocks();
            });
        };
        slipModel.getBorderPane().widthProperty().addListener(sizeListener);
        slipModel.getBorderPane().heightProperty().addListener(sizeListener);
    }

    private void buildDocks() {
        slipModel.getMainBox().getChildren().clear();
        System.out.println("MainBox width: " + slipModel.getMainBox().getWidth());
        // get space for outside of docks, between window edge and docks
        double insets = (slipModel.getMainBox().getWidth() * .02) / 2;
        slipModel.getMainBox().setPadding(new Insets(10, insets, 0, insets));
        // this is the width of each dock
        double dockWidth = (slipModel.getMainBox().getWidth() - (insets * 2)) / 4;
        // sets space between docks
        slipModel.setDockPadding(dockWidth * 0.03); // changing this to .01 will fix the border issue
//        System.out.println("dockWidth * 0.03: " + dockWidth * 0.03);
//        System.out.println("dockWidth * 0.01: " + dockWidth * 0.01);
//        dockWidth * 0.03: 7.5558000000000005
//        dockWidth * 0.01: 2.5186
        slipModel.getMainBox().setSpacing(slipModel.getDockPadding());
        // dock width adjusted for padding
        slipModel.setDockWidth((dockWidth - slipModel.getDockPadding()) * 1.00773); // 1.00773 corrects for error
        // dock height (-10 is for top inset)
        slipModel.setDockHeight((slipModel.getMainBox().getHeight() / 18) - 10);
        // sets spacing between text on each dock
        slipModel.setDockTextSpacing(slipModel.getDockHeight() * .1);

        double first = insets * 2;
        double second = slipModel.getDockPadding() * 3;
        double third = ((dockWidth - slipModel.getDockPadding()) * 1.00773) * 4; // show fix
        System.out.println("total insets: " + first);
        System.out.println("individual padding: " + slipModel.getDockPadding());
        System.out.println("total padding: " + second);
        System.out.println("Dock Width: " + slipModel.getDockWidth() + " height: " + slipModel.getDockHeight());
        System.out.println("total dockWidth: " + third);
        System.out.println("total insets + total padding + total dockwidth: " + (first + second + third));
        System.out.println();

        slipModel.getMainBox().getChildren().add(createDockColumnBox());
        slipModel.getMainBox().getChildren().add(createDockColumnBox());
        slipModel.getMainBox().getChildren().add(createDockColumnBox());
        slipModel.getMainBox().getChildren().add(createDockColumnBox());
    }

    private Node createDockColumnBox() {
        VBox vBox = new VBox();
        HBox.setHgrow(vBox, Priority.ALWAYS);
//        vBox.setStyle("-fx-background-color: blue;");
        vBox.getChildren().add(buildDockSpacer("top-cap"));
        vBox.getChildren().add(buildDockSection(true, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("segment"));
        vBox.getChildren().add(buildDockSection(true, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("segment"));
        vBox.getChildren().add(buildDockSection(true, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("segment"));
        vBox.getChildren().add(buildDockSection(true, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("segment"));
        vBox.getChildren().add(buildDockSection(true, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("segment"));
        vBox.getChildren().add(buildDockSection(true, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("segment"));
        vBox.getChildren().add(buildDockSection(true, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("segment"));
        vBox.getChildren().add(buildDockSection(true, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("segment"));
        vBox.getChildren().add(buildDockSection(true, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("segment"));
        vBox.getChildren().add(buildDockSection(false, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("bottom-cap"));
        vBox.getChildren().add(buildDockSpacer("spacer"));

        vBox.getChildren().add(buildDockSpacer("top-cap"));
        vBox.getChildren().add(buildDockSection(false, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("segment"));
        vBox.getChildren().add(buildDockSection(false, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("segment"));
        vBox.getChildren().add(buildDockSection(false, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("segment"));
        vBox.getChildren().add(buildDockSection(false, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("segment"));
        vBox.getChildren().add(buildDockSection(false, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("segment"));
        vBox.getChildren().add(buildDockSection(false, "Cameron P. C24", "Dalton, C. C26", "C23 Crawford P.", "C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer("bottom-cap"));

        return vBox;
    }


    private Node buildDockSection(boolean leftDockVisible, String mem1, String mem2, String mem3, String mem4) {
        HBox hBox = new HBox();
        double dockSection = slipModel.getDockWidth() * 0.45;
        hBox.setPrefHeight(slipModel.getDockHeight());
        hBox.setMaxHeight(slipModel.getDockHeight());
        hBox.getChildren().add(buildDock(dockSection, true, leftDockVisible, mem1, mem2));
        hBox.getChildren().add(buildWalkWay(leftDockVisible));
        hBox.getChildren().add(buildDock(dockSection, false, leftDockVisible, mem3, mem4));
        return hBox;
    }

    private Node buildDockSpacer(String type) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        VBox vBox = VBoxFx.vBoxOf(slipModel.getDockWidth() * 0.1, slipModel.getDockHeight() * 0.3);
        switch(type) {
            case "top-cap" -> vBox.setStyle("-fx-background-color: white; -fx-border-color: red; -fx-border-width: 1px 1px 0px 1px;");
            case "segment" -> vBox.setStyle("-fx-background-color: white; -fx-border-color: red; -fx-border-width: 0px 1px 0px 1px;");
            case "bottom-cap" -> vBox.setStyle("-fx-background-color: white; -fx-border-color: red; -fx-border-width: 0px 1px 1px 1px;");
        }
        hBox.getChildren().add(vBox);
        return hBox;
    }

    private Node buildWalkWay(boolean leftDockVisible) {
        VBox vBox = VBoxFx.vBoxOf(slipModel.getDockWidth() * 0.1, slipModel.getDockHeight());
//        System.out.println("buildWalkWayWidth: " + slipModel.getDockWidth() * 0.1);
        if (leftDockVisible)
            vBox.setStyle("-fx-background-color: white;");
        else
            vBox.setStyle("-fx-background-color: white; -fx-border-color: red; -fx-border-width: 0px 0px 0px 1px;");
        return vBox;
    }

    private Node buildDock(double dockWidth, boolean isLeftDock, boolean isVisible, String mem1, String mem2) {
        VBox vBox = VBoxFx.vBoxOf(dockWidth, slipModel.getDockHeight());
        vBox.setSpacing(slipModel.getDockSpacing());
        if (isLeftDock) {
            if (isVisible) {
                vBox.setAlignment(Pos.CENTER_RIGHT);
                vBox.setStyle("-fx-background-color: white; -fx-border-color: red; -fx-border-width: 1px 0px 1px 1px;");
                vBox.getChildren().addAll(new Text(mem1), new Text(mem2));
            } // else vBox.setStyle("-fx-border-color: grey; -fx-border-width: 0px 1px 0px 0px;");
        } else {
            vBox.setAlignment(Pos.CENTER_LEFT);
            vBox.setStyle("-fx-background-color: white; -fx-border-color: red; -fx-border-width: 1px 1px 1px 0px;");
            vBox.getChildren().addAll(new Text(mem1), new Text(mem2));
        }
        return vBox;
    }



}
//    private void createRightDock(double x, double y, Pane pane) {
//        double dw = 45; // dock width (includes walkway and dock width)
//        double dl = 100; // dock length
//        double ww = 40; // walk way width
//        double wh = 10; // walk way length (will affect the dock width)
//        Rectangle rectangle1 = new Rectangle(x+ww, y+wh, dl ,dw -wh);
//        Rectangle rectangle2 = new Rectangle(x, y, ww ,ww + wh);
//        Text topText = drawText(x + ww, y + 24, "C24 Testing top", false);
//        Text botText = drawText(x + ww, y + 39, "C26 Testing bot", false);
//        pane.getChildren().addAll(rectangle1, rectangle2,
//                drawLine(x + ww, y, x + ww, y + wh),
//                drawLine(x + ww, y + wh, x + dl + ww, y + wh),
//                drawLine(x + dl + ww, y + wh, x + dl + ww, y + dw),
//                drawLine(x + dl + ww, y + dw, x + ww, y + dw),
//                topText,botText);
//    }


//    private void createLeftDock(double x, double y, Pane pane) {
//        double dw = 45; // dock width
//        double dl = 100; // dock length
//        double wh = 10; // walk way length (will affect the dock width)
//
//        // Create and style the rectangle
//        Rectangle rectangle = new Rectangle(x - dl, y + wh, dl, dw - wh);
//        rectangle.setFill(Color.TRANSPARENT); // Ensure rectangle is transparent if only used for layout
//
//        // Create the VBox and set its properties
//        VBox vBox = new VBox();
//        vBox.setStyle("-fx-background-color: #ff0000; -fx-border-color: black;"); // Add border for debugging
//        vBox.setLayoutX(x - dl);
//        vBox.setLayoutY(y + wh);
//        vBox.setPrefWidth(dl);
//        vBox.setPrefHeight(dw - wh); // Set preferred height
//        vBox.setOpacity(1.0); // Ensure the VBox is fully opaque
//        vBox.setAlignment(Pos.CENTER_LEFT);
//
//        // Create the text elements
//        Text topText = new Text("Testing Top C23");
//        Text botText = new Text("Testing Bot C25");
//        topText.setFont(new Font(11)); // Set font size to 10
//        botText.setFont(new Font(11)); // Set font size to 10
//        topText.setFill(Color.WHITE); // Set text color to white
//        botText.setFill(Color.WHITE); // Set text color to white
//        botText.setTextAlignment(TextAlignment.RIGHT);
//
//        // Add text to the VBox
//        vBox.getChildren().addAll(topText, botText);
//
//        // Add elements to the pane
//        pane.getChildren().addAll(
//                drawLine(x, y, x, y + wh),
//                drawLine(x, y + wh, x - dl, y + wh),
//                drawLine(x - dl, y + wh, x - dl, y + dw),
//                drawLine(x - dl, y + dw, x, y + dw),
//                vBox);
//
//        // Bring VBox to the front
//        vBox.toFront();
//    }
//
//
//    private Text drawText(double startX, double startY, String str, boolean invertText) {
//        Text text = new Text(startX, startY, str);
//        text.setFill(Color.WHITE); // Set text color to white
//        text.setFont(new Font(11)); // Set font size to 10
//        if(invertText)
//        text.setTextAlignment(TextAlignment.RIGHT);
//        return text;
//    }
//
//    private Line drawLine(double startX, double startY, double endX, double endY) {
//        Line line = new Line(startX, startY, endX, endY);
//        line.setStroke(Color.WHITE);
//        line.setStrokeWidth(1);
//        return line;
//    }
//
//}

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