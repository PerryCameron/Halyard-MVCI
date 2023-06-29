package org.ecsail.mvci_boat;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Builder;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.NotesDTO;
import org.ecsail.dto.OfficerDTO;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

public class NotesTableView implements Builder<TableView<NotesDTO>> {
    private final BoatModel boatModel;
    private final BoatView boatView;
    public NotesTableView(BoatView bv) {
        this.boatView = bv;
        this.boatModel = boatView.getBoatModel();
    }

    @Override
    public TableView<NotesDTO> build() {
        TableView<NotesDTO> tableView = TableViewFx.tableViewOf(NotesDTO.class);
        ChangeListener<Boolean> dataLoadedListener = ListenerFx.createSingleUseListener(boatModel.dataLoadedProperty(), () -> {
            tableView.setItems(boatModel.getNotesDTOS());
        });
        boatModel.dataLoadedProperty().addListener(dataLoadedListener);
        tableView.setPrefHeight(100);
        tableView.getColumns().addAll(createColumn1(), createColumn2());
        return tableView;
    }

    private TableColumn<NotesDTO,String> createColumn1() {
        TableColumn<NotesDTO, String> col1 = TableColumnFx.tableColumnOf(NotesDTO::memoDateProperty, "Year");
        col1.setOnEditCommit(
                t -> {
                    NotesDTO note = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    note.setMemoDate(t.getNewValue());
                    boatModel.setSelectedNote(note);
                    boatView.sendMessage().accept(BoatMessage.UPDATE_NOTE);
                }
        );
        col1.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );   // Date
        return col1;
    }

    private TableColumn<NotesDTO,String> createColumn2() {
        TableColumn<NotesDTO, String> col2 = TableColumnFx.tableColumnOf(NotesDTO::memoProperty, "Note");
        col2.setOnEditCommit(
                t -> {
                    NotesDTO note = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    note.setMemo(t.getNewValue());
                    boatModel.setSelectedNote(note);
                    boatView.sendMessage().accept(BoatMessage.UPDATE_NOTE);
                }
        );
        col2.setMaxWidth( 1f * Integer.MAX_VALUE * 85 );   // Note
        return col2;
    }

}
