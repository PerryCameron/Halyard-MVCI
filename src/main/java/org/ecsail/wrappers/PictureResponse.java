package org.ecsail.wrappers;

import org.ecsail.fx.PictureDTO;

public class PictureResponse {

    private boolean success;
    private PictureDTO pictureDTO;
    private String message;

    public PictureResponse(boolean success, String message, PictureDTO pictureDTO) {
        this.success = success;
        this.message = message;
        this.pictureDTO = pictureDTO;
    }

    public PictureResponse() {
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

    public PictureDTO getPictureDTO() {
        return pictureDTO;
    }

    public void setPictureDTO(PictureDTO pictureDTO) {
        this.pictureDTO = pictureDTO;
    }
}

