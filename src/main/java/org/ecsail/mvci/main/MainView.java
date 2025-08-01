package org.ecsail.mvci.main;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Builder;
import javafx.util.Duration;

import java.util.Optional;

import org.ecsail.BaseApplication;
import org.ecsail.static_tools.VersionUtil;
import org.ecsail.widgetfx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Objects;
import java.util.function.Consumer;

import static java.lang.System.getProperty;

public class MainView implements Builder<Region> {
    private static final Logger logger = LoggerFactory.getLogger(MainView.class);
    private final MainModel mainModel;
    Consumer<MainMessage> action;

    public MainView(MainModel mainModel, Consumer<MainMessage> m) {
        this.mainModel = mainModel;
        action = m;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(1028, 900);
        borderPane.setTop(setUpTopPane());
        borderPane.setBottom(setUpBottomPane());
        borderPane.setCenter(setUpCenterPane());
        // closing program with x button
        BaseApplication.primaryStage.setOnHiding(event -> action.accept(MainMessage.CLOSE_ALL_CONNECTIONS_AND_EXIT));
        Image mainIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/title_bar_icon.png")));
        BaseApplication.primaryStage.getIcons().add(mainIcon);
        BaseApplication.primaryStage.setTitle("Halyard");
        setViewListener();
        setCommunicationErrorListener();
        setMembershipDeleteListener();
        return borderPane;
    }

