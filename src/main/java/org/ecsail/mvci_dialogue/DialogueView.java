package org.ecsail.mvci_dialogue;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.ecsail.BaseApplication;
import org.ecsail.enums.Dialogue;

import java.util.function.Supplier;

import static java.lang.System.getProperty;

public class DialogueView implements Builder<Region> {
    private final DialogueModel dialogueModel;
    private final Dialogue dialogue;
    private final BooleanProperty confirm;

    private final Stage stage;
    private Supplier<Boolean> supplier;

    public DialogueView(DialogueModel dialogueModel, Dialogue dialogue, BooleanProperty booleanProperty) {
        this.dialogue = dialogue;
        this.confirm = booleanProperty;
        this.dialogueModel = dialogueModel;
        this.stage = new Stage();
    }


    @Override
    public Region build() {
        setUpStage(stage);
        VBox vBox = new VBox();
        Label label = new Label("Are you sure you want to delete this?");
        vBox.getChildren().add(label);
        vBox.setPrefSize(400, 150);
        switch (dialogue) {
            case CONFORMATION -> vBox.getChildren().add(createConformation(stage));
        }
        return vBox;
    }


    private Node createConformation(Stage stage) {
        System.out.println("Creating conformation");
        HBox hBox = new HBox();
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            if(confirm.get())
                confirm.set(false);
            else {
                confirm.set(true);
                confirm.set(false);
            }
            stage.close();
        });
        Button okButton = new Button("Ok");
        okButton.setOnAction(e ->  {
            if(!confirm.get())
                confirm.set(true);
            else {
                confirm.set(false);
                confirm.set(true);
            }
            stage.close();
        });
        hBox.getChildren().addAll(okButton,cancelButton);
        return hBox;
    }

    private void setUpStage(Stage dialogueStage) {
        dialogueStage.initOwner(BaseApplication.primaryStage);
        dialogueStage.initModality(Modality.APPLICATION_MODAL);
        dialogueModel.primaryXPropertyProperty().bind(BaseApplication.primaryStage.xProperty());
        dialogueModel.primaryYPropertyProperty().bind(BaseApplication.primaryStage.yProperty());
        dialogueStage.setOnShown(windowEvent -> {
            updateSpinnerLocation(dialogueStage);
        });
        dialogueStage.show();
        monitorPropertyChange(dialogueModel.primaryXPropertyProperty(), dialogueStage);
        monitorPropertyChange(dialogueModel.primaryYPropertyProperty(), dialogueStage);
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
