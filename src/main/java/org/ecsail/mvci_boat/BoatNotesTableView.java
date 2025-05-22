package org.ecsail.mvci_boat;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Builder;
import org.ecsail.fx.NotesFx;
import org.ecsail.widgetfx.CallBackFX;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.time.LocalDate;
import java.util.Arrays;

public class BoatNotesTableView implements Builder<TableView<NotesFx>> {
    private final BoatModel boatModel;
    private final BoatView boatView;
    public BoatNotesTableView(BoatView bv) {
        this.boatView = bv;
        this.boatModel = boatView.getBoatModel();
    }

    @Override
    public TableView<NotesFx> build() {
        TableView<NotesFx> tableView = TableViewFx.tableViewOf(NotesFx.class);
        boatModel.setNotesTableView(tableView);
        ChangeListener<Boolean> dataLoadedListener =
                ListenerFx.addSingleFireBooleanListener(boatModel.dataLoadedProperty(),
                        () -> tableView.setItems(boatModel.getNotesDTOS()));
        boatModel.dataLoadedProperty().addListener(dataLoadedListener);
        TableView.TableViewSelectionModel<NotesFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) boatModel.setSelectedNote(newSelection);
        });
        tableView.setPrefHeight(100);
        tableView.getColumns().addAll(Arrays.asList(createColumn1(), createColumn2()));
        return tableView;
    }

    private TableColumn<NotesFx, LocalDate> createColumn1() {
        TableColumn<NotesFx, LocalDate> col = new TableColumn<>("Date");
        col.setCellValueFactory(cellData -> cellData.getValue().memoDateProperty());
        col.setCellFactory(CallBackFX.createDatePickerCellFactory(notesDTO -> {
            boatModel.setSelectedNote(notesDTO);
            boatView.sendMessage().accept(BoatMessage.UPDATE_NOTE);
        }));
        col.prefWidthProperty().bind(boatModel.getNotesTableView().widthProperty().multiply(0.15));
        return col;
    }

    private TableColumn<NotesFx,String> createColumn2() {
        TableColumn<NotesFx, String> col = TableColumnFx.editableStringTableColumn(NotesFx::memoProperty, "Note");
        col.setOnEditCommit(
                t -> {
                    NotesFx note = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    note.setMemo(t.getNewValue());
                    boatModel.setSelectedNote(note);
                    boatView.sendMessage().accept(BoatMessage.UPDATE_NOTE);
                }
        );
        col.prefWidthProperty().bind(boatModel.getNotesTableView().widthProperty().multiply(0.85));
        return col;
    }
}
