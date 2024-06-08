package org.ecsail;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ecsail.mvci_main.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

import static org.ecsail.static_tools.HalyardPaths.LOGFILEDIR;


public class BaseApplication extends Application {

    public static Stage primaryStage;
    public static File outputFile;
    private static final Logger logger = LoggerFactory.getLogger(BaseApplication.class);
    public static void main(String[] args) {
        launch(args);
    }

    private static String logAppVersion() {
        Properties properties = new Properties();
        try (InputStream input = BaseApplication.class.getClassLoader().getResourceAsStream("app.properties")) {
            if (input == null) {
                logger.error("Sorry, unable to find app.properties");
                return "unknown" ;
            }
            properties.load(input);
            String appVersion = properties.getProperty("app.version");
            logger.info("Starting Halyard Application version: " + appVersion);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties.getProperty("app.version");
    }

    private static void startFileLogger() {
        try {
            outputFile = File.createTempFile("debug", ".log", new File(LOGFILEDIR));
            PrintStream output = new PrintStream(new BufferedOutputStream(new FileOutputStream(outputFile)), true);
            System.setOut(output);
            System.setErr(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        logAppVersion();
        startFileLogger();
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
}
