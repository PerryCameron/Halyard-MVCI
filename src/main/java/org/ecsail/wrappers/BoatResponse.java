package org.ecsail.wrappers;

import org.ecsail.pojo.Boat;
import org.ecsail.pojo.BoatOwner;

public class BoatResponse {

    private boolean success;
    private Boat boat;
    private BoatOwner boatOwner;
    private String message;

    public BoatResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.boat = new Boat();
    }

    public BoatResponse() {
    }

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Boat getBoat() { return boat; }
    public void setBoat(Boat boat) { this.boat = boat; }
    public BoatOwner getBoatOwner() { return boatOwner; }
    public void setBoatOwner(BoatOwner boatOwner) {
        this.boatOwner = boatOwner;
    }
}

