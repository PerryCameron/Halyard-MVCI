package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.widgetfx.VBoxFx;

import java.util.Objects;

public class SlipTabView implements Builder<Tab> {

    @Override
    public Tab build() {
        Tab tab = new Tab();
        tab.setText("Slip");
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,5,5)); // makes outer border
        vBox.setId("custom-tap-pane-frame");
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(setSlipImage());
        borderPane.setId("box-background-light");
        vBox.getChildren().add(borderPane);
        tab.setContent(vBox);
        return tab;
    }

    private Node setSlipImage() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,0,0,0));
        Image slipImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/slips/" + filename + "_icon.png")));
        ImageView imageView = new ImageView(slipImage);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(240);
        return vBox;
    }

}
