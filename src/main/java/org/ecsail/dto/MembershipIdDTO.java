package org.ecsail.dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MembershipIdDTO {
	private IntegerProperty mId;
	private SimpleStringProperty fiscalYear;
	private IntegerProperty msId;
	private SimpleStringProperty membershipId;
	private SimpleBooleanProperty renew;
	private SimpleStringProperty memType;
	private SimpleBooleanProperty selected;
	private SimpleBooleanProperty lateRenew;
	
	public MembershipIdDTO(Integer mId, String fiscalYear, Integer msId, String membershipId,
						   Boolean renew, String memType, Boolean selected, Boolean lateRenew) {
		this.mId = new SimpleIntegerProperty(mId);
		this.fiscalYear = new SimpleStringProperty(fiscalYear);
		this.msId = new SimpleIntegerProperty(msId);
		this.membershipId = new SimpleStringProperty(membershipId);
		this.renew = new SimpleBooleanProperty(renew);
		this.memType = new SimpleStringProperty(memType);
		this.selected = new SimpleBooleanProperty(selected);
		this.lateRenew = new SimpleBooleanProperty(lateRenew);
	}

	public MembershipIdDTO(String fiscalYear, Integer msId, String membershipId, String memType) {
		this.mId = new SimpleIntegerProperty(0);
		this.fiscalYear = new SimpleStringProperty("0");
		this.msId = new SimpleIntegerProperty(msId);
		this.membershipId = new SimpleStringProperty(membershipId);
		this.renew = new SimpleBooleanProperty(false);
		this.memType = new SimpleStringProperty(memType);
		this.selected = new SimpleBooleanProperty(false);
		this.lateRenew = new SimpleBooleanProperty(false);
	}



	public final IntegerProperty mIdProperty() {
		return this.mId;
	}
	

	public final int getmId() {
		return this.mIdProperty().get();
	}
	

	public final void setmId(final int mId) {
		this.mIdProperty().set(mId);
	}
	

	public final SimpleStringProperty fiscalYearProperty() {
		return this.fiscalYear;
	}
	

	public final String getFiscalYear() {
		return this.fiscalYearProperty().get();
	}
	

	public final void setFiscalYear(final String fiscalYear) {
		this.fiscalYearProperty().set(fiscalYear);
	}
	

	public final IntegerProperty msIdProperty() {
		return this.msId;
	}
	

	public final int getMsId() {
		return this.msIdProperty().get();
	}
	

	public final void setMsId(final int msId) {
		this.msIdProperty().set(msId);
	}
	

	public final SimpleStringProperty membershipIdProperty() {
		return this.membershipId;
	}
	

	public final String getMembershipId() {
		return this.membershipIdProperty().get();
	}
	

	public final void setMembershipId(final String membershipId) {
		this.membershipIdProperty().set(membershipId);
	}
	

	public final SimpleBooleanProperty renewProperty() {
		return this.renew;
	}
	

	public final boolean isRenew() {
		return this.renewProperty().get();
	}
	

	public final void setIsRenew(final boolean renew) {
		this.renewProperty().set(renew);
	}
	

	public final SimpleStringProperty memTypeProperty() {
		return this.memType;
	}
	

	public final String getMemType() {
		return this.memTypeProperty().get();
	}
	

	public final void setMemType(final String memType) {
		this.memTypeProperty().set(memType);
	}
	

	public final SimpleBooleanProperty selectedProperty() {
		return this.selected;
	}
	

	public final boolean isSelected() {
		return this.selectedProperty().get();
	}
	

	public final void setSelected(final boolean selected) {
		this.selectedProperty().set(selected);
	}

	public final SimpleBooleanProperty lateRenewProperty() {
		return this.lateRenew;
	}
	

	public final boolean isLateRenew() {
		return this.lateRenewProperty().get();
	}
	

	public final void setIsLateRenew(final boolean lateRenew) {
		this.lateRenewProperty().set(lateRenew);
	}

	@Override
	public String toString() {
		return "Object_MembershipId{" +
				"mid=" + mId +
				", fiscal_Year=" + fiscalYear +
				", ms_id=" + msId +
				", membership_id=" + membershipId +
				", isRenew=" + renew +
				", mem_type=" + memType +
				", selected=" + selected +
				", isLateRenew=" + lateRenew +
				'}';
	}
}
