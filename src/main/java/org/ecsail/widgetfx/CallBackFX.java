package org.ecsail.widgetfx;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.ecsail.fx.NotesFx;

import java.time.LocalDate;
import java.util.function.Consumer;

public class CallBackFX {

    public static Callback<TableColumn<NotesFx, LocalDate>,
            TableCell<NotesFx, LocalDate>> createDatePickerCellFactory(Consumer<NotesFx> notesDTOConsumer) {
        return param -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();
            private LocalDate currentItem = null;  // Add this line to store the current item
            {
                datePicker.setId("datepicker-tableview");
                datePicker.setOnAction(event -> {
                    // Only process action event when datePicker has the focus
                    if (datePicker.isFocused()) {
                        commitEdit(datePicker.getValue());
                        NotesFx notesDTO = null;
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