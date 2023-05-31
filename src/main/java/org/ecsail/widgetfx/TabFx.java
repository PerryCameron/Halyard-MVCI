package org.ecsail.widgetfx;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class TabFx {
    public static Tab tabOf(String tabTitle, Node node) {
        Tab tab = new Tab(tabTitle);
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2), "custom-tap-pane-frame", true); // makes outer border
        vBox.getChildren().add(node);
        tab.setContent(vBox);
        return tab;
    }
}
