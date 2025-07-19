package org.ecsail.wrappers;


import org.ecsail.pojo.Officer;

public class PositionResponse {

    private boolean success;
//    @JsonProperty("officerDTO")  // this is to match server
    private Officer position;
    private String message;
    public PositionResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.position = new Officer();
    }
    public PositionResponse() {
    }
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public Officer getPosition() {
        return position;
    }
    public void setPosition(Officer position) {
        this.position = position;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

