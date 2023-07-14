package org.ecsail.dto;

import javafx.beans.property.*;
import org.ecsail.annotation.ColumnName;

public class BoatListDTO {

	@ColumnName(value = "BOAT_ID")
	protected IntegerProperty boatId;
	protected IntegerProperty msId;
	@ColumnName(value = "MANUFACTURER")
	protected StringProperty manufacturer;
	@ColumnName(value = "MANUFACTURE_YEAR")
	protected StringProperty manufactureYear;
	@ColumnName(value = "REGISTRATION_NUM")
	protected StringProperty registrationNum;
	@ColumnName(value = "MODEL")
	protected StringProperty model;
	@ColumnName(value = "BOAT_NAME")
	protected StringProperty boatName;
	@ColumnName(value = "SAIL_NUMBER")
	protected StringProperty sailNumber;
	@ColumnName(value = "HAS_TRAILER")
	protected BooleanProperty hasTrailer;
	@ColumnName(value = "LENGTH")
	protected StringProperty loa;
	@ColumnName(value = "WEIGHT")
	protected StringProperty displacement;
	@ColumnName(value = "KEEL")
	protected StringProperty keel;
	@ColumnName(value = "PHRF")
	protected StringProperty phrf;
	@ColumnName(value = "DRAFT")
	protected StringProperty draft;
	@ColumnName(value = "BEAM")
	protected StringProperty beam;
	@ColumnName(value = "LWL")
	protected StringProperty lwl;
	@ColumnName(value = "AUX")
	protected BooleanProperty aux;

	protected IntegerProperty membershipId;
	protected StringProperty lName;
	protected StringProperty fName;
	protected IntegerProperty numberOfImages;
	
	public BoatListDTO(Integer boatId, Integer msId, String manufacturer, String manufactureYear,
                       String registrationNum, String model, String boatName, String sailNumber,
                       Boolean hasTrailer, String loa, String displacement, String keel, String phrf,
                       String draft, String beam, String lwl, Boolean aux, Integer membershipId,
                       String lName, String fName, Integer numberOfImages) {
		this.boatId = new SimpleIntegerProperty(boatId);
		this.msId = new SimpleIntegerProperty(msId);
		this.manufacturer = new SimpleStringProperty(manufacturer);
		this.manufactureYear = new SimpleStringProperty(manufactureYear);
		this.registrationNum = new SimpleStringProperty(registrationNum);
		this.model = new SimpleStringProperty(model);
		this.boatName = new SimpleStringProperty(boatName);
		this.sailNumber = new SimpleStringProperty(sailNumber);
		this.hasTrailer = new SimpleBooleanProperty(hasTrailer);
		this.loa = new SimpleStringProperty(loa);
		this.displacement = new SimpleStringProperty(displacement);
		this.keel = new SimpleStringProperty(keel);
		this.phrf = new SimpleStringProperty(phrf);
		this.draft = new SimpleStringProperty(draft);
		this.beam = new SimpleStringProperty(beam);
		this.lwl = new SimpleStringProperty(lwl);
		this.aux = new SimpleBooleanProperty(aux);
		this.membershipId = new SimpleIntegerProperty(membershipId);
		this.lName = new SimpleStringProperty(lName);
		this.fName = new SimpleStringProperty(fName);
		this.numberOfImages = new SimpleIntegerProperty(numberOfImages);
	}

	public int getBoatId() {
		return boatId.get();
	}

	public IntegerProperty boatIdProperty() {
		return boatId;
	}

	public void setBoatId(int boatId) {
		this.boatId.set(boatId);
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

	public String getManufacturer() {
		return manufacturer.get();
	}

	public StringProperty manufacturerProperty() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer.set(manufacturer);
	}

	public String getManufactureYear() {
		return manufactureYear.get();
	}

	public StringProperty manufactureYearProperty() {
		return manufactureYear;
	}

