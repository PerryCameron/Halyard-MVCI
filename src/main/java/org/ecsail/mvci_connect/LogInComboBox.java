package org.ecsail.mvci_connect;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import org.ecsail.dto.LoginDTO;

public class LogInComboBox extends ComboBox<LoginDTO> {

    public LogInComboBox(double width, ObservableList<LoginDTO> loginDTOS) {
        super();
        setPrefWidth(width);
        initialize();
        setButtonCell(createListCell());
        getItems().addAll(loginDTOS);
    }
    private void initialize() {
        setCellFactory(p -> createListCell());
    }

    private ListCell<LoginDTO> createListCell() {
        return new ListCell<>() {

            @Override
            protected void updateItem(LoginDTO loginDTO, boolean empty) {
                super.updateItem(loginDTO, empty);
                if (loginDTO == null || empty) {
                    setGraphic(null);
                    setText("");
                } else {
                    setText(loginDTO.getHost());
                }
            }
        };
    }

}
