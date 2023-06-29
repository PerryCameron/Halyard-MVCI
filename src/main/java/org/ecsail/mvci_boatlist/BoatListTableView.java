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
import org.ecsail.widgetfx.TableViewFx;

public class BoatListTableView implements Builder<TableView> {

    private final BoatListView boatListView;

    public BoatListTableView(BoatListView blv) {
        this.boatListView = blv;
    }

    @Override
    public TableView build() {
        TableView<BoatListDTO> tableView = TableViewFx.tableViewOf(BoatListDTO.class);
        tableView.getColumns().addAll(col1(),col2(),col3(),col4(),col5(),col6(),col7(),col8(),col9());
        tableView.setPlaceholder(new Label(""));
        tableView.setRowFactory(tv -> {
            TableRow<BoatListDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    boatListView.boatListModel.setSelectedBoatList(row.getItem());
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
                boatListView.boatListModel.setSelectedBoatList(boatListDTO); // set object as selected object
                boatListView.action.accept(BoatListMessage.UPDATE); // send signal to update selected object in db
            });
            return booleanProp;
        });
        col.setCellFactory(p -> {
            CheckBoxTableCell<BoatListDTO, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        col.setMaxWidth( 1f * Integer.MAX_VALUE * 5 );   // aux
        return col;
    }

    private TableColumn<BoatListDTO,Text> col8() {
        var col = new TableColumn<BoatListDTO, Text>("Images");
        col.setCellValueFactory(new PropertyValueFactory<>("numberOfImages"));
        col.setStyle("-fx-alignment: top-center");
        col.setCellValueFactory(param -> {  // don't need this now but will use for subleases
            BoatListDTO bl = param.getValue();
            String valueDisplayed = String.valueOf(bl.getNumberOfImages());
            Text valueText = new Text(valueDisplayed);
            if(bl.getNumberOfImages() != 0) {
                valueText.setFill(Color.BLUE);
            }
            return new SimpleObjectProperty<>(valueText);
        });
        col.setMaxWidth( 1f * Integer.MAX_VALUE * 9 );   // Images
        return col;
    }

    private TableColumn<BoatListDTO,String> col7() {
        var col = new TableColumn<BoatListDTO, String>("Name");
        col.setCellValueFactory(new PropertyValueFactory<>("boatName"));
        col.setMaxWidth( 1f * Integer.MAX_VALUE * 16 );  // Boat Name
        return col;
    }

    private TableColumn<BoatListDTO,String> col6() {
        var col = new TableColumn<BoatListDTO, String>("Registration");
        col.setCellValueFactory(new PropertyValueFactory<>("registrationNum"));
        col.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );  // Registration
        return col;
    }

    private TableColumn<BoatListDTO,String> col5() {
        var col = new TableColumn<BoatListDTO, String>("Model");
        col.setCellValueFactory(new PropertyValueFactory<>("model"));
        col.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );  // Model
        return col;
    }

    private TableColumn<BoatListDTO,String> col4() {
        var col = new TableColumn<BoatListDTO, String>("First Name");
        col.setCellValueFactory(new PropertyValueFactory<>("fName"));
        col.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );  // First Name
        return col;
    }

    private TableColumn<BoatListDTO,String> col3() {
        var col = new TableColumn<BoatListDTO, String>("Last Name");
        col.setCellValueFactory(new PropertyValueFactory<>("lName"));
        col.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );  // Last Name
        return col;
    }

    private TableColumn<BoatListDTO,String> col2() {
        var col = new TableColumn<BoatListDTO, String>("Boat");
        col.setCellValueFactory(new PropertyValueFactory<>("boatId"));
        col.setStyle("-fx-alignment: top-center");
        col.setMaxWidth( 1f * Integer.MAX_VALUE * 5 );  // boat ID
        return col;
    }

    private TableColumn<BoatListDTO,String> col1() {
        var col = new TableColumn<BoatListDTO, String>("ID");
        col.setCellValueFactory(new PropertyValueFactory<>("membershipId"));
        col.setStyle("-fx-alignment: top-center");
        col.setMaxWidth( 1f * Integer.MAX_VALUE * 5 );  // Membership ID
        return col;
    }
}
