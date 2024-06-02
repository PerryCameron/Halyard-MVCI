package org.ecsail.mvci_slip;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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
        // get space for outside of docks, between window edge and docks
        double insets = (slipModel.getMainBox().getWidth() * .01) / 2;
        slipModel.getMainBox().setPadding(new Insets(10,insets,0,insets));
        // this is the width of each dock
        double dockWidth = (slipModel.getMainBox().getWidth() - (insets * 2)) / 4;
        // sets space between docks
        slipModel.setDockPadding(dockWidth * 0.02);
        slipModel.getMainBox().setSpacing(slipModel.getDockPadding());
        // dock width adjusted for padding
        slipModel.setDockWidth(dockWidth - slipModel.getDockPadding());
        // dock height
        slipModel.setDockHeight((slipModel.getMainBox().getHeight() / 16) - 10);
        // sets spacing between text on each dock
        slipModel.setDockSpacing(slipModel.getDockHeight() * .1);

        System.out.println("Section padding: " + slipModel.getDockPadding());

        System.out.println("Dock length: " + slipModel.getDockWidth() + " height: " + slipModel.getDockHeight());
        slipModel.getMainBox().getChildren().add(createDockColumnBox());
        slipModel.getMainBox().getChildren().add(createDockColumnBox());
        slipModel.getMainBox().getChildren().add(createDockColumnBox());
        slipModel.getMainBox().getChildren().add(createDockColumnBox());
    }

    private Node createDockColumnBox() {
        VBox vBox = new VBox();
        HBox.setHgrow(vBox, Priority.ALWAYS);
//        vBox.setStyle("-fx-background-color: blue;");
        vBox.getChildren().add(buildDockSection("Cameron P. C24","Dalton, C. C26","C23 Crawford P.","C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer());
        vBox.getChildren().add(buildDockSection("Cameron P. C24","Dalton, C. C26","C23 Crawford P.","C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer());
        vBox.getChildren().add(buildDockSection("Cameron P. C24","Dalton, C. C26","C23 Crawford P.","C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer());
        vBox.getChildren().add(buildDockSection("Cameron P. C24","Dalton, C. C26","C23 Crawford P.","C25 Thompson L."));
        vBox.getChildren().add(buildDockSpacer());
        return vBox;
    }

    private Node buildDockSection(String mem1, String mem2 ,String mem3, String mem4) {
        HBox hBox = new HBox();
        double dockSection = slipModel.getDockWidth() * 0.45;
        hBox.setPrefHeight(slipModel.getDockHeight());
        hBox.setMaxHeight(slipModel.getDockHeight());
        hBox.getChildren().add(buildDock(dockSection, true,mem1,mem2));
        hBox.getChildren().add(buildWalkWay(slipModel.getDockWidth() * 0.1));
        hBox.getChildren().add(buildDock(dockSection, false,mem3,mem4));
        return hBox;
    }

    private Node buildDockSpacer() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        Region vBox = VBoxFx.vBoxOf(slipModel.getDockWidth() * 0.1, slipModel.getDockHeight() * 0.3);
        vBox.setStyle("-fx-background-color: white;");
        // Create the left and right lines
        Line leftLine = new Line(0, 0, 0, slipModel.getDockHeight() * 0.3);
        leftLine.setStroke(Color.GREY);
        leftLine.setStrokeWidth(1);

        Line rightLine = new Line(0, 0, 0, slipModel.getDockHeight() * 0.3);
        rightLine.setStroke(Color.GREY);
        rightLine.setStrokeWidth(1);

        // Add the lines and VBox to the HBox
        hBox.getChildren().addAll(leftLine, vBox, rightLine);
        return hBox;
    }


    private Node buildDock(double dockWidth, boolean isLeftDock, String mem1, String mem2) {
        VBox vBox = VBoxFx.vBoxOf(dockWidth, slipModel.getDockHeight());
        vBox.setStyle("-fx-background-color: white; -fx-border-color: grey;");
        vBox.getChildren().addAll(new Text(mem1), new Text(mem2));
        if(isLeftDock) {
            vBox.setAlignment(Pos.CENTER_RIGHT);
        } else {
            vBox.setAlignment(Pos.CENTER_LEFT);
        }
        return vBox;
    }

    private Node buildWalkWay(double walkWayWidth) {
        VBox vBox = VBoxFx.vBoxOf(walkWayWidth, slipModel.getDockHeight());
        System.out.println("walkway " + walkWayWidth + " x " +slipModel.getDockHeight());
        vBox.setStyle("-fx-background-color: white;");
//        vBox.getChildren().add(new Text("x"));
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