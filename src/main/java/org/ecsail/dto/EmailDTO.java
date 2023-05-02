package org.ecsail.dto;

import javafx.beans.property.*;

public class EmailDTO {
	
	private IntegerProperty email_id;
	private IntegerProperty pId;
	private BooleanProperty isPrimaryUse;
	private StringProperty email;
	private BooleanProperty isListed;

	
	public EmailDTO(Integer email_id, Integer pId, Boolean primaryUse, String email, Boolean isListed) {
		this.email_id = new SimpleIntegerProperty(email_id);
		this.pId = new SimpleIntegerProperty(pId);
		this.isPrimaryUse = new SimpleBooleanProperty(primaryUse);
		this.email = new SimpleStringProperty(email);
		this.isListed = new SimpleBooleanProperty(isListed);
	}

    public EmailDTO(Integer pId) {
		this.email_id = new SimpleIntegerProperty(0);
		this.pId = new SimpleIntegerProperty(pId);
		this.isPrimaryUse = new SimpleBooleanProperty(false);
		this.email = new SimpleStringProperty("");
		this.isListed = new SimpleBooleanProperty(true);
    }


    public final IntegerProperty email_idProperty() {
		return this.email_id;
	}
	


	public final int getEmail_id() {
		return this.email_idProperty().get();
	}
	


	public final void setEmail_id(final int email_id) {
		this.email_idProperty().set(email_id);
	}
	


	public final IntegerProperty pIdProperty() {
		return this.pId;
	}
	


	public final int getpId() {
		return this.pIdProperty().get();
	}
	


	public final void setpId(final int pId) {
		this.pIdProperty().set(pId);
	}
	


	public final BooleanProperty isPrimaryUseProperty() {
		return this.isPrimaryUse;
	}
	


	public final boolean isPrimaryUse() {
		return this.isPrimaryUseProperty().get();
	}
	


	public final void setPrimaryUse(final boolean isPrimaryUse) {
		this.isPrimaryUseProperty().set(isPrimaryUse);
	}
	


	public final StringProperty emailProperty() {
		return this.email;
	}
	


	public final String getEmail() {
		return this.emailProperty().get();
	}
	


	public final void setEmail(final String email) {
		this.emailProperty().set(email);
	}
	


	public final BooleanProperty isListedProperty() {
		return this.isListed;
	}
	


	public final boolean isListed() {
		return this.isListedProperty().get();
	}
	


	public final void setIsListed(final boolean isListed) {
		this.isListedProperty().set(isListed);
	}

	@Override
	public String toString() {
		return "EmailDTO " +
				" email_id=" + email_id;
	}
}
