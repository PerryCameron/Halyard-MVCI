package org.ecsail.fx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class BoatOwnerFx {
	private IntegerProperty msId;
	private IntegerProperty boatId;
	
	public BoatOwnerFx(Integer msid, Integer boat_id) {
		super();
		this.msId = new SimpleIntegerProperty(msid);
		this.boatId = new SimpleIntegerProperty(boat_id);
		
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
	

	public final IntegerProperty boatIdProperty() {
		return this.boatId;
	}
	

	public final int getBoatId() {
		return this.boatIdProperty().get();
	}
	

	public final void setBoatId(final int boatId) {
		this.boatIdProperty().set(boatId);
	}
	
}
