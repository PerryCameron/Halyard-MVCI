package org.ecsail.mvci_boat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.DbBoatSettingsDTO;
import org.ecsail.interfaces.ListCallBack;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.function.Consumer;


public class BoatView implements Builder<Region>, ListCallBack {

    BoatModel boatModel;
    Consumer<Mode> action;
    public BoatView(BoatModel rm, Consumer<Mode> a) {
        boatModel = rm;
        action = a;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setRight(setUpPicture());
//        borderPane.setCenter(setUpTableView()); this will be a spacer
        borderPane.setLeft(setUpInfo());
        borderPane.setBottom(setUpNotes());
//        listenForData();
        return borderPane;
    }

    private void listenForData() {
        boatModel.dataLoadedProperty().addListener((observable, oldValue, newValue) -> {

        });
    }

    private Node setUpNotes() {
        return null;
    }

    private Node setUpInfo() {
        VBox vBox = VBoxFx.vBoxOf(350.0,10.0, new Insets(10,0,0,10));

        vBox.getChildren().addAll(boatInfoTitlePane(), ownerTitlePane());
        return vBox;
    }

    private Node ownerTitlePane() {
        TitledPane titledPane = new TitledPane();
        VBox.setVgrow(titledPane, Priority.ALWAYS);
        titledPane.setText("Owner(s)");
        // TODO see line 277
        return titledPane;
    }

    private Node boatInfoTitlePane() {
        TitledPane titledPane = new TitledPane();
        var vBox = new VBox();
        vBox.setId("box-grey");
        ChangeListener<Boolean> dataLoadedListener = ListenerFx.createSingleUseListener(boatModel.dataLoadedProperty(), () -> {
            for (DbBoatSettingsDTO dbBoatSettingsDTO : boatModel.getBoatSettings())
                vBox.getChildren().add(new Row(boatModel, dbBoatSettingsDTO));
        });
        boatModel.dataLoadedProperty().addListener(dataLoadedListener);
        titledPane.setContent(vBox);
        titledPane.setText("Boat Information");
        return titledPane;
    }

    private Node setUpPicture() {
        return null;
    }


}
