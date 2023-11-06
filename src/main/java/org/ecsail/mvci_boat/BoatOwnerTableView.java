package org.ecsail.mvci_boat;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Builder;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.TableViewFx;

public class BoatOwnerTableView implements Builder<TableView<MembershipListDTO>> {
    private final BoatModel boatModel;
    private final BoatView boatView;

    public BoatOwnerTableView(BoatView boatView) {
        this.boatView = boatView;
        this.boatModel = boatView.getBoatModel();
    }

    @Override
    public TableView<MembershipListDTO> build() {
        TableView<MembershipListDTO> tableView = TableViewFx.tableViewOf(MembershipListDTO.class);
        ChangeListener<Boolean> dataLoadedListener = ListenerFx.addSingleFireBooleanListener(boatModel.dataLoadedProperty(), () -> {
            tableView.setItems(boatModel.getBoatOwners());
        });
        TableView.TableViewSelectionModel<MembershipListDTO> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) boatModel.setSelectedOwner(newSelection);
        });
        boatModel.dataLoadedProperty().addListener(dataLoadedListener);
        tableView.getColumns().addAll(createColumn1(), createColumn2());
        return tableView;
    }

//    private TableColumn<MembershipListDTO,Integer> createColumn3() {
//        var col1 = new TableColumn<MembershipListDTO, Integer>("MEM");
//        col1.setCellValueFactory(new PropertyValueFactory<>("membershipId"));
//        col1.setMaxWidth(1f * Integer.MAX_VALUE * 20); // Mem 5%
//        return col1;
//    }

    private TableColumn<MembershipListDTO,String> createColumn2() {
        var col2 = new TableColumn<MembershipListDTO, String>("Last Name");
        col2.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 50); // Join Date 15%
        return col2;
    }

    private TableColumn<MembershipListDTO,String> createColumn1() {
        var col3 = new TableColumn<MembershipListDTO, String>("First Name");
        col3.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 50); // Type
        return col3;
    }
}
