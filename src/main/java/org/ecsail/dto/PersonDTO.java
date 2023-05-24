package org.ecsail.dto;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonDTO {
	private IntegerProperty p_id;
	private IntegerProperty ms_id;
	private IntegerProperty memberType; // 1 == primary 2 == secondary 3 == dependant
	private StringProperty firstName;
	private StringProperty lastName;
	private StringProperty occupation;
	private StringProperty business;
	private StringProperty birthday;
	private BooleanProperty active;
	private StringProperty nickName;
	private IntegerProperty oldMsid;
	private ObservableList<PhoneDTO> phones = FXCollections.observableArrayList();
	private ObservableList<EmailDTO> email = FXCollections.observableArrayList();
	private ObservableList<AwardDTO> awards = FXCollections.observableArrayList();
	private ObservableList<OfficerDTO> officer = FXCollections.observableArrayList();

	public PersonDTO(Integer pid, Integer ms_id, Integer memberType, String firstName, String lastName,
                     String birthday, String occupation,
                     String business, Boolean isActive, String nickName, Integer oldMsid) {
		this.p_id = new SimpleIntegerProperty(pid);
		this.ms_id = new SimpleIntegerProperty(ms_id);
		this.memberType = new SimpleIntegerProperty(memberType);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.birthday = new SimpleStringProperty(birthday);
		this.occupation = new SimpleStringProperty(occupation);
		this.business = new SimpleStringProperty(business);
		this.active = new SimpleBooleanProperty(isActive);
		this.nickName = new SimpleStringProperty(nickName);
		this.oldMsid = new SimpleIntegerProperty(oldMsid);
	}


	public ObservableList<OfficerDTO> getOfficer() {
		return officer;
	}

	public void setOfficer(ObservableList<OfficerDTO> officer) {
		this.officer = officer;
	}

	public ObservableList<AwardDTO> getAwards() {
		return awards;
	}

	public void setAwards(ObservableList<AwardDTO> awards) {
		this.awards = awards;
	}

	public PersonDTO() { }// default constructor

	public ObservableList<PhoneDTO> getPhones() {
		return phones;
	}

	public void setPhones(ObservableList<PhoneDTO> phones) {
		this.phones = phones;
	}

	public ObservableList<EmailDTO> getEmail() {
		return email;
	}

	public void setEmail(ObservableList<EmailDTO> email) {
		this.email = email;
	}

	public String getFullName() {
		if(getFirstName() == null) setFirstName("First");
		if(getLastName() == null) setLastName("Last");
		return getFirstName() + " " + getLastName();
	}

	public String getNameWithInfo() {
		return getFullName() + " (p_id " + getP_id() + ")";
	}

	public final IntegerProperty oldMsidProperty( ) {
		return this.oldMsid;
	}

	public final int getOldMsid() {
		return this.oldMsidProperty().get();
	}

	public final void setOldMsid(final int oldMsid) {
		this.oldMsidProperty().set(oldMsid);
	}

	public final IntegerProperty p_idProperty() {
		return this.p_id;
	}

	public final int getP_id() {
		return this.p_idProperty().get();
	}

	public final void setP_id(final int p_id) {
		this.p_idProperty().set(p_id);
	}

	public final IntegerProperty ms_idProperty() {
		return this.ms_id;
	}

	public final int getMs_id() {
		return this.ms_idProperty().get();
	}

	public final void setMs_id(final int ms_id) {
		this.ms_idProperty().set(ms_id);
	}

	public final IntegerProperty memberTypeProperty() {
		return this.memberType;
	}

	public final int getMemberType() {
		return this.memberTypeProperty().get();
	}

	public final void setMemberType(final int memberType) {
		this.memberTypeProperty().set(memberType);
	}

	public final StringProperty firstNameProperty() {
		return this.firstName;
	}

	public final String getFirstName() {
		return this.firstNameProperty().get();
	}

	public final void setFirstName(final String firstName) {
		this.firstNameProperty().set(firstName);
	}

	public final StringProperty lastNameProperty() {
		return this.lastName;
	}

	public final String getLastName() {
		return this.lastNameProperty().get();
	}

	public final void setLastName(final String lastName) {
		this.lastNameProperty().set(lastName);
	}

	public final StringProperty occupationProperty() {
		return this.occupation;
	}

	public final String getOccupation() {
		return this.occupationProperty().get();
	}

	public final void setOccupation(final String occupation) {
		this.occupationProperty().set(occupation);
	}

	public final StringProperty businessProperty() {
		return this.business;
	}

	public final String getBusiness() {
		return this.businessProperty().get();
	}

	public final void setBusiness(final String business) {
		this.businessProperty().set(business);
	}

	public final StringProperty birthdayProperty() {
		return this.birthday;
	}

	public final String getBirthday() {
		return this.birthdayProperty().get();
	}

	public final void setBirthday(final String birthday) {
		this.birthdayProperty().set(birthday);
	}

	public final BooleanProperty activeProperty() {
		return this.active;
	}

	public final boolean isActive() {
		return this.activeProperty().get();
	}

	public final void setActive(final boolean active) {
		this.activeProperty().set(active);
	}

	public final StringProperty nickNameProperty() {
		return this.nickName;
	}

	public final String getNickName() {
		return this.nickNameProperty().get();
	}

	public final void setNickName(final String nickName) {
		this.nickNameProperty().set(nickName);
	}

}
