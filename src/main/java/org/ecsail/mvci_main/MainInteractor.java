package org.ecsail.mvci_main;

import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;

public class MainInteractor implements ConfigFilePaths {

    private final MainModel mainModel;
    public MainInteractor(MainModel mainModel) {
        this.mainModel = mainModel;
        FileIO.checkPath(SCRIPTS_FOLDER);
        FileIO.checkPath(LOG_FOLDER);
    }


//    public void closeConnections() {
//        try {
//            model.getSqlConnection().close();
//            BaseApplication.logger.info("SQL: Connection closed");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        // if ssh is connected then disconnect
//        if (model.getSshConnection() != null && model.getSshConnection().getSession().isConnected()) {
//            try {
//                model.getSshConnection().getSession().delPortForwardingL(3306);
//                model.getSshConnection().getSession().disconnect();
//                BaseApplication.logger.info("SSH: port forwarding closed");
//            } catch (JSchException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
