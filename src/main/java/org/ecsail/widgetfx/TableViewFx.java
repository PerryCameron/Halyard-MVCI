package org.ecsail.widgetfx;

import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.ecsail.dto.OfficerDTO;

public class TableViewFx {

    public static <T> TableView<T> tableViewOf(Class<T> objectClass) {
        TableView<T> tableView = new TableView<>();
        VBox.setVgrow(tableView, Priority.ALWAYS);
        HBox.setHgrow(tableView, Priority.ALWAYS);
        tableView.setPrefHeight(146);
        tableView.setFixedCellSize(30);
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return tableView;
    }
}
