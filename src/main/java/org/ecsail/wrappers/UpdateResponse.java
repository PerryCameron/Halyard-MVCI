package org.ecsail.wrappers;

public class UpdateResponse {
    private boolean success;
    private String message;


    public UpdateResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public UpdateResponse() {
    }

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

