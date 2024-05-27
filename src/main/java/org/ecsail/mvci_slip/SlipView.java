package org.ecsail.mvci_slip;


import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Builder;
import org.ecsail.mvci_main.MainModel;

import java.util.function.Consumer;

public class SlipView implements Builder<Region> {
    private final SlipModel slipModel;
    Consumer<SlipMessage> action;
    public SlipView(SlipModel slipModel, Consumer<SlipMessage> m) {
        this.slipModel = slipModel;
        action = m;
    }

    @Override
    public Region build() {
        System.out.println("Building the slip view");
        Pane pane = new BorderPane();
        // Create the first line
        Line line1 = new Line();
        line1.setStartX(50);
        line1.setStartY(50);
        line1.setEndX(200);
        line1.setEndY(200);
        line1.setStroke(Color.BLUE);
        line1.setStrokeWidth(2);

        // Create the second line
        Line line2 = new Line(200, 50, 50, 200);
        line2.setStroke(Color.RED);
        line2.setStrokeWidth(2);

        // Add lines to the pane
        pane.getChildren().addAll(line1, line2);
        return pane;
    }



}
