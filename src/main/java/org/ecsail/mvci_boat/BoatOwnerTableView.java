package org.ecsail.mvci_boat;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Builder;
import org.ecsail.fx.MembershipListDTO;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.Arrays;

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
        boatModel.setBoatOwnerTableView(tableView);
        ChangeListener<Boolean> dataLoadedListener = ListenerFx.addSingleFireBooleanListener(boatModel.dataLoadedProperty(), () -> {
            tableView.setItems(boatModel.getBoatOwners());
        });
        TableView.TableViewSelectionModel<MembershipListDTO> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) boatModel.setSelectedOwner(newSelection);
        });
        boatModel.dataLoadedProperty().addListener(dataLoadedListener);
        tableView.getColumns().addAll(Arrays.asList(createColumn1(), createColumn2()));
        return tableView;
    }

    private TableColumn<MembershipListDTO,String> createColumn2() {
        TableColumn<MembershipListDTO, String> col = TableColumnFx.stringTableColumn(MembershipListDTO::lastNameProperty,"Last Name");
        col.prefWidthProperty().bind(boatModel.getBoatOwnerTableView().widthProperty().multiply(0.50));
        return col;
    }

    private TableColumn<MembershipListDTO,String> createColumn1() {
        TableColumn<MembershipListDTO, String> col = TableColumnFx.stringTableColumn(MembershipListDTO::firstNameProperty,"First Name");
        col.prefWidthProperty().bind(boatModel.getBoatOwnerTableView().widthProperty().multiply(0.50));
        return col;
    }
}
