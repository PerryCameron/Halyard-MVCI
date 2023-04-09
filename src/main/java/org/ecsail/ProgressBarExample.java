package org.ecsail;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProgressBarExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create an IntegerProperty object
        IntegerProperty progress = new SimpleIntegerProperty(0);

        // Create a ProgressBar control
        ProgressBar progressBar = new ProgressBar();

        // Bind the progress of the ProgressBar to the IntegerProperty
        progressBar.progressProperty().bind(progress.divide(100.0));

        // Create a VBox to hold the ProgressBar
        VBox root = new VBox(progressBar);

        // Create a Scene with the VBox as its root node
        Scene scene = new Scene(root, 300, 250);

        // Set the title of the stage and show it
        primaryStage.setTitle("Progress Bar Example");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Simulate a task that updates the progress
        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                int finalI = i;
                Platform.runLater(() -> progress.set(finalI));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
