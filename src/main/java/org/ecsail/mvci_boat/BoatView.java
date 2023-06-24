package org.ecsail.mvci_boat;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Builder;
import org.ecsail.dto.DbBoatSettingsDTO;
import org.ecsail.interfaces.ListCallBack;
import org.ecsail.widgetfx.*;

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
        borderPane.setCenter(createSpacer());
        borderPane.setLeft(setUpInfo());
        borderPane.setBottom(setUpNotes());
        return borderPane;
    }

    private Node createSpacer() {
        return RegionFx.regionWidthOf(10.1);
    }

    private Node setUpNotes() {
        HBox hBoxOuter = HBoxFx.hBoxOf(new Insets(10,10,5,10));
        TitledPane titledPane = TitledPaneFx.titledPaneOf("Notes");
        HBox hBox = new HBox();
        VBox vBoxTable = VBoxFx.vBoxOf(new Insets(0,0,0,0));
        VBox vBoxButtons = VBoxFx.vBoxOf(80.0,5.0,new Insets(0,0,0,5));
        vBoxButtons.getChildren().addAll(createButton("Add"), createButton("Delete"));
        vBoxTable.getChildren().add(new NotesTableView().build());
        hBox.getChildren().addAll(vBoxTable,vBoxButtons);
        titledPane.setContent(hBox);
        hBoxOuter.getChildren().add(titledPane);
        // After adding the titledPane to its parent, set Hgrow to ALWAYS
        HBox.setHgrow(titledPane, Priority.ALWAYS);
        // After adding the vBoxTable to its parent, set Hgrow to ALWAYS
        HBox.setHgrow(vBoxTable, Priority.ALWAYS);
        return hBoxOuter;
    }

    private Node setUpInfo() {
        VBox vBox = VBoxFx.vBoxOf(350.0,10.0, new Insets(10,0,0,10));
        vBox.getChildren().addAll(boatInfoTitlePane(), ownerTitlePane());
        return vBox;
    }

    private Node ownerTitlePane() {
        TitledPane titledPane = TitledPaneFx.titledPaneOf("Owner(s)");
        VBox vBox = VBoxFx.vBoxOf(new Insets(10,10,10,10));
        vBox.setId("box-grey");
        vBox.getChildren().add(addTableAndButtons());
        titledPane.setContent(vBox);
        return titledPane;
    }

    private Node addTableAndButtons() {
        HBox hBox = new HBox();
        VBox vBoxTable = VBoxFx.vBoxOf(new Insets(0,10,0,0));
        VBox vBoxButtons = VBoxFx.vBoxOf(80.0,5.0,new Insets(0,0,0,5));
        vBoxButtons.getChildren().addAll(createButton("Add"), createButton("Delete"));
        vBoxTable.getChildren().add(new BoatOwnerTableView().build());
        hBox.getChildren().addAll(vBoxTable,vBoxButtons);
        return hBox;
    }

    private Node createButton(String name) {
        Button button = ButtonFx.buttonOf(name, 60.0);
        switch (name) {
            case "Add" -> System.out.println("Adding owner");
            case "Delete" -> System.out.println("Deleting Owner");
            case ">" -> System.out.println(">");
            case "<" -> System.out.println("<");
            case "Set As Default" -> System.out.println("Set as Default");
        }
        return button;
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
        VBox vBox = new VBox();
        HBox.setHgrow(vBox, Priority.ALWAYS);
        vBox.getChildren().addAll(addPictureControls(), addPicture());
        return vBox;
    }

    private Node addPictureControls() {
        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER, 10.0);
        hBox.setPrefHeight(40);
        hBox.setSpacing(7);
        String[] buttonLabels = { "<",">","Add","Delete","Default"};
        for(String label: buttonLabels) hBox.getChildren().add(createButton(label));
        return hBox;
    }

    private Node addPicture() {
        VBox vBox = VBoxFx.vBoxOf(630,489, true, true);
        vBox.getChildren().add(new ImageViewPane(new ImageView()));
        return vBox;
    }


}
