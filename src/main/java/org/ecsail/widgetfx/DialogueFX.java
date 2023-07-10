package org.ecsail.widgetfx;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DialogueFX  {

    public static Region createConfirmDialogue(String message, BooleanProperty confirm, Stage stage) {
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        vBox.setPrefSize(400, 150);
        Label label = new Label(message);
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
        vBox.getChildren().addAll(label,hBox);
        System.out.println("DialogueFX complete");
        return vBox;
    }


}
