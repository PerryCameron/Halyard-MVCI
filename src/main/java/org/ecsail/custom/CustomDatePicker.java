package org.ecsail.custom;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomDatePicker extends DatePicker {

    public CustomDatePicker() {
    }

    public CustomDatePicker(LocalDate localDate) {
        super(localDate);
        // This is a hack I got from here
        // https://stackoverflow.com/questions/32346893/javafx-datepicker-not-updating-value
        // Apparently datepicker was broken after java 8 and then fixed in java 18
        // this is a work-around until I upgrade this to java 18+
        setConverter(new StringConverter<>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty())
                    return null;
                try {
                    return LocalDate.parse(dateString, dateTimeFormatter);
                } catch (Exception e) {
                    System.out.println("Bad date value entered");
                    return null;
                }
            }
        });
    }

    public void updateValue() {
        try {
            this.setValue(this.getConverter().fromString(this.getEditor().getText()));
        } catch (DateTimeParseException e) {
            this.getEditor().setText(this.getConverter().toString(this.getValue()));
        }
    }
}
