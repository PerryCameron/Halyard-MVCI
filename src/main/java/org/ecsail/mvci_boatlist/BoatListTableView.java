package org.ecsail.mvci_boatlist;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.dto.BoatListDTO;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.Arrays;

public class BoatListTableView implements Builder<TableView<BoatListDTO>> {

    private final BoatListView boatListView;

    public BoatListTableView(BoatListView blv) {
        this.boatListView = blv;
    }

    @Override
    public TableView<BoatListDTO> build() {
        TableView<BoatListDTO> tableView = TableViewFx.tableViewOf(BoatListDTO.class);
        boatListView.getBoatListModel().setTable(tableView);
        tableView.getColumns().addAll(Arrays.asList(col1(),col2(),col3(),col4(),col5(),col6(),col7(),col8(),col9()));
        tableView.setPlaceholder(new Label(""));
        tableView.setRowFactory(tv -> {
            TableRow<BoatListDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    boatListView.getBoatListModel().setSelectedBoatList(row.getItem());
                    boatListView.action.accept(BoatListMessage.LAUNCH_TAB); // send signal to update selected object in db
                }
            });
            return row;
        });
        return tableView;
    }

    private TableColumn<BoatListDTO,Boolean> col9() {
        var col = new TableColumn<BoatListDTO, Boolean>("Aux");
        col.setCellValueFactory(new PropertyValueFactory<>("aux"));
        col.setCellValueFactory(param -> {
            BoatListDTO boatListDTO = param.getValue();
            SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(boatListDTO.isAux());
            booleanProp.addListener((observable, oldValue, newValue) -> {
                boatListDTO.setAux(newValue); // update the object
                boatListView.getBoatListModel().setSelectedBoatList(boatListDTO); // set object as selected object
                boatListView.action.accept(BoatListMessage.UPDATE); // send signal to update selected object in db
            });
            return booleanProp;
        });
        col.setCellFactory(p -> {
            CheckBoxTableCell<BoatListDTO, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        col.prefWidthProperty().bind(boatListView.getBoatListModel().getTable().widthProperty().multiply(0.05));
        return col;
    }

    private TableColumn<BoatListDTO,Text> col8() {
        var col = new TableColumn<BoatListDTO, Text>("Images");
        col.setCellValueFactory(new PropertyValueFactory<>("numberOfImages"));
        col.setStyle("-fx-alignment: center");
        col.setCellValueFactory(param -> {  // don't need this now but will use for subleases
            BoatListDTO bl = param.getValue();
            String valueDisplayed = String.valueOf(bl.getNumberOfImages());
            Text valueText = new Text(valueDisplayed);
            if(bl.getNumberOfImages() != 0) {
                valueText.setFill(Color.BLUE);
            }
            return new SimpleObjectProperty<>(valueText);
        });
        col.prefWidthProperty().bind(boatListView.getBoatListModel().getTable().widthProperty().multiply(0.09));
        return col;
    }

    private TableColumn<BoatListDTO,String> col7() {
        TableColumn<BoatListDTO, String> col = TableColumnFx.stringTableColumn(BoatListDTO::boatNameProperty,"Name");
        col.setStyle("-fx-alignment: center-left");
        col.prefWidthProperty().bind(boatListView.getBoatListModel().getTable().widthProperty().multiply(0.16));
        return col;
    }

    private TableColumn<BoatListDTO,String> col6() {
        TableColumn<BoatListDTO, String> col = TableColumnFx.stringTableColumn(BoatListDTO::registrationNumProperty,"registrationNum");
        col.setStyle("-fx-alignment: center-left");
        col.prefWidthProperty().bind(boatListView.getBoatListModel().getTable().widthProperty().multiply(0.15));
        return col;
    }

    private TableColumn<BoatListDTO,String> col5() {
        TableColumn<BoatListDTO, String> col = TableColumnFx.stringTableColumn(BoatListDTO::modelProperty,"Model");
        col.setStyle("-fx-alignment: center-left");
        col.prefWidthProperty().bind(boatListView.getBoatListModel().getTable().widthProperty().multiply(0.15));
        return col;
    }

    private TableColumn<BoatListDTO,String> col4() {
        TableColumn<BoatListDTO, String> col = TableColumnFx.stringTableColumn(BoatListDTO::fNameProperty,"First Name");
        col.setStyle("-fx-alignment: center-left");
        col.prefWidthProperty().bind(boatListView.getBoatListModel().getTable().widthProperty().multiply(0.15));
        return col;
    }

    private TableColumn<BoatListDTO,String> col3() {
        TableColumn<BoatListDTO, String> col = TableColumnFx.stringTableColumn(BoatListDTO::lNameProperty,"Last Name");
        col.setStyle("-fx-alignment: center-left");
        col.prefWidthProperty().bind(boatListView.getBoatListModel().getTable().widthProperty().multiply(0.15));
        return col;
    }

    private TableColumn<BoatListDTO,Integer> col2() {
        TableColumn<BoatListDTO, Integer> col = TableColumnFx.integerTableColumn(BoatListDTO::boatIdProperty,"Boat");
        col.setStyle("-fx-alignment: center");
        col.prefWidthProperty().bind(boatListView.getBoatListModel().getTable().widthProperty().multiply(0.05));
        return col;
    }

    private TableColumn<BoatListDTO,Integer> col1() {
        TableColumn<BoatListDTO,Integer> col = TableColumnFx.integerTableColumn(BoatListDTO::msIdProperty,"ID");
        col.setStyle("-fx-alignment: center");
        col.prefWidthProperty().bind(boatListView.getBoatListModel().getTable().widthProperty().multiply(0.05));
        return col;
    }
}
