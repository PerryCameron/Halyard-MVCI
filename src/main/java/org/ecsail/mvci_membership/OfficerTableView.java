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
import org.ecsail.dto.OfficerDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.enums.Officer;
import org.ecsail.interfaces.Messages;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.stream.Collectors;

public class OfficerTableView implements Builder<TableView<OfficerDTO>> {
    private final PersonDTO person;
    private final MembershipModel membershipModel;
    private final MembershipView membershipView;

    public OfficerTableView(PersonDTO personDTO, MembershipView membershipView) {
        this.person = personDTO;
        this.membershipModel = membershipView.getMembershipModel();
        this.membershipView = membershipView;
    }

    @Override
    public TableView<OfficerDTO> build() {
        TableView<OfficerDTO> tableView = TableViewFx.tableViewOf(OfficerDTO.class);
        tableView.setItems(person.getOfficer());
        tableView.getColumns().addAll(createColumn1(), createColumn2(), createColumn3());
        return tableView;
    }

    private TableColumn<OfficerDTO, ?> createColumn1() {
        TableColumn<OfficerDTO, String> col1 = TableColumnFx.tableColumnOf(OfficerDTO::fiscal_yearProperty, "Year");
        col1.setSortType(TableColumn.SortType.DESCENDING);
        col1.setOnEditCommit(
                t -> {
                    OfficerDTO officerDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    officerDTO.setFiscal_year(t.getNewValue());
                    membershipView.sendMessage().accept(Messages.MessageType.UPDATE, officerDTO);
                }
        );
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 20);   // Phone
        return col1;
    }

    private TableColumn<OfficerDTO, String> createColumn2() {
        ObservableList<BoardPositionDTO> boardPositions = FXCollections.observableArrayList(membershipModel.getMainModel().getBoardPositionDTOS());
        ObservableList<String> officerList = FXCollections.observableArrayList(boardPositions.stream().map(BoardPositionDTO::position).collect(Collectors.toList()));
        final TableColumn<OfficerDTO, String> col2 = new TableColumn<>("Officers, Chairs and Board");
        col2.setCellValueFactory(param -> {
            OfficerDTO thisOfficer = param.getValue();
            String type = Officer.getByCode(thisOfficer.getOfficer_type(), boardPositions);
            return new SimpleObjectProperty<>(type);
        });

        col2.setCellFactory(ComboBoxTableCell.forTableColumn(officerList));

        col2.setOnEditCommit((TableColumn.CellEditEvent<OfficerDTO, String> event) -> {
            TablePosition<OfficerDTO, String> pos = event.getTablePosition();
            OfficerDTO officerDTO = event.getTableView().getItems().get(pos.getRow());
            officerDTO.setOfficer_type(event.getNewValue());
            membershipView.sendMessage().accept(Messages.MessageType.UPDATE, officerDTO);
        });
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 50);  // Type
        return col2;
    }

    private TableColumn<OfficerDTO, String> createColumn3() {
        TableColumn<OfficerDTO, String> col3 = TableColumnFx.tableColumnOf(OfficerDTO::board_yearProperty, "Exp");
        col3.setOnEditCommit(
                t -> {
                    OfficerDTO officerDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    officerDTO.setBoard_year(t.getNewValue());
                    membershipView.sendMessage().accept(Messages.MessageType.UPDATE, officerDTO);
                }
        );
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 20);  // Listed
        return col3;
    }
}
