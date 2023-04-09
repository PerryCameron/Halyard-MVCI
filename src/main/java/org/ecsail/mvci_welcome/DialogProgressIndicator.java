package org.ecsail.mvci_welcome;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import org.ecsail.widgetfx.HBoxFx;

public class DialogProgressIndicator implements Builder<Region> {
    WelcomeModel welcomeModel;
    public DialogProgressIndicator(WelcomeModel welcomeModel) {
        this.welcomeModel = welcomeModel;
    }

    @Override
    public Region build() {
        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER,new Insets(15,15,15,15));
        hBox.getChildren().add(createProgressBar());
        return hBox;
    }

    private Node createProgressBar() {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefSize(300, 30);
        progressBar.progressProperty().bind(welcomeModel.progressProperty().divide(100));
        return progressBar;
    }
}
