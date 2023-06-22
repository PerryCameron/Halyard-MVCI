package org.ecsail.mvci_boat;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Builder;
import javafx.util.Duration;
import org.ecsail.dto.BoatListRadioDTO;
import org.ecsail.dto.DbBoatSettingsDTO;
import org.ecsail.interfaces.ListCallBack;
import org.ecsail.mvci_boatlist.BoatListModel;
import org.ecsail.mvci_boatlist.BoatListRadioHBox;
import org.ecsail.mvci_boatlist.BoatListSettingsCheckBox;
import org.ecsail.mvci_boatlist.BoatListTableView;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.Collection;
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
        VBox vBox = VBoxFx.vBoxOf(350.0,10.0, new Insets(0,0,0,0));

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
        boatModel.dataLoadedProperty().addListener((observable, oldValue, newValue) -> {
            for(DbBoatSettingsDTO dbBoatSettingsDTO: boatModel.getBoatSettings()) {
                vBox.getChildren().add(new Row(boatModel, dbBoatSettingsDTO));
                System.out.println("Creating Row");
            }
        });
        titledPane.setContent(vBox);
        titledPane.setText("Boat Information");
        return titledPane;
    }

    private Node setUpPicture() {
        return null;
    }


}
