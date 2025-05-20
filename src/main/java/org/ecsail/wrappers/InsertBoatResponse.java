package org.ecsail.wrappers;

import org.ecsail.pojo.BoatDTO;
import org.ecsail.pojo.BoatOwnerDTO;

public class InsertBoatResponse {

    private boolean success;
    private BoatDTO boat;
    private BoatOwnerDTO boatOwner;
    private String message;

    public InsertBoatResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.boat = new BoatDTO();
    }

    public InsertBoatResponse() {
    }

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public BoatDTO getBoat() { return boat; }
    public void setBoat(BoatDTO boat) { this.boat = boat; }
    public BoatOwnerDTO getBoatOwner() { return boatOwner; }
    public void setBoatOwner(BoatOwnerDTO boatOwner) {
        this.boatOwner = boatOwner;
    }
}

