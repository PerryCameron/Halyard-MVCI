package org.ecsail.fx;

import javafx.beans.property.*;
import org.ecsail.pojo.Login;

public class LoginDTOProperty {

	private IntegerProperty id = new SimpleIntegerProperty(1);
	private StringProperty port = new SimpleStringProperty("8080");
	private StringProperty host = new SimpleStringProperty("host");
	private StringProperty user = new SimpleStringProperty("LoginDTOProperty");
	private StringProperty passwd = new SimpleStringProperty("passwd");
	private BooleanProperty isDefault = new SimpleBooleanProperty(true);

	public LoginDTOProperty() {
	}

	public void setAsNew(int id) {
		this.id.set(id);
		this.port.set("");
		this.host.set("");
		this.user.set("");
		this.passwd.set("");
		this.isDefault.set(false);
	}

	public void copyLogin(Login loginDTO) {
		this.id.set(loginDTO.getId());
		this.port.set(String.valueOf(loginDTO.getPort()));
		this.host.set(loginDTO.getHost());
		this.user.set(loginDTO.getUser());
		this.passwd.set(loginDTO.getPasswd());
		this.isDefault.set(loginDTO.isDefault());
	}

	public IntegerProperty idProperty() {
		return id;
	}

	public int getId() {
		return id.get();
	}

	public String getPort() {
		return port.get();
	}

	public StringProperty portProperty() {
		return port;
	}

	public StringProperty hostProperty() {
		return host;
	}

	public StringProperty userProperty() {
		return user;
	}

	public StringProperty passwdProperty() {
		return passwd;
	}

	public boolean isDefault() {
		return isDefault.get();
	}

	public BooleanProperty isDefaultProperty() {
		return isDefault;
	}

	@Override
	public String toString() {
		return "LoginDTOProperty{" +
				"id=" + id.get() +
				", port=" + port.get() +
				", host=" + host.get() +
				", user=" + user.get() +
				", passwd=" + passwd.get() +
				", isDefault=" + isDefault.get() +
				'}';
	}
}
