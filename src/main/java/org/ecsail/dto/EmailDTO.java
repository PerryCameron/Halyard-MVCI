package org.ecsail.dto;

import javafx.beans.property.*;

public class EmailDTO {
	
	private IntegerProperty email_id;
	private IntegerProperty pId;
	private BooleanProperty primaryUse;
	private StringProperty email;
	private BooleanProperty listed;

	
	public EmailDTO(Integer email_id, Integer pId, Boolean primaryUse, String email, Boolean listed) {
		this.email_id = new SimpleIntegerProperty(email_id);
		this.pId = new SimpleIntegerProperty(pId);
		this.primaryUse = new SimpleBooleanProperty(primaryUse);
		this.email = new SimpleStringProperty(email);
		this.listed = new SimpleBooleanProperty(listed);
	}

    public EmailDTO(Integer pId) {
		this.email_id = new SimpleIntegerProperty(0);
		this.pId = new SimpleIntegerProperty(pId);
		this.primaryUse = new SimpleBooleanProperty(false);
		this.email = new SimpleStringProperty("");
		this.listed = new SimpleBooleanProperty(true);
    }

	public int getEmail_id() {
		return email_id.get();
	}

	public IntegerProperty email_idProperty() {
		return email_id;
	}

	public void setEmail_id(int email_id) {
		this.email_id.set(email_id);
	}

	public int getpId() {
		return pId.get();
	}

	public IntegerProperty pIdProperty() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId.set(pId);
	}

	public boolean getIsPrimaryUse() {
		return primaryUse.get();
	}

	public BooleanProperty primaryUseProperty() {
		return primaryUse;
	}

	public void setPrimaryUse(boolean primaryUse) {
		this.primaryUse.set(primaryUse);
	}

	public String getEmail() {
		return email.get();
	}

	public StringProperty emailProperty() {
		return email;
	}

	public void setEmail(String email) {
		this.email.set(email);
	}

	public boolean getIsListed() {
		return listed.get();
	}

	public BooleanProperty listedProperty() {
		return listed;
	}

	public void setListed(boolean listed) {
		this.listed.set(listed);
	}
}
