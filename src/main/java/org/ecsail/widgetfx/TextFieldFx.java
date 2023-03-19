package org.ecsail.widgetfx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.util.Locale;

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
        textField.textProperty().bindBidirectional(integerProperty, new NumberStringConverter("\u0000"));
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
