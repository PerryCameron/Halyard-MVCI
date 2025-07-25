package org.ecsail.wrappers;
import org.ecsail.abstractions.ResponseWrapper;
import org.ecsail.pojo.Note;

public class NoteResponse extends ResponseWrapper<Note> {

    public NoteResponse(Note note) {
        super(note);
    }

    public NoteResponse() {
        super(null);
    }

    @Override
    protected Note createDefaultInstance() {
        return new Note();
    }

    public Note getNote() {
        return getData(); // Use inherited data field
    }

    public void setBoat(Note note) {
        setData(note); // Use inherited data field
    }
}

