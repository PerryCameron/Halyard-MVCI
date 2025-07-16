package org.ecsail.mvci.membership.mvci.person;


import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import org.ecsail.fx.AwardDTOFx;
import org.ecsail.fx.EmailFx;
import org.ecsail.fx.PhoneFx;
import org.ecsail.fx.PictureDTO;
import org.ecsail.mvci.membership.MembershipMessage;
import org.ecsail.pojo.Award;
import org.ecsail.pojo.Email;
import org.ecsail.pojo.Phone;
import org.ecsail.static_tools.HttpClientUtil;
import org.ecsail.widgetfx.DialogueFx;
import org.ecsail.wrappers.*;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class PersonInteractor {
    private final PersonModel personModel;
    private final HttpClientUtil httpClientUtil;
    private static final Logger logger = LoggerFactory.getLogger(PersonInteractor.class);


    public PersonInteractor(PersonModel personModel) {
        this.personModel = personModel;
        this.httpClientUtil = personModel.getMembershipModel().getHttpClient();
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
        saveImageTask.setOnSucceeded(event -> personModel.getImageViewProperty().setImage(saveImageTask.getValue()));
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
            String response = httpClientUtil.postDataToGybe("api/add-picture", personModel.getPicture());
            InsertPictureResponse insertPictureResponse = httpClientUtil.getObjectMapper()
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
        UpdateResponse updateResponse = httpClientUtil.getObjectMapper().readValue(response, UpdateResponse.class);
        // Toggle the appropriate light based on the success field
        if (updateResponse.isSuccess()) {
            logger.info(updateResponse.getMessage());
            return MembershipMessage.SUCCESS;
        } else {
            Platform.runLater(() -> DialogueFx.errorAlert("Unable to perform update", updateResponse.getMessage()));
            logger.error(updateResponse.getMessage());
            return MembershipMessage.FAIL;
        }
    }

    public MembershipMessage insertAward() {
        Award award = new Award(personModel.getPersonDTO());
        try {
            String response = httpClientUtil.postDataToGybe("insert/award", award);
            AwardResponse insertAwardResponse = httpClientUtil.getObjectMapper()
                    .readValue(response, AwardResponse.class);
            if (insertAwardResponse.isSuccess()) {
                personModel.awardTableViewProperty().get().getItems().add(new AwardDTOFx(insertAwardResponse.getAward()));
                personModel.awardTableViewProperty().get().refresh(); //TODO check if this is needed
                return MembershipMessage.SUCCESS;
            } else {
                logger.error("Unable to insert award: {}", insertAwardResponse.getMessage());
                return MembershipMessage.FAIL;
            }
        } catch (Exception e) {
            logger.error("Failed to insert award for pId {}: {}", personModel.getPersonDTO().pIdProperty().get(), e.getMessage(), e);
            return MembershipMessage.FAIL;
        }
    }

    public void insertPhone() {
        Phone phone = new Phone(personModel.getPersonDTO());
        try {
            String response = httpClientUtil.postDataToGybe("insert/phone", phone);
            PhoneResponse insertPhoneResponse = httpClientUtil.getObjectMapper()
                    .readValue(response, PhoneResponse.class);
            if (insertPhoneResponse.isSuccess()) {
                personModel.phoneTableViewProperty().get().getItems().add(new PhoneFx(insertPhoneResponse.getPhone()));
                personModel.phoneTableViewProperty().get().refresh();

            } else {
                logger.error("Unable to insert phone: {}", insertPhoneResponse.getMessage());
                DialogueFx.errorAlert("Unable to create phone entry", insertPhoneResponse.getMessage());
            }
        } catch (Exception e) {
            logger.error("Failed to insert phone for phoneId: {} {}", personModel.selectedPhoneProperty().get().getPhoneId(), e.getMessage(), e); // line 172
            DialogueFx.errorAlert("Unable to create phone entry", e.getMessage());
        }
    }

    // this is complete
    public void insertEmail() {
        Email email = new Email(personModel.getPersonDTO().getpId());
        try {
            String response = httpClientUtil.postDataToGybe("insert/email", email);
            EmailResponse emailResponse = httpClientUtil.getObjectMapper()
                    .readValue(response, EmailResponse.class);
            if (emailResponse.isSuccess()) {
                personModel.emailTableViewProperty().get().getItems().add(new EmailFx(emailResponse.getEmail()));
                personModel.emailTableViewProperty().get().refresh();
                personModel.updateSuccessProperty().set(true); // to signal success and allow green lights to blink
            } else {
                logger.error("Unable to insert email: {}", emailResponse.getMessage());
                DialogueFx.errorAlert("Unable to create email entry", emailResponse.getMessage());
            }
        } catch (Exception e) {
            logger.error("Failed to insert email for phoneId: {} {}", personModel.selectedEmailProperty().get().getEmailId(), e.getMessage(), e); // line 172
            DialogueFx.errorAlert("Unable to create email entry", e.getMessage());
        }
    }

    public AwardResponse updateAward() {
        logger.debug("Updating phone with pId: {}", personModel.selectedAwardProperty().get().getAwardId());
        Award award = new Award(personModel.selectedAwardProperty().get());
        try {
            String response = httpClientUtil.postDataToGybe("update/award", award);
            AwardResponse awardResponse = httpClientUtil.getObjectMapper()
                    .readValue(response, AwardResponse.class);
            if (!awardResponse.isSuccess()) DialogueFx.errorAlert("Unable to update email", awardResponse.getMessage());
            return awardResponse;
        } catch (Exception e) {
            logger.error("Failed to update award with pId {}: {}",
                    personModel.getPersonDTO().pIdProperty().get(), e.getMessage(),e);
            return null;
        }
    }

    public PhoneResponse updatePhone() {
        Phone phone = new Phone(personModel.selectedPhoneProperty().get());
        try {
            String response = httpClientUtil.postDataToGybe("update/phone", phone);
            PhoneResponse phoneResponse = httpClientUtil.getObjectMapper().readValue(response, PhoneResponse.class);
            if (!phoneResponse.isSuccess()) Platform.runLater(() -> DialogueFx.errorAlert("Unable to update email", phoneResponse.getMessage()));
            personModel.updateSuccessProperty().set(phoneResponse.isSuccess());
            return phoneResponse;
        } catch (Exception e) {
            logger.error("Failed to update phone with pId {}: {}",
                    personModel.getPersonDTO().pIdProperty().get(), e.getMessage(), e);
            return null;
        }
    }

    // this is complete
    public EmailResponse updateEmail() {
        Email email = new Email(personModel.selectedEmailProperty().get());
        try {
            String response = httpClientUtil.postDataToGybe("update/email", email);
            EmailResponse emailResponse = httpClientUtil.getObjectMapper()
                    .readValue(response, EmailResponse.class);
            if (!emailResponse.isSuccess()) Platform.runLater(() -> DialogueFx.errorAlert("Unable to update email", emailResponse.getMessage()));
            personModel.updateSuccessProperty().set(emailResponse.isSuccess());
            return emailResponse;
        } catch (Exception e) {
            logger.error("Failed to update email with pId {}: {}",
                    personModel.selectedEmailProperty().get().pIdProperty().get(), e.getMessage(), e);
            return null;
        }
    }

    public void deleteAward() {
        try {
            // Validate inputs
            if (personModel.selectedAwardProperty().get() == null) {
                logger.error("Failed to delete award: No award selected");
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Award Failed", "No award selected"));
                return;
            }
            // Send delete request to server
            String response = httpClientUtil.postDataToGybe("delete/award", personModel.selectedAwardProperty().get());
            if (response == null) {
                logger.error("Failed to delete award {}: Null response from server", personModel.selectedAwardProperty().get().getAwardId());
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Award Failed", "Null response from server"));
                return;
            }
            UpdateResponse updateResponse = httpClientUtil.getObjectMapper()
                    .readValue(response, UpdateResponse.class);
            if (updateResponse == null) {
                logger.error("Failed to delete award {}: Invalid response from server", personModel.selectedAwardProperty().get().getAwardId());
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Award Failed", "Invalid response from server"));
                return;
            }
            if (updateResponse.isSuccess()) {
                logger.info("Successfully deleted award {}", personModel.selectedAwardProperty().get().getAwardId());

                AwardDTOFx awardDTOFx = personModel.selectedAwardProperty().get();
                if (awardDTOFx != null) {
                    Platform.runLater(() -> {
                        personModel.awardTableViewProperty().get().getItems().remove(awardDTOFx);
                        // Refresh table only if necessary
                        personModel.awardTableViewProperty().get().refresh();
                        personModel.updateSuccessProperty().set(true);
                    });
                } else {
                    // Log warning but don’t show error alert, as deletion succeeded
                    logger.warn("Award {} deleted on server but not found in membership list", personModel.selectedAwardProperty().get().getAwardId());
                }
            } else {
                String errorMessage = updateResponse.getMessage() != null ? updateResponse.getMessage() : "Unknown error";
                logger.error("Failed to delete boat {}: {}", personModel.selectedAwardProperty().get().getAwardId(), errorMessage);
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Award Failed", errorMessage));
            }
        } catch (Exception e) {
            logger.error("Failed to delete award {}: {}", personModel.selectedAwardProperty().get().getAwardId(), e.getMessage(), e);
            Platform.runLater(() -> DialogueFx.errorAlert("Delete Award Failed", e.getMessage()));
        }
    }

    public void deletePhone() {
        try {
            // Validate inputs
            if (personModel.selectedPhoneProperty().get() == null) {
                logger.error("Failed to delete phone: No phone selected");
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Phone Failed", "No phone selected"));
                return;
            }
            // Send delete request to server
            String response = httpClientUtil.postDataToGybe("delete/phone", personModel.selectedPhoneProperty().get());
            if (response == null) {
                logger.error("Failed to delete phone: {} Null response from server", personModel.selectedPhoneProperty().get().getPhoneId());
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Phone Failed", "Null response from server"));
                return;
            }
            UpdateResponse updateResponse = httpClientUtil.getObjectMapper()
                    .readValue(response, UpdateResponse.class);
            if (updateResponse == null) {
                logger.error("Failed to delete phone with ID: {} Invalid response from server", personModel.selectedPhoneProperty().get().getPhoneId());
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Phone Failed", "Invalid response from server"));
                return;
            }
            if (updateResponse.isSuccess()) {
                logger.info("Successfully deleted phone {}", personModel.selectedPhoneProperty().get().getPhoneId());
                PhoneFx phoneFx = personModel.selectedPhoneProperty().get();
                if (phoneFx != null) {
                    Platform.runLater(() -> {
                        personModel.phoneTableViewProperty().get().getItems().remove(phoneFx);
                        // Refresh table only if necessary
                        personModel.phoneTableViewProperty().get().refresh();
                        personModel.updateSuccessProperty().set(true);
                    });
                } else {
                    // Log warning but don’t show error alert, as deletion succeeded
                    logger.warn("Phone {} deleted on server but not found in membership list", personModel.selectedPhoneProperty().get().getPhoneId());
                }
            } else {
                String errorMessage = updateResponse.getMessage() != null ? updateResponse.getMessage() : "Unknown error";
                logger.error("Failed to delete phone {}: {}", personModel.selectedPhoneProperty().get().getPhoneId(), errorMessage);
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Phone Failed", errorMessage));
            }
        } catch (Exception e) {
            logger.error("Failed to delete phone {}: {}", personModel.selectedPhoneProperty().get().getPhoneId(), e.getMessage(), e);
            Platform.runLater(() -> DialogueFx.errorAlert("Delete Phone Failed", e.getMessage()));
        }
    }

    public void deleteEmail() {
        try {
            // Validate inputs
            if (personModel.selectedEmailProperty().get() == null) {
                logger.error("Failed to delete email: No email selected");
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Email Failed", "No email selected"));
                return;
            }

            if (personModel.selectedEmailProperty().get().primaryUseProperty().get()) {
                logger.error("This is the primary email for a member: primary emails can not be deleted");
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Email Failed", "This is a primary email for a member\n\nYou must select or add another email and change it to primary before deleting this email"));
                return;
            }

            // Send delete request to server
            String response = httpClientUtil.postDataToGybe("delete/email", personModel.selectedEmailProperty().get());
            if (response == null) {
                logger.error("Failed to delete email: {} Null response from server", personModel.selectedEmailProperty().get().getEmailId());
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Email Failed", "Null response from server"));
                return;
            }
            UpdateResponse updateResponse = httpClientUtil.getObjectMapper().readValue(response, UpdateResponse.class);
            if (updateResponse == null) {
                logger.error("Failed to delete email with ID: {} Invalid response from server", personModel.selectedEmailProperty().get().getEmailId());
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Email Failed", "Invalid response from server"));
                return;
            }
            if (updateResponse.isSuccess()) {
                logger.info("Successfully deleted email {}", personModel.selectedEmailProperty().get().getEmailId());
                EmailFx emailFx = personModel.selectedEmailProperty().get();
                if (emailFx != null) {
                    Platform.runLater(() -> {
                        personModel.emailTableViewProperty().get().getItems().remove(emailFx);
                        // Refresh table only if necessary
                        personModel.emailTableViewProperty().get().refresh();
                        personModel.updateSuccessProperty().set(true);
                    });
                } else {
                    // Log warning but don’t show error alert, as deletion succeeded
                    logger.warn("Email {} deleted on server but not found in membership list", personModel.selectedEmailProperty().get().getEmailId());
                }
            } else {
                String errorMessage = updateResponse.getMessage() != null ? updateResponse.getMessage() : "Unknown error";
                logger.error("Unable to delete email {}: {}", personModel.selectedEmailProperty().get().getEmailId(), errorMessage);
                Platform.runLater(() -> DialogueFx.errorAlert("Delete Email Failed", errorMessage));
            }
        } catch (Exception e) {
            logger.error("Failed to delete email {}: {}", personModel.selectedEmailProperty().get().getEmailId(), e.getMessage(), e);
            Platform.runLater(() -> DialogueFx.errorAlert("Delete Email Failed", e.getMessage()));
        }
    }

    public boolean actionSucceeded() {
        return personModel.updateSuccessProperty().get();
    }

    public void actionReset() {
        personModel.updateSuccessProperty().set(false);
    }
}
