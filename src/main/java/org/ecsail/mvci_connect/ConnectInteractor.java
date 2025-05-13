package org.ecsail.mvci_connect;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.Stage;
//import org.ecsail.connection.Connections;
import okhttp3.Response;
import org.ecsail.dto.LoginDTO;
import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.widgetfx.DialogueFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.HashMap;
import java.util.Optional;

public class ConnectInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(ConnectInteractor.class);
    private final ConnectModel connectModel;
    public ConnectInteractor(ConnectModel connectModel) {
        this.connectModel = connectModel;
    }

    // supplies saved logins from users local hardrive
    public void supplyLogins() {
        List<LoginDTO> loginDTOS = new ArrayList<>();
        if (FileIO.hostFileExists(LOGIN_FILE)) {
            openLoginObjects(loginDTOS);
            logger.info("Added {} logins", loginDTOS.size());
        } else {
            logger.info("login file does not exist.");
            loginDTOS.add(new LoginDTO(1, 8080, "localhost", "user", "password", true)); // Default for first-time use
        }
        connectModel.getLoginDTOS().addAll(loginDTOS);
        copyDefaultToCurrentLogin();
        updateComboBox();

        // Set the server URL for HttpClientUtil
        connectModel.updateServerUrl();

        // Check if authentication is required
        if (requiresAuthentication()) {
            logger.info("Authentication required, showing login dialog.");
            // Dialog is already shown by ConnectView; proceed with login on user action
        } else {
            logger.info("No authentication required, proceeding directly.");
            // Notify controller to proceed without login
            connectModel.setRotateShipWheel(false);
            // Trigger the controller to move forward (e.g., to the main app screen)
            // This will be handled in ConnectController
        }
    }

    private boolean requiresAuthentication() {
        try {
            return connectModel.getHttpClient().requiresAuthentication();
        } catch (IOException e) {
            logger.error("Failed to check authentication status", e);
            DialogueFx.showAlert("Error", "Failed to connect to server: " + e.getMessage());
            return true; // Assume authentication is required if the check fails
        }
    }

    public boolean connectToServer() {
        String username = connectModel.currentLoginProperty().userProperty().get();
        String password = connectModel.currentLoginProperty().passwdProperty().get();
        ObjectMapper objectMapper = new ObjectMapper();

        connectModel.updateServerUrl();
        logger.info("Attempting to login to server: {}", connectModel.getHttpClient().getServerUrl());
        logger.info("Username: {}", username);

        while (true) {
            try (Response response = connectModel.getHttpClient().login(username, password)) {
                logger.info("Login response status: {}", response.code());
                logger.info("Login response headers: {}", response.headers());
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "No response body";
                    logger.warn("Login request failed with status: {}, body: {}", response.code(), errorBody);
                    if (response.code() == 415) {
                        DialogueFx.showAlert("Error", "Unsupported media type. Please contact support.");
                        return false;
                    }
                    Map<String, String> result = response.body() != null ?
                            objectMapper.readValue(errorBody, Map.class) : new HashMap<>();
                    String status = result.getOrDefault("status", "unknown");
                    String message = result.getOrDefault("message", "Unknown error");

                    if ("error".equals(status)) {
                        logger.warn("Login failed: {}", message);
                        DialogueFx.showAlert("Login Failed", message);
                        return false;
                    } else if ("max_sessions".equals(status)) {
                        logger.warn("Maximum sessions reached");
                        boolean retry = DialogueFx.showMaxSessionsDialog();
                        if (retry) {
                            try (Response logoutResponse = connectModel.getHttpClient().logoutOthers()) {
                                logger.info("Logout others response status: {}", logoutResponse.code());
                                if (logoutResponse.isSuccessful()) {
                                    logger.info("Successfully logged out other sessions, retrying login");
                                    continue;
                                } else {
                                    logger.error("Failed to log out other sessions");
                                    DialogueFx.showAlert("Error", "Failed to log out other sessions.");
                                    return false;
                                }
                            }
                        } else {
                            logger.info("User chose not to retry after maximum sessions reached");
                            return false;
                        }
                    } else {
                        logger.error("Unexpected response from server: Status code {}, Status: {}", response.code(), status);
                        DialogueFx.showAlert("Error", "Unexpected response from server: " + response.code());
                        return false;
                    }
                }

                Map<String, String> result = objectMapper.readValue(response.body().string(), Map.class);
                String status = result.get("status");
                String message = result.get("message");

                if ("success".equals(status)) {
                    logger.info("Login successful: {}", message);
                    return true;
                } else {
                    logger.error("Unexpected response status: {}", status);
                    DialogueFx.showAlert("Error", "Unexpected response from server.");
                    return false;
                }
            } catch (IOException e) {
                logger.error("Failed to connect to server", e);
                DialogueFx.showAlert("Error", "Failed to connect to server: " + e.getMessage());
                return false;
            }
        }
    }

    protected Response logOutOthers() throws IOException {
        return connectModel.getHttpClient().logoutOthers();
    }

    private void copyDefaultToCurrentLogin() {
        if (connectModel.getLoginDTOS().size() == 1) {
            connectModel.currentLoginProperty().copyLogin(connectModel.getLoginDTOS().getFirst());
        } else {
            connectModel.getLoginDTOS().stream()
                    .filter(LoginDTO::isDefault)
                    .findFirst()
                    .ifPresentOrElse(
                            defaultLogin -> connectModel.currentLoginProperty().copyLogin(defaultLogin),
                            () -> logger.warn("No default login found to copy to current login.")
                    );
        }
    }

    // copies current loginDTO(bound to textFields) , to the correct loginDTO in the list
    public void copyCurrentLoginToMatchingLoginInList() {
        if (!connectModel.getLoginDTOS().isEmpty()) {
            Optional<LoginDTO> matchingLoginDTO = connectModel.getLoginDTOS().stream()
                    .filter(loginDTO -> loginDTO.getId() == connectModel.currentLoginProperty().getId()).findFirst();
            if(matchingLoginDTO.isPresent()) {
                matchingLoginDTO.get().copyLoginDTOProperty(connectModel.currentLoginProperty());
            } else {
                logger.warn("No matching login object found in login list.");
            }
        }
    }


    public void setCurrentLoginAsDefault() {
        // Clear all defaults
        connectModel.getLoginDTOS().forEach(loginDTO -> loginDTO.setDefault(false));

        // Find the selected login
        Optional<LoginDTO> selectedLogin = connectModel.getLoginDTOS().stream()
                .filter(loginDTO -> loginDTO.getHost().equals(connectModel.getComboBox().getValue()))
                .findFirst();

        // Set it as default and copy to current login
        if (selectedLogin.isPresent()) {
            selectedLogin.get().setDefault(true); // ✅ This was missing
            connectModel.currentLoginProperty().copyLogin(selectedLogin.get());
        } else {
            logger.warn("No login found to set as default.");
        }

        saveLoginObjects();
    }

    protected void createNewLogin() {
        connectModel.getComboBox().getItems().add("");
        int index = findNextIndex();
        connectModel.getLoginDTOS().add(new LoginDTO(index,8080,"","","",false));
        connectModel.currentLoginProperty().setAsNew(index);
        updateComboBox();
    }

    public void deleteLogin() {
        Optional<LoginDTO> loginDTO = connectModel.getLoginDTOS().stream().filter(login -> login.getId() == connectModel.currentLoginProperty()
                .getId()).findFirst();
        if(loginDTO.isPresent()) {
            connectModel.getLoginDTOS().remove(loginDTO.get());
        } else {
            logger.warn("No login found to delete from current login.");
        }
        selectFirstAvailableLogin();
    }

    private void selectFirstAvailableLogin() {
        if (!connectModel.getLoginDTOS().isEmpty()) {
            connectModel.currentLoginProperty().copyLogin(connectModel.getLoginDTOS().getFirst());
            updateComboBox();
        } else {
            createNewLogin();
        }
    }

    // gets the value in the comboBox, finds the correct LoginDTO in the list and copies it to our LoginDTO property
    protected void updateCurrentLogin() {
        String newValue = connectModel.getComboBox().getValue();
        connectModel.getLoginDTOS().stream()
                .filter(login -> login.getHost().equals(newValue))
                .findFirst()
                .ifPresentOrElse(
                        loginDTO -> connectModel.currentLoginProperty().copyLogin(loginDTO),
                        () -> logger.warn("No login found to copy to current login.")
                );
    }


    // makes the comboBox reflect the hostnames in the list of LoginDTOs
    protected void updateComboBox() {
        // Clear both the ComboBox and the backing list
        connectModel.getComboBox().getItems().clear();
        connectModel.getComboValues().clear();

        // Populate comboValues with host names
        for (LoginDTO loginDTO : connectModel.getLoginDTOS()) {
            connectModel.getComboValues().add(loginDTO.getHost());
        }

        // Update ComboBox items
        connectModel.getComboBox().getItems().addAll(connectModel.getComboValues());

        // Set the selected value to the current login's host
        connectModel.getComboBox().setValue(connectModel.currentLoginProperty().hostProperty().getValue());
    }


    public static void openLoginObjects(List<LoginDTO> logins) {
        File g = new File(LOGIN_FILE);
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(g));
                Object obj = in.readObject();
                ArrayList<?> ar = (ArrayList<?>) obj;
                logins.clear();
                for (Object x : ar)
                    logins.add((LoginDTO) x);
                in.close();
            } catch (Exception e) {
                logger.error("Error occurred during reading of {}", LOGIN_FILE);
				logger.error(e.getMessage());
            }
    }

    public void saveLoginObjects() {  // saves user file to disk
        copyCurrentLoginToMatchingLoginInList();
        File g = new File(LOGIN_FILE);
        ArrayList<LoginDTO> unwrappedList = new ArrayList<>(connectModel.getLoginDTOS());
        try	{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(g));
            out.writeObject(unwrappedList);
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            System.exit(0);
        }
        logger.info("{} saved", LOGIN_FILE);
    }

//    public void printLoginObjects() {
//        System.out.println("---Printing login objects");
//        connectModel.getLoginDTOS().forEach(loginDTO -> {
//            System.out.println(loginDTO);
//        });
//        System.out.println("---Printing current object");
//        System.out.println(connectModel.currentLoginProperty().get());
//        System.out.println("");
//    }

    private int findNextIndex() {
        return connectModel.getLoginDTOS().stream()
                .mapToInt(LoginDTO::getId)
                .max()
                .orElse(0) + 1;
    }

    public void logError(String message) {
        logger.error(message);
    }

    public void logInfo(String message) {
        logger.info(message);
    }

    public Stage getStage() {
        return connectModel.getConnectStage();
    }
    public void closeLoginStage() {
        connectModel.getConnectStage().close();
    }
    protected void setRotateShipWheel(boolean rotate) {
        connectModel.setRotateShipWheel(rotate);
    }
}
