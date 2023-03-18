package org.ecsail.MvciMain;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MainModel {
    private Connection sqlConnection;
//    private PortForwardingL sshConnection;
//    private Sftp scp;
//    private LoginDTO currentLogon;
//    private List<LoginDTO> logins;
//    private StringProperty user = new SimpleStringProperty();
//    private StringProperty pass = new SimpleStringProperty();
//    private StringProperty host = new SimpleStringProperty();
//    private StringProperty localSqlPort = new SimpleStringProperty();
//    private AppConfig appConfig;
    private final StringProperty statusLabel = new SimpleStringProperty(""); // keeper here

//    public static ObservableList<MembershipListDTO> activeMemberships;
//    public static ArrayList<BoardPositionDTO> boardPositions;
//    public static ConnectDatabase connect;
//    public static String selectedYear;
//    public static String user = "membership";
//    public static File outputFile;

//    public MainModel() {
//        this.logins = setLogins();
//        this.currentLogon = logins.get(0);
//    }

//    public Runnable connect = () -> {
//        BaseApplication.logger.info("Host is " + host.get());
//        String loopback = "127.0.0.1";
//
//        // create ssh tunnel
//        if(currentLogon.isSshForward()) {
//            BaseApplication.logger.info("SSH tunnel enabled");
//            BaseApplication.logger.info("Attempting to connect to " + host.get());
//            System.out.println("Time to port forward");
//            setSshConnection(new PortForwardingL(currentLogon));
//            BaseApplication.logger.info("Server Alive interval: " + sshConnection.getSession().getServerAliveInterval());
//        } else {
//            BaseApplication.logger.info("SSH connection is not being used");
//        }
//        if(createConnection(user.get(), pass.get(), loopback, Integer.parseInt(localSqlPort.get()))) {
////            BaseApplication.activeMemberships = SqlMembershipList.getRoster(BaseApplication.selectedYear, true);
////            // gets a list of all the board positions to use throughout the application
////            BaseApplication.boardPositions = Officer.getPositionList();
//            this.scp = new Sftp();
//        } else {
//            BaseApplication.logger.error("Can not connect to SQL server");
//        }
//    };

//    protected Boolean createConnection(String user, String password, String ip, int port) {
//        boolean successful = false;
//        try {
//            this.appConfig = new AppConfig();
//            this.appConfig.createDataSource(ip,port,user,password);
//            setSqlConnection(appConfig.getDataSource().getConnection());
//            successful = true;
//            // Creating a Statement object
//        } catch (ClassNotFoundException | SQLException e) {
//            BaseApplication.logger.error(e.getMessage());
//        }
//        return successful;
//    }


    public StringProperty statusLabelProperty() {
        return statusLabel;
    }
    public void setStatusLabel(String statusLabel) {
        this.statusLabel.set(statusLabel);
    }

    public Connection getSqlConnection() {
        return sqlConnection;
    }

    public void setSqlConnection(Connection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }

//    public PortForwardingL getSshConnection() {
//        return sshConnection;
//    }
//
//    public void setSshConnection(PortForwardingL sshConnection) {
//        this.sshConnection = sshConnection;
//    }
//
//    public Sftp getScp() {
//        return scp;
//    }
//
//    public void setScp(Sftp scp) {
//        this.scp = scp;
//    }

//    public LoginDTO getCurrentLogon() {
//        return currentLogon;
//    }
//
//    public void setCurrentLogon(LoginDTO currentLogon) {
//        this.currentLogon = currentLogon;
//    }
//
//    public List<LoginDTO> getLogins() {
//        return logins;
//    }
//
//    public void setLogins(List<LoginDTO> logins) {
//        this.logins = logins;
//    }

//    public AppConfig getAppConfig() {
//        return appConfig;
//    }
//
//    public void setAppConfig(AppConfig appConfig) {
//        this.appConfig = appConfig;
//    }



//    public List<LoginDTO> setLogins() {
//        List<LoginDTO> loginDTOS = new ArrayList<>();
//        if (FileIO.hostFileExists())
//            FileIO.openLoginObjects(loginDTOS);
//        else
//            // we are starting application for the first time
//            loginDTOS.add(new LoginDTO(3306,3306, 22, "", "", "", "",
//                    "", System.getProperty("user.home") + "/.ssh/known_hosts",
//                    System.getProperty("user.home") + "/.ssh/id_rsa", false, false));
//        return loginDTOS;
//    }


}
