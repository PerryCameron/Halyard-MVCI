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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Builder;
import javafx.util.Duration;
import org.ecsail.BaseApplication;
import org.ecsail.dto.LoginDTO;
import org.ecsail.widgetfx.*;

import java.util.Objects;

public class ConnectView implements Builder<Region> {
    private final ConnectModel connectModel;
    public ConnectView(ConnectModel model) {
        this.connectModel = model;
    }

    @Override
    public Region build() {
        BorderPane pane = new BorderPane();
        pane.setCenter(createRightBox());
        pane.setLeft(createLeftBox());
        pane.setBottom(createBottomBox());
        setSelectedLoginDTOListener();
        connectModel.setSelectedLogin(selectLoginDTO());
        populatePropertiesFromSelectedLoginDTO(selectLoginDTO());
        return pane;
    }

    private Node createRightBox() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(15,20,0,20));
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
        passwordField.textProperty().bindBidirectional(connectModel.passProperty());
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
        HBox hBoxHostContainer = new HBox();
        HBox hBoxHostComboBox = CreateComboBox(200);
        HBox hBoxHostTextField = new HBox();
        TextField hostName = TextFieldFx.textFieldOf(200,"Host");
        hBoxHostTextField.getChildren().add(hostName);
        connectModel.getObservableMap().put("host-container",hBoxHostContainer);
        connectModel.getObservableMap().put("host-combo-box",hBoxHostComboBox);
        connectModel.getObservableMap().put("host-text-field", hBoxHostTextField);
        hostName.textProperty().bindBidirectional(connectModel.hostProperty());
        hboxHostLabel.getChildren().add(new Label("Hostname:"));
        hBoxHostContainer.getChildren().add(hBoxHostComboBox);
        hBox.getChildren().addAll(hboxHostLabel,hBoxHostContainer);
        return hBox;
    }

    private HBox CreateComboBox(double width) {
        HBox hBox = new HBox();
        LogInComboBox comboBox = new LogInComboBox();
        comboBox.setPrefWidth(width);
        comboBox.getItems().addAll(connectModel.getLoginDTOS());
        comboBox.setValue(connectModel.getSelectedLogin());
        connectModel.setComboBox(comboBox);
        hBox.getChildren().add(comboBox);
        comboBox.valueProperty().addListener((Observable, oldValue, newValue) -> {
            if(newValue != null) connectModel.setSelectedLogin(newValue);
            else {
                comboBox.getItems().add(ObjectFx.createLoginDTO());
                comboBox.setValue(connectModel.getSelectedLogin());
            }
        });
        return hBox;
    }

    private Node ButtonBox() { // 8
        HBox container = new HBox();
        connectModel.getObservableMap().put("button-box-container", container);
        HBox hBox = HBoxFx.hBoxOf(new Insets(15,5,20,5));
        connectModel.getObservableMap().put("button-box", hBox);
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
        container.getChildren().add(hBox);
        return container;
    }

    private Node createBottomBox() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,0,0,15), connectModel.bottomPaneHeightProperty());
        setModeChangeListener(vBox, connectModel.editModeProperty());
        setModeChangeListener(vBox, connectModel.newModeProperty());
        return vBox;
    }

    private void standardMode(VBox vBox) {
        vBox.getChildren().clear();
        connectModel.getObservableMap().get("button-box-container").getChildren()
                .add(connectModel.getObservableMap().get("button-box"));
        connectModel.getObservableMap().get("host-container").getChildren().clear();
        connectModel.getObservableMap().get("host-container").getChildren()
                .add(connectModel.getObservableMap().get("host-combo-box"));
    }

    private void editMode(VBox vBox) {
        vBox.getChildren().addAll(createSqlPortBox(),createUseSshBox(),createSshUserBox(), createKnownHostsBox(),
                createEditButtonsBox());
        connectModel.getObservableMap().get("button-box-container").getChildren().clear();
        connectModel.getObservableMap().get("host-container").getChildren().clear();
        connectModel.getObservableMap().get("host-container").getChildren()
                .add(connectModel.getObservableMap().get("host-text-field"));
    }

    private Node createSqlPortBox() { // 4
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        TextField localSqlPortText = TextFieldFx.textFieldOf(60, connectModel.localSqlPortProperty());
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
        checkBox.selectedProperty().bindBidirectional(connectModel.sshUsedProperty());
        useSshTunnel.getChildren().add(checkBox);
        hBox.getChildren().addAll(useSshTunnelLabel, useSshTunnel);
        return hBox;
    }

    private Node createSshUserBox() { // 6
        HBox hBox = new HBox();
        HBox hboxSshUserLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90, new Insets(5,5,5,5));
        hboxSshUserLabel.getChildren().add(new Label("ssh user:"));
        HBox hboxSshUserText = HBoxFx.hBoxOf(new Insets(5));
        TextField sshUser = TextFieldFx.textFieldOf(200,connectModel.sshUserProperty());
        hboxSshUserText.getChildren().add(sshUser);
        hBox.getChildren().addAll(hboxSshUserLabel,hboxSshUserText);
        return hBox;
    }

    private Node createKnownHostsBox() { // 7
        HBox hBox = new HBox();
        TextField knownHost = TextFieldFx.textFieldOf(200, connectModel.knownHostsProperty());
        HBox knownHostLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90, new Insets(5,5,5,5));
        knownHostLabel.getChildren().add(new Label("knownhosts:"));
        HBox knownHostText = HBoxFx.hBoxOf(new Insets(5));
        knownHostText.getChildren().add(knownHost);
        Button button = new Button("Create");
        HBox createKnownHost = HBoxFx.hBoxOf(new Insets(5));
        createKnownHost.getChildren().add(button);
        hBox.getChildren().addAll(knownHostLabel,knownHostText,createKnownHost);
        return hBox;
    }

    private Node createEditButtonsBox() {
        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER, new Insets(20,0,20,0),10);
        Button buttonSave = new Button("Save");
        buttonSave.setOnAction(event -> {
            populateSelectedLoginFromControlProperties();
            connectModel.setNewMode(false);
            connectModel.setEditMode(false);
        });
        Button buttonDelete = new Button("Delete");
        buttonDelete.setOnAction(event -> {
            connectModel.getComboBox().getItems().remove(connectModel.getSelectedLogin());
            connectModel.setNewMode(false);
            connectModel.setEditMode(false);
        });
        Button buttonCancel = new Button("Cancel");
        buttonCancel.setOnAction(event -> {
            connectModel.setNewMode(false);
            connectModel.setEditMode(false);
        });
        hBox.getChildren().addAll(buttonSave,buttonDelete,buttonCancel);
        return hBox;
    }

    private void setNewMode(Boolean mode) {
        connectModel.setNewMode(mode);
        // create a new login object put it in the list and select it as the new object
        // might be best to put this in the setNewMode() ore setEditMode() methods
    }

    private void setEditMode(Boolean mode) {
        connectModel.setEditMode(mode);
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
        BaseApplication.loginStage = new Stage();
        BaseApplication.loginStage.setScene(new Scene(region));
        BaseApplication.loginStage.getScene().getStylesheets().add("css/dark/dark.css");
        BaseApplication.loginStage.setAlwaysOnTop(true);
        BaseApplication.loginStage.requestFocus();
        BaseApplication.loginStage.toFront();
        BaseApplication.loginStage.setResizable(false);
        setStageHeightListener();
    }

    private LoginDTO selectLoginDTO() {
        return connectModel.getLoginDTOS().stream()
                .filter(LoginDTO::isDefault).findFirst().orElse(null);
    }

    private LoginDTO changeSelectedLoginDTO(String host) {
        return connectModel.getLoginDTOS()
                .stream().filter(dto -> host.equals(dto.getHost())).findFirst().orElse(null);
    }

    private void setModeChangeListener(VBox vBox, BooleanProperty modeChangeProperty) {
        modeChangeProperty.addListener((observable, oldValue, isEditMode) -> {
            if(isEditMode) editMode(vBox);
            else standardMode(vBox);
        });
    }

    private void setSelectedLoginDTOListener() {
        connectModel.selectedLoginProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) populatePropertiesFromSelectedLoginDTO(newValue);
        });
    }

    private void clearControls() {
        connectModel.setUser("");
        connectModel.setPass("");
        connectModel.setHost("");
        connectModel.setSshUsed(true);
        connectModel.setLocalSqlPort(3306);
        connectModel.setSshUser("");
        connectModel.setKnownHosts("");
    }

    private void populatePropertiesFromSelectedLoginDTO(LoginDTO newValue) {
        connectModel.setUser(newValue.getUser());
        connectModel.setPass(newValue.getPasswd());
        connectModel.setHost(newValue.getHost());
        connectModel.setSshUsed(newValue.isSshForward());
        connectModel.setLocalSqlPort(newValue.getLocalSqlPort());
        connectModel.setSshUser(newValue.getSshUser());
        connectModel.setKnownHosts(newValue.getKnownHostsFile());
    }

    private void populateSelectedLoginFromControlProperties() {
        connectModel.getSelectedLogin().setUser(connectModel.getUser());
        connectModel.getSelectedLogin().setPasswd(connectModel.getPass());
        connectModel.getSelectedLogin().setHost(connectModel.getHost());
        connectModel.getSelectedLogin().setSshForward(connectModel.isSshUsed());
        connectModel.getSelectedLogin().setSshUser(connectModel.getSshUser());
        connectModel.getSelectedLogin().setLocalSqlPort(connectModel.getLocalSqlPort());
        connectModel.getSelectedLogin().setKnownHostsFile(connectModel.getKnownHosts());
    }

    private void setStageHeightListener() {
        connectModel.bottomPaneHeightProperty().addListener((observable) -> {
            connectModel.setTitleBarHeight(BaseApplication.loginStage.getHeight() - BaseApplication.loginStage.getScene().getHeight());
            BaseApplication.loginStage.setHeight(connectModel.getBottomPaneHeight()
                    + connectModel.getTitleBarHeight()
                    + connectModel.getCenterPaneHeight());
        });
    }
}
