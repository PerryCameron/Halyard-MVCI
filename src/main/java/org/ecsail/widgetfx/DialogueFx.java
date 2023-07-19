package org.ecsail.widgetfx;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DialogueFx {

    public static Alert customAlert(String header, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(message);
        tieAlertToStage(alert);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("css/dark/dialogue.css");
        dialogPane.getStyleClass().add("myDialog");
        return alert;
    }

    public static void tieAlertToStage(Alert alert) {
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        // Add a window showing listener to set the position of the dialog
        alertStage.addEventHandler(WindowEvent.WINDOW_SHOWN, EventFx.setStageLocation(alertStage));
    }
}
