package org.ecsail.widgetfx;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.ecsail.dto.NotesDTO;

import java.time.LocalDate;
import java.util.function.Consumer;

public class CallBackFX {

    public static Callback<TableColumn<NotesDTO, LocalDate>,
            TableCell<NotesDTO, LocalDate>> createDatePickerCellFactory(Consumer<NotesDTO> notesDTOConsumer) {
        return param -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();
            {
                datePicker.setId("datepicker-tableview");
                datePicker.setOnAction(event -> {
                    commitEdit(datePicker.getValue());
                    // Check if notesDTO is not null before calling methods on it
                    NotesDTO notesDTO = this.getTableRow().getItem();
                    if(notesDTO != null) {
                        notesDTO.setMemoDate(datePicker.getValue());
                        notesDTOConsumer.accept(notesDTO);
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
