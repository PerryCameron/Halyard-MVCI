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
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.Arrays;

import static org.ecsail.mvci_roster.RosterMessage.LAUNCH_TAB;

public class RosterTableView implements Builder<TableView<MembershipListDTO>> {
    private final RosterModel rosterModel;
    private final RosterView rosterView;

    public RosterTableView(RosterView rosterView) {
        this.rosterView = rosterView;
        this.rosterModel = rosterView.getRosterModel();
    }

    @Override
    public TableView<MembershipListDTO> build() {
        TableView<MembershipListDTO> tableView = TableViewFx.tableViewOf(MembershipListDTO.class);
        rosterModel.setRosterTableView(tableView);
        tableView.getColumns()
                .addAll(Arrays.asList(create1(),create2(),create3(),create4(),create5(),create6(),create7(),create8()));
        tableView.setPlaceholder(new Label(""));
        // auto selector
        TableView.TableViewSelectionModel<MembershipListDTO> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) rosterModel.setSelectedMembershipList(newSelection);
        });
        tableView.setRowFactory(tv -> {
            TableRow<MembershipListDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    rosterView.action.accept(LAUNCH_TAB);
                }
            });
            return row;
        });
        return tableView;
    }

    private TableColumn<MembershipListDTO,Integer> create8() {
        TableColumn<MembershipListDTO, Integer> col = TableColumnFx.integerTableColumn(MembershipListDTO::msIdProperty,"MSID");
        col.setStyle("-fx-alignment: center");
        col.prefWidthProperty().bind(rosterModel.getRosterTableView().widthProperty().multiply(0.10));
        return col;
    }

    private TableColumn<MembershipListDTO, String> create7() {
        TableColumn<MembershipListDTO, String> col = TableColumnFx.stringTableColumn(MembershipListDTO::cityProperty,"City");
        col.setStyle("-fx-alignment: center");
        col.prefWidthProperty().bind(rosterModel.getRosterTableView().widthProperty().multiply(0.15));
        return col;
    }

    private TableColumn<MembershipListDTO,String> create6() {
        TableColumn<MembershipListDTO, String> col = TableColumnFx.stringTableColumn(MembershipListDTO::lastNameProperty,"Last Name");
        col.setStyle("-fx-alignment: center-left");
        col.prefWidthProperty().bind(rosterModel.getRosterTableView().widthProperty().multiply(0.15));
        return col;
    }

    private TableColumn<MembershipListDTO,String> create5() {
        TableColumn<MembershipListDTO, String> col = TableColumnFx.stringTableColumn(MembershipListDTO::firstNameProperty,"First Name");
        col.setStyle("-fx-alignment: center-left");
        col.prefWidthProperty().bind(rosterModel.getRosterTableView().widthProperty().multiply(0.15));
        return col;
    }

    private TableColumn<MembershipListDTO,Text> create4() {
        TableColumn<MembershipListDTO, Text> col = new TableColumn<>("Slip");
        rosterModel.setSlipColumn(col); // allows us to change column name
        col.setCellValueFactory(new PropertyValueFactory<>("slip"));
        col.setStyle("-fx-alignment: center");
        // erasing code below will change nothing
        col.setCellValueFactory(param -> {  // don't need this now but will use for subleases
            MembershipListDTO m = param.getValue();
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

    private TableColumn<MembershipListDTO,Text> create3() {
        TableColumn<MembershipListDTO, Text> col = new TableColumn<>("Type");
        col.setCellValueFactory(new PropertyValueFactory<>("memType"));
        col.setStyle("-fx-alignment: center");
        col.setCellValueFactory(param -> {  // don't need this now but will use for subleases
            MembershipListDTO m = param.getValue();
            Text text = new Text();
            String valueDisplayed = "";
            if (m.getMemType() != null) {
                valueDisplayed = m.getMemType();
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

    private TableColumn<MembershipListDTO,String> create2() {
        TableColumn<MembershipListDTO, String> col = TableColumnFx.stringTableColumn(MembershipListDTO::joinDateProperty,"Join Date");
        col.prefWidthProperty().bind(rosterModel.getRosterTableView().widthProperty().multiply(0.15));
        col.setStyle("-fx-alignment: center");
        return col;
    }

    private TableColumn<MembershipListDTO,Integer> create1() {
        TableColumn<MembershipListDTO, Integer> col = TableColumnFx.integerTableColumn(MembershipListDTO::membershipIdProperty,"ID");
        col.prefWidthProperty().bind(rosterModel.getRosterTableView().widthProperty().multiply(0.05));
        col.setStyle("-fx-alignment: center");
        return col;
    }
}