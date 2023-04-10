package org.ecsail.mvci_main;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.BaseApplication;
import org.ecsail.mvci_welcome.WelcomeController;
import org.ecsail.widgetfx.MenuFx;

import java.util.Objects;

import static java.lang.System.getProperty;

public class MainView implements Builder<Region> {
    private final MainModel mainModel;
    private final Runnable closeConnections;

    public MainView(MainModel mainModel, Runnable closeConnections) {
        this.mainModel = mainModel;
        this.closeConnections = closeConnections;
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
        return borderPane;
    }

    private Node setUpBottomPane() {
        Label statusLabel = new Label();
        statusLabel.setPadding(new Insets(5.0f, 5.0f, 5.0f, 5.0f));
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.textProperty().bind(mainModel.statusLabelProperty());
        mainModel.statusLabelProperty().set("(Not Connected) Ready.");
        return statusLabel;
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

    protected void setStatus(String status) {
        mainModel.statusLabelProperty().set(status);
    }

    protected void closeTabs() {
        mainModel.getMainTabPane().getTabs().clear();
    }
    protected void addTab(String name, Region region) {
        mainModel.getMainTabPane().getTabs().add(new Tab(name, region));
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
