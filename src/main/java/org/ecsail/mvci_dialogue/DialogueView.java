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
import org.ecsail.dto.DialogueDTO;
import org.ecsail.enums.Dialogue;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.function.Supplier;

import static java.lang.System.getProperty;

public class DialogueView implements Builder<Region> {
    private final DialogueModel dialogueModel;
    private final DialogueDTO dialogueDTO;
    private final Stage stage;

    public DialogueView(DialogueModel dialogueModel, DialogueDTO dialogueDTO) {
        this.dialogueDTO = dialogueDTO;
        this.dialogueModel = dialogueModel;
        this.stage = new Stage();
    }

    @Override
    public Region build() {
        setUpStage(stage);
        VBox vBox = VBoxFx.vBoxOf(20.0, new Insets(0,10,0,10));
        vBox.setAlignment(Pos.CENTER);
        Label label = new Label(dialogueDTO.getMessage());
        label.setWrapText(true);
        vBox.setPrefSize(350, 100);
        vBox.getChildren().add(label);
        switch (dialogueDTO.getDialogue()) {
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
            if (!dialogueDTO.confirmedProperty().get())
                dialogueDTO.confirmedProperty().set(setTo);
            else {
                dialogueDTO.confirmedProperty().set(!setTo);
                dialogueDTO.confirmedProperty().set(setTo);
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
