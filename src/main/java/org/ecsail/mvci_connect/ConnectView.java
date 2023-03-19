package org.ecsail.mvci_connect;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Builder;
import javafx.util.Duration;
import org.ecsail.widgetfx.*;

import java.util.Objects;

public class ConnectView implements Builder<Region> {

    private Stage loginStage;
    private ConnectModel connectModel;
    public ConnectView(ConnectModel model) {
        this.connectModel = model;
    }

    @Override
    public Region build() {
        BorderPane pane = new BorderPane();
        pane.setCenter(createRightBox());
        pane.setLeft(createLeftBox());
        pane.setBottom(createBottomBox());
        return pane;
    }

    private Node createRightBox() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(20,20,0,20));
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ships_wheel.png")));
        ImageView imageView = new ImageView(image);
        createRotateShipsWheel(imageView);
        vBox.getChildren().addAll(imageView);
        return vBox;
    }

    private Node createLeftBox() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,0,0,15), connectModel.centerPaneHeightProperty());
        HBox errorBox = new HBox();
        vBox.getChildren().addAll(errorBox, UserBox(), PassBox(), HostBox(), ButtonBox());
        return vBox;
    }

    private Node UserBox() { // 1
        HBox hBox = HBoxFx.hBoxOf(new Insets(20,5,5,5));
        HBox hboxUserLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90);
        HBox hboxUserText = new HBox();
        hboxUserLabel.getChildren().add(new Label("Username:"));
        TextField userName = TextFieldFx.textFieldOf(200,"Username");
        userName.textProperty().bindBidirectional(connectModel.userProperty());
        hboxUserText.getChildren().add(userName);
        hBox.getChildren().addAll(hboxUserLabel, hboxUserText);
        return hBox;
    }

    private Node PassBox() { // 2
        HBox hBox = HBoxFx.hBoxOf(new Insets(5));
        PasswordField passwordField = TextFieldFx.passwordFieldOf(200,"Password");
        HBox hboxPassLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90);
        HBox hboxPassText = new HBox();
        hboxPassLabel.getChildren().add(new Label("Password:"));
        hboxPassText.getChildren().add(passwordField);
        hBox.getChildren().addAll(hboxPassLabel,hboxPassText);
        return hBox;
    }

    private Node HostBox() { // 3
        HBox hBox = HBoxFx.hBoxOf(new Insets(5));
        HBox hboxHostLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90);
        hboxHostLabel.getChildren().add(new Label("Hostname:"));
        HBox hboxHostText = new HBox();
        ComboBox<String> hostName = ComboBoxFx.comboBoxOf(200, connectModel.getChoices());
        hboxHostText.getChildren().add(hostName);
        hBox.getChildren().addAll(hboxHostLabel,hboxHostText);
        return hBox;
    }

    private Node ButtonBox() { // 8
        HBox containerBox = HBoxFx.boundBoxOf(connectModel.containerBoxProperty());
        HBox hBox = HBoxFx.boundBoxOf(new Insets(15,5,20,5), connectModel.buttonBoxProperty());
        HBox buttonBox = HBoxFx.hBoxOf(new Insets(0,0,0,35),10);
        HBox TextBox = HBoxFx.hBoxOf(Pos.CENTER_LEFT,15, 15);
        Text newConnectText = TextFx.linkTextOf("New");
        Text editConnectText = TextFx.linkTextOf("Edit");
        editConnectText.setOnMouseClicked(event -> setEditMode(true));
        newConnectText.setOnMouseClicked(event ->  setNewMode(true));
        Button loginButton = new Button("Login");
        loginButton.setOnAction((event) -> {
            connectModel.setRotateShipWheel(true);
//            connectToServer();
        });
        Button cancelButton1 = new Button("Cancel");
        cancelButton1.setOnAction((event) -> System.exit(0));
        TextBox.getChildren().addAll(newConnectText,editConnectText);
        buttonBox.getChildren().addAll(loginButton,cancelButton1);
        hBox.getChildren().addAll(TextBox, buttonBox);
        containerBox.getChildren().add(hBox);
        return containerBox;
    }

    private void setNewMode(Boolean mode) {
        connectModel.setNewMode(mode);
    }

    private void setEditMode(Boolean mode) {
        connectModel.setEditMode(mode);
    }

    private Node createBottomBox() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,0,0,15), connectModel.bottomPaneHeightProperty());
        setModeChangeListener(vBox, connectModel.editModeProperty(),true);
        setModeChangeListener(vBox, connectModel.newModeProperty(),false);
        return vBox;
    }

    private void setModeChangeListener(VBox vBox, BooleanProperty modeChangeProperty, Boolean isEdit) {
        modeChangeProperty.addListener((observable, oldValue, newValue) -> {
            if(newValue) { // we are changing the mode
                vBox.getChildren().addAll(createSqlPortBox(),createUseSshBox(),createSshUserBox(), createKnownHostsBox(),
                        createEditButtonsBox(), HBoxFx.spacerOf(15));
                connectModel.containerBoxProperty().get().getChildren().clear();
            } else {
                vBox.getChildren().clear();
                connectModel.getContainerBox().getChildren().add(connectModel.getButtonBox());
            }
        });
    }

    private Node createSqlPortBox() { // 4
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        TextField localSqlPortText = TextFieldFx.textFieldOf(60, connectModel.localSqlPortProperty());
        connectModel.setLocalSqlPort("3306"); // shouldn't be a string
        HBox hboxPortLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90, new Insets(5));
        hboxPortLabel.getChildren().add(new Label("SQL Port:"));
        HBox hboxPortText = HBoxFx.hBoxOf(Pos.CENTER_LEFT,200,new Insets(5),20);
        hboxPortText.getChildren().add(localSqlPortText);
        hBox.getChildren().addAll(hboxPortLabel, hboxPortText);
        return hBox;
    }

    private Node createUseSshBox() { // 5
        HBox hBox = new HBox();
        HBox useSshTunnelLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90, new Insets(5,5,5,5));
        useSshTunnelLabel.getChildren().add(new Label("ssh tunnel:"));
        CheckBox checkBox = new CheckBox();
        HBox useSshTunnel = HBoxFx.hBoxOf(Pos.CENTER_LEFT,200,new Insets(5,5,5,5),20);
        useSshTunnel.getChildren().add(checkBox);
        hBox.getChildren().addAll(useSshTunnelLabel, useSshTunnel);
        return hBox;
    }

    private Node createSshUserBox() { // 6
        HBox hBox = new HBox();
        HBox hboxSshUserLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90, new Insets(5,5,5,5));
        hboxSshUserLabel.getChildren().add(new Label("ssh user:"));
        HBox hboxSshUserText = HBoxFx.hBoxOf(new Insets(5));
        TextField sshUser = TextFieldFx.textFieldOf(200,"");
        hboxSshUserText.getChildren().add(sshUser);
        hBox.getChildren().addAll(hboxSshUserLabel,hboxSshUserText);
        return hBox;
    }

    private Node createKnownHostsBox() { // 7
        HBox hBox = new HBox();
        TextField knownHost = TextFieldFx.textFieldOf(200, connectModel.knownHostProperty());
        HBox knownHostLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90, new Insets(5,5,5,5));
        knownHostLabel.getChildren().add(new Label("knownhosts:"));
        HBox knownHostText = HBoxFx.hBoxOf(new Insets(5));
        knownHostText.getChildren().add(knownHost);
        hBox.getChildren().addAll(knownHostLabel,knownHostText);
        return hBox;
    }

    private Node createEditButtonsBox() {
        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER, new Insets(20,0,0,0),10);
        Button buttonSave = new Button("Save");
        Button buttonDelete = new Button("Delete");
        Button buttonCancel = new Button("Cancel");
        buttonCancel.setOnAction(event -> { connectModel.setNewMode(false); connectModel.setEditMode(false); });
        hBox.getChildren().addAll(buttonSave,buttonDelete,buttonCancel);
        return hBox;
    }

    private void createRotateShipsWheel(ImageView imageView) {
            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(5), imageView);
            rotateTransition.setByAngle(360);
            rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
            rotateTransition.setInterpolator(Interpolator.LINEAR);
            connectModel.rotateShipWheelProperty().addListener(observable -> {
                if(connectModel.isRotateShipWheel()) rotateTransition.play();
                else rotateTransition.stop();
            });
    }

    public void createStage(Region region) {
        loginStage = new Stage();
        loginStage.setScene(new Scene(region));
        loginStage.getScene().getStylesheets().add("css/dark/dark.css");
        loginStage.show();
        loginStage.setAlwaysOnTop(true);
        loginStage.requestFocus();
        loginStage.toFront();
        loginStage.setResizable(false);
        setStageHeightListener();
    }

    private void setStageHeightListener() {
        connectModel.bottomPaneHeightProperty().addListener((observable) -> {
            connectModel.setTitleBarHeight(loginStage.getHeight() - loginStage.getScene().getHeight());
            loginStage.setHeight(connectModel.getBottomPaneHeight()
                    + connectModel.getTitleBarHeight()
                    + connectModel.getCenterPaneHeight());
        });

    }
}
