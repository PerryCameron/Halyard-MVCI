package org.ecsail.widgetfx;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class RectangleFX {
    public static Rectangle rectangleOf() {
        Rectangle rectangle = new Rectangle(12,12);
        rectangle.setFill(javafx.scene.paint.Color.GRAY);
        rectangle.setStroke(Color.BLACK);
        return rectangle;
    }
}
