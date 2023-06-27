package org.ecsail.mvci_membership;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Builder;
import org.ecsail.dto.AwardDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.enums.Awards;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

public class AwardTableView implements Builder<TableView<AwardDTO>> {
    private final PersonDTO person;
    private final MembershipView membershipView;

    public AwardTableView(PersonDTO personDTO, MembershipView membershipView) {
        this.person = personDTO;
        this.membershipView = membershipView;
    }

    @Override
    public TableView<AwardDTO> build() {
        TableView<AwardDTO> tableView = TableViewFx.tableViewOf(AwardDTO.class, 146);
        tableView.setItems(person.getAwards());
        tableView.getColumns().addAll(createColumn1(), createColumn2());
        return tableView;
    }

    private TableColumn<AwardDTO, String> createColumn1() {
        TableColumn<AwardDTO, String> col1 = TableColumnFx.tableColumnOf(AwardDTO::awardYearProperty, "Year");
        col1.setSortType(TableColumn.SortType.DESCENDING);
        col1.setOnEditCommit(
                t -> {
                    AwardDTO awardDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    awardDTO.setAwardYear(t.getNewValue());
                    membershipView.sendMessage()
                            .accept(MembershipMessages.action.UPDATE, awardDTO);
                }
        );
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 20);   // Phone
        return col1;
    }

    private TableColumn<AwardDTO,Awards> createColumn2() {
        ObservableList<Awards> awardsList = FXCollections.observableArrayList(Awards.values());
        final TableColumn<AwardDTO, Awards> col2 = new TableColumn<>("Award Type");
        col2.setCellValueFactory(param -> {
            AwardDTO thisAward = param.getValue();
            String awardCode = thisAward.getAwardType();
            Awards type = Awards.getByCode(awardCode);
            return new SimpleObjectProperty<>(type);
        });
        col2.setCellFactory(ComboBoxTableCell.forTableColumn(awardsList));
        col2.setOnEditCommit((TableColumn.CellEditEvent<AwardDTO, Awards> event) -> {
            // get the position on the table
            TablePosition<AwardDTO, Awards> pos = event.getTablePosition();
            // give object a name to manipulate
            AwardDTO awardDTO = event.getTableView().getItems().get(pos.getRow());
            // update the GUI (do this first so UI seems snappy)
            awardDTO.setAwardType(event.getNewValue().getCode());
            // update the SQL
            membershipView.sendMessage().accept(MembershipMessages.action.UPDATE, awardDTO);
        });
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 50);  // Type
        return col2;
    }
}
