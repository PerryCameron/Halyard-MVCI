package org.ecsail.mvci.roster;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;
import javafx.util.Duration;
import org.ecsail.fx.MembershipListRadioDTO;
import org.ecsail.fx.RosterFx;
import org.ecsail.mvci.roster.export.SaveFileChooser;
import org.ecsail.static_tools.HalyardPaths;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.VBoxFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

import static org.ecsail.mvci.roster.RosterMessage.*;

public class RosterView implements Builder<Region> {

    RosterModel rosterModel;
    Consumer<RosterMessage> action;
    private static final Logger logger = LoggerFactory.getLogger(RosterView.class);
    public RosterView(RosterModel rosterModel, Consumer<RosterMessage> action) {
        this.rosterModel = rosterModel;
        this.action = action;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        ChangeListener<Boolean> dataLoadedListener = ListenerFx.addSingleFireBooleanListener(rosterModel.listsLoadedProperty(), () -> {
            logger.debug("Data has been loaded...");
            borderPane.setLeft(setUpLeftPane());
            borderPane.setCenter(setUpTableView());
        });
        rosterModel.listsLoadedProperty().addListener(dataLoadedListener);
        return borderPane;
    }

    private Node setUpTableView() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,0,10));
        TableView<RosterFx> tableView = new RosterTableView(this).build();
        rosterModel.getRosterTableView().setItems(getRosterModel().getRosters());
        vBox.getChildren().add(tableView);
        return vBox;
    }

    private Node setUpLeftPane() {
        VBox vBox = VBoxFx.vBoxOf(220.0,10.0, new Insets(10,0,0,10));
        vBox.getChildren().addAll(setUpRecordCountBox(),setUpSearchBox(),
                createRadioBox(),setUpFieldSelectedToExport());
        return vBox;
    }

    private Node setUpFieldSelectedToExport() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(15, 15, 0, 0));
        VBox checkVBox = new VBox(5);
        VBox buttonVBox = VBoxFx.vBoxOf(new Insets(10, 0, 0, 0), Pos.CENTER);
        TitledPane titledPane = new TitledPane();
        titledPane.setText("Export to XLS");
        titledPane.setExpanded(false);
        rosterModel.getRosterSettings().stream()
                .map(dto -> new RosterSettingsCheckBox(dto, "exportable"))
                .peek(rosterModel.getCheckBoxes()::add)
                .forEach(checkVBox.getChildren()::add);
        buttonVBox.getChildren().add(createExportButton());
        checkVBox.getChildren().add(buttonVBox);
        titledPane.setContent(checkVBox);
        vBox.getChildren().add(titledPane);
        return vBox;
    }

    private Node createExportButton() {
        Button button = new Button("Export to XLS");
        button.setOnAction((event) -> {
        rosterModel.setFileToSave(new SaveFileChooser(HalyardPaths.ROSTERS + "/",
                rosterModel.getSelectedYear() + "_" +
                        rosterModel.getSelectedRadioBox().getRadioLabel().replace(" ", "_"),
                "Excel Files", "*.xlsx").getFile());
            action.accept(EXPORT_XPS);
        });
        return button;
    }

    private Node createRadioBox() {
        VBox vBox = VBoxFx.vBoxOf(7.0, new Insets(20, 0, 0, 20));
        ToggleGroup tg = new ToggleGroup();
        for (MembershipListRadioDTO radio : rosterModel.getRadioChoices()) {
            if (!radio.getMethodName().equals("query")) {
                RosterRadioHBox rosterRadioHBox = new RosterRadioHBox(radio, rosterModel);
                vBox.getChildren().add(rosterRadioHBox);
                rosterRadioHBox.getRadioButton().setToggleGroup(tg);
            }
        }
        return vBox;
    }

    protected void setRadioListener() {
        // this is the one being called on launch of tab
        rosterModel.selectedRadioBoxProperty().addListener(Observable -> action.accept(CHANGE_LIST_TYPE));
    }

    private Node setUpSearchBox() {
        Timeline debounce = new Timeline(new KeyFrame(Duration.seconds(1), event -> action.accept(SEARCH)));
        debounce.setCycleCount(1); // Ensures it only runs once after inactivity
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,0,15,0),Pos.CENTER_LEFT, 5.0);
        Text text = new Text("Search");
        text.setId("invoice-text-number");
        TextField textField = new TextField();
        rosterModel.textFieldStringProperty().bindBidirectional(textField.textProperty());
        hBox.getChildren().addAll(text, textField);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            debounce.stop(); // Reset the timer
            debounce.playFromStart(); // Start the timer
        });
        return hBox;
    }

    private Node setUpRecordCountBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,0,15,0), Pos.CENTER_LEFT, 5.0);
        Text label = new Text("Records");
        label.setId("invoice-text-label");
        Text numberOfRecords = new Text(String.valueOf(rosterModel.getRosters().size()));
        numberOfRecords.textProperty().bind(rosterModel.numberOfRecordsProperty());
        numberOfRecords.setId("invoice-text-number");
        hBox.getChildren().addAll(createYearBox(),label,numberOfRecords);
        return hBox;
    }

    private Node createYearBox() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,10,0,0), Pos.CENTER,10.0);
        ComboBox<Integer> comboBox = new ComboBox<>();
        for(int i = rosterModel.getSelectedYear() + 1; i > 1969; i--) { comboBox.getItems().add(i); }
        comboBox.getSelectionModel().select(1);
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            rosterModel.setSelectedYear(newValue);
            action.accept(UPDATE_YEAR);
        });
        vBox.getChildren().add(comboBox);
        return vBox;
    }

    public RosterModel getRosterModel() {
        return rosterModel;
    }

    public void setColumnFourName(String newName) {
        rosterModel.getSlipColumn().setText(newName);
    }


}
