package org.ecsail.connection;


import com.jcraft.jsch.*;
import org.ecsail.dto.LoginDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

// https://dentrassi.de/2015/07/13/programmatically-adding-a-host-key-with-jsch/
public class PortForwardingL {
    static String passwd;
    private Session session;
    private JSch jsch = new JSch();
    private static final Logger logger = LoggerFactory.getLogger(PortForwardingL.class);
    private static final boolean usePublicKey = true;

    public PortForwardingL(LoginDTO login) {
        logger.info("Connecting with public key..");
        try {
            jsch.setKnownHosts(login.getKnownHostsFile());
            jsch.addIdentity(login.getPrivateKeyFile());
            HostKeyRepository hkr = jsch.getHostKeyRepository();
            HostKey[] hks = hkr.getHostKey();
            if (hks != null) {
                logger.info("Host keys exist");
                // This will print out the keys
//                for (HostKey hk : hks) {
//                    System.out.println(hk.getHost() + " " + hk.getType() + " " + hk.getFingerPrint(jsch));
//                }
            }

            session = jsch.getSession(login.getSshUser(), login.getHost(), login.getSshPort());
            UserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);
            session.connect();

            int assingedPort = 0;
            // this prevents exception from filling log if mysql is running locally for testing
            try {
                logger.info("Attempting to bind SQL port");
                assingedPort = session.setPortForwardingL(login.getLocalSqlPort(), "127.0.0.1", login.getRemoteSqlPort());
            } catch (JSchException e) {
                logger.error(e.getMessage() + " Check to see if database is running locally");
            }
            logger.info("localhost:" + assingedPort + " -> " + "127.0.0.1" + ":" + login.getRemoteSqlPort());
//			this.ftp = new Sftp(jsch, session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class MyUserInfo implements UserInfo {

        public String getPassword() {
            if(usePublicKey) return null;
            // if not using a public key we are using a password
            return passwd;
        }

        public boolean promptYesNo(String str) {
            // change to java fx
            Object[] options = {"yes", "no"};
            int foo = JOptionPane.showOptionDialog(null, str, "Warning", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            return foo == 0;
        }

        public String getPassphrase() {
            return null;
        }

        public boolean promptPassphrase(String message) {
            return false;
        }

        public boolean promptPassword(String message) {
            return false;
        }

        public void showMessage(String message) {
            /// put in a JavaFX message display here.
        }
    }

    public boolean checkSSHConnection() {
        Socket socket;
        try {
            socket = new Socket("localhost", 7);
            if (socket.isConnected()) {
                socket.close();
                return true;
            }
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    public void closeSession() {
        session.disconnect();
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public JSch getJsch() {
        return jsch;
    }

    public void setJsch(JSch jsch) {
        this.jsch = jsch;
    }

}