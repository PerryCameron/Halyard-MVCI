package org.ecsail.widgetfx;

import javafx.scene.control.Button;

public class ButtonFx {
    public static Button bigButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(70);
        button.setId("big-button");
        return button;
    }

    public static Button buttonOf(String text, double width) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        return button;
    }
}
