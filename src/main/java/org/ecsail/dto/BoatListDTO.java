package org.ecsail.dto;

import javafx.beans.property.*;
import org.ecsail.annotation.ColumnName;

public class BoatListDTO {

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

	protected IntegerProperty membershipId;
	protected StringProperty lName;
	protected StringProperty fName;
	protected IntegerProperty numberOfImages;
	
	public BoatListDTO(Integer boat_id, Integer ms_id, String manufacturer, String manufacture_year,
                       String registration_num, String model, String boat_name, String sail_number,
                       Boolean hasTrailer, String length, String weight, String keel, String phrf,
                       String draft, String beam, String lwl, Boolean aux, Integer membershipId,
                       String lName, String fName, Integer numberOfImages) {
		this.membershipId = new SimpleIntegerProperty(membershipId);
		this.lName = new SimpleStringProperty(lName);
		this.fName = new SimpleStringProperty(fName);
		this.numberOfImages = new SimpleIntegerProperty(numberOfImages);
	}

	public final IntegerProperty membershipIdProperty() {
		return this.membershipId;
	}
	

	public final int getMembershipId() {
		return this.membershipIdProperty().get();
	}
	

	public final void setMembershipId(final int membershipId) {
		this.membershipIdProperty().set(membershipId);
	}
	

	public final StringProperty lNameProperty() {
		return this.lName;
	}
	

	public final String getLastName() {
		return this.lNameProperty().get();
	}
	

	public final void setLastName(final String lName) {
		this.lNameProperty().set(lName);
	}
	

	public final StringProperty fNameProperty() {
		return this.fName;
	}
	

	public final String getFirstName() {
		return this.fNameProperty().get();
	}
	

	public final void setFirstName(final String fName) {
		this.fNameProperty().set(fName);
	}

	public final IntegerProperty numberOfImagesProperty() {
		return this.numberOfImages;
	}


	public final int getNumberOfImages() {
		return this.numberOfImagesProperty().get();
	}


	public final void setNumberOfImages(final int numberOfImages) {
		this.numberOfImagesProperty().set(numberOfImages);
	}

}
