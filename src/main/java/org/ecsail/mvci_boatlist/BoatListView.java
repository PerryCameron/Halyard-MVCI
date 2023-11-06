package org.ecsail.mvci_boatlist;

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
import org.ecsail.dto.BoatListDTO;
import org.ecsail.dto.BoatListRadioDTO;
import org.ecsail.dto.DbBoatSettingsDTO;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.function.Consumer;



public class BoatListView implements Builder<Region> {

    BoatListModel boatListModel;
    Consumer<BoatListMessage> action;
    public BoatListView(BoatListModel rm, Consumer<BoatListMessage> m) {
        boatListModel = rm;
        action = m;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        ChangeListener<Boolean> dataLoadedListener = ListenerFx.addSingleFireBooleanListener(boatListModel.listsLoadedProperty(), () -> {
            borderPane.setRight(setUpRightPane());
            borderPane.setCenter(setUpTableView());
        });
        boatListModel.listsLoadedProperty().addListener(dataLoadedListener);
        return borderPane;
    }

    private Node setUpRightPane() {
        VBox vBox = VBoxFx.vBoxOf(220.0,10.0, new Insets(10,10,0,10));
        vBox.getChildren().add(setUpRecordCountBox());
        vBox.getChildren().add(setUpSearchBox());
        vBox.getChildren().add(setUpFieldSelectedToSearchBox());
        vBox.getChildren().add(createRadioBox());
        vBox.getChildren().add(setUpFieldSelectedToExport());
        return vBox;
    }

    private Node setUpFieldSelectedToExport() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(15, 15, 0, 0));
        VBox checkVBox = new VBox(5);
        VBox buttonVBox = VBoxFx.vBoxOf(new Insets(10, 0, 0, 0), Pos.CENTER);
        TitledPane titledPane = new TitledPane();
        titledPane.setText("Export to XLS");
        titledPane.setExpanded(false);
        boatListModel.getBoatListSettings().stream()
                .map(dto -> new BoatListSettingsCheckBox(dto, "exportable"))
                .forEach(checkVBox.getChildren()::add);
        buttonVBox.getChildren().add(createExportButton());
        checkVBox.getChildren().add(buttonVBox);
        titledPane.setContent(checkVBox);
        vBox.getChildren().add(titledPane);
        return vBox;
    }

    private Node createExportButton() {
        Button button = new Button("Export");
        button.setOnAction((event) -> {
            action.accept(BoatListMessage.EXPORT_XPS);
//            rosterModel.setFileToSave(new SaveFileChooser(HalyardPaths.ROSTERS + "/",
//                    rosterModel.getSelectedYear() + "_" +
//                            rosterModel.getSelectedRadioBox().getRadioLabel().replace(" ", "_"),
//                    "Excel Files", "*.xlsx").getFile());
//            exportRoster.run();
        });
        return button;
    }

    protected void setRadioListener() {
//        boatListModel.selectedRadioBoxProperty().addListener(Observable -> changeState.run());
        boatListModel.selectedRadioBoxProperty().addListener(Observable -> action.accept(BoatListMessage.CHANGE_LIST));
    }

    private Node createRadioBox() {
        VBox vBox = VBoxFx.vBoxOf(7.0, new Insets(20, 0, 0, 20));
        ToggleGroup tg = new ToggleGroup();
          for (BoatListRadioDTO radio : boatListModel.getRadioChoices()) {
                BoatListRadioHBox radioHBox = new BoatListRadioHBox(radio, boatListModel);
                vBox.getChildren().add(radioHBox);
                radioHBox.getRadioButton().setToggleGroup(tg);
        };
        return vBox;
    }

    private Node setUpFieldSelectedToSearchBox() {
        return VBoxFx.vBoxOfCheckBoxes(this::setAllCheckBoxes);
    }

    protected Node setAllCheckBoxes() {
        VBox vBox = new VBox(5);
        for(DbBoatSettingsDTO dto: boatListModel.getBoatListSettings()) {
            BoatListSettingsCheckBox checkBox = new BoatListSettingsCheckBox(dto, "searchable");
            boatListModel.getCheckBoxes().add(checkBox);
            vBox.getChildren().add(checkBox);
        }
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
                    pause.setOnFinished(event -> action.accept(BoatListMessage.SEARCH));
                    pause.playFromStart();
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
        TableView<BoatListDTO> tableView = new BoatListTableView(this).build();
        boatListModel.setBoatListTableView(tableView);
        vBox.getChildren().add(tableView);
//        setTabLaunchListener();
        return vBox;
    }

    public Consumer<BoatListMessage> getAction() {
        return action;
    }

    public void setAction(Consumer<BoatListMessage> action) {
        this.action = action;
    }
}
