package org.ecsail.wrappers;

import org.ecsail.pojo.Email;

public class EmailResponse {

    private boolean success;
    //@JsonProperty("phoneDTO")  // this is to match server
    private Email email;
    private String message;

    public EmailResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.email = new Email();
    }

    public EmailResponse() {
    }

    // Getters and setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Email getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