    /*
    This is what happens when we get some sort of communication error
     */
    private void setCommunicationErrorListener() {
        mainModel.clientConnectErrorProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue == true) {
                action.accept(MainMessage.STOP_SPINNER);
                logger.error("MainView::setCommunicationErrorListener() triggered");
                Platform.runLater(() -> {
                    Optional<ButtonType> result = DialogueFx.errorAlertWithAction("There was a problem", mainModel.errorMessageProperty().get());
                    action.accept(MainMessage.SET_CONNECT_ERROR_FALSE);
                    result.ifPresent(button -> {
                        if (button == ButtonType.OK) {
                            action.accept(MainMessage.CLOSE_ALL_CONNECTIONS);
                        }
                    });

                });
            }
        });
    }

    private void setMembershipDeleteListener() {
        mainModel.deleteMembershipProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                closeTabByMsId((Integer) newValue);
            }
        });
    }

    private void setViewListener() {
        ChangeListener<MainMessage> viewListener = ListenerFx.addSingleFireEnumListener(() ->
                viewMessaging(mainModel.returnMessageProperty().get()).run());
        mainModel.returnMessageProperty().addListener(viewListener);
    }

    private Runnable viewMessaging(MainMessage message) { // when database updates, this makes UI reflect.
        return () -> {
            switch (message) {
                case PRIMARY_STAGE_COMPLETE -> action.accept(MainMessage.CREATE_CONNECT_CONTROLLER);
                case SELECT_TAB -> selectTab();
            }
        };
    }

    public void selectTab() {
        mainModel.getMainTabPane().getTabs().stream()
                .filter(tab -> mainModel.getMsId() == (int) tab.getUserData())
                .findFirst()
                .ifPresent(tab -> mainModel.getMainTabPane().getSelectionModel().select(tab));
        mainModel.setReturnMessage(MainMessage.NONE);
    }

    private Node setUpBottomPane() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(statusLabel(), statusLights());
        return hBox;
    }

    private Node statusLights() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(0, 15, 0, 0), Pos.CENTER_RIGHT, 3.0);
        Rectangle receive = RectangleFX.rectangleOf(14, 14);
        Rectangle transmit = RectangleFX.rectangleOf(14, 14);

        // Success animation: 5 blinks, 200ms apart (total 1000ms for 5 on-off cycles)
        Timeline rxSuccessTimeline = createBlinkTimeline(Color.LIGHTGREEN, receive, 200, 5);
        Timeline txSuccessTimeline = createBlinkTimeline(Color.LIGHTGREEN, transmit, 200, 5);

        // Failure animation: 1 blink, 2 seconds long
        Timeline rxFailTimeline = createBlinkTimeline(Color.RED, receive, 2000, 1);
        Timeline txFailTimeline = createBlinkTimeline(Color.RED, transmit, 2000, 1);

        // Listeners for Rx properties
        mainModel.lightRxSuccessProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                rxSuccessTimeline.playFromStart();
            }
        });

        mainModel.lightRxFailProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                rxFailTimeline.playFromStart();
            }
        });

        // Listeners for Tx properties
        mainModel.lightTxSuccessProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                txSuccessTimeline.playFromStart();
            }
        });

        mainModel.lightTxFailProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                txFailTimeline.playFromStart();
            }
        });

        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.getChildren().addAll(transmit, receive);
        return hBox;
    }

    private Timeline createBlinkTimeline(Color color, Rectangle rect, double duration, int cycles) {
        Timeline timeline = new Timeline();
        for (int i = 0; i < cycles; i++) {
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.millis(i * duration), new KeyValue(rect.fillProperty(), Color.GRAY)),
                    new KeyFrame(Duration.millis(i * duration + duration / 2), new KeyValue(rect.fillProperty(), color))
            );
        }
        // Ensure the light returns to gray at the end
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(cycles * duration), new KeyValue(rect.fillProperty(), Color.GRAY))
        );
        return timeline;
    }

    private Node statusLabel() {
        VBox vBox = new VBox();
        vBox.setPrefWidth(400);
        Label statusLabel = new Label();
        statusLabel.setPadding(new Insets(5.0f, 5.0f, 5.0f, 5.0f));
        statusLabel.textProperty().bind(mainModel.statusLabelProperty());
        mainModel.statusLabelProperty().set("(Not Connected) Ready.");
        vBox.getChildren().add(statusLabel);
        return vBox;
    }

    private Node setUpCenterPane() {
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(new Tab("Log in"));
        mainModel.setMainTabPane(tabPane);
        return tabPane;
    }

    private Node setUpTopPane() {
        VBox topElements = new VBox();
        topElements.getChildren().add(setUpMenuBar());
        ToolBar toolbar = new ToolBar();
        topElements.getChildren().add(toolbar);
        return topElements;
    }

    private Node setUpMenuBar() {
        MenuBar menuBar = new MenuBar();
        if (isMac()) menuBar.setUseSystemMenuBar(true);
        menuBar.getMenus().addAll(createFileMenu(), createEditMenu(), createDebugMenu(), createHelpMenu());
        return menuBar;
    }

    private Menu createEditMenu() {
        Menu menu = new Menu("Edit");
        MenuItem undo = MenuFx.menuItemOf("Undo", x -> System.out.println("undo"), KeyCode.Z);
        MenuItem redo = MenuFx.menuItemOf("Redo", x -> System.out.println("Redo"), KeyCode.R);
        SeparatorMenuItem editSeparator = new SeparatorMenuItem();
        MenuItem cut = MenuFx.menuItemOf("Cut", x -> System.out.println("Cut"), KeyCode.X);
        MenuItem copy = MenuFx.menuItemOf("Copy", x -> System.out.println("Copy"), KeyCode.C);
        MenuItem paste = MenuFx.menuItemOf("Paste", x -> System.out.println("Paste"), KeyCode.V);
        menu.getItems().addAll(undo, redo, editSeparator, cut, copy, paste);
        return menu;
    }

    private Menu createHelpMenu() {
        Menu menu = new Menu("Help");
        String message = "Version: " + VersionUtil.getVersion()
                + "\nBuilt: " + VersionUtil.getBuildTimestamp()
                + "\nBundled JDK: " + VersionUtil.getJavaVersion();
        MenuItem showAboutDialogue = MenuFx.menuItemOf("About", x -> {
            Alert alert = DialogueFx.aboutDialogue("Halyard II", message, Alert.AlertType.INFORMATION);
            alert.showAndWait();
        }, null);
        menu.getItems().add(showAboutDialogue);
        return menu;
    }

    private Menu createFileMenu() {
        Menu menu = new Menu("File");
        MenuItem close = MenuFx.menuItemOf("Close Connection", x -> action.accept(MainMessage.CLOSE_ALL_CONNECTIONS), null);
        MenuItem directory = MenuFx.menuItemOf("Render Directory", x -> action.accept(MainMessage.FETCH_DIRECTORY), null);
        menu.getItems().addAll(close, directory);
        return menu;
    }

    private Menu createDebugMenu() {
        Menu menu = new Menu("Debug");
//        MenuItem findDebugLog = new MenuItem("Find Debug Log folder");
//        findDebugLog.setOnAction(e -> showDebugLogFolder());
        MenuItem showDebugLog = MenuFx.menuItemOf("Show Log", x -> action.accept(MainMessage.SHOW_LOG), null);
//        showDebugLog.setOnAction(event -> showDebugLog());
        menu.getItems().add(showDebugLog);
        return menu;
    }

    private static boolean isMac() {
        return getProperty("os.name").contains("Mac");
    }

    protected void closeTabs() {
        mainModel.getMainTabPane().getTabs().clear();
    }

    protected void addNewTab(String name, Region region, int msId, boolean closable) {
        if (PaneFx.tabIsOpen(msId, mainModel.getMainTabPane())) {
            mainModel.setMsId(msId);
            selectTab();
        } else {
            Tab newTab = new Tab(name, region);
            newTab.setClosable(closable);
            newTab.setUserData(msId);
            mainModel.getMainTabPane().getTabs().add(newTab);
            mainModel.getMainTabPane().getSelectionModel().select(newTab);
        }
    }

    public void closeTabByMsId(int msId) {
        TabPane tabPane = mainModel.getMainTabPane();
        for (Tab tab : tabPane.getTabs()) {
            Object userData = tab.getUserData();
            if (userData instanceof Integer && ((Integer) userData).intValue() == msId) {
                tabPane.getTabs().remove(tab);
                break;
            }
        }
    }


}
