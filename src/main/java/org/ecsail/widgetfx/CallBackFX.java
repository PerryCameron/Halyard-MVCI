package org.ecsail.widgetfx;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.ecsail.fx.NotesDTOFx;

import java.time.LocalDate;
import java.util.function.Consumer;

public class CallBackFX {

//    public static Callback<TableColumn<NotesDTO, LocalDate>,
//            TableCell<NotesDTO, LocalDate>> createDatePickerCellFactory(Consumer<NotesDTO> notesDTOConsumer) {
//        return param -> new TableCell<>() {
//            private final DatePicker datePicker = new DatePicker();
//            private final EventHandler<ActionEvent> datePickerAction = event -> {
//                commitEdit(datePicker.getValue());
//                NotesDTO notesDTO = null;
//                // Prevents exception on load for NotesTableView.class
//                if(this.getTableRow() != null) notesDTO = this.getTableRow().getItem();
//                // Also prevents exception on load but for BoatNotesTableView.class
//                if(notesDTO != null) {
//                    notesDTO.setMemoDate(datePicker.getValue());
//                    notesDTOConsumer.accept(notesDTO);
//                }
//            };
//
//            {
//                datePicker.setId("datepicker-tableview");
//                datePicker.setOnAction(datePickerAction);
//            }
//
//            @Override
//            protected void updateItem(LocalDate item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    datePicker.setOnAction(null);
//                    datePicker.setValue(item);
//                    datePicker.setOnAction(datePickerAction);
//                    setGraphic(datePicker);
//                }
//            }
//        };
//    }


//    public static Callback<TableColumn<NotesDTO, LocalDate>, // version 2 inhibits manual change of datePicker
//            TableCell<NotesDTO, LocalDate>> createDatePickerCellFactory(Consumer<NotesDTO> notesDTOConsumer) {
//        return param -> new TableCell<>() {
//            private final DatePicker datePicker = new DatePicker();
//            private boolean programmaticChange = false; // prevents a change from scrolling tableview
//
//            {
//                datePicker.setId("datepicker-tableview");
//                datePicker.setOnAction(event -> {
//                    if (programmaticChange) {
//                        return;
//                    }
//                    commitEdit(datePicker.getValue());
//                    NotesDTO notesDTO = null;
//                    // Prevents exception on load for NotesTableView.class
//                    if(this.getTableRow() != null) notesDTO = this.getTableRow().getItem();
//                    // Also prevents exception on load but for BoatNotesTableView.class
//                    if(notesDTO != null) {
//                        notesDTO.setMemoDate(datePicker.getValue());
//                        notesDTOConsumer.accept(notesDTO);
//                    }
//                });
//            }
//
//            @Override
//            protected void updateItem(LocalDate item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    programmaticChange = true;
//                    datePicker.setValue(item);
//                    programmaticChange = false;
//                    setGraphic(datePicker);
//                }
//            }
//        };
//    }


    public static Callback<TableColumn<NotesDTOFx, LocalDate>,
            TableCell<NotesDTOFx, LocalDate>> createDatePickerCellFactory(Consumer<NotesDTOFx> notesDTOConsumer) {
        return param -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();
            private LocalDate currentItem = null;  // Add this line to store the current item
            {
                datePicker.setId("datepicker-tableview");
                datePicker.setOnAction(event -> {
                    // Only process action event when datePicker has the focus
                    if (datePicker.isFocused()) {
                        commitEdit(datePicker.getValue());
                        NotesDTOFx notesDTO = null;
                        // Prevents exception on load for NotesTableView.class
                        if (this.getTableRow() != null) notesDTO = this.getTableRow().getItem();
                        // Also prevents exception on load but for BoatNotesTableView.class
                        if (notesDTO != null) {
                            notesDTO.setMemoDate(datePicker.getValue());
                            notesDTOConsumer.accept(notesDTO);
                        }
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
