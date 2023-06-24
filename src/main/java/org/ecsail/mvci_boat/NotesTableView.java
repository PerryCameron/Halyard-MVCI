package org.ecsail.mvci_boat;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Builder;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.NotesDTO;
import org.ecsail.dto.OfficerDTO;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.TableColumnFx;
import org.ecsail.widgetfx.TableViewFx;

public class NotesTableView implements Builder<TableView<NotesDTO>> {
    private final BoatModel boatModel;

    public NotesTableView(BoatModel m) {
        this.boatModel = m;
    }

    @Override
    public TableView<NotesDTO> build() {
        TableView<NotesDTO> tableView = TableViewFx.tableViewOf(NotesDTO.class);
        System.out.println("setting items " + boatModel.getNotesDTOS().size());
        tableView.setItems(boatModel.getNotesDTOS());
        tableView.setPrefHeight(100);
        tableView.getColumns().addAll(createColumn1(), createColumn2());
        return tableView;
    }

    private TableColumn<NotesDTO,String> createColumn1() {
        TableColumn<NotesDTO, String> col1 = TableColumnFx.tableColumnOf(NotesDTO::memoDateProperty, "Year");
//        col1.setCellValueFactory(new PropertyValueFactory<>("membershipId"));
        col1.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setMemoDate(t.getNewValue());
                    int memo_id = t.getTableView().getItems().get(t.getTablePosition().getRow()).getBoatId();
//                    note.updateMemo(memo_id, "memo_date", t.getNewValue());
                }
        );
        col1.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );   // Date
        return col1;
    }

    private TableColumn<NotesDTO,String> createColumn2() {
        TableColumn<NotesDTO, String> col2 = TableColumnFx.tableColumnOf(NotesDTO::memoProperty, "Note");
//        col2.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        col2.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setMemo(t.getNewValue());
                    int memo_id = t.getTableView().getItems().get(t.getTablePosition().getRow()).getMemoId();
//                    note.updateMemo(memo_id, "memo", t.getNewValue());
                }
        );
        col2.setMaxWidth( 1f * Integer.MAX_VALUE * 85 );   // Note
        return col2;
    }

}
