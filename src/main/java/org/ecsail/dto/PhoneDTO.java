package org.ecsail.dto;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class PhoneDTO {
	
	private IntegerProperty phone_Id;
	private IntegerProperty pid;
	private BooleanProperty isListed;
	private StringProperty phoneNumber;
	private StringProperty phoneType;
	private ObservableList<String> membershiptypes; 
	private ComboBox<String> combo_box;
	public PhoneDTO(Integer phone_Id, Integer pid, Boolean isListed, String phoneNumber, String phoneType) {
		this.phone_Id = new SimpleIntegerProperty(phone_Id);
		this.pid = new SimpleIntegerProperty(pid);
		this.isListed = new SimpleBooleanProperty(isListed);
		this.phoneNumber = new SimpleStringProperty(phoneNumber);
		this.phoneType = new SimpleStringProperty(phoneType);
		this.membershiptypes = FXCollections.observableArrayList("Home", "Cell", "Work", "Emergency");
		this.combo_box = new ComboBox<String>(membershiptypes);
		combo_box.setValue("none");
	}

	public PhoneDTO(Integer pid) {
		this.phone_Id = new SimpleIntegerProperty(0);
		this.pid = new SimpleIntegerProperty(pid);
		this.isListed = new SimpleBooleanProperty(true);
		this.phoneNumber = new SimpleStringProperty("");
		this.phoneType = new SimpleStringProperty("");
	}
	
	//final ComboBox<String> combo_box


	public ComboBox<String> getCombo_box() {
		return combo_box;
	}

	public void setCombo_box(ComboBox<String> combo_box) {
		this.combo_box = combo_box;
	}
	
	public final IntegerProperty phone_IdProperty() {
		return this.phone_Id;
	}

	public final int getPhone_Id() {
		return this.phone_IdProperty().get();
	}

	public final void setPhone_Id(final int phone_Id) {
		this.phone_IdProperty().set(phone_Id);
	}
	

	public final IntegerProperty pidProperty() {
		return this.pid;
	}
	

	public final int getPid() {
		return this.pidProperty().get();
	}
	

	public final void setPid(final int pid) {
		this.pidProperty().set(pid);
	}
	

	public final BooleanProperty isListedProperty() {
		return this.isListed;
	}
	

	public final boolean isIsListed() {
		return this.isListedProperty().get();
	}
	

	public final void setIsListed(final boolean isListed) {
		this.isListedProperty().set(isListed);
	}
	

	public final StringProperty phoneNumberProperty() {
		return this.phoneNumber;
	}
	

	public final String getPhoneNumber() {
		return this.phoneNumberProperty().get();
	}
	

	public final void setPhoneNumber(final String phoneNumber) {
		this.phoneNumberProperty().set(phoneNumber);
	}

	public final StringProperty phoneTypeProperty() {
		return this.phoneType;
	}
	

	public final String getPhoneType() {
		return this.phoneTypeProperty().get();
	}
	

	public final void setPhoneType(final String phoneType) {
		this.phoneTypeProperty().set(phoneType);
	}

	@Override
	public String toString() {
		return "PhoneDTO " +
				" phone_ID=" + phone_Id;
	}
}
