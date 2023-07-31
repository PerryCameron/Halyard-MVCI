package org.ecsail.mvci_main;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Builder;
import javafx.util.Duration;
import org.ecsail.BaseApplication;
import org.ecsail.interfaces.Status;
import org.ecsail.mvci_boatlist.BoatListMessage;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.MenuFx;
import org.ecsail.widgetfx.RectangleFX;


import java.util.Objects;
import java.util.function.Consumer;

import static java.lang.System.getProperty;

public class MainView implements Builder<Region> {
    private final MainModel mainModel;
    Consumer<MainMessages> action;
    public MainView(MainModel mainModel, Consumer<MainMessages> m) {
        this.mainModel = mainModel;
        action = m;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1028,830);
        borderPane.setTop(setUpTopPane());
        borderPane.setBottom(setUpBottomPane());
        borderPane.setCenter(setUpCenterPane());
        // closing program with x button
        BaseApplication.primaryStage.setOnHiding(event -> action.accept(MainMessages.CLOSE_ALL_CONNECTIONS));
        Image mainIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/title_bar_icon.png")));
        BaseApplication.primaryStage.getIcons().add(mainIcon);
        BaseApplication.primaryStage.setTitle("Halyard");
        setPrimaryStageCompleteListener();
        return borderPane;
    }

    private Node setUpBottomPane() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(statusLabel(), statusLights());
        return hBox;
    }

    private Node statusLights() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(0,15,0,0), Pos.CENTER_RIGHT, 3.0);
        Rectangle receive = RectangleFX.rectangleOf(14,14);
        Rectangle transmit = RectangleFX.rectangleOf(14,14);
        mainModel.getLightAnimationMap().put("receiveError", createTimeLine(Color.RED, receive,5000));
        mainModel.getLightAnimationMap().put("receiveSuccess", createTimeLine(Color.LIGHTGREEN, receive,5000));
        mainModel.getLightAnimationMap().put("transmitError", createTimeLine(Color.RED, transmit,100));
        mainModel.getLightAnimationMap().put("transmitSuccess", createTimeLine(Color.LIGHTGREEN, transmit,100));
        mainModel.lightStatusPropertyProperty()
                .addListener((observable, oldValue, newValue) -> updateStatusLights(newValue));
        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.getChildren().addAll(transmit, receive);
        return hBox;
    }

    private Timeline createTimeLine(Color color, Rectangle rect, double speed) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rect.fillProperty(), Color.GRAY)),
                new KeyFrame(Duration.millis(10), new KeyValue(rect.fillProperty(), color)),
                new KeyFrame(Duration.millis(speed), new KeyValue(rect.fillProperty(), Color.GRAY))
        );
        return timeline;
    }

    private Node statusLabel() {
        VBox vBox = new VBox();
        vBox.setPrefWidth(400);
        Label statusLabel = new Label();
        statusLabel.setPadding(new Insets(5.0f, 5.0f, 5.0f, 5.0f));
        statusLabel.textProperty().bind(mainModel.statusLabelProperty());
        mainModel.statusLabelProperty().set("(Not Connected) Ready.");
        vBox.getChildren().add(statusLabel);
        return vBox;
    }

    private Node setUpCenterPane() {
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(new Tab("Log in"));
        mainModel.setMainTabPane(tabPane);
        return tabPane;
    }

    private Node setUpTopPane() {
        VBox topElements = new VBox();
        topElements.getChildren().add(setUpMenuBar());
        ToolBar toolbar = new ToolBar();
        topElements.getChildren().add(toolbar);
        return topElements;
    }

    private Node setUpMenuBar() {
        MenuBar menuBar = new MenuBar();
        if(isMac()) menuBar.setUseSystemMenuBar(true);
        menuBar.getMenus().addAll(createEditMenu(),createFileMenu());
        return menuBar;
    }
    private Menu createEditMenu() {
        Menu menu = new Menu("Edit");
        MenuItem undo = MenuFx.menuItemOf("Undo", x -> System.out.println("undo"), KeyCode.Z);
        MenuItem redo = MenuFx.menuItemOf("Redo", x -> System.out.println("Redo"), KeyCode.R);
        SeparatorMenuItem editSeparator = new SeparatorMenuItem();
        MenuItem cut = MenuFx.menuItemOf("Cut", x -> System.out.println("Cut"), KeyCode.X);
        MenuItem copy = MenuFx.menuItemOf("Copy", x -> System.out.println("Copy"), KeyCode.C);
        MenuItem paste = MenuFx.menuItemOf("Paste", x -> System.out.println("Paste"), KeyCode.V);
        menu.getItems().addAll(undo, redo, editSeparator, cut, copy, paste);
        return menu;
    }

    private Menu createFileMenu() {
        Menu menu = new Menu("File");
        MenuItem backUp = MenuFx.menuItemOf("Backup DataBase", x -> action.accept(MainMessages.BACKUP_DATABASE), KeyCode.N);
        menu.getItems().add(backUp);
        return menu;
    }


    private static boolean isMac() {
        return getProperty("os.name").contains("Mac");
    }
    protected void closeTabs() {
        mainModel.getMainTabPane().getTabs().clear();
    }
    protected void addTab(String name, Region region) {
        Tab newTab = new Tab(name, region);
        mainModel.getMainTabPane().getTabs().add(newTab);
        mainModel.getMainTabPane().getSelectionModel().select(newTab);
    }

    private void setPrimaryStageCompleteListener() {
        mainModel.primaryStageCompleteProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) action.accept(MainMessages.CREATE_CONNECT_CONTROLLER);
        });
    }

    private void updateStatusLights(Status.light status) {
        switch (status) {
            case TX_GREEN -> mainModel.getLightAnimationMap().get("transmitSuccess").playFromStart();
            case RX_GREEN -> mainModel.getLightAnimationMap().get("receiveSuccess").playFromStart();
            case TX_RED -> mainModel.getLightAnimationMap().get("transmitError").playFromStart();
            case RX_RED -> mainModel.getLightAnimationMap().get("receiveError").playFromStart();
        }
    }

//    private static void startFileLogger() {
//        try {
//            outputFile = File.createTempFile("debug", ".log", new File(LOGFILEDIR));
//            PrintStream output = new PrintStream(new BufferedOutputStream(new FileOutputStream(outputFile)), true);
//            System.setOut(output);
//            System.setErr(output);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//

}
