package org.ecsail.dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MembershipIdDTO {
	private IntegerProperty mId;
	private IntegerProperty fiscalYear;
	private IntegerProperty msId;
	private IntegerProperty membershipId;
	private SimpleBooleanProperty renew;
	private SimpleStringProperty memType;
	private SimpleBooleanProperty selected;
	private SimpleBooleanProperty lateRenew;
	
	public MembershipIdDTO(Integer mId, int fiscalYear, Integer msId, int membershipId,
						   Boolean renew, String memType, Boolean selected, Boolean lateRenew) {
		this.mId = new SimpleIntegerProperty(mId);
		this.fiscalYear = new SimpleIntegerProperty(fiscalYear);
		this.msId = new SimpleIntegerProperty(msId);
		this.membershipId = new SimpleIntegerProperty(membershipId);
		this.renew = new SimpleBooleanProperty(renew);
		this.memType = new SimpleStringProperty(memType);
		this.selected = new SimpleBooleanProperty(selected);
		this.lateRenew = new SimpleBooleanProperty(lateRenew);
	}

	public MembershipIdDTO(int fiscalYear, Integer msId, int membershipId, String memType) {
		this.mId = new SimpleIntegerProperty(0);
		this.fiscalYear = new SimpleIntegerProperty(fiscalYear);
		this.msId = new SimpleIntegerProperty(msId);
		this.membershipId = new SimpleIntegerProperty(membershipId);
		this.renew = new SimpleBooleanProperty(false);
		this.memType = new SimpleStringProperty(memType);
		this.selected = new SimpleBooleanProperty(false);
		this.lateRenew = new SimpleBooleanProperty(false);
	}

	public MembershipIdDTO() {
		this.mId = new SimpleIntegerProperty(0);
		this.fiscalYear = new SimpleIntegerProperty(0);
		this.msId = new SimpleIntegerProperty(0);
		this.membershipId = new SimpleIntegerProperty(0);
		this.renew = new SimpleBooleanProperty(false);
		this.memType = new SimpleStringProperty("");
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


	public int getFiscalYear() {
		return fiscalYear.get();
	}

	public IntegerProperty fiscalYearProperty() {
		return fiscalYear;
	}

	public void setFiscalYear(int fiscalYear) {
		this.fiscalYear.set(fiscalYear);
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


	public int getMembershipId() {
		return membershipId.get();
	}

	public IntegerProperty membershipIdProperty() {
		return membershipId;
	}

	public void setMembershipId(int membershipId) {
		this.membershipId.set(membershipId);
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
