package org.ecsail.mvci_boats;

import javafx.animation.PauseTransition;
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
import org.ecsail.dto.MembershipListRadioDTO;
import org.ecsail.mvci_roster.RadioHBox;
import org.ecsail.mvci_roster.RosterTableView;
import org.ecsail.mvci_roster.SettingsCheckBox;
import org.ecsail.mvci_roster.export.SaveFileChooser;
import org.ecsail.static_calls.HalyardPaths;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.VBoxFx;

public class BoatListView implements Builder<Region> {

    BoatListModel boatListModel;
    public BoatListView(BoatListModel rm) {
        boatListModel = rm;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setRight(setUpRightPane());
        borderPane.setCenter(setUpTableView());
        return borderPane;
    }

    private Node setUpRightPane() {
        VBox vBox = VBoxFx.vBoxOf(220.0,10.0, new Insets(10,0,0,10));
        vBox.getChildren().add(setUpRecordCountBox());
        vBox.getChildren().add(setUpSearchBox());
        vBox.getChildren().add(setUpFieldSelectedToSearchBox());
        vBox.getChildren().add(createRadioBox());
        vBox.getChildren().add(setUpFieldSelectedToExport());
        return vBox;
    }

    private Node setUpFieldSelectedToExport() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(15,15,0,0));
        VBox checkVBox = new VBox(5);
        VBox buttonVBox = VBoxFx.vBoxOf(new Insets(10,0,0,0),Pos.CENTER);
        TitledPane titledPane = new TitledPane();
        titledPane.setText("Export to XLS");
        titledPane.setExpanded(false);
//        rosterModel.listsLoadedProperty().addListener(observable -> {
//            rosterModel.getRosterSettings().stream()
//                    .map(dto -> new SettingsCheckBox(dto, "exportable"))
//                    .peek(rosterModel.getCheckBoxes()::add)
//                    .forEach(checkVBox.getChildren()::add);
//        });
        buttonVBox.getChildren().add(createExportButton());
        checkVBox.getChildren().add(buttonVBox);
        titledPane.setContent(checkVBox);
        vBox.getChildren().add(titledPane);
        return vBox;

    }

    private Node createExportButton() {
        Button button = new Button("Export to XLS");
        button.setOnAction((event) -> {
//            rosterModel.setFileToSave(new SaveFileChooser(HalyardPaths.ROSTERS + "/",
//                    rosterModel.getSelectedYear() + "_" +
//                            rosterModel.getSelectedRadioBox().getRadioLabel().replace(" ", "_"),
//                    "Excel Files", "*.xlsx").getFile());
//            exportRoster.run();
        });
        return button;
    }

    private Node createRadioBox() {
        VBox vBox = VBoxFx.vBoxOf(7.0, new Insets(20,0,0,20));
        ToggleGroup tg = new ToggleGroup();
        // reactive listener used at opening of tab only
//        rosterModel.listsLoadedProperty().addListener(observable -> {
//            for (MembershipListRadioDTO radio : rosterModel.getRadioChoices()) {
//                if (!radio.getMethodName().equals("query")) {
//                    RadioHBox radioHBox = new RadioHBox(radio, rosterModel);
//                    vBox.getChildren().add(radioHBox);
//                    radioHBox.getRadioButton().setToggleGroup(tg);
//                }
//            }
//        });
        return vBox;
    }

    private Node setUpFieldSelectedToSearchBox() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,15,0,57));
        TitledPane titledPane = new TitledPane();
        titledPane.setText("Searchable Fields");
        titledPane.setExpanded(false);
//        rosterModel.listsLoadedProperty().addListener(observable -> {
//            titledPane.setContent(setAllCheckBoxes());
//        });
        vBox.getChildren().add(titledPane);
        return vBox;
    }

    private Node setUpSearchBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,0,15,0),Pos.CENTER_LEFT, 5.0);
        Text text = new Text("Search");
        text.setId("invoice-text-number");
        TextField textField = new TextField();
        boatListModel.textFieldStringProperty().bindBidirectional(textField.textProperty());
        hBox.getChildren().addAll(text, textField);
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
//                    pause.setOnFinished(event -> search.run());
//                    pause.playFromStart();
                }
        );
        return hBox;
    }

    private Node setUpRecordCountBox() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setPadding(new Insets(5,0,15,0));
        Text label = new Text("Records");
        label.setId("invoice-text-label");
        Text numberOfRecords = new Text(String.valueOf(0));
        numberOfRecords.textProperty().bind(boatListModel.numberOfRecordsProperty());
        numberOfRecords.setId("invoice-text-number");
        hBox.getChildren().addAll(label,numberOfRecords);
        return hBox;
    }

    private Node setUpTableView() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,0,10));
//        TableView tableView = new RosterTableView(rosterModel).build();
//        rosterModel.setRosterTableView(tableView);
//        vBox.getChildren().add(tableView);
//        setTabLaunchListener();
        return vBox;
    }
}
