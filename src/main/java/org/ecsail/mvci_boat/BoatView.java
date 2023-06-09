package org.ecsail.mvci_boat;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Builder;
import org.apache.poi.ss.formula.functions.T;
import org.ecsail.dto.BoatPhotosDTO;
import org.ecsail.dto.DbBoatSettingsDTO;
import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.widgetfx.*;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;


public class BoatView implements Builder<Region>, ConfigFilePaths {

    BoatModel boatModel;
    Consumer<BoatMessage> action;
    public BoatView(BoatModel rm, Consumer<BoatMessage> a) {
        boatModel = rm;
        action = a;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(setUpPicture());
        borderPane.setLeft(setUpInfo());
        borderPane.setBottom(setUpNotes());
        return borderPane;
    }

    private Node setUpNotes() {
        HBox hBoxOuter = HBoxFx.hBoxOf(new Insets(10,10,5,10));
        TitledPane titledPane = TitledPaneFx.titledPaneOf("Notes");
        HBox hBox = new HBox();
        VBox vBoxTable = VBoxFx.vBoxOf(new Insets(0,0,0,0));
        VBox vBoxButtons = VBoxFx.vBoxOf(80.0,5.0,new Insets(0,0,0,5));
        vBoxButtons.getChildren().addAll(
                createButton("Add",BoatMessage.ADD_NOTE),
                createButton("Delete",BoatMessage.DELETE_NOTE));
        vBoxTable.getChildren().add(new BoatNotesTableView(this).build());
        hBox.getChildren().addAll(vBoxTable,vBoxButtons);
        titledPane.setContent(hBox);
        hBoxOuter.getChildren().add(titledPane);
        // After adding the titledPane to its parent, set Hgrow to ALWAYS
        HBox.setHgrow(titledPane, Priority.ALWAYS);
        // After adding the vBoxTable to its parent, set Hgrow to ALWAYS
        HBox.setHgrow(vBoxTable, Priority.ALWAYS);
        System.out.println("Size is " + boatModel.getNotesDTOS().size());
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
        vBoxButtons.getChildren().addAll(
                createButton("Add",BoatMessage.ADD_OWNER),
                createButton("Delete",BoatMessage.DELETE_OWNER));
        vBoxTable.getChildren().add(new BoatOwnerTableView(this).build());
        hBox.getChildren().addAll(vBoxTable,vBoxButtons);
        return hBox;
    }

    private Node createButton(String name, BoatMessage type) {
        Button button = ButtonFx.buttonOf(name, 60.0);
        switch (type) {
            case ADD_IMAGE ->  button.setOnAction(event -> action.accept(BoatMessage.ADD_IMAGE));
            case ADD_NOTE -> button.setOnAction(event -> action.accept(BoatMessage.ADD_NOTE));
            case ADD_OWNER -> button.setOnAction(event -> action.accept(BoatMessage.ADD_OWNER));
            case DELETE_IMAGE -> button.setOnAction(event -> deleteImage());
            case DELETE_NOTE -> button.setOnAction(event -> deleteNote());
            case DELETE_OWNER -> button.setOnAction(event -> deleteOwner());
            case SET_DEFAULT -> button.setOnAction(event -> action.accept(BoatMessage.SET_DEFAULT));
            case MOVE_FORWARD -> button.setOnAction((event) -> moveToNextImage(true));
            case MOVE_BACKWARD -> button.setOnAction((event) -> moveToNextImage(false));
        };
        return button;
    }

    private void makeAlert(String[] string, Object o, BoatMessage boatMessage) {
        if(o != null) {
            Alert alert = DialogueFx.customAlert(string[0], string[1], Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) action.accept(boatMessage);
        } else {
            Alert alert = DialogueFx.customAlert(string[2],string[3], Alert.AlertType.INFORMATION);
            alert.showAndWait();
        }
    }

    private void deleteNote() {
        String[] strings = {
                "Delete Note",
                "Are you sure you want to delete this note?",
                "Missing Selection",
                "You need to select a note first"};
        makeAlert(strings, boatModel.getSelectedNote(), BoatMessage.DELETE_NOTE);
    }

    private void deleteOwner() {
        String[] strings = {
                "Delete Owner",
                "Are you sure you want to delete this owner?",
                "Missing Selection",
                "You need to select an owner first"};
        makeAlert(strings, boatModel.getSelectedOwner(), BoatMessage.DELETE_OWNER);
    }

