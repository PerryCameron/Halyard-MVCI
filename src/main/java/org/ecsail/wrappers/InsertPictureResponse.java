package org.ecsail.wrappers;

import org.ecsail.fx.PictureDTO;
import org.ecsail.pojo.Award;

public class InsertPictureResponse {

    private boolean success;
    private PictureDTO pictureDTO;
    private String message;

    public InsertPictureResponse(boolean success, String message, PictureDTO pictureDTO) {
        this.success = success;
        this.message = message;
        this.pictureDTO = pictureDTO;
    }

    public InsertPictureResponse() {
    }

    // Getters and setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

