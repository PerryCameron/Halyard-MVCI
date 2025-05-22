package org.ecsail.fx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.ecsail.pojo.Officer;

import java.time.Year;

public class OfficerFx {

	private IntegerProperty officerId;
	private IntegerProperty pId;
	private IntegerProperty boardYear;
	private StringProperty officerType;
	private IntegerProperty fiscalYear;  // TODO this should be integer
	
	public OfficerFx(Integer officerId, Integer pId, Integer boardYear,
					 String officerType, Integer fiscalYear) {
		this.officerId = new SimpleIntegerProperty(officerId);
		this.pId = new SimpleIntegerProperty(pId);
		this.boardYear = new SimpleIntegerProperty(boardYear);
		this.officerType = new SimpleStringProperty(officerType);
		this.fiscalYear = new SimpleIntegerProperty(fiscalYear);
	}

    public OfficerFx(Integer pId) {
		this.officerId = new SimpleIntegerProperty(0);
		this.pId = new SimpleIntegerProperty(pId);
		this.boardYear = new SimpleIntegerProperty(0);
		this.officerType = new SimpleStringProperty("BM");
		this.fiscalYear = new SimpleIntegerProperty(Year.now().getValue());
    }

	public OfficerFx(Officer officer) {
		this.officerId = new SimpleIntegerProperty(officer.getOfficerId());
		this.pId = new SimpleIntegerProperty(officer.getpId());
		this.boardYear = new SimpleIntegerProperty(officer.getBoardYear());
		this.officerType = new SimpleStringProperty(officer.getOfficerType());
		this.fiscalYear = new SimpleIntegerProperty(officer.getFiscalYear());
	}

    public final IntegerProperty officerIdProperty() {
		return this.officerId;
	}
	

	public final int getOfficerId() {
		return this.officerIdProperty().get();
	}
	

	public final void setOfficerId(final int officerId) {
		this.officerIdProperty().set(officerId);
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


	public Integer getBoardYear() {
		return boardYear.get();
	}

	public IntegerProperty boardYearProperty() {
		return boardYear;
	}

	public void setBoardYear(Integer boardYear) {
		this.boardYear.set(boardYear);
	}

	public final StringProperty officerTypeProperty() {
		return this.officerType;
	}
	

	public final String getOfficerType() {
		return this.officerTypeProperty().get();
	}
	

	public final void setOfficerType(final String officerType) {
		this.officerTypeProperty().set(officerType);
	}
	

	public final IntegerProperty fiscalYearProperty() {
		return this.fiscalYear;
	}
	

	public final Integer getFiscalYear() {
		return this.fiscalYearProperty().get();
	}
	

	public final void setFiscalYear(final Integer fiscalYear) {
		this.fiscalYearProperty().set(fiscalYear);
	}

	@Override
	public String toString() {
		return "Object_Officer [officer_id=" + officerId +"]";
	}
}
