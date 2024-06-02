package org.ecsail.mvci_connect;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;
import javafx.util.Duration;
import org.ecsail.BaseApplication;
import org.ecsail.dto.LoginDTO;
import org.ecsail.interfaces.RunState;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.TextFieldFx;
import org.ecsail.widgetfx.TextFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.Objects;
import java.util.function.Consumer;

public class ConnectView implements Builder<Region> {
    private final ConnectModel connectModel;
    private final RunState runState;
    Consumer<ConnectMessage> action;

//    private final LoginDTOListSupplier loginSupplier;
    public ConnectView(ConnectModel model, Consumer<ConnectMessage> action) {
        this.connectModel = model;
        this.runState = new RunStateImpl(model);
        this.action = action;
    }

    @Override
    public Region build() {
        setUpStage();
        BorderPane pane = new BorderPane();
        pane.setCenter(createRightBox());
        pane.setLeft(createLeftBox());
        pane.setBottom(createBottomBox());
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
        vBox.getChildren().addAll(UserBox(), PassBox(), HostBox(), createButtonBox());
        return vBox;
    }

    private Node UserBox() { // 1
        HBox hBox = HBoxFx.hBoxOf(new Insets(20,5,5,5));
        HBox hboxUserLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90);
        HBox hboxUserText = new HBox();
        hboxUserLabel.getChildren().add(new Label("Username:"));
        TextField userName = TextFieldFx.textFieldOf(200,"Username");
        userName.textProperty().bindBidirectional(connectModel.sqlUserProperty());
        hboxUserText.getChildren().add(userName);
        hBox.getChildren().addAll(hboxUserLabel, hboxUserText);
        return hBox;
    }

    private Node PassBox() { // 2
        HBox hBox = HBoxFx.hBoxOf(new Insets(5));
        PasswordField passwordField = TextFieldFx.passwordFieldOf(200,"Password");
        passwordField.textProperty().bindBidirectional(connectModel.sqlPassProperty());
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
        HBox hBoxHostTextField = new HBox();
        TextField hostName = TextFieldFx.textFieldOf(200,"Host");
        hBoxHostTextField.getChildren().add(hostName);
        connectModel.getHBoxMap().put("host-container-box",hBoxHostContainer);
        connectModel.getHBoxMap().put("host-text-field", hBoxHostTextField);
        hostName.textProperty().bindBidirectional(connectModel.hostProperty());
        hboxHostLabel.getChildren().add(new Label("Hostname:"));
        hBoxHostContainer.getChildren().add(createComboBox());
        hBox.getChildren().addAll(hboxHostLabel,hBoxHostContainer);
        return hBox;
    }

    private Node createComboBox() {
        HBox hBox = new HBox();
        connectModel.getHBoxMap().put("host-combo-box",hBox);
        LogInComboBox comboBox = new LogInComboBox(200, connectModel.getLoginDTOS());
        connectModel.setComboBox(comboBox);
        comboBox.setValue(comboBox.getItems().stream().filter(LoginDTO::isDefault).findFirst().orElse(null));
        updateFields();
        hBox.getChildren().add(comboBox);
        comboBox.valueProperty().addListener((Observable, oldValue, newValue) -> {
            if(newValue != null) updateFields();
            else connectModel.getComboBox().getSelectionModel().select(connectModel.getComboBox().getItems().size() - 1);
        });
        return hBox;
    }

    private Node createButtonBox() { // 8
        HBox container = new HBox();
        connectModel.getHBoxMap().put("button-container-box", container);
        HBox hBox = HBoxFx.hBoxOf(new Insets(15,5,20,5));
        connectModel.getHBoxMap().put("button-box", hBox);
        HBox buttonBox = HBoxFx.hBoxOf(new Insets(0,0,0,35),10);
        HBox TextBox = HBoxFx.hBoxOf(Pos.CENTER_LEFT,15, 15);
        Text newConnectText = TextFx.linkTextOf("New");
        Text editConnectText = TextFx.linkTextOf("Edit");
        editConnectText.setOnMouseClicked(event -> runState.setMode(RunState.Mode.EDIT));
        newConnectText.setOnMouseClicked(event -> runState.setMode(RunState.Mode.NEW));
        Button loginButton = new Button("Login");
        loginButton.setOnAction((event) -> {
            connectModel.setRotateShipWheel(true);
            action.accept(ConnectMessage.CONNECT_TO_SERVER);
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction((event) -> System.exit(0));
        TextBox.getChildren().addAll(newConnectText,editConnectText);
        buttonBox.getChildren().addAll(loginButton,cancelButton);
        hBox.getChildren().addAll(TextBox, buttonBox);
        container.getChildren().add(hBox);
        return container;
    }

    private Node createBottomBox() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,0,0,15), connectModel.bottomPaneHeightProperty());
        connectModel.getVBoxMap().put("bottom-container-box",createBottomContainerBox());
        connectModel.getVBoxMap().put("bottom-box", vBox);
        return connectModel.getVBoxMap().get("bottom-box");
    }

    private VBox createBottomContainerBox() {
        VBox vBox = new VBox();
        vBox.getChildren().addAll(createSqlPortBox(),createUseSshBox(),createDataBaseBox(), createSshUserBox(),
                createKnownHostsBox(), createPrivateKeyBox(), createEditButtonsBox());
        return vBox;
    }

    private Node createSqlPortBox() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        TextField localSqlPortText = TextFieldFx.textFieldOf(60, connectModel.localSqlPortProperty());
        HBox hboxPortLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90, new Insets(5));
        hboxPortLabel.getChildren().add(new Label("SQL Port:"));
        HBox hboxPortText = HBoxFx.hBoxOf(Pos.CENTER_LEFT,100,new Insets(5),20);
        hboxPortText.getChildren().add(localSqlPortText);
        hBox.getChildren().addAll(hboxPortLabel, hboxPortText, setDefaultCheckBox());
        return hBox;
    }

    private Node createUseSshBox() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        TextField localSshPortText = TextFieldFx.textFieldOf(60, connectModel.sshPortProperty());
        HBox useSshTunnelLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90, new Insets(5));
        useSshTunnelLabel.getChildren().add(new Label("SSH Port:"));
        HBox hboxPortText = HBoxFx.hBoxOf(Pos.CENTER_LEFT,100,new Insets(5),20);
        hboxPortText.getChildren().add(localSshPortText);
        hBox.getChildren().addAll(useSshTunnelLabel, hboxPortText, setUseSshCheckBox());
        return hBox;
    }

    private Node setDefaultCheckBox() {
        CheckBox checkBox = new CheckBox("Default login");
        checkBox.selectedProperty().bindBidirectional(connectModel.defaultProperty());
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                connectModel.getComboBox().getItems().stream().forEach(loginDto -> loginDto.setDefault(false));
                connectModel.getComboBox().getValue().setDefault(true);
            }
        });
        return checkBox;
    }

    private Node setUseSshCheckBox() {
        CheckBox checkBox = new CheckBox("SSH Tunnel");
        checkBox.selectedProperty().bindBidirectional(connectModel.sshUsedProperty());
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                connectModel.getComboBox().getValue().setSshForward(newValue);
        });
        return checkBox;
    }

    private Node createDataBaseBox() {
        HBox hBox = new HBox();
        HBox hboxDataBaseLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90, new Insets(5));
        hboxDataBaseLabel.getChildren().add(new Label("Database:"));
        HBox hboxDataBaseText = HBoxFx.hBoxOf(new Insets(5));
        TextField dataBase = TextFieldFx.textFieldOf(200,connectModel.databaseProperty());
        hboxDataBaseText.getChildren().add(dataBase);
        hBox.getChildren().addAll(hboxDataBaseLabel,hboxDataBaseText);
        return hBox;
    }

    private Node createSshUserBox() {
        HBox hBox = new HBox();
        HBox hboxSshUserLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90, new Insets(5));
        hboxSshUserLabel.getChildren().add(new Label("ssh user:"));
        HBox hboxSshUserText = HBoxFx.hBoxOf(new Insets(5));
        TextField sshUser = TextFieldFx.textFieldOf(200,connectModel.sshUserProperty());
        hboxSshUserText.getChildren().add(sshUser);
        hBox.getChildren().addAll(hboxSshUserLabel,hboxSshUserText);
        return hBox;
    }

    private Node createKnownHostsBox() {
        HBox hBox = new HBox();
        TextField knownHost = TextFieldFx.textFieldOf(200, connectModel.knownHostsProperty());
        HBox knownHostLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90, new Insets(5,5,5,5));
        knownHostLabel.getChildren().add(new Label("knownhosts:"));
        HBox knownHostText = HBoxFx.hBoxOf(new Insets(5));
        knownHostText.getChildren().add(knownHost);
        Button button = new Button("Select");
        HBox createKnownHost = HBoxFx.hBoxOf(new Insets(5));
        createKnownHost.getChildren().add(button);
        hBox.getChildren().addAll(knownHostLabel,knownHostText,createKnownHost);
        return hBox;
    }

    private Node createPrivateKeyBox() {
        HBox hBox = new HBox();
        TextField privateKey = TextFieldFx.textFieldOf(200, connectModel.privateKeyProperty());
        HBox privateKeyLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90, new Insets(5,5,5,5));
        privateKeyLabel.getChildren().add(new Label("Private Key:"));
        HBox privateKeyText = HBoxFx.hBoxOf(new Insets(5));
        privateKeyText.getChildren().add(privateKey);
        Button button = new Button("Select");
        HBox createPrivateKey = HBoxFx.hBoxOf(new Insets(5));
        createPrivateKey.getChildren().add(button);
        hBox.getChildren().addAll(privateKeyLabel,privateKeyText,createPrivateKey);
        return hBox;
    }

    private Node createEditButtonsBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(20,0,20,0),Pos.CENTER, 10);
        Button buttonSave = new Button("Save");
        buttonSave.setOnAction(event -> {
            updateSelectedLogin();
            runState.setMode(RunState.Mode.NORMAL);
            action.accept(ConnectMessage.SAVE_LOGINS);
            updateFields();
        });
        Button buttonDelete = new Button("Delete");
        buttonDelete.setOnAction(event -> {
            connectModel.getComboBox().getItems().remove(connectModel.getComboBox().getValue());
            action.accept(ConnectMessage.SAVE_LOGINS);
            runState.setMode(RunState.Mode.NORMAL);
        });
        Button buttonCancel = new Button("Cancel");
        buttonCancel.setOnAction(event -> runState.setMode(RunState.Mode.NORMAL));
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

    private void updateFields() {
        LoginDTO newValue = connectModel.getComboBox().getValue();
        connectModel.setSqlUser(newValue.getSqlUser());
        connectModel.setSqlPass(newValue.getSqlPasswd());
        connectModel.setHost(newValue.getHost());
        connectModel.setSshUsed(newValue.isSshForward());
        connectModel.setLocalSqlPort(newValue.getLocalSqlPort());
        connectModel.setSshUser(newValue.getSshUser());
        connectModel.setKnownHosts(newValue.getKnownHostsFile());
        connectModel.setDefault(newValue.isDefault());
        connectModel.setSshPort(newValue.getSshPort());
        connectModel.setDatabase(newValue.getDatabase());
        connectModel.setPrivateKey(newValue.getPrivateKeyFile());
    }

    private void updateSelectedLogin() {
        connectModel.getComboBox().getValue().setSqlUser(connectModel.getSqlUser());
        connectModel.getComboBox().getValue().setSqlPasswd(connectModel.getSqlPass());
        connectModel.getComboBox().getValue().setHost(connectModel.getHost());
        connectModel.getComboBox().getValue().setSshForward(connectModel.sshUsed());
        connectModel.getComboBox().getValue().setSshUser(connectModel.getSshUser());
        connectModel.getComboBox().getValue().setLocalSqlPort(connectModel.getLocalSqlPort());
        connectModel.getComboBox().getValue().setKnownHostsFile(connectModel.getKnownHosts());
        connectModel.getComboBox().getValue().setDefault(connectModel.isDefault());
        connectModel.getComboBox().getValue().setSshPort(connectModel.getSshPort());
        connectModel.getComboBox().getValue().setDatabase(connectModel.getDatabase());
        connectModel.getComboBox().getValue().setPrivateKeyFile(connectModel.getPrivateKey());
    }

    protected void setStageHeightListener() {
        connectModel.bottomPaneHeightProperty().addListener((observable) -> {
            connectModel.setTitleBarHeight(connectModel.getConnectStage().getHeight() -
                    connectModel.getConnectStage().getScene().getHeight());
            connectModel.getConnectStage().setHeight(connectModel.getBottomPaneHeight()
                    + connectModel.getTitleBarHeight()
                    + connectModel.getCenterPaneHeight());
        });
    }

    public void setUpStage() {
        connectModel.getConnectStage().setAlwaysOnTop(true);
        connectModel.getConnectStage().requestFocus();
        connectModel.getConnectStage().toFront();
        connectModel.getConnectStage().setResizable(false);
        connectModel.getConnectStage().setX(BaseApplication.primaryStage.getX() + 260);
        connectModel.getConnectStage().setY(BaseApplication.primaryStage.getY() + 300);
        connectModel.getConnectStage().show();
    }
}
