package org.ecsail.mvci.membership.components;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Builder;
import org.ecsail.fx.BoardPositionDTO;
import org.ecsail.fx.OfficerFx;
import org.ecsail.fx.PersonFx;
import org.ecsail.enums.Officer;
import org.ecsail.mvci.membership.MembershipMessage;
import org.ecsail.mvci.membership.MembershipModel;
import org.ecsail.mvci.membership.MembershipView;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.Arrays;
import java.util.stream.Collectors;



public class OfficerTableView implements Builder<TableView<OfficerFx>> {
    private final PersonFx person;
    private final MembershipModel membershipModel;
    private final MembershipView membershipView;

    public OfficerTableView(PersonFx personDTO, MembershipView membershipView) {
        this.person = personDTO;
        this.membershipModel = membershipView.getMembershipModel();
        this.membershipView = membershipView;
    }

    @Override
    public TableView<OfficerFx> build() {
        TableView<OfficerFx> tableView = TableViewFx.tableViewOf(146,true);
        tableView.setItems(person.getOfficers());
        tableView.getColumns().addAll(Arrays.asList(createColumn1(), createColumn2(), createColumn3()));
        TableView.TableViewSelectionModel<OfficerFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) membershipModel.setSelectedOfficer(newSelection);
        });
        return tableView;
    }

    private TableColumn<OfficerFx, Integer> createColumn1() {
        TableColumn<OfficerFx, Integer> col1 = TableColumnFx.editableIntegerTableColumn(OfficerFx::fiscalYearProperty, "Year");
        col1.setSortType(TableColumn.SortType.DESCENDING);
        col1.setOnEditCommit(
                t -> {
                    OfficerFx officerDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    officerDTO.setFiscalYear(t.getNewValue());
                    membershipModel.setSelectedOfficer(officerDTO);
                    membershipView.sendMessage().accept(MembershipMessage.UPDATE_POSITION);
                }
        );
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 20);   // Phone
        return col1;
    }

    private TableColumn<OfficerFx, String> createColumn2() {
        ObservableList<BoardPositionDTO> boardPositions = FXCollections.observableArrayList(membershipModel.getBoardPositionDTOS());
        ObservableList<String> officerList = FXCollections.observableArrayList(boardPositions.stream().map(BoardPositionDTO::position).collect(Collectors.toList()));
        final TableColumn<OfficerFx, String> col2 = new TableColumn<>("Officers, Chairs and Board");
        col2.setCellValueFactory(param -> {
            OfficerFx officerDTO = param.getValue();
            String type = Officer.getByCode(officerDTO.getOfficerType(), boardPositions);
            return new SimpleObjectProperty<>(type);
        });

        col2.setCellFactory(ComboBoxTableCell.forTableColumn(officerList));

        col2.setOnEditCommit((TableColumn.CellEditEvent<OfficerFx, String> event) -> {
            TablePosition<OfficerFx, String> pos = event.getTablePosition();
            OfficerFx officerDTO = event.getTableView().getItems().get(pos.getRow());
//            officerDTO.setOfficerType(event.getNewValue());
            officerDTO.setOfficerType(Officer.getByName(event.getNewValue(), boardPositions));
            membershipModel.setSelectedOfficer(officerDTO);
            membershipView.sendMessage().accept(MembershipMessage.UPDATE_POSITION);
        });
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 50);  // Type
        return col2;
    }

    private TableColumn<OfficerFx, Integer> createColumn3() {
        TableColumn<OfficerFx, Integer> col1 = TableColumnFx.editableIntegerTableColumn(OfficerFx::boardYearProperty, "Exp");
        col1.setOnEditCommit(
                t -> {
                    OfficerFx officerDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    officerDTO.setBoardYear(t.getNewValue());
                    membershipModel.setSelectedOfficer(officerDTO);
                    membershipView.sendMessage().accept(MembershipMessage.UPDATE_POSITION);                }
        );
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 20);   // Phone
        return col1;
    }
}
