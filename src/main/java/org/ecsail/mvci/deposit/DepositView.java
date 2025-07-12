package org.ecsail.mvci.deposit;


import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.function.Consumer;

public class DepositView implements Builder<Region> {
    private final DepositModel depositModel;
    Consumer<DepositMessage> action;

    public DepositView(DepositModel depositModel, Consumer<DepositMessage> m) {
        this.depositModel = depositModel;
        action = m;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(hBox);

        return borderPane;
    }
}
