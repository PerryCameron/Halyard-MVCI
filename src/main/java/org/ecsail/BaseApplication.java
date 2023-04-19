package org.ecsail;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ecsail.mvci_main.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BaseApplication extends Application {

    public static Stage primaryStage;
    private static final Logger logger = LoggerFactory.getLogger(BaseApplication.class);
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        logger.info("Starting application");
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
        logger.info("Primary stage shown");
    }
}
