package org.ecsail.mvci_dialogue;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.ecsail.connection.Connections;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.interfaces.Status;
import org.ecsail.repository.implementations.BoardPositionsRepositoryImpl;
import org.ecsail.repository.interfaces.BoardPositionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DialogueInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(DialogueInteractor.class);
    private final DialogueModel dialogueModel;
    public DialogueInteractor(DialogueModel dialogueModel) {
        this.dialogueModel = dialogueModel;
    }

    public Stage getStage() {
        return dialogueModel.getDialogueStage();
    }
}
