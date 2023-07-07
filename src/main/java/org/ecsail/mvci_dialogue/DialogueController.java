package org.ecsail.mvci_dialogue;


import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.ecsail.interfaces.Controller;
import org.ecsail.interfaces.Status;
import org.ecsail.mvci_main.MainController;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DialogueController extends Controller implements Status {

    private final DialogueInteractor dialogueInteractor;
    private final DialogueView dialogueView;
    private final MainController mainController;

    public DialogueController(MainController mainController, Region region) {
        DialogueModel dialogueModel = new DialogueModel();
        dialogueInteractor = new DialogueInteractor(dialogueModel);
        dialogueView = new DialogueView(dialogueModel, region);
        this.mainController = mainController;
    }


    @Override
    public Region getView() {
        return dialogueView.build();
    }

    public Stage getStage() {
        return dialogueInteractor.getStage();
    }

}
