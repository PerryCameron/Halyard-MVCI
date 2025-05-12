//package org.ecsail.connection;
//
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.ecsail.mvci_connect.ConnectModel;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//public class Connections {
//    private Connection sqlConnection;
//    private PortForwardingL sshConnection;
//    private Sftp scp;
//    private DataSource dataSource;
//    private static final Logger logger = LoggerFactory.getLogger(Connections.class);
//    private final ConnectModel connectModel;
//    public Connections(ConnectModel connectModel) {
//        this.connectModel = connectModel;
//    }
//
//    public Boolean connect()  {
//        logger.info("Host is " + connectModel.getHost());
//        String loopback = "127.0.0.1";
//        // create ssh tunnel
//        logger.info("SSH tunnel enabled: " + connectModel.sshUsedProperty().get());
//        if(connectModel.sshUsedProperty().get()) {
//            logger.info("SSH tunnel enabled");
//            logger.info("Attempting to connect to " + connectModel.getHost());
//            setSshConnection(new PortForwardingL(connectModel.getComboBox().getValue()));
//            logger.info("Server Alive interval: " + sshConnection.getSession().getServerAliveInterval());
//        } else
//            logger.info("SSH connection is not being used");
//        if(createDataBaseConnection(
//                connectModel.getSqlUser(),
//                connectModel.getSqlPass(),
//                loopback,
//                connectModel.getLocalSqlPort(),
//                connectModel.getDatabase()))
//        {
//            this.scp = new Sftp(sshConnection);
//        } else {
//            logger.error("Can not connect to SQL server");
//        } // below prevents exception when sshConnection is null, example: if checked to not use.
//        if(sshConnection != null) return sshConnection.getSession().isConnected();
//        return false;
//    }
//
//    protected Boolean createDataBaseConnection(String user, String password, String ip, int port, String database) {
//        boolean successful = false;
//        try {
//            createDataSource(ip,port,user,password,database);
//            setSqlConnection(dataSource.getConnection());
//            successful = true;
//            logger.info("SQL Connection established  - " + dataSource);
//        } catch (ClassNotFoundException | SQLException e) {
//            logger.error(e.getMessage());
//        }
//        return successful;
//    }
//
//    public void createDataSource(String ip, int port, String user, String pass, String database) throws ClassNotFoundException {
////        Class.forName("com.mysql.cj.jdbc.Driver");
//        Class.forName("org.mariadb.jdbc.Driver");
//        dataSource = new DriverManagerDataSource(
//                "jdbc:mariadb://" + ip + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false&serverTimezone=UTC",
//                user,
//                pass
//        );
//    }
//
//    public Connection getSqlConnection() {
//        return sqlConnection;
//    }
//
//    public PortForwardingL getSshConnection() {
//        return sshConnection;
//    }
//
//    public Sftp getScp() {
//        return scp;
//    }
//
//    public void setSshConnection(PortForwardingL sshConnection) {
//        this.sshConnection = sshConnection;
//    }
//
//    public void setSqlConnection(Connection sqlConnection) {
//        this.sqlConnection = sqlConnection;
//    }
//
//    public DataSource getDataSource() {
//        return dataSource;
//    }
//}
