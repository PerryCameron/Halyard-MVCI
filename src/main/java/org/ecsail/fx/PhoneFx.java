package org.ecsail.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import org.ecsail.pojo.Phone;

public class PhoneFx {
	
	private IntegerProperty phoneId;
	private IntegerProperty pId;
	private BooleanProperty phoneListed;
	private StringProperty phone;
	private StringProperty phoneType;
	private ObservableList<String> membershipTypes;

	private ComboBox<String> combo_box;
	public PhoneFx(Integer phoneId, Integer pId, Boolean phoneListed, String phone, String phoneType) {
		this.phoneId = new SimpleIntegerProperty(phoneId);
		this.pId = new SimpleIntegerProperty(pId);
		this.phoneListed = new SimpleBooleanProperty(phoneListed);
		this.phone = new SimpleStringProperty(phone);
		this.phoneType = new SimpleStringProperty(phoneType);
		this.membershipTypes = FXCollections.observableArrayList("Home", "Cell", "Work", "Emergency");
		this.combo_box = new ComboBox<String>(membershipTypes);
		combo_box.setValue("none");
	}

	public PhoneFx(Integer pId) {
		this.phoneId = new SimpleIntegerProperty(0);
		this.pId = new SimpleIntegerProperty(pId);
		this.phoneListed = new SimpleBooleanProperty(true);
		this.phone = new SimpleStringProperty("");
		this.phoneType = new SimpleStringProperty("");
	}

	public PhoneFx(Phone phone) {
		this.phoneId = new SimpleIntegerProperty(phone.getPhoneId());
		this.pId = new SimpleIntegerProperty(phone.getpId());
		this.phoneListed = new SimpleBooleanProperty(phone.isPhoneListed());
		this.phone = new SimpleStringProperty(phone.getPhone());
		this.phoneType = new SimpleStringProperty(phone.getPhoneType());
	}


	public ComboBox<String> getCombo_box() {
		return combo_box;
	}

	public void setCombo_box(ComboBox<String> combo_box) {
		this.combo_box = combo_box;
	}
	
	public final IntegerProperty phoneIdProperty() {
		return this.phoneId;
	}

	public final int getPhoneId() {
		return this.phoneIdProperty().get();
	}

	public final void setPhoneId(final int phoneId) {
		this.phoneIdProperty().set(phoneId);
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

	public final BooleanProperty phoneListedProperty() {
		return this.phoneListed;
	}
	

	public final boolean getPhoneListed() {
		return this.phoneListedProperty().get();
	}
	

	public final void setPhoneListed(final boolean phoneListed) {
		this.phoneListedProperty().set(phoneListed);
	}
	

	public final StringProperty phoneProperty() {
		return this.phone;
	}
	

	public final String getPhone() {
		return this.phoneProperty().get();
	}
	

	public final void setPhone(final String phone) {
		this.phoneProperty().set(phone);
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
				" phone_ID=" + phoneId;
	}
}
