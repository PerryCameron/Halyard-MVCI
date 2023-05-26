package org.ecsail.widgetfx;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class RectangleFX {
    public static Rectangle rectangleOf(double width, double height) {
        Rectangle rectangle = new Rectangle(width,height);
        rectangle.setFill(javafx.scene.paint.Color.GRAY);
        rectangle.setStroke(Color.BLACK);
        return rectangle;
    }
}
