package org.ecsail.mvci_connect;

import javafx.collections.FXCollections;
import org.ecsail.dto.LoginDTO;
import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.widgetfx.ObjectFx;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectInteractor implements ConfigFilePaths {
    private ConnectModel connectModel;
    public ConnectInteractor(ConnectModel connectModel) {
        this.connectModel = connectModel;
    }

    public List<LoginDTO> supplyLogins() {
        List<LoginDTO> loginDTOS = new ArrayList<>();
        if (FileIO.hostFileExists(LOGIN_FILE))
            openLoginObjects(loginDTOS);
        else {
            System.out.println("Starting application for first time");
            loginDTOS.add(ObjectFx.createLoginDTO()); // we are starting application for the first time
        }
        System.out.println(loginDTOS.size());
        return loginDTOS;
    }

    public static void openLoginObjects(List<LoginDTO> logins) {
        File g = new File(LOGIN_FILE);
        if (g.exists()) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(g));
                Object obj = in.readObject();
                ArrayList<?> ar = (ArrayList<?>) obj;
                logins.clear();
                for (Object x : ar) {
                    logins.add((LoginDTO) x);
                }
                in.close();
            } catch (Exception e) {
//				BaseApplication.logger.error("Error occurred during reading of " + HalyardPaths.HOSTS);
//				BaseApplication.logger.error(e.getMessage());
                e.printStackTrace();
            }
        } else {
//			BaseApplication.logger.error("No such file exists: " + HalyardPaths.HOSTS);
        }
    }

    public void saveLoginObjects() {  // saves user file to disk
        System.out.println("Saving to Disk");
        File g = new File(LOGIN_FILE);
        ArrayList<LoginDTO> unwrappedList = new ArrayList<>(connectModel.getComboBox().getItems());
        System.out.println("unwrappedList has " + unwrappedList.size());
        try	{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(g));
            out.writeObject(unwrappedList);
            out.close();
        } catch (Exception e) {
//            BaseApplication.logger.error(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
//        BaseApplication.logger.info(HalyardPaths.HOSTS + " saved");
    }

}
