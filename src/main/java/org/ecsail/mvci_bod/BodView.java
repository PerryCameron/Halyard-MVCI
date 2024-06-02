package org.ecsail.mvci_bod;


import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.function.Consumer;

public class BodView implements Builder<Region> {
    private final BodModel BodModel;
    Consumer<BodMessage> action;

    public BodView(BodModel BodModel, Consumer<BodMessage> m) {
        this.BodModel = BodModel;
        action = m;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(hBox);
//        slipModel.setMainBox(hBox);
//        slipModel.setBorderPane(borderPane);
//        setListsLoadedListener();
        return borderPane;
    }

//    private void setListsLoadedListener() {
//        slipModel.listsLoadedProperty().addListener((obs, wasLoaded, isNowLoaded) -> {
//            if (isNowLoaded) {
//                buildDocks();
//                setSizeListener();
//            }
//        });
//    }

//    private void setSizeListener() {
//        ChangeListener<Number> sizeListener = (obs, oldVal, newVal) -> Platform.runLater(this::buildDocks);
//        slipModel.getBorderPane().widthProperty().addListener(sizeListener);
//        slipModel.getBorderPane().heightProperty().addListener(sizeListener);
//    }


}
