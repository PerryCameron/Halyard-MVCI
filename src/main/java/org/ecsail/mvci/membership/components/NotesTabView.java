package org.ecsail.mvci.membership.components;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
    private Button addButton;
    private Button deleteButton;
    private Button editButton;
    private Button saveButton;

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
        this.addButton = ButtonFx.buttonOf("Add", 60, this::insertNote);
        this.deleteButton = ButtonFx.buttonOf("Delete", 60, this::deleteNote);
        this.editButton = ButtonFx.buttonOf("Edit", 60, this::editNote);
        this.saveButton = ButtonFx.buttonOf("Save", 60, this::saveNote);
        vBox.getChildren().addAll(
                addButton,
                deleteButton,
                editButton,
                saveButton);
        ButtonFx.buttonVisible(saveButton, false);
        return vBox;
    }

    private void saveNote() {
        membershipView.sendMessage().accept(MembershipMessage.UPDATE_NOTE);
        membershipModel.getSelectedNote().setMemo(membershipModel.getTextArea().getText());
        ButtonFx.buttonVisible(editButton, true);
        ButtonFx.buttonVisible(saveButton, false);
    }

    private void editNote() {
        membershipModel.getTextArea().setEditable(true);
        ButtonFx.buttonVisible(editButton, false);
        ButtonFx.buttonVisible(saveButton, true);

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

    private Node addTextArea() {
        TextArea textArea = membershipModel.getTextArea();
        HBox.setHgrow(textArea, Priority.ALWAYS);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        return textArea;
    }

    private Node addTable() {
        TableView<NotesFx> tableView = TableViewFx.tableViewOf(150,false);
        tableView.setItems(FXCollections.observableArrayList(membershipView.getMembershipModel().membershipProperty().get().getMemos()));
        tableView.getColumns().addAll(Arrays.asList(col1(), col2()));
        TableView.TableViewSelectionModel<NotesFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                membershipModel.setSelectedNote(newSelection);
                membershipModel.textAreaProperty().get().setText(newSelection.getMemo());
            }
        });
        // if there are notes in the List then select the first
        if(!tableView.getItems().isEmpty()) {
            membershipModel.setSelectedNote(tableView.getItems().getFirst());
            membershipModel.textAreaProperty().get().setText(tableView.getItems().getFirst().getMemo());
            tableView.getSelectionModel().select(tableView.getItems().getFirst());
        }
        membershipView.getMembershipModel().setNotesTableView(tableView);
        return tableView;
    }

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
