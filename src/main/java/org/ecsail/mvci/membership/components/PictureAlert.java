package org.ecsail.mvci.membership.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.ecsail.mvci.membership.MembershipModel;
import org.ecsail.mvci.membership.MembershipView;
import org.ecsail.mvci.membership.mvci.person.PersonMessage;
import org.ecsail.mvci.membership.mvci.person.PersonView;
import org.ecsail.widgetfx.ButtonFx;
import org.ecsail.widgetfx.DialogueFx;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.VBoxFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PictureAlert implements Builder<Alert> {
    private final MembershipView membershipView;
    private final MembershipModel membershipModel;
    private final PersonView personView;
    private final Alert alert;
    private final DialogPane dialogPane;
    private static final Logger logger = LoggerFactory.getLogger(PictureAlert.class);


    public PictureAlert(PersonView personView, MembershipView membershipView) {
        this.personView = personView;
        this.membershipView = membershipView;
        this.membershipModel = membershipView.getMembershipModel();
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
        VBox vBox = VBoxFx.vBoxOf(10, Pos.CENTER);
        HBox hBox = HBoxFx.hBoxOf(10, Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);
        Button pasteButton = ButtonFx.utilityButton("Paste Clipboard Image", this::pasteImage);
        Button closeButton = ButtonFx.utilityButton("Cancel", this::cleanAlertClose);
        Label label = new Label("Press Win + Shift + S to open the Snipping Tool in snip mode. \nClick and drag to select the area, then release.\n\nThe captured image is automatically copied to the clipboard.\n" +
                "\n\n");
        hBox.getChildren().addAll(pasteButton, closeButton);
        vBox.getChildren().addAll(label, hBox);
        return vBox;
    }

    private void pasteImage() {
        personView.sendMessage().accept(PersonMessage.SAVE_IMAGE);
        cleanAlertClose();
    }

    private void cleanAlertClose() {
        alert.setResult(ButtonType.CANCEL);
        alert.close(); // Use close() instead of hide()
        alert.hide();
    }



}
