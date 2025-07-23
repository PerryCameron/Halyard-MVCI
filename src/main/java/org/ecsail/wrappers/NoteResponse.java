package org.ecsail.wrappers;

import org.ecsail.pojo.Note;

public class NoteResponse {

    private boolean success;
    private Note note;
    private String message;

    public NoteResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.note = new Note();
    }

    public NoteResponse() {
    }

    // Getters and setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

