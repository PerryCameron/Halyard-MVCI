package org.ecsail.mvci.membership.components;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.ecsail.widgetfx.DialogueFx;


public class PictureAlert implements Builder<Alert> {
    private Alert alert;
    private DialogPane dialogPane;

    public PictureAlert(ImageView imageView) {
        this.alert = new Alert(Alert.AlertType.NONE);
        this.dialogPane = new DialogPane();
        dialogPane.getStylesheets().add("css/dark/dialogue.css");
        dialogPane.getStyleClass().add("myDialog");
        alert.setDialogPane(dialogPane);
        dialogPane.setContent(contentBox());
        DialogueFx.tieAlertToStage(alert, 400, 400);
        DialogueFx.getTitleIcon(dialogPane);

    }

    @Override
    public Alert build() {
        alert.setTitle("Picture Editor");
        alert.showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (isShowing) {
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.setOnCloseRequest(event -> cleanAlertClose());
            }
        });
        return alert;
    }

    private Node contentBox() {
        VBox vBox = new VBox(10);
        Button addButton = new Button("Add Picture");
        Label label = new Label("hit window-shift-S and then select image to put in clipboard, then hit button to paste");
        addButton.setOnAction(event -> {
            System.out.println("Add Picture");
        });
        vBox.getChildren().addAll(label, addButton);
        return vBox;
    }

    private void cleanAlertClose() {
        alert.setResult(ButtonType.CANCEL);
        alert.close(); // Use close() instead of hide()
        alert.hide();
    }


}
