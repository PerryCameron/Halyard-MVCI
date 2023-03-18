package org.ecsail.MvciMain;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.ecsail.BaseApplication;

import java.util.Objects;
import java.util.function.Consumer;

public class MainView implements Builder<Region> {
    private final MainModel mainModel;
    private final Consumer<Runnable> closeConnections;

    private Stage loginStage = new Stage();
    public MainView(MainModel mainModel, Consumer<Runnable> closeConnections) {
        this.mainModel = mainModel;
        this.closeConnections = closeConnections;
    }

    @Override
    public Region build() {
        //        startFileLogger();
//        BaseApplication.logger.info("Starting application...");
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1028,830);
        borderPane.setTop(setUpTopPane());
        borderPane.setBottom(setUpBottomPane());
        borderPane.setCenter(setUpCenterPane());
        // closing program with x button
        BaseApplication.primaryStage.setOnHiding(event -> closeConnections.accept(null));
        Image mainIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/title_bar_icon.png")));
        BaseApplication.primaryStage.getIcons().add(mainIcon);
        BaseApplication.primaryStage.setTitle("Halyard");

//        for (Plugin plugin : plugins) {
//            try {
//                plugin.setup(stage, tabPane, toolbar, this, menuBar);
//            } catch (Exception e) {
//                logger.error("Unable to start plugin");
//                logger.error(plugin.getClass().getName());
//                e.printStackTrace();
//            }
//        }


        //put window to front to avoid it to being hidden behind another.
//        stage.setAlwaysOnTop(true);
//        stage.requestFocus();
//        stage.toFront();
//        stage.setAlwaysOnTop(false);
//        connect = new ConnectDatabase(stage);

//        BaseApplication.getMainStage().getScene().getStylesheets().addAll(
//                "css/dark/dark.css",
//                "css/dark/tabpane.css",
//                "css/dark/tableview.css",
//                "css/dark/chart.css",
//                "css/dark/bod.css",
//                "css/dark/table_changes.css",
//                "css/dark/invoice.css");
        return borderPane;
    }

    private Node setUpBottomPane() {
        Label statusLabel = new Label();
        statusLabel.setPadding(new Insets(5.0f, 5.0f, 5.0f, 5.0f));
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setText("(Not Connected) Ready.");
        statusLabel.textProperty().bind(mainModel.statusLabelProperty());
        return statusLabel;
    }

    private Node setUpCenterPane() {
        TabPane tabPane = new TabPane();
        return tabPane;
    }

    private Node setUpTopPane() {
        VBox topElements = new VBox();
        MenuBar menuBar = new MenuBar();
        topElements.getChildren().add(menuBar);
        ToolBar toolbar = new ToolBar();
        topElements.getChildren().add(toolbar);
        return topElements;
    }



//    public void log(String s) {
//        statusLabel.setText(s);
//    }

//    private final Plugin[] plugins = new Plugin[]{
//            new StandardMenus(),
//            new FileDrop(),
//            new SearchToolBar(),
//            new LogFile()
//    };


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
//    public static void connectDatabase() {
//        connect = new ConnectDatabase(stage);
//    }
//
//    public static PortForwardingL getSSHConnection() {
//        return connect.getSshConnection();
//    }
//
//    public static DataSource getDataSource() {
//        return connect.getMainModel().getAppConfig().getDataSource();
//    }
//
//    public static Model getModel() { return connect.getMainModel(); }
//    }

}
