package org.ecsail.wrappers;

import org.ecsail.abstractions.ResponseWrapper;
import org.ecsail.pojo.Boat;
import org.ecsail.pojo.BoatOwner;

public class BoatResponse extends ResponseWrapper<Boat> {

    private BoatOwner boatOwner;

    public BoatResponse(Boat boat) {
        super(boat); // Pass the boat parameter to the parent constructor
    }

    public BoatResponse() {
        super(null); // Pass null to the parent constructor, which will use createDefaultInstance()
    }

    @Override
    protected Boat createDefaultInstance() {
        return new Boat();
    }

    public BoatOwner getBoatOwner() {
        return boatOwner;
    }

    public void setBoatOwner(BoatOwner boatOwner) {
        this.boatOwner = boatOwner;
    }

    // Optionally override getters/setters for boat to use a more specific name
    public Boat getBoat() {
        return getData(); // Use inherited data field
    }

    public void setBoat(Boat boat) {
        setData(boat); // Use inherited data field
    }
}

