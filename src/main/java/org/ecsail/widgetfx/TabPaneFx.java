package org.ecsail.widgetfx;

import javafx.scene.control.TabPane;

public class TabPaneFx {

    public static TabPane tabPaneOf(TabPane.TabClosingPolicy t, double width) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(t);
        tabPane.setPrefWidth(width);
        return tabPane;
    }
}
