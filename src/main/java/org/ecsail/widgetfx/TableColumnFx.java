package org.ecsail.widgetfx;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;

import java.util.function.Function;

public class TableColumnFx {
    public static <T> TableColumn<T, String> tableColumnOf(Function<T, StringProperty> property, String label) {
        TableColumn<T, String> col = new TableColumn<>(label);
        col.setCellValueFactory(cellData -> property.apply(cellData.getValue()));
        col.setCellFactory(column -> EditCellFx.createStringEditCell());
        return col ;
    }
}
