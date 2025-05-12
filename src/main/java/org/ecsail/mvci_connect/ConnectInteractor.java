package org.ecsail.mvci_connect;

import javafx.stage.Stage;
//import org.ecsail.connection.Connections;
import org.ecsail.dto.LoginDTO;
import org.ecsail.dto.LoginDTOProperty;
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
        // we need to find the default, and set the
        System.out.println("just set current login property: " + connectModel.currentLoginProperty().get().userProperty().get());
        System.out.println("They are loaded" + connectModel.getLoginDTOS());
    }

    private void copyDefaultToCurrentLogin() {
        if (connectModel.getLoginDTOS().size() == 1) {
            connectModel.currentLoginProperty().get().copyLogin(connectModel.getLoginDTOS().get(0));
        } else {
            Optional<LoginDTO> defaultLoginOpt = connectModel.getLoginDTOS().stream()
                    .filter(LoginDTO::isDefault)
                    .findFirst();
            if (defaultLoginOpt.isPresent()) {
                connectModel.currentLoginProperty().get().copyLogin(defaultLoginOpt.get());
            } else {
                logger.warn("No default login found to copy to current login.");
            }
        }
    }

    // copies the contents of what is in text fields to matching LoginDTO in list
    private void copyCurrentToMatchingLogin() {
        if (connectModel.getLoginDTOS().size() > 0) {
            Optional<LoginDTO> matchingLoginDTO = connectModel.getLoginDTOS().stream().filter(loginDTO -> loginDTO.getId() == connectModel.currentLoginProperty().get().getId()).findFirst();
            if(matchingLoginDTO.isPresent()) {
                matchingLoginDTO.get().copyLoginDTOProperty(connectModel.currentLoginProperty().get());
            } else {
                logger.warn("No matching login object found in login list.");
            }
        }
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
        copyCurrentToMatchingLogin();
        File g = new File(LOGIN_FILE);
        ArrayList<LoginDTO> unwrappedList = new ArrayList<>(connectModel.getComboBox().getItems());
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



    public void logError(String message) {
        logger.error(message);
    }

    public void logInfo(String message) {
        logger.info(message);
    }
    public void loadCommonLists() {
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
