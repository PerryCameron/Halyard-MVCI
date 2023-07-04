package org.ecsail.mvci_dialogue;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Builder;
import javafx.util.Duration;
import org.ecsail.BaseApplication;
import org.ecsail.interfaces.Status;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.MenuFx;
import org.ecsail.widgetfx.RectangleFX;

import java.util.Objects;

import static java.lang.System.getProperty;

public class DialogueView implements Builder<Region> {
    private final DialogueModel dialogueModel;

    public DialogueView(DialogueModel dialogueModel) {
        this.dialogueModel = dialogueModel;
    }



    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();

        return borderPane;
    }

    private void setUpStage() {
        dialogueModel.getDialogueStage().initOwner(BaseApplication.primaryStage);
        dialogueModel.getDialogueStage().initModality(Modality.APPLICATION_MODAL);
        dialogueModel.primaryXPropertyProperty().bind(BaseApplication.primaryStage.xProperty());
        dialogueModel.primaryYPropertyProperty().bind(BaseApplication.primaryStage.yProperty());
//        loadingModel.setOffsets(50.0, 50.0);
//        updateSpinnerLocation();
//        setOffsetListener();
//        monitorPropertyChange(loadingModel.primaryXPropertyProperty());
//        monitorPropertyChange(loadingModel.primaryYPropertyProperty());
    }

}
