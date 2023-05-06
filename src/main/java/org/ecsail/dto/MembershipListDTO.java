package org.ecsail.dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MembershipListDTO {
	private IntegerProperty msId; /// unique auto key for Membership
	private IntegerProperty pId;  /// pid of Main Member
	private IntegerProperty membershipId;
	private StringProperty joinDate;
	private StringProperty memType;  // Type of Membership (Family, Regular, Lake Associate(race fellow), Social
	//private BooleanProperty activeMembership;  // Is the membership active?
	private StringProperty address;
	private StringProperty city;
	private StringProperty state;
	private StringProperty zip;
	private StringProperty lName;
	private StringProperty fName;
	private StringProperty slip;
	private IntegerProperty subLeaser;
	private IntegerProperty selectedYear;

	public MembershipListDTO(Integer msId, Integer pId, Integer membershipId, String joinDate, String memType, String address, String city,
							 String state, String zip, String lName, String fName, String slip, Integer subLeaser,
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
		this.lName = new SimpleStringProperty(lName);
		this.fName = new SimpleStringProperty(fName);
		this.slip = new SimpleStringProperty(slip);
		this.subLeaser = new SimpleIntegerProperty(subLeaser);
		this.selectedYear = new SimpleIntegerProperty(selectedYear);
	}

	public MembershipListDTO() {
		super();
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

	public int getpId() {
		return pId.get();
	}

	public IntegerProperty pIdProperty() {
		return pId;
	}

	public void setpId(int pId) {
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

	public String getlName() {
		return lName.get();
	}

	public StringProperty lNameProperty() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName.set(lName);
	}

	public String getfName() {
		return fName.get();
	}

	public StringProperty fNameProperty() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName.set(fName);
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
}