	public void setManufactureYear(String manufactureYear) {
		this.manufactureYear.set(manufactureYear);
	}

	public String getRegistrationNum() {
		return registrationNum.get();
	}

	public StringProperty registrationNumProperty() {
		return registrationNum;
	}

	public void setRegistrationNum(String registrationNum) {
		this.registrationNum.set(registrationNum);
	}

	public String getModel() {
		return model.get();
	}

	public StringProperty modelProperty() {
		return model;
	}

	public void setModel(String model) {
		this.model.set(model);
	}

	public String getBoatName() {
		return boatName.get();
	}

	public StringProperty boatNameProperty() {
		return boatName;
	}

	public void setBoatName(String boatName) {
		this.boatName.set(boatName);
	}

	public String getSailNumber() {
		return sailNumber.get();
	}

	public StringProperty sailNumberProperty() {
		return sailNumber;
	}

	public void setSailNumber(String sailNumber) {
		this.sailNumber.set(sailNumber);
	}

	public boolean hasTrailer() {
		return hasTrailer.get();
	}

	public BooleanProperty hasTrailerProperty() {
		return hasTrailer;
	}

	public void setHasTrailer(boolean hasTrailer) {
		this.hasTrailer.set(hasTrailer);
	}
	public boolean isHasTrailer() {
		return hasTrailer.get();
	}

	public String getLoa() {
		return loa.get();
	}

	public StringProperty loaProperty() {
		return loa;
	}

	public void setLoa(String loa) {
		this.loa.set(loa);
	}

	public String getDisplacement() {
		return displacement.get();
	}

	public StringProperty displacementProperty() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement.set(displacement);
	}

	public String getKeel() {
		return keel.get();
	}

	public StringProperty keelProperty() {
		return keel;
	}

	public void setKeel(String keel) {
		this.keel.set(keel);
	}

	public String getPhrf() {
		return phrf.get();
	}

	public StringProperty phrfProperty() {
		return phrf;
	}

	public void setPhrf(String phrf) {
		this.phrf.set(phrf);
	}

	public String getDraft() {
		return draft.get();
	}

	public StringProperty draftProperty() {
		return draft;
	}

	public void setDraft(String draft) {
		this.draft.set(draft);
	}

	public String getBeam() {
		return beam.get();
	}

	public StringProperty beamProperty() {
		return beam;
	}

	public void setBeam(String beam) {
		this.beam.set(beam);
	}

	public String getLwl() {
		return lwl.get();
	}

	public StringProperty lwlProperty() {
		return lwl;
	}

	public void setLwl(String lwl) {
		this.lwl.set(lwl);
	}

	public boolean isAux() {
		return aux.get();
	}

	public BooleanProperty auxProperty() {
		return aux;
	}

	public void setAux(boolean aux) {
		this.aux.set(aux);
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

	public int getNumberOfImages() {
		return numberOfImages.get();
	}

	public IntegerProperty numberOfImagesProperty() {
		return numberOfImages;
	}

	public void setNumberOfImages(int numberOfImages) {
		this.numberOfImages.set(numberOfImages);
	}

	@Override
	public String toString() {
		return "BoatListDTO{" +
				"boatId=" + boatId +
				", msId=" + msId +
				", manufacturer=" + manufacturer +
				", manufactureYear=" + manufactureYear +
				", registrationNum=" + registrationNum +
				", model=" + model +
				", boatName=" + boatName +
				", sailNumber=" + sailNumber +
				", hasTrailer=" + hasTrailer +
				", loa=" + loa +
				", displacement=" + displacement +
				", keel=" + keel +
				", phrf=" + phrf +
				", draft=" + draft +
				", beam=" + beam +
				", lwl=" + lwl +
				", aux=" + aux +
				", membershipId=" + membershipId +
				", lName=" + lName +
				", fName=" + fName +
				", numberOfImages=" + numberOfImages +
				'}';
	}
}
