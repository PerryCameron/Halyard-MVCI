package org.ecsail.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.pojo.Person;

import java.time.LocalDate;

public class PersonFx {
	private IntegerProperty pId;
	private IntegerProperty msId;
	private IntegerProperty memberType; // 1 == primary 2 == secondary 3 == dependant
	private StringProperty firstName;
	private StringProperty lastName;
	private StringProperty occupation;
	private StringProperty business;
	private ObjectProperty<LocalDate> birthday;
	private BooleanProperty active;
	private StringProperty nickName;
	private IntegerProperty oldMsid;
	private ObservableList<PhoneFx> phones = FXCollections.observableArrayList();
	private ObservableList<EmailDTOFx> email = FXCollections.observableArrayList();
	private ObservableList<AwardDTOFx> awards = FXCollections.observableArrayList();
	private ObservableList<OfficerFx> officer = FXCollections.observableArrayList();

	public PersonFx(Integer pid, Integer ms_id, Integer memberType, String firstName, String lastName,
					LocalDate birthday, String occupation,
					String business, Boolean isActive, String nickName, Integer oldMsid) {
		this.pId = new SimpleIntegerProperty(pid);
		this.msId = new SimpleIntegerProperty(ms_id);
		this.memberType = new SimpleIntegerProperty(memberType);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.birthday = new SimpleObjectProperty<>(birthday);
		this.occupation = new SimpleStringProperty(occupation);
		this.business = new SimpleStringProperty(business);
		this.active = new SimpleBooleanProperty(isActive);
		this.nickName = new SimpleStringProperty(nickName);
		this.oldMsid = new SimpleIntegerProperty(oldMsid);
	}

	public PersonFx(Integer msId, Integer memberType, Boolean isActive) {
		this.pId = new SimpleIntegerProperty(0);
		this.msId = new SimpleIntegerProperty(msId);
		this.memberType = new SimpleIntegerProperty(memberType);
		this.firstName = new SimpleStringProperty("");
		this.lastName = new SimpleStringProperty("");
		this.birthday = new SimpleObjectProperty<>(null);
		this.occupation = new SimpleStringProperty("");
		this.business = new SimpleStringProperty("");
		this.active = new SimpleBooleanProperty(isActive);
		this.nickName = new SimpleStringProperty("");
		this.oldMsid = new SimpleIntegerProperty(0);
	}

	public PersonFx() {
		this.pId = new SimpleIntegerProperty(0);
		this.msId = new SimpleIntegerProperty(0);
		this.memberType = new SimpleIntegerProperty(0);
		this.firstName = new SimpleStringProperty("");
		this.lastName = new SimpleStringProperty("");
		this.birthday = new SimpleObjectProperty<>(null);
		this.occupation = new SimpleStringProperty("");
		this.business = new SimpleStringProperty("");
		this.active = new SimpleBooleanProperty(true);
		this.nickName = new SimpleStringProperty("");
		this.oldMsid = new SimpleIntegerProperty(0);
	}

	public PersonFx(int msId) {
		this.pId = new SimpleIntegerProperty(0);
		this.msId = new SimpleIntegerProperty(msId);
		this.memberType = new SimpleIntegerProperty(0);
		this.firstName = new SimpleStringProperty("");
		this.lastName = new SimpleStringProperty("");
		this.birthday = new SimpleObjectProperty<>(null);
		this.occupation = new SimpleStringProperty("");
		this.business = new SimpleStringProperty("");
		this.active = new SimpleBooleanProperty(true);
		this.nickName = new SimpleStringProperty("");
		this.oldMsid = new SimpleIntegerProperty(0);
	}

	public PersonFx(PersonFx p) { // for cloning
		this.pId = new SimpleIntegerProperty(p.getpId());
		this.msId = new SimpleIntegerProperty(p.getMsId());
		this.memberType = new SimpleIntegerProperty(p.getMemberType());
		this.firstName = new SimpleStringProperty(p.getFirstName());
		this.lastName = new SimpleStringProperty(p.getLastName());
		this.birthday = new SimpleObjectProperty<>(p.getBirthday());
		this.occupation = new SimpleStringProperty(p.getOccupation());
		this.business = new SimpleStringProperty(p.getBusiness());
		this.active = new SimpleBooleanProperty(p.isActive());
		this.nickName = new SimpleStringProperty(p.getNickName());
		this.oldMsid = new SimpleIntegerProperty(p.getOldMsid());
	}

	public PersonFx(Person p) { // for cloning
		this.pId = new SimpleIntegerProperty(p.getpId());
		this.msId = new SimpleIntegerProperty(p.getMsId());
		this.memberType = new SimpleIntegerProperty(p.getMemberType());
		this.firstName = new SimpleStringProperty(p.getFirstName());
		this.lastName = new SimpleStringProperty(p.getLastName());
		this.birthday = new SimpleObjectProperty<>(p.getBirthday() != null ? LocalDate.parse(p.getBirthday()) : null);
		this.occupation = new SimpleStringProperty(p.getOccupation());
		this.business = new SimpleStringProperty(p.getBusiness());
		this.active = new SimpleBooleanProperty(p.getActive() == 1);
		this.nickName = new SimpleStringProperty(p.getNickName());
		this.oldMsid = new SimpleIntegerProperty(p.getOldMsid());
	}




	public ObservableList<OfficerFx> getOfficers() {
		return officer;
	}

	public void setOfficer(ObservableList<OfficerFx> officer) {
		this.officer = officer;
	}

	public ObservableList<AwardDTOFx> getAwards() {
		return awards;
	}

	public void setAwards(ObservableList<AwardDTOFx> awards) {
		this.awards = awards;
	}

	public ObservableList<PhoneFx> getPhones() {
		return phones;
	}

	public void setPhones(ObservableList<PhoneFx> phones) {
		this.phones = phones;
	}

	public ObservableList<EmailDTOFx> getEmail() {
		return email;
	}

	public void setEmail(ObservableList<EmailDTOFx> email) {
		this.email = email;
	}

	public String getFullName() {
		if(getFirstName() == null) setFirstName("First");
		if(getLastName() == null) setLastName("Last");
		return getFirstName() + " " + getLastName();
	}

	public String getNameWithInfo() {
		return getFullName() + " (p_id " + getpId() + ")";
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

	public final IntegerProperty pIdProperty() {
		return this.pId;
	}

	public final int getpId() {
		return this.pIdProperty().get();
	}

	public final void setpId(final int pId) {
		this.pIdProperty().set(pId);
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

	public LocalDate getBirthday() {
		return birthday.get();
	}

	public ObjectProperty<LocalDate> birthdayProperty() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday.set(birthday);
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

	@Override
	public String toString() {
		return super.toString() + " PersonDTO {" +
				"pId=" + pId +
				", msId=" + msId +
				", memberType=" + memberType +
				", firstName=" + firstName +
				", lastName=" + lastName +
				", occupation=" + occupation +
				", business=" + business +
				", birthday=" + birthday +
				", active=" + active +
				", nickName=" + nickName +
				", oldMsid=" + oldMsid +
				", phones=" + phones +
				", email=" + email +
				", awards=" + awards +
				", officer=" + officer +
				'}';
	}
}
