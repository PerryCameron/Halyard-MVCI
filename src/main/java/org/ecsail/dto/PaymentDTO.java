package org.ecsail.dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PaymentDTO {
	private IntegerProperty payId;
	private IntegerProperty invoiceId;
	private StringProperty checkNumber;
	private StringProperty paymentType;
	private StringProperty paymentDate;
	private StringProperty PaymentAmount;
	private IntegerProperty depositId;
	
	public PaymentDTO(Integer payId, Integer invoiceId, String checkNumber, String paymentType, String paymentDate,
					  String paymentAmount, Integer depositId) {
		this.payId = new SimpleIntegerProperty(payId);
		this.invoiceId = new SimpleIntegerProperty(invoiceId);
		this.checkNumber = new SimpleStringProperty(checkNumber);
		this.paymentType = new SimpleStringProperty(paymentType);
		this.paymentDate = new SimpleStringProperty(paymentDate);
		this.PaymentAmount = new SimpleStringProperty(paymentAmount);
		this.depositId = new SimpleIntegerProperty(depositId);
	}

	public final IntegerProperty payIdProperty() {
		return this.payId;
	}
	

	public final int getPayId() {
		return this.payIdProperty().get();
	}
	

	public final void setPayId(final int payId) {
		this.payIdProperty().set(payId);
	}
	

	public final IntegerProperty invoiceIdProperty() {
		return this.invoiceId;
	}
	

	public final int getInvoiceId() {
		return this.invoiceIdProperty().get();
	}
	

	public final void setInvoiceId(final int invoiceId) {
		this.invoiceIdProperty().set(invoiceId);
	}
	

	public final StringProperty checkNumberProperty() {
		return this.checkNumber;
	}
	

	public final String getCheckNumber() {
		return this.checkNumberProperty().get();
	}
	

	public final void setCheckNumber(final String checkNumber) {
		this.checkNumberProperty().set(checkNumber);
	}
	

	public final StringProperty paymentTypeProperty() {
		return this.paymentType;
	}
	

	public final String getPaymentType() {
		return this.paymentTypeProperty().get();
	}
	

	public final void setPaymentType(final String paymentType) {
		this.paymentTypeProperty().set(paymentType);
	}
	

	public final StringProperty paymentDateProperty() {
		return this.paymentDate;
	}
	

	public final String getPaymentDate() {
		return this.paymentDateProperty().get();
	}
	

	public final void setPaymentDate(final String paymentDate) {
		this.paymentDateProperty().set(paymentDate);
	}
	

	public final StringProperty PaymentAmountProperty() {
		return this.PaymentAmount;
	}
	

	public final String getPaymentAmount() {
		return this.PaymentAmountProperty().get();
	}
	

	public final void setPaymentAmount(final String PaymentAmount) {
		this.PaymentAmountProperty().set(PaymentAmount);
	}


	public final IntegerProperty depositIdProperty() {
		return this.depositId;
	}
	

	public final int getDepositId() {
		return this.depositIdProperty().get();
	}
	

	public final void setDepositId(final int depositId) {
		this.depositIdProperty().set(depositId);
	}

	@Override
	public String toString() {
		return "Object_Payment [pay_id=" + payId + ", invoice_id=" + invoiceId + ", checkNumber=" + checkNumber
				+ ", paymentType=" + paymentType + ", paymentDate=" + paymentDate + ", PaymentAmount=" + PaymentAmount
				+ ", deposit_id=" + depositId + "]";
	}
}
