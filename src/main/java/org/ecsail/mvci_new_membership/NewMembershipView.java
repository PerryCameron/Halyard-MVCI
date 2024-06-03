package org.ecsail.mvci_new_membership;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.apache.poi.ss.formula.functions.T;

import java.util.function.Consumer;

public class NewMembershipView implements Builder<Region> {
    private final NewMembershipModel newMembershipModel;
    Consumer<NewMembershipMessage> action;

    public NewMembershipView(NewMembershipModel newMembershipModel, Consumer<NewMembershipMessage> m) {
        this.newMembershipModel = newMembershipModel;
        action = m;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10,10,10,10));
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10,0,0,10));
        newMembershipModel.setWhiteVBox(vBox);
        hBox.getChildren().add(vBox);
        vBox.setStyle("-fx-background-color: white;");  //green
        borderPane.setCenter(hBox);
        HBox.setHgrow(vBox, Priority.ALWAYS);
        setMessageListener();
        return borderPane;
    }

    public void setMessageListener() {
        // Add listener to tabMessage property
        newMembershipModel.tabMessageProperty().addListener((observable, oldValue, newValue) -> {
            Text newMemLog = new Text(newValue);
            newMembershipModel.getWhiteVBox().getChildren().add(newMemLog);
            // Add your custom action here
        });
    }
}
