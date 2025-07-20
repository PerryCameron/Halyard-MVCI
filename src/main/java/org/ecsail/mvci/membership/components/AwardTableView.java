package org.ecsail.mvci.membership.components;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Builder;
import org.ecsail.fx.AwardFx;
import org.ecsail.fx.PersonFx;
import org.ecsail.enums.Awards;
import org.ecsail.mvci.membership.mvci.person.PersonMessage;
import org.ecsail.mvci.membership.mvci.person.PersonView;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

import java.util.Arrays;

public class AwardTableView implements Builder<TableView<AwardFx>> {
    private final PersonFx person;
    private final PersonView personView;

    public AwardTableView(PersonView personView) {
        this.personView = personView;
        this.person = personView.getPersonModel().getPersonDTO();
    }

    @Override
    public TableView<AwardFx> build() {
        TableView<AwardFx> tableView = TableViewFx.tableViewOf(146,true);
        tableView.setItems(person.getAwards());
        tableView.getColumns().addAll(Arrays.asList(createColumn1(), createColumn2()));
        TableView.TableViewSelectionModel<AwardFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) personView.getPersonModel().selectedAwardProperty().set(newSelection);
        });
        return tableView;
    }

    private TableColumn<AwardFx, String> createColumn1() {
        TableColumn<AwardFx, String> col1 = TableColumnFx.editableStringTableColumn(AwardFx::awardYearProperty, "Year");
        col1.setSortType(TableColumn.SortType.DESCENDING);
        col1.setOnEditCommit(
                t -> {
                    personView.getPersonModel().selectedAwardProperty().get().setAwardYear(t.getNewValue());
                    personView.sendMessage().accept(PersonMessage.UPDATE_AWARD);
                }
        );
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 20);   // Phone
        return col1;
    }

    private TableColumn<AwardFx,Awards> createColumn2() {
        ObservableList<Awards> awardsList = FXCollections.observableArrayList(Awards.values());
        final TableColumn<AwardFx, Awards> col2 = new TableColumn<>("Award Type");
        col2.setCellValueFactory(param -> {
            AwardFx thisAward = param.getValue();
            String awardCode = thisAward.getAwardType();
            Awards type = Awards.getByCode(awardCode);
            return new SimpleObjectProperty<>(type);
        });
        col2.setCellFactory(ComboBoxTableCell.forTableColumn(awardsList));
        col2.setOnEditCommit((TableColumn.CellEditEvent<AwardFx, Awards> event) -> {
            // update the GUI (do this first so UI seems snappy)
            personView.getPersonModel().selectedAwardProperty().get().setAwardType(event.getNewValue().getCode());
            // update the SQL
            personView.sendMessage().accept(PersonMessage.UPDATE_AWARD);
        });
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 50);  // Type
        return col2;
    }
}
