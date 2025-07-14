package org.ecsail.mvci.membership.mvci.person;


import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import org.ecsail.fx.AwardDTOFx;
import org.ecsail.fx.PersonFx;
import org.ecsail.fx.PictureDTO;
import org.ecsail.mvci.membership.MembershipMessage;
import org.ecsail.pojo.Award;
import org.ecsail.pojo.Note;
import org.ecsail.widgetfx.DialogueFx;
import org.ecsail.wrappers.InsertAwardResponse;
import org.ecsail.wrappers.InsertPictureResponse;
import org.ecsail.wrappers.UpdateResponse;
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
                    // create object to send via HTTP, add PID and Convert to PNG bytes (~100 KB)
                    PictureDTO pictureDTO = new PictureDTO(personModel.getPersonDTO().getpId(), convertToPngBytes(resizedImage));
                    // add to our model to sent HTTP via interactor
                    personModel.pictureProperty().set(pictureDTO);
                    // write picture to database
                    insertOrUpdatePicture();
                    // send image so it can be added on success
                    return new Image(new ByteArrayInputStream(pictureDTO.getPicture()));
                } catch (Exception e) {
                    DialogueFx.errorAlert("Error", "Error saving image: " + e.getMessage());
                }
                return null;
            }
        };
        saveImageTask.setOnSucceeded(event -> {
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

    public MembershipMessage insertOrUpdatePicture() {
        try {
            String response = personModel.getMembershipModel().getHttpClient().postDataToGybe("api/add-picture", personModel.getPicture());
            InsertPictureResponse insertPictureResponse = personModel.getMembershipModel().getHttpClient().getObjectMapper()
                    .readValue(response, InsertPictureResponse.class);
            if (insertPictureResponse.isSuccess()) {
                System.out.println("We succeeded");
                return MembershipMessage.SUCCESS;
            } else {
                logger.error("Unable to insert award: {}", insertPictureResponse.getMessage());
                return MembershipMessage.FAIL;
            }
        } catch (Exception e) {
            logger.error("Failed to insert award for pId {}: {}", personModel.getPersonDTO().pIdProperty().get(), e.getMessage(), e);
            return MembershipMessage.FAIL;
        }
    }


    private MembershipMessage processUpdateResponse(String response) throws JsonProcessingException {
        logger.debug("Update response: {}", response);
        UpdateResponse updateResponse = personModel.getMembershipModel().getHttpClient()
                .getObjectMapper().readValue(response, UpdateResponse.class);
        // Toggle the appropriate light based on the success field
        if (updateResponse.isSuccess()) {
            logger.info(updateResponse.getMessage());
            return MembershipMessage.SUCCESS;
        } else {
            Platform.runLater(() -> {
                DialogueFx.errorAlert("Unable to perform update", updateResponse.getMessage());
            });
            logger.error(updateResponse.getMessage());
            return MembershipMessage.FAIL;
        }
    }
}
