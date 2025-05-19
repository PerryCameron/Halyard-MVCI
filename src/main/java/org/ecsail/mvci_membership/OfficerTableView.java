package org.ecsail.mvci_membership;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Builder;
import org.ecsail.dto.BoardPositionDTO;
import org.ecsail.dto.OfficerDTOFx;
import org.ecsail.dto.PersonDTOFx;
import org.ecsail.enums.Officer;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.Arrays;
import java.util.stream.Collectors;



public class OfficerTableView implements Builder<TableView<OfficerDTOFx>> {
    private final PersonDTOFx person;
    private final MembershipModel membershipModel;
    private final MembershipView membershipView;

    public OfficerTableView(PersonDTOFx personDTO, MembershipView membershipView) {
        this.person = personDTO;
        this.membershipModel = membershipView.getMembershipModel();
        this.membershipView = membershipView;
    }

    @Override
    public TableView<OfficerDTOFx> build() {
        TableView<OfficerDTOFx> tableView = TableViewFx.tableViewOf(OfficerDTOFx.class, 146);
        tableView.setItems(person.getOfficers());
        tableView.getColumns().addAll(Arrays.asList(createColumn1(), createColumn2(), createColumn3()));
        TableView.TableViewSelectionModel<OfficerDTOFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) membershipModel.setSelectedOfficer(newSelection);
        });
        return tableView;
    }

    private TableColumn<OfficerDTOFx, String> createColumn1() {
        TableColumn<OfficerDTOFx, String> col1 = TableColumnFx.editableStringTableColumn(OfficerDTOFx::fiscalYearProperty, "Year");
        col1.setSortType(TableColumn.SortType.DESCENDING);
        col1.setOnEditCommit(
                t -> {
                    OfficerDTOFx officerDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    officerDTO.setFiscalYear(t.getNewValue());
                    membershipModel.setSelectedOfficer(officerDTO);
                    membershipView.sendMessage().accept(MembershipMessage.UPDATE_OFFICER);
                }
        );
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 20);   // Phone
        return col1;
    }

    private TableColumn<OfficerDTOFx, String> createColumn2() {
        ObservableList<BoardPositionDTO> boardPositions = FXCollections.observableArrayList(membershipModel.getBoardPositionDTOS());
        ObservableList<String> officerList = FXCollections.observableArrayList(boardPositions.stream().map(BoardPositionDTO::position).collect(Collectors.toList()));
        final TableColumn<OfficerDTOFx, String> col2 = new TableColumn<>("Officers, Chairs and Board");
        col2.setCellValueFactory(param -> {
            OfficerDTOFx officerDTO = param.getValue();
            String type = Officer.getByCode(officerDTO.getOfficerType(), boardPositions);
            return new SimpleObjectProperty<>(type);
        });

        col2.setCellFactory(ComboBoxTableCell.forTableColumn(officerList));

        col2.setOnEditCommit((TableColumn.CellEditEvent<OfficerDTOFx, String> event) -> {
            TablePosition<OfficerDTOFx, String> pos = event.getTablePosition();
            OfficerDTOFx officerDTO = event.getTableView().getItems().get(pos.getRow());
//            officerDTO.setOfficerType(event.getNewValue());
            officerDTO.setOfficerType(Officer.getByName(event.getNewValue(), boardPositions));
            membershipModel.setSelectedOfficer(officerDTO);
            membershipView.sendMessage().accept(MembershipMessage.UPDATE_OFFICER);
        });
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 50);  // Type
        return col2;
    }

    private TableColumn<OfficerDTOFx, String> createColumn3() {
        TableColumn<OfficerDTOFx, String> col1 = TableColumnFx.editableStringTableColumn(OfficerDTOFx::boardYearProperty, "Exp");
        col1.setOnEditCommit(
                t -> {
                    OfficerDTOFx officerDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    officerDTO.setBoardYear(t.getNewValue());
                    membershipModel.setSelectedOfficer(officerDTO);
                    membershipView.sendMessage().accept(MembershipMessage.UPDATE_OFFICER);                }
        );
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 20);   // Phone
        return col1;
    }
}
