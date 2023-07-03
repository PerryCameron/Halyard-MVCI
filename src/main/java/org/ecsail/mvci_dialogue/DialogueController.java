package org.ecsail.mvci_dialogue;


import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.interfaces.Status;

public class DialogueController extends Controller implements Status {

    private final DialogueInteractor dialogueInteractor;
    private final DialogueView dialogueView;

    public DialogueController() {
        DialogueModel dialogueModel = new DialogueModel();
        dialogueInteractor = new DialogueInteractor(dialogueModel);
        dialogueView = new DialogueView(dialogueModel);
    }

    @Override
    public Region getView() { return dialogueView.build(); }

}
