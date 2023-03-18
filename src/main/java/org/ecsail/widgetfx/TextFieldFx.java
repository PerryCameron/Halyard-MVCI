package org.ecsail.widgetfx;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class TextFieldFx {

    public static TextField textFieldOf(double width, String prompt) {
       TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setPrefWidth(width);
        return textField;
    }

    public static PasswordField passwordFieldOf(double width, String prompt) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(prompt);
        passwordField.setPrefWidth(width);
        return passwordField;
    }

}
