package org.ecsail.custom;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

public class CustomIntegerTableCell<S> extends TextFieldTableCell<S, Integer> {

    private TextField textField;

    public CustomIntegerTableCell(StringConverter<Integer> converter) {
        super(converter);
    }

    @Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        }

        setGraphic(textField);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textField.selectAll();

        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                commitEdit(Integer.valueOf(textField.getText()));
            }
        });
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getConverter().toString(getItem()));
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    @Override
    public void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getConverter().toString(getItem()));
                }
                setGraphic(textField);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(getConverter().toString(getItem()));
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    private void createTextField() {
        textField = new TextField(getConverter().toString(getItem()));
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnAction(e -> commitEdit(Integer.valueOf(textField.getText())));
    }
}
