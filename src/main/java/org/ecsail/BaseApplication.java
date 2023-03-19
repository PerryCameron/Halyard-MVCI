package org.ecsail;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ecsail.mvci_main.MainController;


public class BaseApplication extends Application {

    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setScene(new Scene(new MainController().getView()));
        primaryStage.getScene().getStylesheets().addAll(
                "css/dark/dark.css",
                "css/dark/tabpane.css",
                "css/dark/tableview.css",
                "css/dark/chart.css",
                "css/dark/bod.css",
                "css/dark/table_changes.css",
                "css/dark/invoice.css");
        primaryStage.show();
    }

//    public static void setUpForFirstTime() {
//        HalyardPaths.checkPath(System.getProperty("user.home") + "/.ecsc/scripts");
//        HalyardPaths.checkPath(System.getProperty("user.home") + "/.ecsc/logs");
//    }

//    public static Logger logger = LoggerFactory.getLogger(BaseApplication.class);
}
