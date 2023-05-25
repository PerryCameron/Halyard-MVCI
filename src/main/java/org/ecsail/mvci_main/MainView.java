package org.ecsail.mvci_main;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.util.Builder;
import org.ecsail.BaseApplication;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.MenuFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.Objects;

import static java.lang.System.getProperty;

public class MainView implements Builder<Region> {
    private final MainModel mainModel;
    private final Runnable closeConnections;
    private final Runnable createConnectController;

    public MainView(MainModel mainModel, Runnable closeConnections, Runnable createConnectController) {
        this.mainModel = mainModel;
        this.closeConnections = closeConnections;
        this.createConnectController = createConnectController;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1028,830);
        borderPane.setTop(setUpTopPane());
        borderPane.setBottom(setUpBottomPane());
        borderPane.setCenter(setUpCenterPane());
        // closing program with x button
        BaseApplication.primaryStage.setOnHiding(event -> closeConnections.run());
        Image mainIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/title_bar_icon.png")));
        BaseApplication.primaryStage.getIcons().add(mainIcon);
        BaseApplication.primaryStage.setTitle("Halyard");
        setPrimaryStageCompleteListener();
        return borderPane;
    }

    private Node setUpBottomPane() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(statusLabel(), changeLabel());
        return hBox;
    }

    private Node changeLabel() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,0,0,0), Pos.CENTER_RIGHT);
        vBox.setStyle("-fx-background-color: #4d6955;");
        Label changeLabel = new Label();
        HBox.setHgrow(vBox, Priority.ALWAYS);
        changeLabel.setPadding(new Insets(5.0f, 5.0f, 5.0f, 5.0f));
        changeLabel.textProperty().bind(mainModel.changeStatusLabelProperty());
        vBox.getChildren().add(changeLabel);
        return vBox;
    }

    private Node statusLabel() {
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: #e83115;");
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
        menuBar.getMenus().addAll(createEditMenu());
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
            if(newValue) createConnectController.run();
        });
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