    private void deleteImage() {
        String[] strings = {
                "Delete Image",
                "Are you sure you want to delete this owner?",
                "Missing Selection",
                "You need to select an owner first"};
        makeAlert(strings, boatModel.getSelectedImage(), BoatMessage.DELETE_IMAGE);
    }

    private Node boatInfoTitlePane() {
        TitledPane titledPane = new TitledPane();
        var vBox = new VBox();
        vBox.setId("box-grey");
        ChangeListener<Boolean> dataLoadedListener = ListenerFx.createSingleUseListener(boatModel.dataLoadedProperty(), () -> {
            for (DbBoatSettingsDTO dbBoatSettingsDTO : boatModel.getBoatSettings())
                vBox.getChildren().add(new Row(this, dbBoatSettingsDTO));
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
        hBox.getChildren().add(createButton("<",BoatMessage.MOVE_BACKWARD));
        hBox.getChildren().add(createButton(">",BoatMessage.MOVE_FORWARD));
        hBox.getChildren().add(createButton("Add",BoatMessage.ADD_IMAGE));
        hBox.getChildren().add(createButton("Delete",BoatMessage.DELETE_IMAGE));
        hBox.getChildren().add(createButton("Default",BoatMessage.SET_DEFAULT));
        return hBox;
    }

    private Node addPicture() {
        VBox vBox = VBoxFx.vBoxOf(630,489, true, true);
        vBox.setPadding(new Insets(0,10,0,10));
        ImageView imageView = new ImageView();
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);
        imageView.setCache(true);
        boatModel.setImageView(imageView);
        ChangeListener<Boolean> dataLoadedListener = ListenerFx.createSingleUseListener(boatModel.dataLoadedProperty(), () -> {
            boatModel.setSelectedImage(getDefaultBoatPhotoDTO());
            setDefaultImage();
        });
        boatModel.dataLoadedProperty().addListener(dataLoadedListener);
        vBox.getChildren().add(new ImageViewPane(imageView));
        return vBox;
    }

    private BoatPhotosDTO getDefaultBoatPhotoDTO() {
        return boatModel.getImages().stream()
                .filter(BoatPhotosDTO::isDefault)
                .findFirst()
                .orElse(new BoatPhotosDTO(0, 0, "", "no_image.png", 0, true));
    }

    private void moveToNextImage(boolean moveForward) {
        // put them in ascending order, in case a new image has recently been added
        boatModel.getImages().sort(Comparator.comparingInt(BoatPhotosDTO::getFileNumber));
        // find index of current image
        int index = boatModel.getImages().indexOf(boatModel.getSelectedImage());
        if (moveForward) {
            if (index < boatModel.getImages().size() - 1) index++;
            else index = 0;
        } else { // we are moving backwards
            if (index == 0) index = boatModel.getImages().size() - 1;
            else index--;
        }
        boatModel.setSelectedImage(boatModel.getImages().get(index));
        String localFile = BOAT_LOCAL_PATH + boatModel.getSelectedImage().getFilename();
        String remoteFile = BOAT_REMOTE_PATH + boatModel.getSelectedImage().getFilename();
        // if we don't have file on local computer then retrieve it
//        if (!FileIO.fileExists(localFile)) scp.getFile(remoteFile, localFile);
        Image image = new Image("file:" + localFile);
        boatModel.getImageView().setImage(image);
    }

    private void setDefaultImage() {
        Image image;
        String localFile = BOAT_LOCAL_PATH + boatModel.getSelectedImage().getFilename();
        String remoteFile = BOAT_REMOTE_PATH + boatModel.getSelectedImage().getFilename();
        if(boatModel.getSelectedImage().getFilename().equals("no_image.png")) {
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/no_image.png")));
        } else if(FileIO.fileExists(localFile)) {
            image = new Image("file:" + localFile);
        } else {
//   TODO         scp.getFile(remoteFile,localFile);
            image = new Image("file:" + localFile);
        }
        boatModel.getImageView().setImage(image);
    }

    protected BoatModel getBoatModel() {
        return boatModel;
    }

    protected Consumer<BoatMessage> sendMessage() {
        return action;
    }

}
