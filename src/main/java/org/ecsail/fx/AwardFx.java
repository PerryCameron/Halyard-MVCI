package org.ecsail.fx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.ecsail.pojo.Award;

import java.time.Year;

public class AwardFx {
	
	private IntegerProperty awardId;
	private IntegerProperty pId;
	private StringProperty awardYear;  // TODO this should be integer
	private StringProperty awardType;


	
	public AwardFx(Integer awardId, Integer pId, String awardYear, String awardType) {
		this.awardId = new SimpleIntegerProperty(awardId);
		this.pId = new SimpleIntegerProperty(pId);
		this.awardYear = new SimpleStringProperty(awardYear);
		this.awardType = new SimpleStringProperty(awardType);
	}

    public AwardFx(int pId) {
		this.awardId = new SimpleIntegerProperty(0);
		this.pId = new SimpleIntegerProperty(pId);
		this.awardYear = new SimpleStringProperty(Year.now().toString());
		this.awardType = new SimpleStringProperty("");
    }

	public AwardFx(Award award) {
		this.awardId = new SimpleIntegerProperty(award.getAwardId());
		this.pId = new SimpleIntegerProperty(award.getpId());
		this.awardYear = new SimpleStringProperty(award.getAwardYear());
		this.awardType = new SimpleStringProperty(award.getAwardType());
	}

    public final IntegerProperty awardIdProperty() {
		return this.awardId;
	}

	public final int getAwardId() {
		return this.awardIdProperty().get();
	}

	public final void setAwardId(final int awardId) {
		this.awardIdProperty().set(awardId);
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
	



	public final StringProperty awardYearProperty() {
		return this.awardYear;
	}
	



	public final String getAwardYear() {
		return this.awardYearProperty().get();
	}
	



	public final void setAwardYear(final String awardYear) {
		this.awardYearProperty().set(awardYear);
	}
	



	public final StringProperty awardTypeProperty() {
		return this.awardType;
	}
	



	public final String getAwardType() {
		return this.awardTypeProperty().get();
	}
	



	public final void setAwardType(final String awardType) {
		this.awardTypeProperty().set(awardType);
	}


	@Override
	public String toString() {
		return "AwardFx{" +
				"awardId=" + awardId +
				", pId=" + pId +
				", awardYear=" + awardYear +
				", awardType=" + awardType +
				'}';
	}
}
