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
        userName.textProperty().bindBidirectional(connectModel.currentLoginProperty().get().userProperty());
        hboxUserText.getChildren().add(userName);
        hBox.getChildren().addAll(hboxUserLabel, hboxUserText);
        return hBox;
    }

    private Node PassBox() { // 2
        HBox hBox = HBoxFx.hBoxOf(new Insets(5));
        PasswordField passwordField = TextFieldFx.passwordFieldOf(200,"Password");
        passwordField.textProperty().bindBidirectional(connectModel.currentLoginProperty().get().passwdProperty());
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
        hostName.textProperty().bindBidirectional(connectModel.currentLoginProperty().get().hostProperty());
        hboxHostLabel.getChildren().add(new Label("Hostname:"));
        hBoxHostContainer.getChildren().add(createComboBox());
        hBox.getChildren().addAll(hboxHostLabel,hBoxHostContainer);
        return hBox;
    }

    private Node createComboBox() {
        HBox hBox = new HBox();
        connectModel.getHBoxMap().put("host-combo-box",hBox);
        ComboBox<String> comboBox = connectModel.getComboBox();
        comboBox.setPrefWidth(200);
        action.accept(ConnectMessage.UPDATE_CURRENT_LOGIN);
        hBox.getChildren().add(comboBox);
        comboBox.valueProperty().addListener((Observable, oldValue, newValue) -> {
            if(newValue != null) {
                System.out.println("---------------------------------------------------------");
                System.out.println("New Value Selected ----->" + newValue);
                action.accept(ConnectMessage.SET_CURRENT_LOGIN_AS_DEFAULT);
            }
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
        editConnectText.setOnMouseClicked(event -> runState.setMode(RunState.Mode.EDIT, this));

        newConnectText.setOnMouseClicked(event -> runState.setMode(RunState.Mode.NEW, this));
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
                vBox.getChildren().addAll(portBox(), createEditButtonsBox());
        return vBox;
    }

    private Node portBox() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        TextField portTextField = TextFieldFx.textFieldOf(60, "port");
        portTextField.textProperty().bindBidirectional(connectModel.currentLoginProperty().get().portProperty());
        HBox hboxPortLabel = HBoxFx.hBoxOf(Pos.CENTER_LEFT, 90, new Insets(5));
        hboxPortLabel.getChildren().add(new Label("Port:"));
        HBox hboxPortText = HBoxFx.hBoxOf(Pos.CENTER_LEFT,100,new Insets(5),20);
        hboxPortText.getChildren().add(portTextField);
        hBox.getChildren().addAll(hboxPortLabel, hboxPortText, setDefaultCheckBox());
        return hBox;
    }

    private Node setDefaultCheckBox() {
        CheckBox checkBox = new CheckBox("Default login");
        return checkBox;
    }

    private Node createEditButtonsBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(20,0,20,0),Pos.CENTER, 10);
        Button buttonSave = new Button("Save");
        buttonSave.setOnAction(event -> {
            // copies current loginDTO(bound to textFields) , to the correct loginDTO in the list
            action.accept(ConnectMessage.COPY_CURRENT_TO_MATCHING); // copies values in textfield to matching loginDTO in list
            // back to normal mode
            runState.setMode(RunState.Mode.NORMAL, this);
            // saves the changes
            action.accept(ConnectMessage.SAVE_LOGINS);
            // makes sure the combo box is refreshed, if it was changed
            action.accept(ConnectMessage.UPDATE_COMBO_BOX);
        });
        Button buttonDelete = new Button("Delete");
        buttonDelete.setOnAction(event -> {
            action.accept(ConnectMessage.DELETE_CURRENT_LOGIN);
            action.accept(ConnectMessage.SAVE_LOGINS);
            runState.setMode(RunState.Mode.NORMAL, this);
        });
        Button buttonCancel = new Button("Cancel");
        buttonCancel.setOnAction(event -> runState.setMode(RunState.Mode.NORMAL, this));
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
