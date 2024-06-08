package org.ecsail.mvci_roster;

import javafx.animation.PauseTransition;
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
import org.ecsail.dto.DbRosterSettingsDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.MembershipListRadioDTO;
import org.ecsail.mvci_roster.export.SaveFileChooser;
import org.ecsail.static_tools.HalyardPaths;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.VBoxFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

import static org.ecsail.mvci_roster.RosterMessage.*;

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
        TableView<MembershipListDTO> tableView = new RosterTableView(this).build();
        rosterModel.setRosterTableView(tableView);
        vBox.getChildren().add(tableView);
        return vBox;
    }

    private Node setUpLeftPane() {
        VBox vBox = VBoxFx.vBoxOf(220.0,10.0, new Insets(10,0,0,10));
        vBox.getChildren().addAll(setUpRecordCountBox(),setUpSearchBox(),setUpFieldSelectedToSearchBox(),
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

    private Node setUpFieldSelectedToSearchBox() {
        return VBoxFx.vBoxOfCheckBoxes(this::setAllCheckBoxes);
    }

    protected Node setAllCheckBoxes() {
        VBox checkVBox = new VBox(5);
        for(DbRosterSettingsDTO dto: rosterModel.getRosterSettings()) {
            RosterSettingsCheckBox checkBox = new RosterSettingsCheckBox(dto, "searchable");
            rosterModel.getCheckBoxes().add(checkBox);
            checkVBox.getChildren().add(checkBox);
        }
        return checkVBox;
    }

    private Node setUpSearchBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,0,15,0),Pos.CENTER_LEFT, 5.0);
        Text text = new Text("Search");
        text.setId("invoice-text-number");
        TextField textField = new TextField();
        rosterModel.textFieldStringProperty().bindBidirectional(textField.textProperty());
        hBox.getChildren().addAll(text, textField);
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    pause.setOnFinished(event -> action.accept(SEARCH));
                    pause.playFromStart();
                }
        );
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
