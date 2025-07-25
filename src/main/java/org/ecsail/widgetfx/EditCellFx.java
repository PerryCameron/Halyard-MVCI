package org.ecsail.widgetfx;

import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.util.StringConverter;


public class EditCellFx<S, T> extends TableCell<S, T> {

    // Text field for editing
    // TODO: allow this to be a plugable control.
    private final TextField textField = new TextField();
    
    // Converter for converting the text in the text field to the user type, and vice-versa:
    private final StringConverter<T> converter ;
    
    public EditCellFx(StringConverter<T> converter) {
        this.converter = converter ;

        itemProperty().addListener((obx, oldItem, newItem) -> {
            if (newItem == null) {
                setText(null);
            } else {
                setText(converter.toString(newItem));
            }
        });
        setGraphic(textField);
        setContentDisplay(ContentDisplay.TEXT_ONLY);

        textField.setOnAction(evt -> commitEdit(this.converter.fromString(textField.getText())));
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                commitEdit(this.converter.fromString(textField.getText()));
            }
        });
    }
    
    /**
     * Convenience converter that does nothing (converts Strings to themselves and vice-versa...).
     */
    public static final StringConverter<String> IDENTITY_CONVERTER = new StringConverter<>() {

        @Override
        public String toString(String object) {
            return object;
        }

        @Override
        public String fromString(String string) {
            return string;
        }

    };
    
    /**
     * Convenience method for creating an EditCell for a String value.
     * @return an editable cell
     */
    public static <S> EditCellFx<S, String> createStringEditCell() {
        return new EditCellFx<>(IDENTITY_CONVERTER);
    }

    public static <S> EditCellFx<S, Integer> createIntegerEditCell() {
        return new EditCellFx<>(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return object == null ? "" : object.toString();
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return string == null || string.isEmpty() ? null : Integer.valueOf(string);
                } catch (NumberFormatException e) {
                    return null; // or handle invalid input differently
                }
            }
        });
    }
    

    // set the text of the text field and display the graphic
    @Override
    public void startEdit() {
        super.startEdit();
        textField.setText(converter.toString(getItem()));
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textField.requestFocus();   
    }

    // revert to text display
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    // commits the edit. Update property if possible and revert to text display
    @Override
    public void commitEdit(T item) {
        
        // This block is necessary to support commit on losing focus, because the baked-in mechanism
        // sets our editing state to false before we can intercept the loss of focus.
        // The default commitEdit(...) method simply bails if we are not editing...
        if (! isEditing() && ! item.equals(getItem())) {
            TableView<S> table = getTableView();
            if (table != null) {
                TableColumn<S, T> column = getTableColumn();
                CellEditEvent<S, T> event = new CellEditEvent<>(table,
                        new TablePosition<>(table, getIndex(), column),
                        TableColumn.editCommitEvent(), item);
                Event.fireEvent(column, event);
            }
        }

        super.commitEdit(item);
        
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

}