package org.ecsail.mvci_boat;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Builder;
import org.ecsail.dto.NotesDTO;
import org.ecsail.widgetfx.CallBackFX;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.time.LocalDate;
import java.util.Arrays;

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
        ChangeListener<Boolean> dataLoadedListener =
                ListenerFx.addSingleFireBooleanListener(boatModel.dataLoadedProperty(),
                        () -> tableView.setItems(boatModel.getNotesDTOS()));
        boatModel.dataLoadedProperty().addListener(dataLoadedListener);
        TableView.TableViewSelectionModel<NotesDTO> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) boatModel.setSelectedNote(newSelection);
        });
        tableView.setPrefHeight(100);
        tableView.getColumns().addAll(Arrays.asList(createColumn1(), createColumn2()));
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
        TableColumn<NotesDTO, String> col2 = TableColumnFx.editableStringTableColumn(NotesDTO::memoProperty, "Note");
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
