package org.ecsail.dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Memo2DTO {

	private SimpleStringProperty membershipId;
	private IntegerProperty memo_id;
	private IntegerProperty msid;
	private StringProperty memo_date;
	private StringProperty memo;
	private IntegerProperty invoice_id;
	private StringProperty category;

	public Memo2DTO(String membershipId, Integer memo_id, Integer msid, String memo_date,
                    String memo, Integer invoice_id, String category) {
		super();
		this.membershipId = new SimpleStringProperty(membershipId);
		this.memo_id = new SimpleIntegerProperty(memo_id);
		this.msid = new SimpleIntegerProperty(msid);
		this.memo_date = new SimpleStringProperty(memo_date);
		this.memo = new SimpleStringProperty(memo);
		this.invoice_id = new SimpleIntegerProperty(invoice_id);
		this.category = new SimpleStringProperty(category);
	}

	public final IntegerProperty memo_idProperty() {
		return this.memo_id;
	}


	public final int getMemo_id() {
		return this.memo_idProperty().get();
	}


	public final void setMemo_id(final int memo_id) {
		this.memo_idProperty().set(memo_id);
	}


	public final IntegerProperty msidProperty() {
		return this.msid;
	}


	public final int getMsid() {
		return this.msidProperty().get();
	}


	public final void setMsid(final int msid) {
		this.msidProperty().set(msid);
	}


	public final StringProperty memo_dateProperty() {
		return this.memo_date;
	}


	public final String getMemo_date() {
		return this.memo_dateProperty().get();
	}


	public final void setMemo_date(final String memo_date) {
		this.memo_dateProperty().set(memo_date);
	}


	public final StringProperty memoProperty() {
		return this.memo;
	}


	public final String getMemo() {
		return this.memoProperty().get();
	}


	public final void setMemo(final String memo) {
		this.memoProperty().set(memo);
	}


	public final IntegerProperty invoice_idProperty() {
		return this.invoice_id;
	}


	public final int getInvoice_id() {
		return this.invoice_idProperty().get();
	}


	public final void setInvoice_id(final int invoice_id) {
		this.invoice_idProperty().set(invoice_id);
	}


	public final StringProperty categoryProperty() {
		return this.category;
	}


	public final String getCategory() {
		return this.categoryProperty().get();
	}


	public final void setCategory(final String category) {
		this.categoryProperty().set(category);
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
}
