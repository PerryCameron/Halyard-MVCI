package org.ecsail.mvci_connect;

import javafx.stage.Stage;
//import org.ecsail.connection.Connections;
import org.ecsail.dto.LoginDTO;
import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConnectInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(ConnectInteractor.class);
    private final ConnectModel connectModel;
    public ConnectInteractor(ConnectModel connectModel) {
        this.connectModel = connectModel;
    }

    public void supplyLogins() {
        List<LoginDTO> loginDTOS = new ArrayList<>();
        if (FileIO.hostFileExists(LOGIN_FILE)) {
            openLoginObjects(loginDTOS);
            logger.info("Added {} logins", loginDTOS.size());
        } else {
            logger.info("login file does not exist.");
            loginDTOS.add(new LoginDTO(1,8080,"hostName","LoginDTO","passWord",true)); // we are starting application for the first time
        }
        connectModel.getLoginDTOS().addAll(loginDTOS);
        copyDefaultToCurrentLogin();
        updateComboBox();
    }

    private void copyDefaultToCurrentLogin() {
        if (connectModel.getLoginDTOS().size() == 1) {
            connectModel.currentLoginProperty().get().copyLogin(connectModel.getLoginDTOS().get(0));
        } else {
            connectModel.getLoginDTOS().stream()
                    .filter(LoginDTO::isDefault)
                    .findFirst()
                    .ifPresentOrElse(
                            defaultLogin -> connectModel.currentLoginProperty().get().copyLogin(defaultLogin),
                            () -> logger.warn("No default login found to copy to current login.")
                    );
        }
    }

    // copies current loginDTO(bound to textFields) , to the correct loginDTO in the list
    public void copyCurrentLoginToMatchingLoginInList() {
        if (connectModel.getLoginDTOS().size() > 0) {
            Optional<LoginDTO> matchingLoginDTO = connectModel.getLoginDTOS().stream().filter(loginDTO -> loginDTO.getId() == connectModel.currentLoginProperty().get().getId()).findFirst();
            if(matchingLoginDTO.isPresent()) {
                matchingLoginDTO.get().copyLoginDTOProperty(connectModel.currentLoginProperty().get());
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
            connectModel.currentLoginProperty().get().copyLogin(selectedLogin.get());
        } else {
            logger.warn("No login found to set as default.");
        }

        saveLoginObjects();
    }

    protected void createNewLogin() {
        connectModel.getComboBox().getItems().add("");
        int index = findNextIndex();
        connectModel.getLoginDTOS().add(new LoginDTO(index,8080,"","","",false));
        connectModel.currentLoginProperty().get().setAsNew(index);
        updateComboBox();
    }

    public void deleteLogin() {
        Optional<LoginDTO> loginDTO = connectModel.getLoginDTOS().stream().filter(login -> login.getId() == connectModel.currentLoginProperty()
                .get().getId()).findFirst();
        if(loginDTO.isPresent()) {
            connectModel.getLoginDTOS().remove(loginDTO.get());
        } else {
            logger.warn("No login found to delete from current login.");
        }
        selectFirstAvailableLogin();
    }

    private void selectFirstAvailableLogin() {
        if (connectModel.getLoginDTOS().size() > 0) {
            connectModel.currentLoginProperty().get().copyLogin(connectModel.getLoginDTOS().get(0));
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
                        loginDTO -> connectModel.currentLoginProperty().get().copyLogin(loginDTO),
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
        connectModel.getComboBox().setValue(connectModel.currentLoginProperty().get().hostProperty().getValue());
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
                e.printStackTrace();
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
            e.printStackTrace();
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
