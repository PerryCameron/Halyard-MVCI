package org.ecsail.widgetfx;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabPaneFx {

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
        tabPane.setId(id);
        return tabPane;
    }

    public static TabPane tabPaneOf(TabPane.TabClosingPolicy t, String id) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(t);
        tabPane.setId(id);
        return tabPane;
    }
}
