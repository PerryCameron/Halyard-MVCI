package org.ecsail.dto;

import javafx.beans.property.*;

import java.io.Serial;
import java.io.Serializable;

public class LoginDTO implements Serializable {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 7434038316336853182L;

	private final IntegerProperty localSqlPort;
	private final IntegerProperty remoteSqlPort;
	private IntegerProperty sshPort;
	private StringProperty host;
	private StringProperty user;
	private StringProperty passwd;
	private StringProperty sshUser;
	private StringProperty sshPass;
	private StringProperty knownHostsFile;
	private StringProperty publicKeyFile;
	private BooleanProperty isDefault;
	private BooleanProperty sshForward;

	public LoginDTO(Integer localSqlPort, Integer remoteSqlPort, Integer sshPort, String host, String user,
					String passwd, String sshUser, String sshPass, String knownHostsFile, String publicKeyFile,
					Boolean isDefault, Boolean sshForward) {
		this.localSqlPort= new SimpleIntegerProperty(localSqlPort);
		this.remoteSqlPort = new SimpleIntegerProperty(remoteSqlPort);
		this.sshPort = new SimpleIntegerProperty(sshPort);
		this.host = new SimpleStringProperty(host);
		this.user = new SimpleStringProperty(user);
		this.passwd = new SimpleStringProperty(passwd);
		this.sshUser = new SimpleStringProperty(sshUser);
		this.sshPass = new SimpleStringProperty(sshPass);
		this.knownHostsFile = new SimpleStringProperty(knownHostsFile);
		this.publicKeyFile = new SimpleStringProperty(publicKeyFile);
		this.isDefault = new SimpleBooleanProperty(isDefault);
		this.sshForward = new SimpleBooleanProperty(sshForward);
	}

	public int getLocalSqlPort() {
		return localSqlPort.get();
	}

	public IntegerProperty localSqlPortProperty() {
		return localSqlPort;
	}

	public void setLocalSqlPort(int localSqlPort) {
		this.localSqlPort.set(localSqlPort);
	}

	public int getRemoteSqlPort() {
		return remoteSqlPort.get();
	}

	public IntegerProperty remoteSqlPortProperty() {
		return remoteSqlPort;
	}

	public void setRemoteSqlPort(int remoteSqlPort) {
		this.remoteSqlPort.set(remoteSqlPort);
	}

	public int getSshPort() {
		return sshPort.get();
	}

	public IntegerProperty sshPortProperty() {
		return sshPort;
	}

	public void setSshPort(int sshPort) {
		this.sshPort.set(sshPort);
	}

	public String getHost() {
		return host.get();
	}

	public StringProperty hostProperty() {
		return host;
	}

	public void setHost(String host) {
		this.host.set(host);
	}

	public String getUser() {
		return user.get();
	}

	public StringProperty userProperty() {
		return user;
	}

	public void setUser(String user) {
		this.user.set(user);
	}

	public String getPasswd() {
		return passwd.get();
	}

	public StringProperty passwdProperty() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd.set(passwd);
	}

	public String getSshUser() {
		return sshUser.get();
	}

	public StringProperty sshUserProperty() {
		return sshUser;
	}

	public void setSshUser(String sshUser) {
		this.sshUser.set(sshUser);
	}

	public String getSshPass() {
		return sshPass.get();
	}

	public StringProperty sshPassProperty() {
		return sshPass;
	}

	public void setSshPass(String sshPass) {
		this.sshPass.set(sshPass);
	}

	public String getKnownHostsFile() {
		return knownHostsFile.get();
	}

	public StringProperty knownHostsFileProperty() {
		return knownHostsFile;
	}

	public void setKnownHostsFile(String knownHostsFile) {
		this.knownHostsFile.set(knownHostsFile);
	}

	public String getPublicKeyFile() {
		return publicKeyFile.get();
	}

	public StringProperty publicKeyFileProperty() {
		return publicKeyFile;
	}

	public void setPublicKeyFile(String publicKeyFile) {
		this.publicKeyFile.set(publicKeyFile);
	}

	public boolean isDefault() {
		return isDefault.get();
	}

	public BooleanProperty isDefaultProperty() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault.set(isDefault);
	}

	public boolean isSshForward() {
		return sshForward.get();
	}

	public BooleanProperty sshForwardProperty() {
		return sshForward;
	}

	public void setSshForward(boolean sshForward) {
		this.sshForward.set(sshForward);
	}

	@Override
	public String toString() {
		return "LoginDTO: " + this.getHost();
	}
}
