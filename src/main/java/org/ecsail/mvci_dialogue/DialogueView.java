package org.ecsail.mvci_dialogue;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
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
import java.util.function.Supplier;

import static java.lang.System.getProperty;

public class DialogueView implements Builder<Region> {
    private final DialogueModel dialogueModel;
    private final Region region;
    private Supplier<Boolean> supplier;

    public DialogueView(DialogueModel dialogueModel, Region region) {
        this.dialogueModel = dialogueModel;
        this.region = region;
    }

    @Override
    public Region build() {
        setUpStage();
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(region);
        return borderPane;
    }

    private void setUpStage() {
        dialogueModel.getDialogueStage().initOwner(BaseApplication.primaryStage);
        dialogueModel.getDialogueStage().initModality(Modality.APPLICATION_MODAL);
        dialogueModel.primaryXPropertyProperty().bind(BaseApplication.primaryStage.xProperty());
        dialogueModel.primaryYPropertyProperty().bind(BaseApplication.primaryStage.yProperty());
        dialogueModel.getDialogueStage().setOnShown(windowEvent -> {
            updateSpinnerLocation();
        });
        dialogueModel.getDialogueStage().show();
        monitorPropertyChange(dialogueModel.primaryXPropertyProperty());
        monitorPropertyChange(dialogueModel.primaryYPropertyProperty());
    }

    public void monitorPropertyChange(DoubleProperty property) {
        property.addListener((observable, oldValue, newValue) -> {
            updateSpinnerLocation();
        });
    }

    private void updateSpinnerLocation() {
        double centerXPosition = BaseApplication.primaryStage.getX() + BaseApplication.primaryStage.getWidth() / 2d;
        double centerYPosition = BaseApplication.primaryStage.getY() + BaseApplication.primaryStage.getHeight() / 2d;
            dialogueModel.getDialogueStage().setX(centerXPosition -
                    dialogueModel.getDialogueStage().getWidth()  / 2d);
            dialogueModel.getDialogueStage().setY(centerYPosition -
                    dialogueModel.getDialogueStage().getHeight()  / 2d);
    }

}
