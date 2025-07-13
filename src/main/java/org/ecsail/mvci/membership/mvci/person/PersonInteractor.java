package org.ecsail.mvci.membership.mvci.person;


import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import org.ecsail.mvci.membership.MembershipInteractor;
import org.ecsail.widgetfx.DialogueFx;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class PersonInteractor {
    private PersonModel personModel;
    private static final Logger logger = LoggerFactory.getLogger(PersonInteractor.class);


    public PersonInteractor(PersonModel personModel) {
        this.personModel = personModel;
    }

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
            personModel.getImageViewProperty().setImage(saveImageTask.getValue());
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
