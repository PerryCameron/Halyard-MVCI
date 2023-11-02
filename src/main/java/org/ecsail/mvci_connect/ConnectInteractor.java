package org.ecsail.mvci_connect;

import javafx.stage.Stage;
import org.ecsail.connection.Connections;
import org.ecsail.dto.LoginDTO;
import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.widgetfx.ObjectFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(ConnectInteractor.class);
    private final ConnectModel connectModel;
    private final Connections connections;
    public ConnectInteractor(ConnectModel connectModel) {
        this.connectModel = connectModel;
        this.connections = new Connections(connectModel);
    }

    public void supplyLogins() {
        List<LoginDTO> loginDTOS = new ArrayList<>();
        if (FileIO.hostFileExists(LOGIN_FILE))
            openLoginObjects(loginDTOS);
        else {
            logger.info("Starting application for first time");
            loginDTOS.add(ObjectFx.createLoginDTO()); // we are starting application for the first time
        }
        connectModel.setLoginDTOS((ArrayList<LoginDTO>) loginDTOS);
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
				logger.error("Error occurred during reading of " + LOGIN_FILE);
				logger.error(e.getMessage());
                e.printStackTrace();
            }
    }

    public void saveLoginObjects() {  // saves user file to disk
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
        logger.info(LOGIN_FILE + " saved");
    }

    public Connections getConnections() {
        return connections;
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
    protected String getHost() {
        return connectModel.getHost();
    }
}
