package org.ecsail.widgetfx;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    public static Button buttonOf(String text, double width, Runnable runnable) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setOnAction(event -> runnable.run());
        return button;
    }

    public static ToggleButton toggleButtonOf(String text, double width, ToggleGroup tg) {
        ToggleButton button = new ToggleButton(text);
        button.setPrefWidth(width);
        button.setToggleGroup(tg);
        return button;
    }

    public static void buttonVisible(Button button, boolean value) {
        button.setVisible(value);
        button.setManaged(value);
    }

    public static Button utilityButton(String text, double width) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setContentDisplay(ContentDisplay.LEFT); // Image and text side-by-side, left-aligned
        button.setAlignment(Pos.CENTER_LEFT); // Align content to the left within the button
        return button;
    }

    public static Button utilityButton(String text, double width, Runnable runnable) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setContentDisplay(ContentDisplay.LEFT); // Image and text side-by-side, left-aligned
        button.setAlignment(Pos.CENTER_LEFT); // Align content to the left within the button
        button.setOnAction(event -> runnable.run());
        return button;
    }

    public static Button utilityButton(String text) {
        Button button = new Button(text);
        button.setContentDisplay(ContentDisplay.LEFT); // Image and text side-by-side, left-aligned
        button.setAlignment(Pos.CENTER_LEFT); // Align content to the left within the button
        return button;
    }

    public static Button utilityButton(String text, Runnable runnable) {
        Button button = new Button(text);
        button.setContentDisplay(ContentDisplay.LEFT); // Image and text side-by-side, left-aligned
        button.setAlignment(Pos.CENTER_LEFT); // Align content to the left within the button
        button.setOnAction(event -> runnable.run());
        return button;
    }

}
