package org.ecsail.widgetfx;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class TextFx {

    public static Text linkTextOf(String label) {
        Text text = new Text(label);
        text.setFill(Color.CORNFLOWERBLUE);
        text.setOnMouseExited(ex -> text.setFill(Color.CORNFLOWERBLUE));
        text.setOnMouseEntered(en -> text.setFill(Color.RED));
        return text;
    }

}
