package org.ecsail.mvci_roster;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.dto.RosterDTOFx;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.Arrays;

import static org.ecsail.mvci_roster.RosterMessage.LAUNCH_TAB;

public class RosterTableView implements Builder<TableView<RosterDTOFx>> {
    private final RosterModel rosterModel;
    private final RosterView rosterView;

    public RosterTableView(RosterView rosterView) {
        this.rosterView = rosterView;
        this.rosterModel = rosterView.getRosterModel();
    }

    @Override
    public TableView<RosterDTOFx> build() {
        TableView<RosterDTOFx> tableView = TableViewFx.tableViewOf(RosterDTOFx.class);
        rosterModel.rosterTableViewProperty().set(tableView);
        tableView.getColumns()
                .addAll(Arrays.asList(create1(),create2(),create3(),create4(),create6(),create7(),create8()));
        tableView.setPlaceholder(new Label(""));
        // auto selector
        TableView.TableViewSelectionModel<RosterDTOFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) rosterModel.selectedMembershipListProperty().set(newSelection);
        });
        tableView.setRowFactory(tv -> {
            TableRow<RosterDTOFx> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    rosterView.action.accept(LAUNCH_TAB);
                }
            });
            return row;
        });
        return tableView;
    }

    private TableColumn<RosterDTOFx,Integer> create8() {
        TableColumn<RosterDTOFx, Integer> col = TableColumnFx.integerTableColumn(RosterDTOFx::msIdProperty,"MSID");
        col.setStyle("-fx-alignment: center");
        col.prefWidthProperty().bind(rosterModel.getRosterTableView().widthProperty().multiply(0.10));
        return col;
    }

    private TableColumn<RosterDTOFx, String> create7() {
        TableColumn<RosterDTOFx, String> col = TableColumnFx.stringTableColumn(RosterDTOFx::cityProperty,"City");
        col.setStyle("-fx-alignment: center");
        col.prefWidthProperty().bind(rosterModel.getRosterTableView().widthProperty().multiply(0.15));
        return col;
    }

    private TableColumn<RosterDTOFx,String> create6() {
        TableColumn<RosterDTOFx, String> col = TableColumnFx.stringTableColumn(RosterDTOFx::nameProperty,"Name");
        col.setStyle("-fx-alignment: center-left");
        col.prefWidthProperty().bind(rosterModel.getRosterTableView().widthProperty().multiply(0.15));
        return col;
    }


    private TableColumn<RosterDTOFx,Text> create4() {
        TableColumn<RosterDTOFx, Text> col = new TableColumn<>("Slip");
        rosterModel.slipColumnProperty().set(col); // allows us to change column name
        col.setCellValueFactory(new PropertyValueFactory<>("slip"));
        col.setStyle("-fx-alignment: center");
        // erasing code below will change nothing
        col.setCellValueFactory(param -> {  // don't need this now but will use for subleases
            RosterDTOFx m = param.getValue();
            String valueDisplayed = "";
            if (m.getSlip() != null) {
                valueDisplayed = m.getSlip();
            }
            Text text = new Text(valueDisplayed);
            return new SimpleObjectProperty<>(setTextColor(text));
        });
        col.prefWidthProperty().bind(rosterModel.getRosterTableView().widthProperty().multiply(0.10));
        return col;
    }

    private Text setTextColor(Text text) {
        if(text.getText().startsWith("O")) {
            text.setFill(Color.BLUE);
            text.setText(text.getText().substring(1));
        } else if(text.getText().startsWith("SO")) {
            text.setFill(Color.RED);
            text.setText(text.getText().substring(2));
        } else text.setFill(Color.CHOCOLATE);
        return text;
    }

    private TableColumn<RosterDTOFx,Text> create3() {
        TableColumn<RosterDTOFx, Text> col = new TableColumn<>("Type");
        col.setCellValueFactory(new PropertyValueFactory<>("memType"));
        col.setStyle("-fx-alignment: center");
        col.setCellValueFactory(param -> {  // don't need this now but will use for subleases
            RosterDTOFx rosterDTOFx = param.getValue();
            Text text = new Text();
            String valueDisplayed = "";
            if (rosterDTOFx.typeProperty().get() != null) {
                valueDisplayed = rosterDTOFx.typeProperty().get();
            }
            switch (valueDisplayed) {
                case "SO" -> text.setFill(Color.GREEN);
                case "FM" -> text.setFill(Color.BLUE);
                case "RM" -> text.setFill(Color.RED);
            }
            text.setText(valueDisplayed);
            return new SimpleObjectProperty<>(text);
        });
        col.prefWidthProperty().bind(rosterModel.getRosterTableView().widthProperty().multiply(0.10));
        return col;
    }

    private TableColumn<RosterDTOFx,String> create2() {
        TableColumn<RosterDTOFx, String> col = TableColumnFx.stringTableColumn(RosterDTOFx::joinDateProperty,"Join Date");
        col.prefWidthProperty().bind(rosterModel.getRosterTableView().widthProperty().multiply(0.15));
        col.setStyle("-fx-alignment: center");
        return col;
    }

    private TableColumn<RosterDTOFx,Integer> create1() {
        TableColumn<RosterDTOFx, Integer> col = TableColumnFx.integerTableColumn(RosterDTOFx::idProperty,"ID");
        col.prefWidthProperty().bind(rosterModel.getRosterTableView().widthProperty().multiply(0.05));
        col.setStyle("-fx-alignment: center");
        return col;
    }
}