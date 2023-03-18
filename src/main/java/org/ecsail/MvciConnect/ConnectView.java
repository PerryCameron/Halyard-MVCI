package org.ecsail.MvciConnect;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.ecsail.widgetfx.ComboBoxFx;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.TextFieldFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.Objects;

public class ConnectView implements Builder<Region> {

    private Stage loginStage;
    private ConnectModel connectModel;
    public ConnectView(ConnectModel model) {
        this.connectModel = model;
    }

    @Override
    public Region build() {
        Pane pane = new Pane();
        pane.setPrefSize(200,300);
        VBox mainBox = VBoxFx.vBoxOf(500,10,10,10,10,"box-frame-dark");
        mainBox.getChildren().add(new HBox(createLeftBox(), createRightBox()));
        pane.getChildren().add(mainBox);
        return pane;
    }

    private Node createRightBox() {
        VBox vBox = VBoxFx.vBoxOf(20,20,0,20);
        Image loginLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ships_wheel.png")));
        ImageView logo = new ImageView(loginLogo);
        vBox.getChildren().addAll(logo);
        return vBox;
    }

    private Node createLeftBox() {
        VBox vBox = VBoxFx.vBoxOf(0,0,0,15);
        HBox errorHBox = new HBox();
        HBox box4 = new HBox();
        box4.setAlignment(Pos.CENTER_LEFT);
        HBox box5 = HBoxFx.hBoxOf(5,5,5,5);
        HBox box6 = new HBox();
        HBox box7 = new HBox();
        HBox box8 = HBoxFx.hBoxOf(15,5,5,5);
        vBox.getChildren().addAll(errorHBox,createUserBox(), createPassBox(),createHostBox(),box4,box5,box6,box7,box8);
        return vBox;
    }


    private Node createUserBox() {
        HBox hBox = HBoxFx.hBoxOf(20,5,5,5);
        HBox hboxUserLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90);
        HBox hboxUserText = new HBox();
        hboxUserLabel.getChildren().add(new Label("Username:"));
        TextField userName = TextFieldFx.textFieldOf(200,"Username");
        userName.textProperty().bindBidirectional(connectModel.userProperty());
        hboxUserText.getChildren().add(userName);
        hBox.getChildren().addAll(hboxUserLabel, hboxUserText);
        return hBox;
    }

    private Node createPassBox() {
        HBox hBox = HBoxFx.hBoxOf(5,5,5,5);
        PasswordField passwordField = TextFieldFx.passwordFieldOf(200,"Password");
        HBox hboxPassLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90);
        HBox hboxPassText = new HBox();
        hboxPassLabel.getChildren().add(new Label("Password:"));
        hboxPassText.getChildren().add(passwordField);
        hBox.getChildren().addAll(hboxPassLabel,hboxPassText);
        return hBox;
    }

    private Node createHostBox() {
        HBox hBox = HBoxFx.hBoxOf(5,5,5,5);
        HBox hboxHostLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90);
        hboxHostLabel.getChildren().add(new Label("Hostname:"));
        HBox hboxHostText = new HBox();
        ComboBox<String> hostName = ComboBoxFx.comboBoxOf(200, connectModel.getChoices());
        hboxHostText.getChildren().add(hostName);
        hBox.getChildren().addAll(hboxHostLabel,hboxHostText);
        return hBox;
    }

    public void createStage(Region region) {
        loginStage = new Stage();
        loginStage.setScene(new Scene(region,500,200));
        loginStage.getScene().getStylesheets().add("css/dark/dark.css");
        loginStage.show();
        loginStage.setAlwaysOnTop(true);
        loginStage.requestFocus();
        loginStage.toFront();
    }
}
