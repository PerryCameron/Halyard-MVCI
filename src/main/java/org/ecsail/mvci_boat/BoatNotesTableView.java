package org.ecsail.mvci_boat;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.util.Builder;
import javafx.util.Callback;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.NotesDTO;
import org.ecsail.widgetfx.CallBackFX;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.time.LocalDate;

public class BoatNotesTableView implements Builder<TableView<NotesDTO>> {
    private final BoatModel boatModel;
    private final BoatView boatView;
    public BoatNotesTableView(BoatView bv) {
        this.boatView = bv;
        this.boatModel = boatView.getBoatModel();
    }

    @Override
    public TableView<NotesDTO> build() {
        TableView<NotesDTO> tableView = TableViewFx.tableViewOf(NotesDTO.class);
        ChangeListener<Boolean> dataLoadedListener = ListenerFx.createSingleUseListener(boatModel.dataLoadedProperty(), () -> {
            tableView.setItems(boatModel.getNotesDTOS());
        });
        tableView.setRowFactory(tv -> {
            TableRow<NotesDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    boatModel.setSelectedNote(row.getItem());
                }
            });
            return row;
        });
        boatModel.dataLoadedProperty().addListener(dataLoadedListener);
        tableView.setPrefHeight(100);
        tableView.getColumns().addAll(createColumn1(), createColumn2());
        return tableView;
    }

    private TableColumn<NotesDTO, LocalDate> createColumn1() {
        TableColumn<NotesDTO, LocalDate> col1 = new TableColumn<>("Date");
        col1.setCellValueFactory(cellData -> cellData.getValue().memoDateProperty());
        col1.setCellFactory(CallBackFX.createDatePickerCellFactory(notesDTO -> {
            boatModel.setSelectedNote(notesDTO);
            boatView.sendMessage().accept(BoatMessage.UPDATE_NOTE);
        }));
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 15);   // Date
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
