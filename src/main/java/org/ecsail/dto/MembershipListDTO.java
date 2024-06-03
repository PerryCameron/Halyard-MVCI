package org.ecsail.dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MembershipListDTO {
	private IntegerProperty msId; /// unique auto key for Membership
	private IntegerProperty pId;  /// pid of Main Member
	private IntegerProperty membershipId;
	private StringProperty joinDate; // TODO change this to objectProperty<LocalDate>
	private StringProperty memType;  // Type of Membership (Family, Regular, Lake Associate(race fellow), Social
	private StringProperty address;
	private StringProperty city;
	private StringProperty state;
	private StringProperty zip;
	private StringProperty lastName;
	private StringProperty firstName;
	private StringProperty slip;
	private IntegerProperty subLeaser;
	private IntegerProperty selectedYear;
	private ObservableList<BoatDTO> boatDTOS = FXCollections.observableArrayList();
	private ObservableList<NotesDTO> notesDTOS = FXCollections.observableArrayList();
	private ObservableList<MembershipIdDTO> membershipIdDTOS = FXCollections.observableArrayList();
	private ObservableList<InvoiceDTO> invoiceDTOS = FXCollections.observableArrayList();

	public MembershipListDTO(Integer msId, Integer pId, Integer membershipId, String joinDate, String memType, String address, String city,
							 String state, String zip, String lastName, String firstName, String slip, Integer subLeaser,
							 Integer selectedYear) {
		this.msId = new SimpleIntegerProperty(msId);
		this.pId = new SimpleIntegerProperty(pId);
		this.membershipId = new SimpleIntegerProperty(membershipId);
		this.joinDate = new SimpleStringProperty(joinDate);
		this.memType = new SimpleStringProperty(memType);
		this.address = new SimpleStringProperty(address);
		this.city = new SimpleStringProperty(city);
		this.state = new SimpleStringProperty(state);
		this.zip = new SimpleStringProperty(zip);
		this.lastName = new SimpleStringProperty(lastName);
		this.firstName = new SimpleStringProperty(firstName);
		this.slip = new SimpleStringProperty(slip);
		this.subLeaser = new SimpleIntegerProperty(subLeaser);
		this.selectedYear = new SimpleIntegerProperty(selectedYear);
	}

	public MembershipListDTO(String lastName, String firstName) {
		this.msId = new SimpleIntegerProperty(0);
		this.pId = new SimpleIntegerProperty(0);
		this.membershipId = new SimpleIntegerProperty(0);
		this.joinDate = new SimpleStringProperty("");
		this.memType = new SimpleStringProperty("");
		this.address = new SimpleStringProperty("");
		this.city = new SimpleStringProperty("");
		this.state = new SimpleStringProperty("");
		this.zip = new SimpleStringProperty("");
		this.lastName = new SimpleStringProperty(lastName);
		this.firstName = new SimpleStringProperty(firstName);
		this.slip = new SimpleStringProperty("");
		this.subLeaser = new SimpleIntegerProperty(0);
		this.selectedYear = new SimpleIntegerProperty(0);
	}

	public MembershipListDTO(String joinDate, String memType, int selectedYear) {
		this.joinDate = new SimpleStringProperty(joinDate);
		this.memType = new SimpleStringProperty(memType);
		this.lastName = new SimpleStringProperty("");
		this.firstName = new SimpleStringProperty("");
		this.slip = new SimpleStringProperty("");
		this.subLeaser = new SimpleIntegerProperty(0);
		this.membershipId = new SimpleIntegerProperty(0);
		this.selectedYear = new SimpleIntegerProperty(selectedYear);
	}

	public MembershipListDTO() {
		this.msId = new SimpleIntegerProperty(0);
		this.pId = new SimpleIntegerProperty(0);
		this.membershipId = new SimpleIntegerProperty(0);
		this.joinDate = new SimpleStringProperty("");
		this.memType = new SimpleStringProperty("");
		this.address = new SimpleStringProperty("");
		this.city = new SimpleStringProperty("");
		this.state = new SimpleStringProperty("");
		this.zip = new SimpleStringProperty("");
		this.lastName = new SimpleStringProperty("");
		this.firstName = new SimpleStringProperty("");
		this.slip = new SimpleStringProperty("");
		this.subLeaser = new SimpleIntegerProperty(0);
		this.selectedYear = new SimpleIntegerProperty(0);
	}

	public ObservableList<MembershipIdDTO> getMembershipIdDTOS() {
		return membershipIdDTOS;
	}

	public void setMembershipIdDTOS(ObservableList<MembershipIdDTO> membershipIdDTOS) {
		this.membershipIdDTOS = membershipIdDTOS;
	}

	public ObservableList<NotesDTO> getNotesDTOS() {
		return notesDTOS;
	}

	public void setNotesDTOS(ObservableList<NotesDTO> notesDTOS) {
		this.notesDTOS = notesDTOS;
	}

	public ObservableList<BoatDTO> getBoatDTOS() {
		return boatDTOS;
	}

	public void setBoatDTOS(ObservableList<BoatDTO> boatDTOS) {
		this.boatDTOS = boatDTOS;
	}

	public int getMsId() {
		return msId.get();
	}

	public IntegerProperty msIdProperty() {
		return msId;
	}

	public void setMsId(int msId) {
		this.msId.set(msId);
	}

	public int getPId() {
		return pId.get();
	}

	public IntegerProperty pIdProperty() {
		return pId;
	}

	public void setPId(int pId) {
		this.pId.set(pId);
	}



	public String getJoinDate() {
		return joinDate.get();
	}

	public StringProperty joinDateProperty() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate.set(joinDate);
	}

	public String getMemType() {
		return memType.get();
	}

	public StringProperty memTypeProperty() {
		return memType;
	}

	public void setMemType(String memType) {
		this.memType.set(memType);
	}

	public String getAddress() {
		return address.get();
	}

	public StringProperty addressProperty() {
		return address;
	}

	public void setAddress(String address) {
		this.address.set(address);
	}

	public String getCity() {
		return city.get();
	}

	public StringProperty cityProperty() {
		return city;
	}

	public void setCity(String city) {
		this.city.set(city);
	}

	public String getState() {
		return state.get();
	}

	public StringProperty stateProperty() {
		return state;
	}

	public void setState(String state) {
		this.state.set(state);
	}

	public String getZip() {
		return zip.get();
	}

	public StringProperty zipProperty() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip.set(zip);
	}

	public String getLastName() {
		return lastName.get();
	}

	public StringProperty lastNameProperty() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}

	public String getFirstName() {
		return firstName.get();
	}

	public StringProperty firstNameProperty() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}

	public String getSlip() {
		return slip.get();
	}

	public StringProperty slipProperty() {
		return slip;
	}

	public void setSlip(String slip) {
		this.slip.set(slip);
	}

	public int getSubLeaser() {
		return subLeaser.get();
	}

	public IntegerProperty subLeaserProperty() {
		return subLeaser;
	}

	public void setSubLeaser(int subLeaser) {
		this.subLeaser.set(subLeaser);
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

	public int getSelectedYear() {
		return selectedYear.get();
	}

	public IntegerProperty selectedYearProperty() {
		return selectedYear;
	}

	public void setSelectedYear(int selectedYear) {
		this.selectedYear.set(selectedYear);
	}

	public ObservableList<InvoiceDTO> getInvoiceDTOS() {
		return invoiceDTOS;
	}

	public void setInvoiceDTOS(ObservableList<InvoiceDTO> invoiceDTOS) {
		this.invoiceDTOS = invoiceDTOS;
	}

	@Override
	public String toString() {
		return "MembershipListDTO{" +
				"msId=" + msId +
				", pId=" + pId +
				", membershipId=" + membershipId +
				", joinDate=" + joinDate +
				", memType=" + memType +
				", address=" + address +
				", city=" + city +
				", state=" + state +
				", zip=" + zip +
				", lastName=" + lastName +
				", firstName=" + firstName +
				", slip=" + slip +
				", subLeaser=" + subLeaser +
				", selectedYear=" + selectedYear +
				", boatDTOS=" + boatDTOS +
				", notesDTOS=" + notesDTOS +
				'}';
	}
}
