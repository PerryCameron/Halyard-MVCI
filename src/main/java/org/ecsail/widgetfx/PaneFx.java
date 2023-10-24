package org.ecsail.widgetfx;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PaneFx {

    public static boolean tabIsOpen(int msId, TabPane tabPane) {
        return tabPane.getTabs().stream()
                .anyMatch(tab -> Integer.valueOf(msId).equals(tab.getUserData()));

    }

    public static TabPane tabPaneOf(TabPane.TabClosingPolicy t, double width) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(t);
        tabPane.setPrefWidth(width);
        return tabPane;
    }

    public static TabPane tabPaneOf(TabPane.TabClosingPolicy t, double width, String id) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(t);
        tabPane.setPrefWidth(width);
//        tabPane.prefWidthProperty().bind(hBox.widthProperty().multiply(0.5));
        tabPane.setId(id);
        return tabPane;
    }

    public static TabPane tabPaneOf(TabPane.TabClosingPolicy t, String id) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(t);
        tabPane.setId(id);
        return tabPane;
    }

    public static ScrollPane scrollPaneOf() {
        ScrollPane scrollPane = new ScrollPane();
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        return scrollPane;
    }
}
