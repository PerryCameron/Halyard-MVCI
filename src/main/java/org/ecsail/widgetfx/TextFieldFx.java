package org.ecsail.widgetfx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

public class TextFieldFx {

    public static TextField textFieldOf(double width, String prompt) {
       TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setPrefWidth(width);
        return textField;
    }

    public static TextField textFieldOf(double width, IntegerProperty integerProperty) {
        TextField textField = new TextField();
        textField.setPrefWidth(width);
        textField.textProperty().bindBidirectional(integerProperty, new NumberStringConverter("#"));
        return textField;
    }

    public static TextField textFieldOf(double width, StringProperty stringProperty) {
        TextField textField = new TextField();
        textField.setPrefWidth(width);
        textField.textProperty().bindBidirectional(stringProperty);
        return textField;
    }

    public static PasswordField passwordFieldOf(double width, String prompt) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(prompt);
        passwordField.setPrefWidth(width);
        return passwordField;
    }
}
