package org.ecsail.wrappers;

import org.ecsail.pojo.Award;
import org.ecsail.pojo.Boat;
import org.ecsail.pojo.BoatOwner;

public class InsertAwardResponse {

    private boolean success;
    private Award award;
    private String message;

    public InsertAwardResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.award = new Award();
    }

    public InsertAwardResponse() {
    }

    // Getters and setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

