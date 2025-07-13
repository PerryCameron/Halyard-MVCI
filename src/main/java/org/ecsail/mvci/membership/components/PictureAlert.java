package org.ecsail.mvci.membership.components;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.ecsail.mvci.membership.MembershipModel;
import org.ecsail.mvci.membership.MembershipView;
import org.ecsail.widgetfx.ButtonFx;
import org.ecsail.widgetfx.DialogueFx;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class PictureAlert implements Builder<Alert> {
    private final MembershipView membershipView;
    private final MembershipModel membershipModel;
    private final PersonTabView personTabView;
    private Alert alert;
    private DialogPane dialogPane;
    private static final Logger logger = LoggerFactory.getLogger(PictureAlert.class);


    public PictureAlert(PersonTabView personTabView, MembershipView membershipView) {
        this.personTabView = personTabView;
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
        VBox vBox = new VBox(10);
        Button pasteButton = ButtonFx.utilityButton("Paste Clipboard Image", 200);
        Button sizeButton = ButtonFx.utilityButton("get ImageView size", 200);
        Label label = new Label("Hit window-shift-S and then select image to put in clipboard");
        pasteButton.setOnAction(event -> {
            System.out.println("Add Picture");
            saveImage();
        });
        sizeButton.setOnAction(event -> {
            double width = personTabView.getImageView().getFitWidth();
            double height = personTabView.getImageView().getFitHeight();
            double vwidth = vBox.getWidth();
            double vheight = vBox.getHeight();
            System.out.println(width + " " + height);
            System.out.println("vbox= " + vwidth + " " + vheight);
        });
        vBox.getChildren().addAll(label, pasteButton, sizeButton);
        return vBox;
    }

    private void cleanAlertClose() {
        alert.setResult(ButtonType.CANCEL);
        alert.close(); // Use close() instead of hide()
        alert.hide();
    }

    ////////////////  TEMP adding in here /////////////////////////

    public void saveImage() {

        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (!clipboard.hasImage()) {
            DialogueFx.errorAlert("Error", "No image found in clipboard. Use Windows + Shift + S to capture an image.");
            return;
        }
        Image fxImage = clipboard.getImage();
        // Convert clipboard image to BufferedImage
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);
        Task<Image> saveImageTask = new Task<>() {
            @Override
            protected Image call() {
                try {
                    // Resize image to 357x265 pixels
                    BufferedImage resizedImage = resizeImage(bufferedImage, 192, 222);
                    // Convert to PNG bytes (~100 KB)
                    byte[] imageBytes = convertToPngBytes(resizedImage);
                    // Update ImageView
                    Image newImage = new Image(new ByteArrayInputStream(imageBytes));
 //                   globalSparesRepo.saveImageToDatabase(partModel.selectedSpareProperty().get().getSpareItem(), imageBytes);
                    return newImage;
                } catch (Exception e) {
                    DialogueFx.errorAlert("Error", "Error saving image: " + e.getMessage());
                }
                return null;
            }
        };
        saveImageTask.setOnSucceeded(event -> {
           // globalSparesRepo.updateSpare(partModel.selectedSpareProperty().get());
            System.out.println("setting image");
            personTabView.getImageView().setImage(saveImageTask.getValue());
        });
        saveImageTask.setOnFailed(event -> {
            Throwable e = saveImageTask.getException();
            logger.error("Failed to save image: {}", e.getMessage(), e);
            DialogueFx.errorAlert("Error", "Failed to save image: " + e.getMessage());
        });
        // Start the task on a background thread
        saveImageTask.run();
    }

    private BufferedImage resizeImage(BufferedImage original, int targetWidth, int targetHeight) {
        // Skip resizing if original is smaller
        if (original.getWidth() <= targetWidth && original.getHeight() <= targetHeight) {
            System.out.println("No resize is needed");
            return original;
        }
        // Resize with Imgscalr, preserving aspect ratio
        return Scalr.resize(original, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC,
                targetWidth, targetHeight);
    }

    // run on non-FX thread
    private byte[] convertToPngBytes(BufferedImage image) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }


}
