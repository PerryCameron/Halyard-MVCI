package org.ecsail.mvci_roster;

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
import org.ecsail.widgetfx.VBoxFx;

public class RosterView implements Builder<Region> {

    RosterModel rosterModel;
    public RosterView(RosterModel rm) {
        rosterModel = rm;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(setUpLeftPane());
        borderPane.setCenter(setUpTableView());
        return borderPane;
    }

    private Node setUpTableView() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,5,0,0));
        RosterTableView tableView = new RosterTableView(rosterModel);
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
        VBox vBox = VBoxFx.vBoxOf(new Insets(15,15,0,0));
        VBox checkVBox = new VBox(5);
        VBox buttonVBox = VBoxFx.vBoxOf(new Insets(10,0,0,0),Pos.CENTER);
        Button button = new Button("Export to XLS");
        TitledPane titledPane = new TitledPane();
        titledPane.setText("Export to XLS");
        titledPane.setExpanded(false);
//        for(DbRosterSettingsDTO dto: parent.rosterSettings) {
//            SettingsCheckBox checkBox = new SettingsCheckBox(this, dto, "exportable");
//            parent.checkBoxes.add(checkBox);
//            checkVBox.getChildren().add(checkBox);
//        }
//        button.setOnAction((event) -> chooseRoster());
        buttonVBox.getChildren().add(button);
        checkVBox.getChildren().add(buttonVBox);
        titledPane.setContent(checkVBox);
        vBox.getChildren().add(titledPane);
        return vBox;
    }

    private Node createRadioBox() {
        ToggleGroup tg = new ToggleGroup();
        VBox vBox = new VBox();
        vBox.setSpacing(7);
        vBox.setPadding(new Insets(20,0,0,20));
//        for(MembershipListRadioDTO radio: parent.radioChoices) {
//            if(!radio.getMethodName().equals("query")) {
//                RadioHBox radioHBox = new RadioHBox(radio, this);
//                vBox.getChildren().add(radioHBox);
//                radioHBox.getRadioButton().setToggleGroup(tg);
//            }
//        }
        return vBox;
    }

    private Node setUpFieldSelectedToSearchBox() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0,15,0,57));
        TitledPane titledPane = new TitledPane();
        titledPane.setText("Searchable Fields");
        titledPane.setExpanded(false);
        VBox checkVBox = new VBox();
        checkVBox.setSpacing(5);
//        for(DbRosterSettingsDTO dto: parent.rosterSettings) {
//            SettingsCheckBox checkBox = new SettingsCheckBox(this, dto, "searchable");
//            parent.checkBoxes.add(checkBox);
//            checkVBox.getChildren().add(checkBox);
//        }
        titledPane.setContent(checkVBox);
        vBox.getChildren().add(titledPane);
        return vBox;
    }

    private Node setUpSearchBox() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(0,15,0,0));
        Text text = new Text("Search");
        text.setId("invoice-text-number");
        TextField textField = new TextField();
        textField.textProperty().bind(rosterModel.textFieldProperty());
        hBox.getChildren().addAll(text, textField);
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        // this is awesome, stole from stackoverflow.com
        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    pause.setOnFinished(event -> fillTableView(textField.getText()));  //TODO don't need to send parameter
                    pause.playFromStart();
                }
        );
        return hBox;
    }

    // TODO move to interactor
    private void fillTableView(String searchTerm) {
//        if(!searchTerm.equals("")) {
//            parent.searchedRosters.clear();
//            parent.searchedRosters.addAll(searchString(searchTerm));
//            parent.rosterTableView.setItems(parent.searchedRosters);
//            parent.isActiveSearch = true;
//        } else { // if search box has been cleared
//            parent.rosterTableView.setItems(parent.rosters);
//            parent.isActiveSearch = false;
//            parent.searchedRosters.clear();
//        }
//        updateRecordCount();
    }

    private Node setUpRecordCountBox() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,0,15,0));
        Text label = new Text("Records");
        label.setId("invoice-text-label");
        Text numberOfRecords = new Text(String.valueOf(rosterModel.getRosters().size()));
        numberOfRecords.textProperty().bind(rosterModel.numberOfRecordsProperty());
        numberOfRecords.setId("invoice-text-number");
        hBox.getChildren().addAll(createYearBox(),label,numberOfRecords);
        return hBox;
    }

    private Node createYearBox() {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(0,10,0,0));
        ComboBox<Integer> comboBox = new ComboBox<>();
        for(int i = rosterModel.getSelectedYear() + 1; i > 1969; i--) { comboBox.getItems().add(i); }
        comboBox.getSelectionModel().select(1);
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            rosterModel.setSelectedYear(newValue);
//            makeListByRadioButtonChoice();
//            updateRecordCount();
//            parent.rosterTableView.sort();
        });
        vBox.getChildren().add(comboBox);
        return vBox;
    }

//    protected void makeListByRadioButtonChoice()  {
//        parent.rosters.clear();
//        Method method;
//        try {
//            method = parent.membershipRepository.getClass().getMethod(parent.selectedRadioBox.getMethod(),String.class);
//            parent.rosters.setAll((List<MembershipListDTO>) method.invoke(parent.membershipRepository, parent.selectedYear));
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//        if(!parent.textField.getText().equals("")) fillTableView(parent.textField.getText());
//        updateRecordCount();
//        parent.rosters.sort(Comparator.comparing(MembershipListDTO::getMembershipId));
//    }

}
