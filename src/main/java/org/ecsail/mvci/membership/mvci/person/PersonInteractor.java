package org.ecsail.mvci.membership.mvci.person;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import org.ecsail.fx.*;
import org.ecsail.mvci.membership.MembershipMessage;
import org.ecsail.pojo.*;
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

    public PersonMessage saveImage() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (!clipboard.hasImage()) {
            return setFailMessage("Error", 0, "No image found in clipboard. Use Windows + Shift + S to capture an image.");
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
        return null;  // TODO fix this
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
                return MembershipMessage.SUCCESS;
            } else {
                logger.error("Unable to insert picture: {}", insertPictureResponse.getMessage());
                return MembershipMessage.FAIL;
            }
        } catch (Exception e) {
            logger.error("Failed to insert picture for pId {}: {}", personModel.getPersonDTO().pIdProperty().get(), e.getMessage(), e);
            return MembershipMessage.FAIL;
        }
    }

    public PersonMessage insertAward() {
        Award award = new Award(personModel.getPersonDTO());
        try {
            String response = httpClientUtil.postDataToGybe("insert/award", award);
            AwardResponse insertAwardResponse = httpClientUtil.getObjectMapper()
                    .readValue(response, AwardResponse.class);
            if (insertAwardResponse.isSuccess()) {
                personModel.awardTableViewProperty().get().getItems().add(new AwardFx(insertAwardResponse.getAward()));
                personModel.awardTableViewProperty().get().refresh();
                return PersonMessage.SUCCESS;
            } else {
                return setFailMessage("Unable to insert award", 0, insertAwardResponse.getMessage());
            }
        } catch (Exception e) {
            return setFailMessage("Failed to insert award", 0, e.getMessage());
        }
    }

    public PersonMessage insertPhone() {
        Phone phone = new Phone(personModel.getPersonDTO());
        try {
            String response = httpClientUtil.postDataToGybe("insert/phone", phone);
            PhoneResponse insertPhoneResponse = httpClientUtil.getObjectMapper()
                    .readValue(response, PhoneResponse.class);
            if (insertPhoneResponse.isSuccess()) {
                personModel.phoneTableViewProperty().get().getItems().add(new PhoneFx(insertPhoneResponse.getPhone()));
                personModel.phoneTableViewProperty().get().refresh();
                return PersonMessage.SUCCESS;
            } else {
                return setFailMessage("Unable to insert phone", 0, insertPhoneResponse.getMessage());
            }
        } catch (Exception e) {
            return setFailMessage("Failed to insert phone", 0, e.getMessage());
        }
    }

    public PersonMessage insertEmail() {
        Email email = new Email(personModel.getPersonDTO().getpId());
        try {
            String response = httpClientUtil.postDataToGybe("insert/email", email);
            EmailResponse emailResponse = httpClientUtil.getObjectMapper()
                    .readValue(response, EmailResponse.class);
            if (emailResponse.isSuccess()) {
                personModel.emailTableViewProperty().get().getItems().add(new EmailFx(emailResponse.getEmail()));
                personModel.emailTableViewProperty().get().refresh();
                return PersonMessage.SUCCESS;
            } else {
                return setFailMessage("Unable to create email entry", 0, emailResponse.getMessage());
            }
        } catch (Exception e) {
            return setFailMessage("Failed to insert email", 0, e.getMessage());
        }
    }

    public PersonMessage insertPosition() {
        Officer officer = new Officer(personModel.getPersonDTO().getpId());
        try {
            String response = httpClientUtil.postDataToGybe("insert/position", officer);
            System.out.println(response);
            PositionResponse positionResponse = httpClientUtil.getObjectMapper()
                    .readValue(response, PositionResponse.class);
            if (positionResponse.isSuccess()) {
                personModel.officerTableViewProperty().get().getItems().add(new OfficerFx(officer));
                personModel.emailTableViewProperty().get().refresh();
                return PersonMessage.SUCCESS;
            } else {
                System.out.println("This is the path I went down");
                return setFailMessage("Failed to insert position"
                        ,0
                        ,positionResponse.getMessage());
            }
        } catch (Exception e) {
            return setFailMessage("Failed to insert position"
                    ,0
                    ,e.getMessage());
        }
    }

    public PersonMessage setFailMessage(String title, int id, String message) {
        personModel.errorMessageProperty().set(title + ":" + id + ":" + message);
        return PersonMessage.FAIL;
    }

    public PersonMessage updateAward() {
        Award award = new Award(personModel.selectedAwardProperty().get());
        try {
            String response = httpClientUtil.postDataToGybe("update/award", award);
            AwardResponse awardResponse = httpClientUtil.getObjectMapper().readValue(response, AwardResponse.class);
            if (awardResponse.isSuccess()) {
                return PersonMessage.SUCCESS;
            } else {
                return setFailMessage("Unable to update award", 0, awardResponse.getMessage());
            }
        } catch (Exception e) {
            return setFailMessage("Failed to update award", award.getAwardId(), e.getMessage());
        }
    }

    public PersonMessage updatePhone() {
        Phone phone = new Phone(personModel.selectedPhoneProperty().get());
        try {
            String response = httpClientUtil.postDataToGybe("update/phone", phone);
            PhoneResponse phoneResponse = httpClientUtil.getObjectMapper().readValue(response, PhoneResponse.class);
            if(phoneResponse.isSuccess()) {
                return PersonMessage.SUCCESS;
            } else {
                return setFailMessage("Failed to update phone", phone.getPhoneId(), phoneResponse.getMessage());
            }
        } catch (Exception e) {
            return setFailMessage("Failed to update phone", phone.getPhoneId(), e.getMessage());
        }
    }

    public PersonMessage updateEmail() {
        Email email = new Email(personModel.selectedEmailProperty().get());
        try {
            String response = httpClientUtil.postDataToGybe("update/email", email);
            EmailResponse emailResponse = httpClientUtil.getObjectMapper().readValue(response, EmailResponse.class);
            if (emailResponse.isSuccess()) {
                return PersonMessage.SUCCESS;
            } else {
                return setFailMessage("Failed to update email", email.getEmailId(), emailResponse.getMessage());
            }
        } catch (Exception e) {
            return setFailMessage("Failed to update email", email.getEmailId(), e.getMessage());
        }
    }

    public PersonMessage updatePosition() {
        Officer officer = new Officer(personModel.selectedPositionProperty().get());
        try {
            String response = httpClientUtil.postDataToGybe("update/position", officer);
            PositionResponse positionResponse = httpClientUtil.getObjectMapper().readValue(response, PositionResponse.class);
            if (positionResponse.isSuccess()) return PersonMessage.SUCCESS;
            else return setFailMessage("Failed to update position", officer.getOfficerId(), positionResponse.getMessage());
        } catch (Exception e) {
            return setFailMessage("Failed to update position", officer.getOfficerId(), e.getMessage());
        }
    }

    public PersonMessage updatePerson() {
        Person person = new Person(personModel.getPersonDTO());
        try {
            String response = httpClientUtil.postDataToGybe("update/person", person);
            System.out.println(response);
            PersonResponse personResponse = httpClientUtil.getObjectMapper().readValue(response, PersonResponse.class);
            if(personResponse.isSuccess()) return PersonMessage.SUCCESS;
            else return setFailMessage("Failed to update person", person.getpId(), personResponse.getMessage());
        } catch (Exception e) {
            return setFailMessage("aFailed to update person", person.getpId(), e.getMessage());
        }
    }

    public PersonMessage deleteAward() {
        try {
            if (personModel.selectedAwardProperty().get() == null)
                return setFailMessage("Failed to delete award", 0, "No award selected");
            String response = httpClientUtil.postDataToGybe("delete/award", personModel.selectedAwardProperty().get());
            if (response == null)
                return setFailMessage("Failed to delete award", personModel.selectedAwardProperty().get().getAwardId(), "Null response from server");
            UpdateResponse updateResponse = httpClientUtil.getObjectMapper().readValue(response, UpdateResponse.class);
            if (updateResponse == null)
                return setFailMessage("Failed to delete award", personModel.selectedAwardProperty().get().getAwardId(), "Invalid response from server");
            if (updateResponse.isSuccess()) {
                logger.info("Successfully deleted award {}", personModel.selectedAwardProperty().get().getAwardId());
                AwardFx awardDTOFx = personModel.selectedAwardProperty().get();
                if (awardDTOFx != null) {
                    Platform.runLater(() -> {
                        personModel.awardTableViewProperty().get().getItems().remove(awardDTOFx);
                        personModel.awardTableViewProperty().get().refresh();
                    });
                    return PersonMessage.SUCCESS;
                } else {
                    return setFailMessage("Failed to delete award: ", personModel.selectedAwardProperty().get().getAwardId(), "Null response from server");
                }
            } else {
                String errorMessage = updateResponse.getMessage() != null ? updateResponse.getMessage() : "Unknown error";
                return setFailMessage("Failed to delete award", personModel.selectedAwardProperty().get().getAwardId(), errorMessage);
            }
        } catch (Exception e) {
            return setFailMessage("Failed to delete award", personModel.selectedAwardProperty().get().getAwardId(), e.getMessage());
        }
    }

    public PersonMessage deletePhone() {
        try {
            // Validate inputs
            if (personModel.selectedPhoneProperty().get() == null)
                return setFailMessage("Failed to delete phone", 0, "No valid phone selected");
            String response = httpClientUtil.postDataToGybe("delete/phone", personModel.selectedPhoneProperty().get());
            if (response == null)
                return setFailMessage("Failed to delete phone", 0, "Null response from server");
            UpdateResponse updateResponse = httpClientUtil.getObjectMapper().readValue(response, UpdateResponse.class);
            if (updateResponse == null)
                return setFailMessage("Failed to delete phone", 0, "Invalid response from server");
            if (updateResponse.isSuccess()) {
                logger.info("Successfully deleted phone {}", personModel.selectedPhoneProperty().get().getPhoneId());
                PhoneFx phoneFx = personModel.selectedPhoneProperty().get();
                if (phoneFx != null) {
                    Platform.runLater(() -> {
                        personModel.phoneTableViewProperty().get().getItems().remove(phoneFx);
                        personModel.phoneTableViewProperty().get().refresh();
                    });
                    return PersonMessage.SUCCESS;
                } else {
                    return setFailMessage("Phone", personModel.selectedPhoneProperty().get().getPhoneId() , "deleted on server but not found in membership list");
                }
            } else {
                String errorMessage = updateResponse.getMessage() != null ? updateResponse.getMessage() : "Unknown error";
                return setFailMessage("Failed to delete phone", personModel.selectedPhoneProperty().get().getPhoneId(), errorMessage);
            }
        } catch (Exception e) {
            return setFailMessage("Failed to delete phone", personModel.selectedPhoneProperty().get().getPhoneId(), e.getMessage());
        }
    }

    public PersonMessage deleteEmail() {
        try {
            if (personModel.selectedEmailProperty().get() == null)
                return setFailMessage("Failed to delete email", 0, "No email selected");
            if (personModel.selectedEmailProperty().get().primaryUseProperty().get())
                return setFailMessage("Failed to delete email", 0, "primary emails can not be deleted");
            String response = httpClientUtil.postDataToGybe("delete/email", personModel.selectedEmailProperty().get());
            if (response == null)
                return setFailMessage("Failed to delete email", personModel.selectedEmailProperty().get().getEmailId(), "Null response from server");
            UpdateResponse updateResponse = httpClientUtil.getObjectMapper().readValue(response, UpdateResponse.class);
            if (updateResponse == null)
                return setFailMessage("Failed to delete email", personModel.selectedEmailProperty().get().getEmailId(), "Invalid response from server");
            if (updateResponse.isSuccess()) {
                logger.info("Successfully deleted email {}", personModel.selectedEmailProperty().get().getEmailId());
                EmailFx emailFx = personModel.selectedEmailProperty().get();
                if (emailFx != null) {
                    Platform.runLater(() -> {
                        personModel.emailTableViewProperty().get().getItems().remove(emailFx);
                        personModel.emailTableViewProperty().get().refresh();
                    });
                    return PersonMessage.SUCCESS;
                } else
                    return setFailMessage("Email",personModel.selectedEmailProperty().get().getEmailId() , "deleted on server but not found in membership list");
            } else {
                String errorMessage = updateResponse.getMessage() != null ? updateResponse.getMessage() : "Unknown error";
                return setFailMessage("Unable to delete email", personModel.selectedEmailProperty().get().getEmailId(), errorMessage);
            }
        } catch (Exception e) {
            return setFailMessage("Unable to delete email", personModel.selectedEmailProperty().get().getEmailId(), e.getMessage());
        }
    }

    public PersonMessage deletePosition() {
        try {
            if (personModel.selectedPositionProperty().get() == null)
                return setFailMessage("Failed to delete position", 0, "No valid position selected");
            // Send delete request to server
            String response = httpClientUtil.postDataToGybe("delete/position", personModel.selectedPositionProperty().get());
            if (response == null)
                return setFailMessage("Failed to delete position", 0, "Null response from server");
            UpdateResponse updateResponse = httpClientUtil.getObjectMapper().readValue(response, UpdateResponse.class);
            if (updateResponse == null)
                return setFailMessage("Failed to delete position", 0, "Invalid response from server");
            if (updateResponse.isSuccess()) {
                logger.info("Successfully deleted position {}", personModel.selectedPositionProperty().get().getOfficerId());
                OfficerFx officerFx = personModel.selectedPositionProperty().get();
                if (officerFx != null) {
                    Platform.runLater(() -> {
                        personModel.officerTableViewProperty().get().getItems().remove(officerFx);
                        personModel.officerTableViewProperty().get().refresh();
                    });
                    return PersonMessage.SUCCESS;
                } else
                    return setFailMessage("Position", personModel.selectedPositionProperty().get().getOfficerId(), "deleted on server but not found in membership list");
            } else {
                String errorMessage = updateResponse.getMessage() != null ? updateResponse.getMessage() : "Unknown error";
                return setFailMessage("Unable to delete position", personModel.selectedPositionProperty().get().getOfficerId(), errorMessage);
            }
        } catch (Exception e) {
            return setFailMessage("Unable to delete position", personModel.selectedPositionProperty().get().getOfficerId(), e.getMessage());
        }
    }

    public String[] getErrorMessage() {
        return personModel.errorMessageProperty().get().split(":");
    }

    public void logError(String message) {
        logger.error(message);
    }

    public void logServerSideError() {
        logger.error("Task failed on server side");
    }
}
