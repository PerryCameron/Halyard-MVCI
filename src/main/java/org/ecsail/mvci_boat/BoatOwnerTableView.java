package org.ecsail.mvci_boat;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Builder;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.widgetfx.TableViewFx;

public class BoatOwnerTableView implements Builder<TableView<MembershipListDTO>> {
    @Override
    public TableView<MembershipListDTO> build() {
        TableView<MembershipListDTO> tableView = TableViewFx.tableViewOf(MembershipListDTO.class);
//        tableView.setItems(person.getOfficer());
        tableView.getColumns().addAll(createColumn1(), createColumn2(), createColumn3());
        return tableView;
    }

    private TableColumn<MembershipListDTO,?> createColumn3() {
        var col1 = new TableColumn<MembershipListDTO, Integer>("MEM");
        col1.setCellValueFactory(new PropertyValueFactory<>("membershipId"));
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 20); // Mem 5%
        return col1;
    }

    private TableColumn<MembershipListDTO,?> createColumn2() {
        var col2 = new TableColumn<MembershipListDTO, String>("Last Name");
        col2.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 40); // Join Date 15%
        return col2;
    }

    private TableColumn<MembershipListDTO,?> createColumn1() {
        var col3 = new TableColumn<MembershipListDTO, String>("First Name");
        col3.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 40); // Type
        return col3;
    }
}