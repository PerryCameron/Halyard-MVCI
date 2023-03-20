package org.ecsail;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ecsail.mvci_main.MainController;


public class BaseApplication extends Application {

    public static Stage primaryStage;
    public static Stage loginStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setScene(new Scene(new MainController().getView()));
        primaryStage.getScene().getStylesheets().addAll("css/dark/dark.css");
        primaryStage.show();
        loginStage.setX(primaryStage.getX() + 260);
        loginStage.setY(primaryStage.getY() + 300);
        loginStage.show();
    }

//    public static Logger logger = LoggerFactory.getLogger(BaseApplication.class);
}
