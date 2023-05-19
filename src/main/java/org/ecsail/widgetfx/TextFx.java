package org.ecsail.widgetfx;

import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
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

    public static Text textOf(String label, String id) {
        Text text = new Text(label);
        text.setId(id);
        return text;
    }

    public static Text textOf(String label, String id, StringProperty stringProperty) {
        Text text = new Text(label);
        text.setId(id);
        text.textProperty().bind(stringProperty);
        return text;
    }


}
