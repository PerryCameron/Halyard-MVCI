package org.ecsail.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class MembershipListDTO {
	// Fields for deserialization (standard Java types)
	private int msId;
	private int pId;
	private int membershipId;
	private String joinDate; // TODO: Change to LocalDate later
	private String memType;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String lastName;
	private String firstName;
	private String slip;
	private int subLeaser;
	private int selectedYear;

	// JavaFX properties for UI binding
	private final IntegerProperty msIdProperty = new SimpleIntegerProperty();
	private final IntegerProperty pIdProperty = new SimpleIntegerProperty();
	private final IntegerProperty membershipIdProperty = new SimpleIntegerProperty();
	private final StringProperty joinDateProperty = new SimpleStringProperty();
	private final StringProperty memTypeProperty = new SimpleStringProperty();
	private final StringProperty addressProperty = new SimpleStringProperty();
	private final StringProperty cityProperty = new SimpleStringProperty();
	private final StringProperty stateProperty = new SimpleStringProperty();
	private final StringProperty zipProperty = new SimpleStringProperty();
	private final StringProperty lastNameProperty = new SimpleStringProperty();
	private final StringProperty firstNameProperty = new SimpleStringProperty();
	private final StringProperty slipProperty = new SimpleStringProperty();
	private final IntegerProperty subLeaserProperty = new SimpleIntegerProperty();
	private final IntegerProperty selectedYearProperty = new SimpleIntegerProperty();
	private final ObservableList<BoatFx> boatDTOS = FXCollections.observableArrayList();
	private final ObservableList<NoteFx> notesDTOS = FXCollections.observableArrayList();
	private final ObservableList<MembershipIdFx> membershipIdDTOS = FXCollections.observableArrayList();
	private final ObservableList<InvoiceFx> invoiceDTOS = FXCollections.observableArrayList();

	public MembershipListDTO() {}

	// Getters and setters for standard fields (used by Jackson)
	public int getMsId() {
		return msId;
	}

	public void setMsId(int msId) {
		this.msId = msId;
		this.msIdProperty.set(msId);
	}

	public int getPId() {
		return pId;
	}

	public void setPId(int pId) {
		this.pId = pId;
		this.pIdProperty.set(pId);
	}

	public int getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(int membershipId) {
		this.membershipId = membershipId;
		this.membershipIdProperty.set(membershipId);
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
		this.joinDateProperty.set(joinDate);
	}

	public String getMemType() {
		return memType;
	}

	public void setMemType(String memType) {
		this.memType = memType;
		this.memTypeProperty.set(memType);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
		this.addressProperty.set(address);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
		this.cityProperty.set(city);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		this.stateProperty.set(state);
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
		this.zipProperty.set(zip);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		this.lastNameProperty.set(lastName);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		this.firstNameProperty.set(firstName);
	}

	public String getSlip() {
		return slip;
	}

	public void setSlip(String slip) {
		this.slip = slip;
		this.slipProperty.set(slip);
	}

	public int getSubLeaser() {
		return subLeaser;
	}

	public void setSubLeaser(int subLeaser) {
		this.subLeaser = subLeaser;
		this.subLeaserProperty.set(subLeaser);
	}

	public int getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(int selectedYear) {
		this.selectedYear = selectedYear;
		this.selectedYearProperty.set(selectedYear);
	}

	public List<BoatFx> getBoatDTOs() {
		return new ArrayList<>(boatDTOS);
	}

	public void setBoatDTOs(List<BoatFx> boatDTOs) {
		this.boatDTOS.setAll(boatDTOs != null ? boatDTOs : new ArrayList<>());
	}

	public List<NoteFx> getNotesDTOs() {
		return new ArrayList<>(notesDTOS);
	}

	public void setNotesDTOs(List<NoteFx> notesDTOs) {
		this.notesDTOS.setAll(notesDTOs != null ? notesDTOs : new ArrayList<>());
	}

	public List<MembershipIdFx> getMembershipIdDTOs() {
		return new ArrayList<>(membershipIdDTOS);
	}

	public void setMembershipIdDTOs(List<MembershipIdFx> membershipIdDTOs) {
		this.membershipIdDTOS.setAll(membershipIdDTOs != null ? membershipIdDTOs : new ArrayList<>());
	}

	public List<InvoiceFx> getInvoiceDTOs() {
		return new ArrayList<>(invoiceDTOS);
	}

	public void setInvoiceDTOs(List<InvoiceFx> invoiceDTOs) {
		this.invoiceDTOS.setAll(invoiceDTOs != null ? invoiceDTOs : new ArrayList<>());
	}

	// Getters for JavaFX properties (used by UI)
	public IntegerProperty msIdProperty() {
		return msIdProperty;
	}

	public IntegerProperty pIdProperty() {
		return pIdProperty;
	}

	public IntegerProperty membershipIdProperty() {
		return membershipIdProperty;
	}

	public StringProperty joinDateProperty() {
		return joinDateProperty;
	}

	public StringProperty memTypeProperty() {
		return memTypeProperty;
	}

	public StringProperty addressProperty() {
		return addressProperty;
	}

	public StringProperty cityProperty() {
		return cityProperty;
	}

	public StringProperty stateProperty() {
		return stateProperty;
	}

	public StringProperty zipProperty() {
		return zipProperty;
	}

	public StringProperty lastNameProperty() {
		return lastNameProperty;
	}

	public StringProperty firstNameProperty() {
		return firstNameProperty;
	}

	public StringProperty slipProperty() {
		return slipProperty;
	}

	public IntegerProperty subLeaserProperty() {
		return subLeaserProperty;
	}

	public IntegerProperty selectedYearProperty() {
		return selectedYearProperty;
	}

	public ObservableList<BoatFx> getBoatDTOS() {
		return boatDTOS;
	}

	public ObservableList<NoteFx> getNotesDTOS() {
		return notesDTOS;
	}

	public ObservableList<MembershipIdFx> getMembershipIdDTOS() {
		return membershipIdDTOS;
	}

	public ObservableList<InvoiceFx> getInvoiceDTOS() {
		return invoiceDTOS;
	}
}