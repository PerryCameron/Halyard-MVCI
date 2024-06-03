package org.ecsail.widgetfx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import org.ecsail.dto.OfficerDTO;

import java.util.function.Function;

public class TableColumnFx {
    public static <T> TableColumn<T, String> tableColumnOf(Function<T, StringProperty> property, String label) {
        TableColumn<T, String> col = new TableColumn<>(label);
        col.setCellValueFactory(cellData -> property.apply(cellData.getValue()));
        col.setCellFactory(column -> EditCellFx.createStringEditCell());
        return col;
    }

    public static <T> TableColumn<T, Integer> tableColumnOfInteger(Function<T, IntegerProperty> property, String label) {
        TableColumn<T, Integer> col = new TableColumn<>(label);
        col.setCellValueFactory(cellData -> property.apply(cellData.getValue()).asObject());
        col.setCellFactory(tc -> new TextFieldTableCell<>(new IntegerStringConverter()));
        return col;
    }
}
