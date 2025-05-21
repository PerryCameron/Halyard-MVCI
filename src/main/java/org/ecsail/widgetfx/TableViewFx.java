package org.ecsail.widgetfx;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TableViewFx {

    public static <T> TableView<T> tableViewOf(Class<T> objectClass, double prefHeight) {
        TableView<T> tableView = new TableView<>();
        HBox.setHgrow(tableView, Priority.ALWAYS);
        tableView.setPrefHeight(prefHeight);
        tableView.setFixedCellSize(30);
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        return tableView;
    }

    public static <T> TableView<T> tableViewOf(Class<T> objectClass) {
        TableView<T> tableView = new TableView<>();
        VBox.setVgrow(tableView, Priority.ALWAYS);
        HBox.setHgrow(tableView, Priority.ALWAYS);
        tableView.setFixedCellSize(30);
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        return tableView;
    }

    public static <T> void requestFocusOnTable(TableView<T> tableView) {
        TableColumn<T, ?> firstColumn = tableView.getColumns().get(0);
        tableView.layout();
        tableView.requestFocus();
        tableView.getSelectionModel().select(0);
        tableView.getFocusModel().focus(0);
        tableView.edit(0, firstColumn);
    }
}
