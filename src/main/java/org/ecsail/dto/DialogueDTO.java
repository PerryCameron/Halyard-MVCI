package org.ecsail.dto;

import javafx.beans.property.BooleanProperty;
import org.ecsail.enums.Dialogue;

public class DialogueDTO {
    Dialogue dialogue;
    BooleanProperty confirmed;
    String message;

    public DialogueDTO(Dialogue dialogue, BooleanProperty confirmed, String message) {
        this.dialogue = dialogue;
        this.confirmed = confirmed;
        this.message = message;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
    }

    public boolean getConfirmed() {
        return confirmed.get();
    }

    public BooleanProperty confirmedProperty() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed.set(confirmed);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
