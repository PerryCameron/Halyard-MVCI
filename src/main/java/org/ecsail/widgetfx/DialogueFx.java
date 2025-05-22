package org.ecsail.widgetfx;

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.ecsail.BaseApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

public class DialogueFx {

    private static final Logger logger = LoggerFactory.getLogger(DialogueFx.class);

    public static Alert aboutDialogue(String header, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header); // I would like the header to be a larger font
        alert.setContentText(message);

        alert.setTitle("");

        Image image = new Image(Objects.requireNonNull(DialogueFx.class.getResourceAsStream("/images/halyard-64.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(64); // Adjust the height as needed
        imageView.setFitWidth(64);  // Adjust the width as needed
        alert.setGraphic(imageView);

        // Modify the header text programmatically
        DialogPane dialogPane = alert.getDialogPane();
        getTitleIcon(dialogPane);
        Label headerLabel = (Label) dialogPane.lookup(".dialog-pane .header-panel .label");
        if (headerLabel != null) {
            headerLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        }
        tieAlertToStage(alert, 400, 200);
        dialogPane.getStylesheets().add("css/dark/dialogue.css");
        dialogPane.getStyleClass().add("myDialog");
        return alert;
    }

    public static Alert customAlert(String header, String message, Alert.AlertType type) {
        System.out.println("launching custom alert");
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(message);
        tieAlertToStage(alert, 600, 400);
        DialogPane dialogPane = alert.getDialogPane();
        getTitleIcon(dialogPane);
        dialogPane.getStylesheets().add("css/dark/dialogue.css");
        dialogPane.getStyleClass().add("myDialog");
        return alert;
    }

//    public static Alert errorAlert(String header, String message) {
//        System.out.println("errorAlert");
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setHeaderText(header);
//        alert.setContentText(message);
//        tieAlertToStage(alert, 400, 200);
//        DialogPane dialogPane = alert.getDialogPane();
//        dialogPane.getStylesheets().add("css/dark/dialogue.css");
//        dialogPane.getStyleClass().add("myDialog");
//        alert.showAndWait();
//        return alert;
//    }

    public static void customAlertWithShow(String header, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(message);
//        tieAlertToStage(alert, 600, 400);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("css/dark/dialogue.css");
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();
    }

    public static boolean showMaxSessionsDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Session Limit Reached");
        alert.setHeaderText("You’ve reached the maximum number of sessions (3).");
        alert.setContentText("Please log out from other devices to continue.");
        tieAlertToStage(alert, 600, 400);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("css/dark/dialogue.css");
        dialogPane.getStyleClass().add("myDialog");
        ButtonType logoutOthersButton = new ButtonType("Log Out from Other Devices");
        ButtonType tryAgainLaterButton = new ButtonType("Try Again Later", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(logoutOthersButton, tryAgainLaterButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == logoutOthersButton;
    }

    public static boolean showServerUnreachableDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Server Unreachable");
        alert.setHeaderText("Server unreachable: try again?");
        alert.setContentText("The server is not responding. Would you like to try again?");
        tieAlertToStage(alert, 400, 200);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("css/dark/dialogue.css");
        dialogPane.getStyleClass().add("myDialog");
        getTitleIcon(dialogPane);
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType closeButton = new ButtonType("Close Application", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, closeButton);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }

    public static void errorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        tieAlertToStage(alert, 400, 200);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("css/dark/dialogue.css");
        dialogPane.getStyleClass().add("myDialog");
        getTitleIcon(dialogPane);
        alert.showAndWait();
    }

    public static Optional<ButtonType> errorAlertWithAction(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        tieAlertToStage(alert, 400, 200);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("css/dark/dialogue.css");
        dialogPane.getStyleClass().add("myDialog");
        getTitleIcon(dialogPane);
        return alert.showAndWait();
    }


    public static void infoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        tieAlertToStage(alert, 400, 200);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("css/dark/dialogue.css");
        dialogPane.getStyleClass().add("myDialog");
        getTitleIcon(dialogPane);
        alert.showAndWait();
    }

    public static void tieAlertToStage(Alert alert, double stageWidth, double stageHeight) {
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        // Flag to ensure positioning runs only once
        final boolean[] hasPositioned = {false};
        // Position the dialog only once when about to show
        EventHandler<WindowEvent> positionHandler = e -> {
            if (!hasPositioned[0]) {
                if (BaseApplication.primaryStage == null) {
                    System.out.println("Warning: primaryStage is null");
                    return;
                }
                hasPositioned[0] = true;
                double primaryX = BaseApplication.primaryStage.getX();
                double primaryY = BaseApplication.primaryStage.getY();
                double primaryWidth = BaseApplication.primaryStage.getWidth();
                double primaryHeight = BaseApplication.primaryStage.getHeight();


                alertStage.setX(primaryX + (primaryWidth / 2) - (stageWidth / 2));
                alertStage.setY(primaryY + (primaryHeight / 2) - (stageHeight / 2));
            }
        };

        // Add handler and remove it after first show to prevent re-triggering
        alertStage.setOnShowing(positionHandler);
        alertStage.setOnShown(e -> alertStage.removeEventHandler(WindowEvent.WINDOW_SHOWING, positionHandler));
    }

    public static boolean verifyAction(String[] string, Object o) {
        if(o != null) {
            Alert alert = DialogueFx.customAlert(string[0], string[1], Alert.AlertType.CONFIRMATION);
            tieAlertToStage(alert, 400, 200);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) return true;
        } else {
            Alert alert = DialogueFx.customAlert(string[2],string[3], Alert.AlertType.INFORMATION);
            alert.showAndWait();
        }
        return false;
    }

    private static void getTitleIcon(DialogPane dialogPane) {
        // Set custom icon for the title bar
        Stage alertStage = (Stage) dialogPane.getScene().getWindow();
        try {
            // Load icon from resources (adjust path as needed)
            Image icon = new Image(Objects.requireNonNull(
                    DialogueFx.class.getResourceAsStream("/images/halyard-16.png")));
            alertStage.getIcons().add(icon);
        } catch (Exception e) {
            logger.error("Failed to load icon: {}", e.getMessage());
        }
    }
}
