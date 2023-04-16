package org.ecsail.mvci_membership;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.AwardDTO;
import org.ecsail.dto.OfficerDTO;
import org.ecsail.dto.PersonDTO;
import org.ecsail.enums.Awards;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

public class AwardTableView implements Builder<TableView> {
    private final PersonDTO person;
    private final MembershipModel membershipModel;
    private final MembershipView membershipView;

    public AwardTableView(PersonDTO personDTO, MembershipView membershipView) {
        this.person = personDTO;
        this.membershipModel = membershipView.getMembershipModel();
        this.membershipView = membershipView;
    }

    @Override
    public TableView build() {
        TableView<AwardDTO> tableView = TableViewFx.tableViewOf(AwardDTO.class);
        tableView.setItems(person.getAwards());
        tableView.getColumns().addAll(createColumn1(), createColumn2());
        return tableView;
    }

    private TableColumn<AwardDTO,?> createColumn1() {
        TableColumn<AwardDTO, String> col1 = TableColumnFx.tableColumnOf(AwardDTO::awardYearProperty,"Year");
        col1.setSortType(TableColumn.SortType.DESCENDING);
        col1.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setAwardYear(t.getNewValue());
//         TODO           SqlUpdate.updateAward("award_year", t.getRowValue().getAwardId(), t.getNewValue());  // have to get by money id and pid eventually
                }
        );
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 20);   // Phone
        return col1;
    }

    private TableColumn<AwardDTO,?> createColumn2() {
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
            // use enum to convert DB value
            Awards newAward = event.getNewValue();
            // give object a name to manipulate
            AwardDTO thisAward = event.getTableView().getItems().get(pos.getRow());
            // update the SQL
//    TODO        SqlUpdate.updateAward("award_type", thisAward.getAwardId(), newAward.getCode());
            // update the GUI
            thisAward.setAwardType(newAward.getCode());
        });
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 50);  // Type
        return col2;
    }
}
