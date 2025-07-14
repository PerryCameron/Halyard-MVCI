package org.ecsail.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ecsail.pojo.Phone;

public class InsertPhoneResponse {

    private boolean success;
    //@JsonProperty("phoneDTO")  // this is to match server
    private Phone phone;
    private String message;

    public InsertPhoneResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.phone = new Phone();
    }

    public InsertPhoneResponse() {
    }

    // Getters and setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Phone getPhone() {
        return phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

