package org.ecsail.mvci_boat;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Builder;
import javafx.util.Callback;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.NotesDTO;
import org.ecsail.dto.OfficerDTO;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.time.LocalDate;

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

    private TableColumn<NotesDTO, LocalDate> createColumn1() {
        TableColumn<NotesDTO, LocalDate> col1 = new TableColumn<>("Date");
        col1.setCellValueFactory(cellData -> cellData.getValue().memoDateProperty());
        col1.setCellFactory(createDatePickerCellFactory());
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

    private Callback<TableColumn<NotesDTO, LocalDate>, TableCell<NotesDTO, LocalDate>> createDatePickerCellFactory() {
        return param -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.setOnAction(event -> {
                    commitEdit(datePicker.getValue());
                    // Check if notesDTO is not null before calling methods on it
                    NotesDTO notesDTO = this.getTableRow().getItem();
                    if(notesDTO != null) {
                        notesDTO.setMemoDate(datePicker.getValue());
                        boatModel.setSelectedNote(notesDTO);
                        boatView.sendMessage().accept(BoatMessage.UPDATE_NOTE);
                    }
                });
            }

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    datePicker.setValue(item);
                    setGraphic(datePicker);
                }
            }
        };
    }

}
