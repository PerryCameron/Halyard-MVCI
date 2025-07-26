package org.ecsail.mvci.membership.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.fx.NoteFx;
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
    private Button editButton;
    private Button saveButton;
    private Button cancelButton;
    private TableView<NoteFx> tableView;
    private DatePicker datePicker;

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
        hBox.getChildren().addAll(createTableStackPane(), addTextArea(), getButtonControls());
        return hBox;
    }

    private Node getButtonControls() {
        VBox vBox = VBoxFx.vBoxOf(5.0, new Insets(10, 5, 0, 5));
        Button addButton = ButtonFx.buttonOf("Add", 60, this::insertNote);
        Button deleteButton = ButtonFx.buttonOf("Delete", 60, this::deleteNote);
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
        membershipModel.setTableInEditMode(false); // Exit edit mode
        tableView.setVisible(true); // Show TableView
        datePicker.setVisible(false); // Hide DatePicker
        ButtonFx.buttonVisible(editButton, true);
        ButtonFx.buttonVisible(saveButton, false);
        ButtonFx.buttonVisible(cancelButton, false);
    }

    private void saveNote() {
        membershipView.sendMessage().accept(MembershipMessage.UPDATE_NOTE);
        membershipModel.getSelectedNote().setMemo(membershipModel.getTextArea().getText());
        if (datePicker.getValue() != null) {
            membershipModel.getSelectedNote().setMemoDate(datePicker.getValue());
        }
        cancelEdit();
        tableView.refresh(); // Refresh to update date in TableView
    }

    private void editNote() {
        if (membershipModel.getSelectedNote() != null) {
            membershipModel.getTextArea().setEditable(true);
            membershipModel.setTableInEditMode(true); // Enter edit mode
            tableView.setVisible(false); // Hide TableView
            datePicker.setVisible(true); // Show DatePicker
            datePicker.setValue(membershipModel.getSelectedNote().getMemoDate()); // Set current date
            ButtonFx.buttonVisible(editButton, false);
            ButtonFx.buttonVisible(saveButton, true);
            ButtonFx.buttonVisible(cancelButton, true);
        }
    }

    private void insertNote() {
        membershipView.sendMessage().accept(MembershipMessage.INSERT_NOTE);
        editNote();
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

    private Node createTableStackPane() {
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(addTable(), createDatePicker());
        tableView.setVisible(true); // TableView visible by default
        datePicker.setVisible(false); // DatePicker hidden by default
        StackPane.setAlignment(datePicker, Pos.TOP_CENTER); // Align DatePicker to top-center
        StackPane.setMargin(datePicker, new Insets(10, 0, 0, 0)); // Optional: Add top margin for spacing
        stackPane.setPrefWidth(210); // Match TableView width
        return stackPane;
    }

    private Node createDatePicker() {
        this.datePicker = new DatePicker();
        datePicker.setPrefWidth(150); // Match date column width
        return datePicker;
    }

    private Node addTable() {
        this.tableView = TableViewFx.tableViewOf(200, false);
        tableView.setItems(membershipView.getMembershipModel().membershipProperty().get().getMemos());
        tableView.getColumns().addAll(Arrays.asList(col1(), col2()));
        TableView.TableViewSelectionModel<NoteFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                membershipModel.setSelectedNote(newSelection);
                membershipModel.textAreaProperty().get().setText(newSelection.getMemo());
            }
        });
        tableView.setPrefWidth(210);
        // If there are notes in the list, select the first
        if (!tableView.getItems().isEmpty()) {
            membershipModel.setSelectedNote(tableView.getItems().getFirst());
            membershipModel.textAreaProperty().get().setText(tableView.getItems().getFirst().getMemo());
            tableView.getSelectionModel().select(tableView.getItems().getFirst());
        }
        membershipView.getMembershipModel().setNotesTableView(tableView);
        return tableView;
    }

    private TableColumn<NoteFx, String> col2() {
        TableColumn<NoteFx, String> col = new TableColumn<>("Type");
        col.setCellValueFactory(new PropertyValueFactory<>("category"));
        col.setPrefWidth(60);
        col.setMaxWidth(60);
        return col;
    }

    private TableColumn<NoteFx, LocalDate> col1() {
        TableColumn<NoteFx, LocalDate> col = new TableColumn<>("Date");
        col.setCellValueFactory(cellData -> cellData.getValue().memoDateProperty());
        col.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
            }
        });
        col.setPrefWidth(150);
        col.setMaxWidth(150);
        return col;
    }
}
