package org.ecsail.fx;

import javafx.beans.property.*;
import org.ecsail.annotation.ColumnName;
import org.ecsail.pojo.Boat;

public class BoatFx {
	@ColumnName(value = "BOAT_ID")
	protected IntegerProperty boatId;
	// this is here to carry value when passing object, not saved in db
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
	
	public BoatFx(Integer boatId, Integer msId, String manufacturer, String manufactureYear,
				  String registrationNum, String model, String boatName, String sailNumber,
				  Boolean hasTrailer, String loa, String displacement, String keel, String phrf,
				  String draft, String beam, String lwl, Boolean aux) {

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
	}

	public BoatFx(int msId) {
		this.boatId = new SimpleIntegerProperty(0);
		this.msId = new SimpleIntegerProperty(msId);
		this.manufacturer = new SimpleStringProperty("");
		this.manufactureYear = new SimpleStringProperty("");
		this.registrationNum = new SimpleStringProperty("");
		this.model = new SimpleStringProperty("");
		this.boatName = new SimpleStringProperty("");
		this.sailNumber = new SimpleStringProperty("");
		this.hasTrailer = new SimpleBooleanProperty(true);
		this.loa = new SimpleStringProperty("");
		this.displacement = new SimpleStringProperty("");
		this.keel = new SimpleStringProperty("");
		this.phrf = new SimpleStringProperty("");
		this.draft = new SimpleStringProperty("");
		this.beam = new SimpleStringProperty("");
		this.lwl = new SimpleStringProperty("");
		this.aux = new SimpleBooleanProperty(false);
	}

    public BoatFx(Boat boat) {
		this.boatId = new SimpleIntegerProperty(boat.getBoatId());
		this.msId = new SimpleIntegerProperty(0);
		this.manufacturer = new SimpleStringProperty(boat.getManufacturer());
		this.manufactureYear = new SimpleStringProperty(boat.getManufactureYear());
		this.registrationNum = new SimpleStringProperty(boat.getRegistrationNum());
		this.model = new SimpleStringProperty(boat.getModel());
		this.boatName = new SimpleStringProperty(boat.getBoatName());
		this.sailNumber = new SimpleStringProperty(boat.getSailNumber());
		this.hasTrailer = new SimpleBooleanProperty(boat.isHasTrailer());
		this.loa = new SimpleStringProperty(boat.getLength());
		this.displacement = new SimpleStringProperty(boat.getWeight());
		this.keel = new SimpleStringProperty(boat.getKeel());
		this.phrf = new SimpleStringProperty(boat.getPhrf());
		this.draft = new SimpleStringProperty(boat.getDraft());
		this.beam = new SimpleStringProperty(boat.getBeam());
		this.lwl = new SimpleStringProperty(boat.getLwl());
		this.aux = new SimpleBooleanProperty(false);
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

	public boolean isHasTrailer() {
		return hasTrailer.get();
	}

	public BooleanProperty hasTrailerProperty() {
		return hasTrailer;
	}

	public void setHasTrailer(boolean hasTrailer) {
		this.hasTrailer.set(hasTrailer);
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
}
