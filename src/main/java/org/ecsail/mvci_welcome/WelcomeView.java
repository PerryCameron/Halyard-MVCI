package org.ecsail.mvci_welcome;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.widgetfx.ButtonFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.Arrays;

public class WelcomeView implements Builder<Region> {

    WelcomeModel welcomeModel;
    public WelcomeView(WelcomeModel welcomeModel) {
        this.welcomeModel = welcomeModel;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setRight(setUpRightPane());
        return borderPane;
    }

    private Node setUpRightPane() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(15,10,0,0), 400.0,350.0,10.0);
        String[] categories = {"People","Slips","Board of Directors","Create New Membership","Deposits",
                "Rosters","Boats","Notes","Jotform"};
        Arrays.stream(categories).forEach(category -> vBox.getChildren().add(newBigButton(category)));
        return vBox;
    }

    private Node newBigButton(String category) {
        Button button = ButtonFx.bigButton(category);
        button.setOnAction((event) -> System.out.println(category));
        return button;
    }


}
