package org.ecsail.dto;

import java.io.Serial;
import java.io.Serializable;

public class LoginDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 7434038316336853182L;

	private int localSqlPort;
	private int remoteSqlPort;
	private int sshPort;
	private String host;
	private String sqlUser;
	private String sqlPasswd;
	private String sshUser;
	private String sshPass;
	private String database;
	private String knownHostsFile;
	private String privateKeyFile;
	private boolean isDefault;
	private boolean sshForward;


	public LoginDTO(int localSqlPort, int remoteSqlPort, int sshPort, String host, String sqlUser, String sqlPasswd, String sshUser, String sshPass, String database, String knownHostsFile, String privateKeyFile, boolean isDefault, boolean sshForward) {
		this.localSqlPort = localSqlPort;
		this.remoteSqlPort = remoteSqlPort;
		this.sshPort = sshPort;
		this.host = host;
		this.sqlUser = sqlUser;
		this.sqlPasswd = sqlPasswd;
		this.sshUser = sshUser;
		this.sshPass = sshPass;
		this.database = database;
		this.knownHostsFile = knownHostsFile;
		this.privateKeyFile = privateKeyFile;
		this.isDefault = isDefault;
		this.sshForward = sshForward;
	}

	public int getLocalSqlPort() {
		return localSqlPort;
	}

	public void setLocalSqlPort(int localSqlPort) {
		this.localSqlPort = localSqlPort;
	}

	public int getRemoteSqlPort() {
		return remoteSqlPort;
	}

	public int getSshPort() {
		return sshPort;
	}

	public void setSshPort(int sshPort) {
		this.sshPort = sshPort;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSqlUser() {
		return sqlUser;
	}

	public void setSqlUser(String sqlUser) {
		this.sqlUser = sqlUser;
	}

	public String getSqlPasswd() {
		return sqlPasswd;
	}

	public void setSqlPasswd(String sqlPasswd) {
		this.sqlPasswd = sqlPasswd;
	}

	public String getSshUser() {
		return sshUser;
	}

	public void setSshUser(String sshUser) {
		this.sshUser = sshUser;
	}
	
	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getKnownHostsFile() {
		return knownHostsFile;
	}

	public void setKnownHostsFile(String knownHostsFile) {
		this.knownHostsFile = knownHostsFile;
	}

	public String getPrivateKeyFile() {
		return privateKeyFile;
	}

	public void setPrivateKeyFile(String privateKeyFile) {
		this.privateKeyFile = privateKeyFile;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean aDefault) {
		isDefault = aDefault;
	}

	public boolean isSshForward() {
		return sshForward;
	}

	public void setSshForward(boolean sshForward) {
		this.sshForward = sshForward;
	}

	@Override
	public String toString() {
		return "LoginDTO{" +
				"localSqlPort=" + localSqlPort +
				", remoteSqlPort=" + remoteSqlPort +
				", sshPort=" + sshPort +
				", host='" + host + '\'' +
				", sqlUser='" + sqlUser + '\'' +
				", sqlPasswd='" + sqlPasswd + '\'' +
				", sshUser='" + sshUser + '\'' +
				", sshPass='" + sshPass + '\'' +
				", database='" + database + '\'' +
				", knownHostsFile='" + knownHostsFile + '\'' +
				", privateKeyFile='" + privateKeyFile + '\'' +
				", isDefault=" + isDefault +
				", sshForward=" + sshForward +
				'}';
	}
}
