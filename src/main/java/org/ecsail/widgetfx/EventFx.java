package org.ecsail.widgetfx;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.ecsail.BaseApplication;

public class EventFx {
    public static EventHandler<WindowEvent> setStageLocation(Stage stage) {
        return e -> {
            // Position the dialog at the center of the main stage
            stage.setX(BaseApplication.primaryStage.getX() + (BaseApplication.primaryStage.getWidth() / 2) - (stage.getWidth() / 2));
            stage.setY(BaseApplication.primaryStage.getY() + (BaseApplication.primaryStage.getHeight() / 2) - (stage.getHeight() / 2));
        };
    }
}
