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
import org.ecsail.mvci.membership.mvci.person.PersonMessage;
import org.ecsail.mvci.membership.mvci.person.PersonView;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.Arrays;
import java.util.stream.Collectors;



public class OfficerTableView implements Builder<TableView<OfficerFx>> {
    private final PersonFx person;
    private final MembershipModel membershipModel;
    private final PersonView personView;

    public OfficerTableView(PersonView personView) {
        this.personView = personView;
        this.person = personView.getPersonDTO();
        this.membershipModel = personView.getPersonModel().getMembershipModel();
    }

    @Override
    public TableView<OfficerFx> build() {
        TableView<OfficerFx> tableView = TableViewFx.tableViewOf(146,true);
        tableView.setItems(person.getOfficers());
        tableView.getColumns().addAll(Arrays.asList(createColumn1(), createColumn2(), createColumn3()));
        TableView.TableViewSelectionModel<OfficerFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) personView.getPersonModel().selectedPositionProperty().set(newSelection);
        });
        return tableView;
    }

    private TableColumn<OfficerFx, Integer> createColumn1() {
        TableColumn<OfficerFx, Integer> col = TableColumnFx.editableIntegerTableColumn(OfficerFx::fiscalYearProperty, "Year");
        col.setSortType(TableColumn.SortType.DESCENDING);
        col.setOnEditCommit(
                t -> {
                    personView.getPersonModel().selectedPositionProperty().get().fiscalYearProperty().set(t.getNewValue());
                    personView.sendMessage().accept(PersonMessage.UPDATE_POSITION);
                }
        );
        col.setMaxWidth(1f * Integer.MAX_VALUE * 20);   // Phone
        return col;
    }

    private TableColumn<OfficerFx, String> createColumn2() {
        ObservableList<BoardPositionDTO> boardPositions = FXCollections.observableArrayList(membershipModel.getBoardPositionDTOS());
        ObservableList<String> officerList = FXCollections.observableArrayList(boardPositions.stream().map(BoardPositionDTO::position).collect(Collectors.toList()));
        final TableColumn<OfficerFx, String> col = new TableColumn<>("Officers, Chairs and Board");
        col.setCellValueFactory(param -> {
            OfficerFx officerDTO = param.getValue();
            String type = Officer.getByCode(officerDTO.getOfficerType(), boardPositions);
            return new SimpleObjectProperty<>(type);
        });

        col.setCellFactory(ComboBoxTableCell.forTableColumn(officerList));

        col.setOnEditCommit((TableColumn.CellEditEvent<OfficerFx, String> event) -> {
            TablePosition<OfficerFx, String> pos = event.getTablePosition();
            OfficerFx officerDTO = event.getTableView().getItems().get(pos.getRow());
            officerDTO.setOfficerType(Officer.getByName(event.getNewValue(), boardPositions));
            personView.getPersonModel().selectedPositionProperty().set(officerDTO);
            personView.sendMessage().accept(PersonMessage.UPDATE_POSITION);
        });
        col.setMaxWidth(1f * Integer.MAX_VALUE * 50);  // Type
        return col;
    }

    private TableColumn<OfficerFx, Integer> createColumn3() {
        TableColumn<OfficerFx, Integer> col = TableColumnFx.editableIntegerTableColumn(OfficerFx::boardYearProperty, "Exp");
        col.setOnEditCommit(
                t -> {
                    OfficerFx officerDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    officerDTO.setBoardYear(t.getNewValue());
                    personView.getPersonModel().selectedPositionProperty().set(officerDTO);
                    personView.sendMessage().accept(PersonMessage.UPDATE_POSITION);                }
        );
        col.setMaxWidth(1f * Integer.MAX_VALUE * 20);   // Phone
        return col;
    }
}
