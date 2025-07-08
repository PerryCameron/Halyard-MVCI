package org.ecsail.mvci.membership.components;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.fx.NotesFx;
import org.ecsail.mvci.membership.MembershipMessage;
import org.ecsail.mvci.membership.MembershipModel;
import org.ecsail.mvci.membership.MembershipView;
import org.ecsail.widgetfx.*;

import java.time.LocalDate;
import java.util.Arrays;

public class NotesTabView implements Builder<Tab> {
    private final MembershipView membershipView;
    private final MembershipModel membershipModel;

    public NotesTabView(MembershipView membershipView) {
        this.membershipView = membershipView;
        this.membershipModel = membershipView.getMembershipModel();
    }

    @Override
    public Tab build() {
        return TabFx.tabOf("Notes", createTableAndButtonsBox());
    }

    private Node createTableAndButtonsBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5, 5, 5, 5), "box-background-light", true);
        hBox.getChildren().addAll(addTable(), addTextArea(), getButtonControls());
        return hBox;
    }

    private Node getButtonControls() {
        VBox vBox = VBoxFx.vBoxOf(5.0, new Insets(10, 5, 5, 10));
        vBox.getChildren().addAll(
                ButtonFx.buttonOf("Add", 60, this::insertNote),
                ButtonFx.buttonOf("Delete", 60, this::deleteNote));
        return vBox;
    }

    private void insertNote() {
        membershipView.sendMessage().accept(MembershipMessage.INSERT_NOTE);
    }

    private void deleteNote() {
        String[] strings = {
                "Delete Note",
                "Are you sure you want to delete this note?",
                "Missing Selection",
                "You need to select a note first"};
        if (DialogueFx.verifyAction(strings, membershipModel.getSelectedNote()))
            membershipView.sendMessage().accept(MembershipMessage.DELETE_NOTE);
    }

    private Node addTable() {
        TableView<NotesFx> tableView = TableViewFx.tableViewOf(NotesFx.class, 200);
        tableView.setItems(FXCollections.observableArrayList(membershipView.getMembershipModel().membershipProperty().get().getMemos()));
        tableView.getColumns().addAll(Arrays.asList(col1(), col2()));
        TableView.TableViewSelectionModel<NotesFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                membershipModel.setSelectedNote(newSelection);
                membershipModel.textAreaProperty().get().setText(newSelection.getMemo());
            }
        });
        membershipView.getMembershipModel().setNotesTableView(tableView);
        return tableView;
    }

    private Node addTextArea() {
        TextArea textArea = membershipModel.getTextArea();
        textArea.setPrefWidth(700);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-background-color: white; -fx-control-inner-background: white;");
        return textArea;
    }

//    private TableColumn<NotesFx, String> col3() {
//        TableColumn<NotesFx, String> col3 = TableColumnFx.editableStringTableColumn(NotesFx::memoProperty, "Note");
//        col3.setPrefWidth(740);
//        col3.setOnEditCommit(t -> {
//            NotesFx notesDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
//            notesDTO.setMemo(t.getNewValue());
//            membershipModel.setSelectedNote(notesDTO);
//            System.out.println("Column 3 and note: " + notesDTO.getMemoId());
//            membershipView.sendMessage().accept(MembershipMessage.UPDATE_NOTE);
//        });
//        col3.setMaxWidth(1f * Integer.MAX_VALUE * 80);   // Note
//        return col3;
//    }

    private TableColumn<NotesFx, String> col2() {
        TableColumn<NotesFx, String> col2 = new TableColumn<>("Type");
        col2.setCellValueFactory(new PropertyValueFactory<>("category"));
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 5);    // Type
        return col2;
    }

    private TableColumn<NotesFx, LocalDate> col1() {
        TableColumn<NotesFx, LocalDate> col1 = new TableColumn<>("Date");
        col1.setCellValueFactory(cellData -> cellData.getValue().memoDateProperty());
        col1.setCellFactory(CallBackFX.createDatePickerCellFactory(notesDTO -> {
            membershipModel.setSelectedNote(notesDTO);
            membershipView.sendMessage().accept(MembershipMessage.UPDATE_NOTE);
        }));
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 95);   // Date
        return col1;
    }
}
