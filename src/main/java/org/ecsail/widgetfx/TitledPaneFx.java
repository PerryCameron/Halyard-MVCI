package org.ecsail.widgetfx;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TitledPaneFx {

    public static TitledPane titledPaneOf(String text) {
        TitledPane titledPane = new TitledPane();
        VBox.setVgrow(titledPane, Priority.ALWAYS);
        titledPane.setText(text);
        return titledPane;
    }
}
