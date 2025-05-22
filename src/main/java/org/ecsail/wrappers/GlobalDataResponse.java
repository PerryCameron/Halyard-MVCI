package org.ecsail.wrappers;

import org.ecsail.fx.BoardPositionDTO;
import org.ecsail.pojo.HalyardUser;

import java.util.List;

// this is the data we need from the server to operate application
public class GlobalDataResponse {
    private boolean success;
    private String message;
    private HalyardUser user;
    private List<BoardPositionDTO> boardPositions;

    public GlobalDataResponse() {
    }

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

    public HalyardUser getUser() {
        return user;
    }

    public void setUser(HalyardUser user) {
        this.user = user;
    }

    public List<BoardPositionDTO> getBoardPositions() {
        return boardPositions;
    }

    public void setBoardPositions(List<BoardPositionDTO> boardPositions) {
        this.boardPositions = boardPositions;
    }
}
