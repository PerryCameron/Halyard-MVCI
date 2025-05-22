package org.ecsail.mvci_membership.components;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Builder;
import org.ecsail.fx.AwardDTOFx;
import org.ecsail.fx.PersonFx;
import org.ecsail.enums.Awards;
import org.ecsail.mvci_membership.MembershipMessage;
import org.ecsail.mvci_membership.MembershipModel;
import org.ecsail.mvci_membership.MembershipView;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.Arrays;

public class AwardTableView implements Builder<TableView<AwardDTOFx>> {
    private final PersonFx person;
    private final MembershipView membershipView;
    private final MembershipModel membershipModel;

    public AwardTableView(PersonFx personDTO, MembershipView membershipView) {
        this.person = personDTO;
        this.membershipView = membershipView;
        this.membershipModel = membershipView.getMembershipModel();
    }

    @Override
    public TableView<AwardDTOFx> build() {
        TableView<AwardDTOFx> tableView = TableViewFx.tableViewOf(AwardDTOFx.class, 146);
        tableView.setItems(person.getAwards());
        tableView.getColumns().addAll(Arrays.asList(createColumn1(), createColumn2()));
        TableView.TableViewSelectionModel<AwardDTOFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) membershipModel.setSelectedAward(newSelection);
        });
        return tableView;
    }

    private TableColumn<AwardDTOFx, String> createColumn1() {
        TableColumn<AwardDTOFx, String> col1 = TableColumnFx.editableStringTableColumn(AwardDTOFx::awardYearProperty, "Year");
        col1.setSortType(TableColumn.SortType.DESCENDING);
        col1.setOnEditCommit(
                t -> {
                    membershipModel.getSelectedAward().setAwardYear(t.getNewValue());
                    membershipView.sendMessage().accept(MembershipMessage.UPDATE_AWARD);
                }
        );
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 20);   // Phone
        return col1;
    }

    private TableColumn<AwardDTOFx,Awards> createColumn2() {
        ObservableList<Awards> awardsList = FXCollections.observableArrayList(Awards.values());
        final TableColumn<AwardDTOFx, Awards> col2 = new TableColumn<>("Award Type");
        col2.setCellValueFactory(param -> {
            AwardDTOFx thisAward = param.getValue();
            String awardCode = thisAward.getAwardType();
            Awards type = Awards.getByCode(awardCode);
            return new SimpleObjectProperty<>(type);
        });
        col2.setCellFactory(ComboBoxTableCell.forTableColumn(awardsList));
        col2.setOnEditCommit((TableColumn.CellEditEvent<AwardDTOFx, Awards> event) -> {
            // update the GUI (do this first so UI seems snappy)
            membershipModel.getSelectedAward().setAwardType(event.getNewValue().getCode());
            // update the SQL
            membershipView.sendMessage().accept(MembershipMessage.UPDATE_AWARD);
        });
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 50);  // Type
        return col2;
    }
}
