package org.ecsail.connection;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.ecsail.mvci_connect.ConnectModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Connections {
    private Connection sqlConnection;
    private PortForwardingL sshConnection;
    private Sftp scp;
    private DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(Connections.class);
    private ConnectModel connectModel;
    public Connections(ConnectModel connectModel) {
        this.connectModel = connectModel;
    }

    public Boolean connect()  {
        logger.info("Host is " + connectModel.getHost());
        String loopback = "127.0.0.1";
        // create ssh tunnel
        if(connectModel.isSshUsed()) {
            logger.info("SSH tunnel enabled");
            logger.info("Attempting to connect to " + connectModel.getHost());
            setSshConnection(new PortForwardingL(connectModel.getComboBox().getValue()));
            logger.info("Server Alive interval: " + sshConnection.getSession().getServerAliveInterval());
        } else
            logger.info("SSH connection is not being used");
        if(createConnection(connectModel.getUser(), connectModel.getPass(), loopback, connectModel.getLocalSqlPort())) {
            this.scp = new Sftp(sshConnection);
        } else {
            logger.error("Can not connect to SQL server");
        }
        return sshConnection.getSession().isConnected();
    };

    protected Boolean createConnection(String user, String password, String ip, int port) {
        boolean successful = false;
        try {
            createDataSource(ip,port,user,password);
            setSqlConnection(dataSource.getConnection());
            successful = true;
            logger.info("SQL Connection established." + dataSource);
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e.getMessage());
        }
        return successful;
    }

    public void createDataSource(String ip, int port, String user, String pass) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        dataSource = new DriverManagerDataSource(
                "jdbc:mysql://" + ip + ":" + port + "/ECSC_SQL?autoReconnect=true&useSSL=false&serverTimezone=UTC",
                user,
                pass
        );
    }

    public Connection getSqlConnection() {
        return sqlConnection;
    }

    public PortForwardingL getSshConnection() {
        return sshConnection;
    }

    public Sftp getScp() {
        return scp;
    }

    public void setSshConnection(PortForwardingL sshConnection) {
        this.sshConnection = sshConnection;
    }

    public void setSqlConnection(Connection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
