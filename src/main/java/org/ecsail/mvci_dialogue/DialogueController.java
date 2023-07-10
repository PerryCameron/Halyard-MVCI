package org.ecsail.mvci_dialogue;


import javafx.beans.property.BooleanProperty;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.ecsail.enums.Dialogue;
import org.ecsail.interfaces.Controller;
import org.ecsail.interfaces.Status;
import org.ecsail.mvci_main.MainController;

public class DialogueController extends Controller implements Status {

    private final DialogueInteractor dialogueInteractor;
    private final DialogueView dialogueView;
    private final MainController mainController;

    public DialogueController(MainController mainController, Dialogue dialogue, BooleanProperty booleanProperty) {
        DialogueModel dialogueModel = new DialogueModel();
        dialogueInteractor = new DialogueInteractor(dialogueModel);
        dialogueView = new DialogueView(dialogueModel, dialogue, booleanProperty);
        this.mainController = mainController;
    }


    @Override
    public Region getView() {
        return dialogueView.build();
    }

    public Stage getStage() { return dialogueView.getStage(); }


}
