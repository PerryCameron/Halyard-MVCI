package org.ecsail.mvci.membership.mvci.person;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import org.ecsail.fx.*;
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
import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class PersonInteractor {
    private final PersonModel personModel;
    private final HttpClientUtil httpClientUtil;
    private static final Logger logger = LoggerFactory.getLogger(PersonInteractor.class);


    public PersonInteractor(PersonModel personModel) {
        this.personModel = personModel;
        this.httpClientUtil = personModel.getMembershipModel().getHttpClient();
    }

    public Optional<Image> getClipBoardImage() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (!clipboard.hasImage()) {
            DialogueFx.errorAlert("Error", "No image found in clipboard. Use Windows + Shift + S to capture an image.");
            return Optional.empty();
        }
        return Optional.ofNullable(clipboard.getImage());
    }

    public void saveImage(Image fxImage, ExecutorService executor) {
        Task<Image> task = new Task<>() {
            @Override
            protected Image call() {
                try {
                    // Convert clipboard image to BufferedImage
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);
                    // Resize image to 357x265 pixels
                    BufferedImage resizedImage = resizeImage(bufferedImage, 192, 222);
                    // create object to send via HTTP, add PID and Convert to PNG bytes (~100 KB)
                    Picture picture = new Picture(personModel.getPersonDTO().getpId(), convertToPngBytes(resizedImage));
                    // add to our model to sent HTTP via interactor
                    personModel.pictureProperty().set(picture);
                    // write picture to database
                    if (insertOrUpdatePicture() == PersonMessage.SUCCESS) {
                        // send image so it can be added on success
                        return new Image(new ByteArrayInputStream(picture.getPicture()));
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    DialogueFx.errorAlert("Error", e.getMessage());
                    return null;
                }
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            if (task.getValue() != null) {
                personModel.getImageViewProperty().setImage(task.getValue());
            }
        });
        executor.execute(task);
    }

    public void getImage(ExecutorService executor) {
        Task<Image> task = new Task<>() {
            @Override
            protected Image call() {
                try {
                    Person person = new Person(personModel.getPersonDTO());
                    try {
                        String response = httpClientUtil.postDataToGybe("get-picture", person);
                        PictureResponse pictureResponse = httpClientUtil.getObjectMapper().readValue(response, PictureResponse.class);
                        if (pictureResponse.isSuccess()) return new Image(new ByteArrayInputStream(pictureResponse.getPicture().getPicture()));
                        else
                            return null;
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    DialogueFx.errorAlert("Error", e.getMessage());
                    return null;
                }
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            if (task.getValue() != null) {
                personModel.getImageViewProperty().setImage(task.getValue());
                personModel.imageLoadedProperty().set(true);
            }
        });
        executor.execute(task);
    }

    private BufferedImage resizeImage(BufferedImage original, int targetWidth, int targetHeight) {
        // Skip resizing if original is smaller
        if (original.getWidth() <= targetWidth && original.getHeight() <= targetHeight) {
            return original;
        }
        // Resize with Imgscalr, preserving aspect ratio
        return Scalr.resize(original, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC,
                targetWidth, targetHeight);
    }

    private byte[] convertToPngBytes(BufferedImage image) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    public PersonMessage insertOrUpdatePicture() {
        try {
            String response = httpClientUtil.postDataToGybe("add-picture", personModel.getPicture());
            PictureResponse pictureResponse = httpClientUtil.getObjectMapper()
                    .readValue(response, PictureResponse.class);
            if (pictureResponse.isSuccess()) {
                return PersonMessage.SUCCESS;
            } else {
                logger.error("Unable to insert picture: {}", pictureResponse.getMessage());
                return PersonMessage.FAIL;
            }
        } catch (Exception e) {
            logger.error("Failed to insert picture for pId {}: {}", personModel.getPersonDTO().pIdProperty().get(), e.getMessage(), e);
            return PersonMessage.FAIL;
        }
    }

    public PersonMessage insertAward() {
        Award award = new Award(personModel.getPersonDTO());
        try {
            String response = httpClientUtil.postDataToGybe("insert/award", award);
            AwardResponse insertAwardResponse = httpClientUtil.getObjectMapper()
                    .readValue(response, AwardResponse.class);
            if (insertAwardResponse.isSuccess()) {
                personModel.awardTableViewProperty().get().getItems().addFirst(new AwardFx(insertAwardResponse.getAward()));
                personModel.awardTableViewProperty().get().getSelectionModel().selectFirst();
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
                personModel.phoneTableViewProperty().get().getItems().addFirst(new PhoneFx(insertPhoneResponse.getPhone()));
                personModel.phoneTableViewProperty().get().getSelectionModel().selectFirst();
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
                personModel.emailTableViewProperty().get().getItems().addFirst(new EmailFx(emailResponse.getEmail()));
                //personModel.emailTableViewProperty().get().refresh();
                personModel.emailTableViewProperty().get().getSelectionModel().selectFirst();
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
            PositionResponse positionResponse = httpClientUtil.getObjectMapper()
                    .readValue(response, PositionResponse.class);
            if (positionResponse.isSuccess()) {
                personModel.officerTableViewProperty().get().getItems().addFirst(new OfficerFx(officer));
                //personModel.officerTableViewProperty().get().refresh();
                personModel.officerTableViewProperty().get().getSelectionModel().selectFirst();
                return PersonMessage.SUCCESS;
            } else {
                return setFailMessage("Failed to insert position"
                        , 0
                        , positionResponse.getMessage());
            }
        } catch (Exception e) {
            return setFailMessage("Failed to insert position"
                    , 0
                    , e.getMessage());
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
            if (phoneResponse.isSuccess()) {
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
            else
                return setFailMessage("Failed to update position", officer.getOfficerId(), positionResponse.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return setFailMessage("Failed to update position", officer.getOfficerId(), e.getMessage());
        }
    }

    public PersonMessage updatePerson() {
        Person person = new Person(personModel.getPersonDTO());
        try {
            String response = httpClientUtil.postDataToGybe("update/person", person);
            PersonResponse personResponse = httpClientUtil.getObjectMapper().readValue(response, PersonResponse.class);
            if (personResponse.isSuccess()) return PersonMessage.SUCCESS;
            else return setFailMessage("Failed to update person", person.getpId(), personResponse.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return setFailMessage("Unable to update person", person.getpId(), e.getMessage());
        }
    }

    public PersonMessage deleteAward() {
        try {
            if (personModel.selectedAwardProperty().get() == null)
                return setFailMessage("Failed to delete award", 0, "No award selected");
            String response = httpClientUtil.postDataToGybe("delete/award", personModel.selectedAwardProperty().get());
            if (response == null)
                return setFailMessage("Failed to delete award", personModel.selectedAwardProperty().get().getAwardId(), "Null response from server");
            AwardResponse awardResponse = httpClientUtil.getObjectMapper().readValue(response, AwardResponse.class);
            if (awardResponse == null)
                return setFailMessage("Failed to delete award", personModel.selectedAwardProperty().get().getAwardId(), "Invalid response from server");
            if (awardResponse.isSuccess()) {
                logger.info(awardResponse.getMessage());
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
                String errorMessage = awardResponse.getMessage() != null ? awardResponse.getMessage() : "Unknown error";
                return setFailMessage("Failed to delete award", personModel.selectedAwardProperty().get().getAwardId(), errorMessage);
            }
        } catch (Exception e) {
            return setFailMessage("Failed to delete award", personModel.selectedAwardProperty().get().getAwardId(), e.getMessage());
        }
    }

    public PersonMessage deletePhone() {
        try {
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
                    return setFailMessage("Phone", personModel.selectedPhoneProperty().get().getPhoneId(), "deleted on server but not found in membership list");
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
            if (personModel.selectedEmailProperty().get().primaryUseProperty().get()) {
                return setFailMessage("Failed to delete email", 0, "primary emails can not be deleted");
            }
            String response = httpClientUtil.postDataToGybe("delete/email", personModel.selectedEmailProperty().get());
            if (response == null)
                return setFailMessage("Failed to delete email", personModel.selectedEmailProperty().get().getEmailId(), "Null response from server");
            EmailResponse emailResponse = httpClientUtil.getObjectMapper().readValue(response, EmailResponse.class);
            if (emailResponse == null)
                return setFailMessage("Failed to delete email", personModel.selectedEmailProperty().get().getEmailId(), "Invalid response from server");
            if (emailResponse.isSuccess()) {
                logger.info(emailResponse.getMessage());
                EmailFx emailFx = personModel.selectedEmailProperty().get();
                if (emailFx != null) {
                    Platform.runLater(() -> {
                        personModel.emailTableViewProperty().get().getItems().remove(emailFx);
                        personModel.emailTableViewProperty().get().refresh();
                    });
                    return PersonMessage.SUCCESS;
                } else
                    return setFailMessage("Email", personModel.selectedEmailProperty().get().getEmailId(), "deleted on server but not found in membership list");
            } else {
                String errorMessage = emailResponse.getMessage() != null ? emailResponse.getMessage() : "Unknown error";
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
            String response = httpClientUtil.postDataToGybe("delete/position", personModel.selectedPositionProperty().get());
            if (response == null)
                return setFailMessage("Failed to delete position", 0, "Null response from server");
            PositionResponse positionResponse = httpClientUtil.getObjectMapper().readValue(response, PositionResponse.class);
            if (positionResponse == null)
                return setFailMessage("Failed to delete position", 0, "Invalid response from server");
            if (positionResponse.isSuccess()) {
                logger.info(positionResponse.getMessage());
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
                String errorMessage = positionResponse.getMessage() != null ? positionResponse.getMessage() : "Unknown error";
                return setFailMessage("Unable to delete position", personModel.selectedPositionProperty().get().getOfficerId(), errorMessage);
            }
        } catch (Exception e) {
            return setFailMessage("Unable to delete position", personModel.selectedPositionProperty().get().getOfficerId(), e.getMessage());
        }
    }

    public PersonMessage deleteMemberFromDatabase() {
        try {
            String response = httpClientUtil.postDataToGybe("delete/person", new Person(personModel.getPersonDTO()));
            if (response == null)
                return setFailMessage("Failed to delete person", 0, "Null response from server");
            PersonResponse personResponse = httpClientUtil.getObjectMapper().readValue(response, PersonResponse.class);
            if (personResponse == null)
                return setFailMessage("Failed to delete person", 0, "Invalid response from server");
            if (personResponse.isSuccess()) {
                logger.info(personResponse.getMessage());
                if (personModel.getPersonDTO() != null) {
                    Platform.runLater(() -> {
                        TabPane tabPane = personModel.getMembershipModel().getPeopleTabPane();
                        tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedIndex());
                    });
                    return PersonMessage.SUCCESS;
                } else
                    return setFailMessage("Position", personModel.selectedPositionProperty().get().getOfficerId(), "deleted on server but not found in membership list");
            } else {
                String errorMessage = personResponse.getMessage() != null ? personResponse.getMessage() : "Unknown error";
                return setFailMessage("Unable to delete person", personModel.getPersonDTO().pIdProperty().get(), errorMessage);
            }
        } catch (Exception e) {
            return setFailMessage("Unable to delete person", personModel.getPersonDTO().pIdProperty().get(), e.getMessage());
        }
    }

    public PersonMessage moveMemberToMembership() {
        System.out.println("Move member to membership: " + personModel.moveToMSIDProperty().get());
        PersonResponse personResponse = new PersonResponse(new Person(personModel.getPersonDTO()));
        personResponse.setMessage("move:" + personModel.moveToMSIDProperty().get());
        try {
            String response = httpClientUtil.postDataToGybe("update/person/memtype", personResponse);
            PersonResponse returnPersonResponse = httpClientUtil.getObjectMapper().readValue(response, PersonResponse.class);
            if (returnPersonResponse.isSuccess()) return PersonMessage.SUCCESS;
            else return setFailMessage("Failed to update person", personModel.getPersonDTO().getpId(), returnPersonResponse.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return setFailMessage("Unable to update person", personModel.getPersonDTO().getpId(), e.getMessage());
        }
    }

    public PersonMessage changeMemberType() {
        System.out.println("Change member type: " + personModel.messageProperty().get());
        PersonResponse personResponse = new PersonResponse(new Person(personModel.getPersonDTO()));
        //personResponse.setMessage("change:" + personModel.messageProperty().get().toString());
        personResponse.setMessage(personModel.messageProperty().get().toString());
        try {
            String response = httpClientUtil.postDataToGybe("update/person/change-mem-type", personResponse);
            PersonResponse returnPersonResponse = httpClientUtil.getObjectMapper().readValue(response, PersonResponse.class);
            if (returnPersonResponse.isSuccess()) {
                System.out.println(personResponse.getMessage());
                setUItoMatchChange(personResponse.getMessage());
                return PersonMessage.SUCCESS;
            }
            else return setFailMessage("Failed to update person's memtype", personModel.getPersonDTO().getpId(), returnPersonResponse.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return setFailMessage("Unable to update person's memtype", personModel.getPersonDTO().getpId(), e.getMessage());
        }
    }

    private void setUItoMatchChange(String message) {
        TabPane tabPane = personModel.getMembershipModel().getPeopleTabPane();
        Tab primary = null;
        Tab secondary = null;
        // Find Primary and Secondary tabs
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals("Primary")) {
                primary = tab;
            } else if (tab.getText().equals("Secondary")) {
                secondary = tab;
            }
        }
        Optional<PersonFx> primaryFX = getPersonWithType(1);
        Optional<PersonFx> secondaryFX = getPersonWithType(2);
        switch (message) {
            case "SWAP_PRIMARY_AND_SECONDARY" -> {
                // Ensure both tabs are found before updating
                if (primary != null && secondary != null) {
                    Tab finalSecondary = secondary;
                    Tab finalPrimary = primary;
                    Platform.runLater(() -> {
                        finalSecondary.setText("Primary");
                        finalPrimary.setText("Secondary");
                    });
                    if(primaryFX.isPresent()) {
                        primaryFX.get().setMemberType(2);
                    }
                    if(secondaryFX.isPresent()) {
                        secondaryFX.get().setMemberType(1);
                    }
                } else {
                    logger.error("Primary or Secondary tab not found");
                }
            }
            case "MAKE_PRIMARY_DEPENDENT_AND_SECONDARY_PRIMARY" -> {
                if (primary != null && secondary != null) {
                    Tab finalSecondary = secondary;
                    Tab finalPrimary = primary;
                    Platform.runLater(() -> {
                        finalSecondary.setText("Primary");
                        finalPrimary.setText("Dependent");
                    });
                    if(primaryFX.isPresent()) { primaryFX.get().setMemberType(3); }
                    if(secondaryFX.isPresent()) { primaryFX.get().setMemberType(1); }
                } else {
                    logger.error("Primary or Secondary tab not found");
                }
            }
        }
    }

    public Optional<PersonFx> getPersonWithType(int type) {
        return personModel.getMembershipModel()
                .membershipProperty()
                .get()
                .getPeople()
                .stream()
                .filter(personFx -> personFx.getMemberType() == type)
                .findFirst();
    }

    public PersonMessage detachMemberFromMembership() {
        System.out.println("Detach Member from membership: " + personModel.getPersonDTO().getFullName());
        return PersonMessage.SUCCESS;
    }

    public PersonMessage detachPrimaryMemberFromMembership() {
        System.out.println("Delete Primary Member from database: " + personModel.getPersonDTO().getFullName());
        return PersonMessage.SUCCESS;
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
