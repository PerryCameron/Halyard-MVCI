package org.ecsail.mvci_boat;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.util.Builder;
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

    private final BoatModel boatModel;
    protected Consumer<BoatMessage> action;
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
        hBoxOuter.setVisible(false);
        ChangeListener<Boolean> dataLoadedListener = ListenerFx.addSingleFireBooleanListener(boatModel.dataLoadedProperty(), () -> hBoxOuter.setVisible(true));
        boatModel.dataLoadedProperty().addListener(dataLoadedListener);
        TitledPane titledPane = TitledPaneFx.titledPaneOf("Notes");
        HBox hBox = new HBox();
        VBox vBoxTable = VBoxFx.vBoxOf(new Insets(0,0,0,0));
        VBox vBoxButtons = VBoxFx.vBoxOf(80.0,5.0,new Insets(0,0,0,5));
        vBoxButtons.getChildren().addAll(
                createButton("Add",BoatMessage.INSERT_NOTE),
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
        vBox.setVisible(false);
        ChangeListener<Boolean> dataLoadedListener = ListenerFx.addSingleFireBooleanListener(boatModel.dataLoadedProperty(), () -> vBox.setVisible(true));
        boatModel.dataLoadedProperty().addListener(dataLoadedListener);
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
                createButton("Add",BoatMessage.OWNER_DIALOGUE),
                createButton("Delete",BoatMessage.DELETE_OWNER));
        vBoxTable.getChildren().add(new BoatOwnerTableView(this).build());
        hBox.getChildren().addAll(vBoxTable,vBoxButtons);
        return hBox;
    }

    private Node createButton(String name, BoatMessage type) {
        Button button = ButtonFx.buttonOf(name, 60.0);
        switch (type) {
            case INSERT_NOTE -> button.setOnAction(event -> action.accept(BoatMessage.INSERT_NOTE));
            case OWNER_DIALOGUE -> button.setOnAction(event -> launchCustomOwnerDialogue());
            case DELETE_IMAGE -> button.setOnAction(event -> deleteImage());
            case DELETE_NOTE -> button.setOnAction(event -> deleteNote());
            case DELETE_OWNER -> button.setOnAction(event -> deleteOwner());
            case SET_DEFAULT -> button.setOnAction(event -> action.accept(BoatMessage.SET_DEFAULT));
            case MOVE_FORWARD -> button.setOnAction(event -> moveToNextImage(true));
            case MOVE_BACKWARD -> button.setOnAction(event -> moveToNextImage(false));
        }
        return button;
    }

    private void launchCustomOwnerDialogue() {
        CustomOwnerDialogue customOwnerDialogue = new CustomOwnerDialogue(this);
        Alert alert = customOwnerDialogue.build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            boatModel.getBoatOwners().add(boatModel.getSelectedOwner());
            action.accept(BoatMessage.INSERT_OWNER);
            customOwnerDialogue.removeListeners();
        }
    }

    private void deleteNote() {
        String[] strings = {
                "Delete Note",
                "Are you sure you want to delete this note?",
                "Missing Selection",
                "You need to select a note first"};
        if(DialogueFx.verifyAction(strings, boatModel.getSelectedNote()))
            action.accept(BoatMessage.DELETE_NOTE);
    }

    private void deleteOwner() {
        String[] strings = {
                "Delete Owner",
                "Are you sure you want to delete this owner?",
                "Missing Selection",
                "You need to select an owner first"};
        if(DialogueFx.verifyAction(strings, boatModel.getSelectedOwner()))
            action.accept(BoatMessage.DELETE_OWNER);
    }

    private void deleteImage() {
        String[] strings = {
                "Delete Image",
                "Are you sure you want to delete this image?",
                "Missing Selection",
                "You need to select an image first"};
        if(DialogueFx.verifyAction(strings, boatModel.getSelectedImage()))
            action.accept(BoatMessage.DELETE_IMAGE);
    }

    private Node boatInfoTitlePane() {
        TitledPane titledPane = new TitledPane();
        titledPane.setVisible(false);
        var vBox = new VBox();
        vBox.setId("box-grey");
        ChangeListener<Boolean> dataLoadedListener = ListenerFx.addSingleFireBooleanListener(boatModel.dataLoadedProperty(), () -> {
            for (DbBoatSettingsDTO dbBoatSettingsDTO : boatModel.getBoatSettings())
                vBox.getChildren().add(new Row(this, dbBoatSettingsDTO));
            titledPane.setVisible(true);
        });
        boatModel.dataLoadedProperty().addListener(dataLoadedListener);
        titledPane.setContent(vBox);
        titledPane.setText("Boat Information");
        return titledPane;
    }

    private Node setUpPicture() {
        VBox vBox = new VBox();
        vBox.setVisible(false);
        HBox.setHgrow(vBox, Priority.ALWAYS);
        vBox.getChildren().addAll(addPictureControls(), addPicture(vBox));
        return vBox;
    }

    private Node addPictureControls() {
        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER, 10.0);
        hBox.setPrefHeight(40);
        hBox.setSpacing(7);
        hBox.getChildren().add(createButton("<",BoatMessage.MOVE_BACKWARD));
        hBox.getChildren().add(createButton(">",BoatMessage.MOVE_FORWARD));
        hBox.getChildren().add(createButton("Delete",BoatMessage.DELETE_IMAGE));
        hBox.getChildren().add(createButton("Default",BoatMessage.SET_DEFAULT));
        return hBox;
    }

    private Node addPicture(VBox parentVBox) {
        VBox vBox = VBoxFx.vBoxOf(630,489, true, true);
        vBox.setPadding(new Insets(0,10,0,10));
        ImageView imageView = new ImageView();
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);
        imageView.setCache(true);
        boatModel.setImageView(imageView);
        ChangeListener<Boolean> dataLoadedListener = ListenerFx.addSingleFireBooleanListener(boatModel.dataLoadedProperty(), () -> {
            BoatPhotosDTO boatPhotosDTO = getDefaultBoatPhotoDTO();
            boatModel.setSelectedImage(boatPhotosDTO);
            setImage();
            parentVBox.setVisible(true);
        });
        boatModel.dataLoadedProperty().addListener(dataLoadedListener);
        imageView.setOnDragOver(event -> handleDragOver(event, imageView));
        imageView.setOnDragDropped(this::handleDragDrop);
        vBox.getChildren().add(new ImageViewPane(imageView));
        return vBox;
    }

    public void handleDragOver(DragEvent event, ImageView imageView) {
        if (event.getGestureSource() != imageView && event.getDragboard().hasFiles()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    private void handleDragDrop(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            boatModel.setSelectedPath(db.getFiles().get(0).getAbsolutePath());
            action.accept(BoatMessage.INSERT_IMAGE);
            success = true; // you probably want to set success to true here based on the context.
        }
        event.setDropCompleted(success);
        event.consume();
    }

    private BoatPhotosDTO getDefaultBoatPhotoDTO() {
        return boatModel.getImages().stream()
                .filter(BoatPhotosDTO::isDefault)
                .findFirst()
                .orElse(new BoatPhotosDTO(0, 0, "", "no_image.png", 0, true));
    }

    private void moveToNextImage(boolean moveForward) {
        if(boatModel.getImages().size() > 0) { // prevents exception if button hit and there are no images
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
            setImage();
        }
    }

    private void setImage() {
        Image image;
        String localFile;
        if(boatModel.getSelectedImage().getFilename().equals("no_image.png"))
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/no_image.png")));
        else {
            localFile = BOAT_LOCAL_PATH + boatModel.getSelectedImage().getFilename();
            // if we don't have file on local computer then retrieve it
            if (!FileIO.fileExists(localFile)) action.accept(BoatMessage.DOWNLOAD_IMAGE);
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
