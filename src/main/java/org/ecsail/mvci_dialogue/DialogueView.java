package org.ecsail.mvci_dialogue;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.ecsail.BaseApplication;
import org.ecsail.enums.Dialogue;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.function.Supplier;

import static java.lang.System.getProperty;

public class DialogueView implements Builder<Region> {
    private final DialogueModel dialogueModel;
    private final Dialogue dialogue;
    private final BooleanProperty confirm;
    private final Stage stage;

    public DialogueView(DialogueModel dialogueModel, Dialogue dialogue, BooleanProperty booleanProperty) {
        this.dialogue = dialogue;
        this.confirm = booleanProperty;
        this.dialogueModel = dialogueModel;
        this.stage = new Stage();
    }

    @Override
    public Region build() {
        setUpStage(stage);
        VBox vBox = VBoxFx.vBoxOf(20.0, new Insets(0,0,0,0));
        vBox.setAlignment(Pos.CENTER);
        Label label = new Label("Are you sure you want to delete this?");
        vBox.getChildren().add(label);
        vBox.setPrefSize(350, 100);
        switch (dialogue) {
            case CONFORMATION -> vBox.getChildren().add(createConformation(stage));
        }
        return vBox;
    }

    private Node createConformation(Stage stage) {
        HBox hBox = HBoxFx.hBoxOf(new Insets(0,0,0,0), Pos.CENTER, 15.0);
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(getButtonActionEventEventHandler(stage, false));
        Button okButton = new Button("Ok");
        okButton.setOnAction(getButtonActionEventEventHandler(stage, true));
        hBox.getChildren().addAll(okButton,cancelButton);
        return hBox;
    }

    private EventHandler<ActionEvent> getButtonActionEventEventHandler(Stage stage, boolean setTo) {
        return e -> {
            if (!confirm.get())
                confirm.set(setTo);
            else {
                confirm.set(!setTo);
                confirm.set(setTo);
            }
            stage.close();
        };
    }

    private void setUpStage(Stage stage) {
        stage.initOwner(BaseApplication.primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        dialogueModel.primaryXPropertyProperty().bind(BaseApplication.primaryStage.xProperty());
        dialogueModel.primaryYPropertyProperty().bind(BaseApplication.primaryStage.yProperty());
        stage.setOnShown(windowEvent -> {
            updateSpinnerLocation(stage);
        });
        stage.show();
        monitorPropertyChange(dialogueModel.primaryXPropertyProperty(), stage);
        monitorPropertyChange(dialogueModel.primaryYPropertyProperty(), stage);
    }

    public void monitorPropertyChange(DoubleProperty property, Stage stage) {
        property.addListener((observable, oldValue, newValue) -> {
            updateSpinnerLocation(stage);
        });
    }

    private void updateSpinnerLocation(Stage dialogueStage) {
        double centerXPosition = BaseApplication.primaryStage.getX() + BaseApplication.primaryStage.getWidth() / 2d;
        double centerYPosition = BaseApplication.primaryStage.getY() + BaseApplication.primaryStage.getHeight() / 2d;
        dialogueStage.setX(centerXPosition -
                dialogueStage.getWidth()  / 2d);
        dialogueStage.setY(centerYPosition -
                dialogueStage.getHeight()  / 2d);
    }

    public Stage getStage() {
        return stage;
    }
}
