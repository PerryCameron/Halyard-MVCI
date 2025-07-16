package org.ecsail.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.ecsail.pojo.Award;

public class AwardResponse {

    private boolean success;
    @JsonProperty("awardDTO")  // this is to match server
    private Award award;
    private String message;

    public AwardResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.award = new Award();
    }

    public AwardResponse() {
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

