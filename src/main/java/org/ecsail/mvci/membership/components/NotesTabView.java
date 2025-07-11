package org.ecsail.mvci.membership.components;

import javafx.beans.property.SimpleStringProperty;
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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class NotesTabView implements Builder<Tab> {
    private final MembershipView membershipView;
    private final MembershipModel membershipModel;
    private Button addButton;
    private Button deleteButton;
    private Button editButton;
    private Button saveButton;
    private Button cancelButton;
    private TableView<NotesFx> tableView;

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
        hBox.setSpacing(5);
        hBox.getChildren().addAll(addTable(), addTextArea(), getButtonControls());
        return hBox;
    }

    private Node getButtonControls() {
        VBox vBox = VBoxFx.vBoxOf(5.0, new Insets(10, 5, 0, 5));
        this.addButton = ButtonFx.buttonOf("Add", 60, this::insertNote);
        this.deleteButton = ButtonFx.buttonOf("Delete", 60, this::deleteNote);
        this.editButton = ButtonFx.buttonOf("Edit", 60, this::editNote);
        this.saveButton = ButtonFx.buttonOf("Save", 60, this::saveNote);
        this.cancelButton = ButtonFx.buttonOf("Cancel", 60, this::cancelEdit);
        vBox.getChildren().addAll(
                addButton,
                deleteButton,
                editButton,
                cancelButton,
                saveButton);
        ButtonFx.buttonVisible(saveButton, false);
        ButtonFx.buttonVisible(cancelButton, false);
        return vBox;
    }

    private void cancelEdit() {
        membershipModel.getTextArea().setEditable(false);
        ButtonFx.buttonVisible(editButton, true);
        ButtonFx.buttonVisible(saveButton, false);
        ButtonFx.buttonVisible(cancelButton, false);
        tableView.getColumns().remove(0);
        tableView.getColumns().add(0,col1());
    }

    private void saveNote() {
        membershipView.sendMessage().accept(MembershipMessage.UPDATE_NOTE);
        membershipModel.getSelectedNote().setMemo(membershipModel.getTextArea().getText());
        membershipModel.getTextArea().setEditable(false);
        ButtonFx.buttonVisible(editButton, true);
        ButtonFx.buttonVisible(saveButton, false);
        ButtonFx.buttonVisible(cancelButton, false);
        tableView.getColumns().remove(0);
        tableView.getColumns().add(0,col1());
    }

    private void editNote() {
        membershipModel.getTextArea().setEditable(true);
        ButtonFx.buttonVisible(editButton, false);
        ButtonFx.buttonVisible(saveButton, true);
        ButtonFx.buttonVisible(cancelButton, true);
        tableView.getColumns().remove(0);
        tableView.getColumns().add(0,editCol());
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
        this.tableView = TableViewFx.tableViewOf(200,false);
        tableView.setItems(FXCollections.observableArrayList(membershipView.getMembershipModel().membershipProperty().get().getMemos()));
        tableView.getColumns().addAll(Arrays.asList(col1(), col2()));
        TableView.TableViewSelectionModel<NotesFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                membershipModel.setSelectedNote(newSelection);
                membershipModel.textAreaProperty().get().setText(newSelection.getMemo());
            }
        });
        tableView.setPrefWidth(210);
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
        TableColumn<NotesFx, String> col = new TableColumn<>("Type");
        col.setCellValueFactory(new PropertyValueFactory<>("category"));
        col.setPrefWidth(60); // Reasonable preferred width
        col.setMaxWidth(60); // Type
        return col;
    }

    private TableColumn<NotesFx, String> col1() {
        TableColumn<NotesFx, String> col = new TableColumn<>("Date");
        col.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getMemoDate();
            return new SimpleStringProperty(date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "");
        });
        col.setPrefWidth(150); // Reasonable preferred width
        col.setMaxWidth(150);  // Reasonable max width
        return col;
    }

    private TableColumn<NotesFx, LocalDate> editCol() {
        TableColumn<NotesFx, LocalDate> col = new TableColumn<>("Date");
        col.setCellValueFactory(cellData -> cellData.getValue().memoDateProperty());
        col.setCellFactory(CallBackFX.createDatePickerCellFactory(notesDTO -> {
            membershipModel.setSelectedNote(notesDTO);
//            membershipView.sendMessage().accept(MembershipMessage.UPDATE_NOTE);
        }));
        col.setPrefWidth(150); // Reasonable preferred width
        col.setMaxWidth(150);  // Reasonable max width
        return col;
    }
}
