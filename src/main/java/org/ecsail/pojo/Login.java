package org.ecsail.pojo;

import org.ecsail.fx.LoginDTOProperty;

import java.io.Serial;
import java.io.Serializable;

public class Login implements Serializable {

	@Serial
	private static final long serialVersionUID = 7434038316336853182L;

	private int id;
	private int port;
	private String host;
	private String user;
	private String passwd;
	private boolean isDefault;

	public Login(int id, int port, String host, String user, String passwd, boolean isDefault) {
		this.id = id;
		this.port = port;
		this.host = host;
		this.user = user;
		this.passwd = passwd;
		this.isDefault = isDefault;
	}

	public void copyLoginDTOProperty(LoginDTOProperty loginDTOProperty) {
		this.id = loginDTOProperty.idProperty().getValue();
		this.port = Integer.parseInt(loginDTOProperty.portProperty().getValue());
		this.host = loginDTOProperty.hostProperty().getValue();
		this.user = loginDTOProperty.userProperty().getValue();
		this.passwd = loginDTOProperty.passwdProperty().getValue();
		this.isDefault = loginDTOProperty.isDefaultProperty().getValue();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPortAsString() {
		return String.valueOf(port);
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean aDefault) {
		isDefault = aDefault;
	}

	@Override
	public String toString() {
		return "LoginDTO{" +
				"port=" + port +
				", host='" + host + '\'' +
				", user='" + user + '\'' +
				", passwd='" + passwd + '\'' +
				", isDefault=" + isDefault +
				'}';
	}
}
