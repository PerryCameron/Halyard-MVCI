package org.ecsail.widgetfx;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class VBoxFx {

    public static VBox vBoxOf(double width, double top, double right, double bottom, double left, String style) {
        VBox vBox = new VBox();
        vBox.setPrefWidth(width);
        vBox.setPadding(new Insets(top,right,bottom,left));
        vBox.setId(style);
        return vBox;
    }

    public static VBox vBoxOf(double top, double right, double bottom, double left) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(top,right,bottom,left));
        return vBox;
    }
}
