package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.NotesDTO;
import org.ecsail.interfaces.Messages;
import org.ecsail.widgetfx.*;

public class NotesTabView implements Builder<Tab> {
    private final MembershipView membershipView;

    public NotesTabView(MembershipView membershipView) {
        this.membershipView = membershipView;
    }

    @Override
    public Tab build() {
        return TabFx.tabOf("Notes", createTableAndButtonsBox());
    }

    private Node createTableAndButtonsBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5),"box-background-light",true);
        hBox.getChildren().addAll(addTable(), getButtonControls());
        return hBox;
    }

    private Node getButtonControls() {
        VBox vBox = VBoxFx.vBoxOf(5.0, new Insets(10,5,5,10));
        vBox.getChildren().addAll(createButton1(),createButton2());
        return vBox;
    }

    private Node createButton1() {
        Button button = ButtonFx.buttonOf("Add", 60);
        button.setOnAction(event -> {

        });
        return button;
    }

    private Node createButton2() {
        Button button = ButtonFx.buttonOf("Delete", 60);
        button.setOnAction(event -> {

        });
        return button;
    }

    private Node addTable() {
        TableView tableView = TableViewFx.tableViewOf(NotesDTO.class, 200);
        tableView.setItems(membershipView.getMembershipModel().getMembership().getNotesDTOS());
        tableView.getColumns().addAll(col1(),col2(),col3());
        membershipView.getMembershipModel().setNotesTableView(tableView);
        return tableView;
    }

    private TableColumn<NotesDTO,String> col3() {
        TableColumn<NotesDTO, String> col3 = TableColumnFx.tableColumnOf(NotesDTO::memoProperty,"Note");
        col3.setPrefWidth(740);
        col3.setOnEditCommit(t -> {
                    NotesDTO notesDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    notesDTO.setMemo(t.getNewValue());
                    membershipView.sendMessage().accept(Messages.MessageType.UPDATE,notesDTO);
        });
        col3.setMaxWidth( 1f * Integer.MAX_VALUE * 85 );   // Note
        return col3;
    }

    private TableColumn<NotesDTO,String> col2() {
        TableColumn<NotesDTO,String> col2 = new TableColumn<NotesDTO, String>("Type");
        col2.setCellValueFactory(new PropertyValueFactory<>("category"));
        col2.setMaxWidth( 1f * Integer.MAX_VALUE * 5 );    // Type
        return col2;
    }

    private TableColumn<NotesDTO,String> col1() {
        TableColumn<NotesDTO, String> col1 = TableColumnFx.tableColumnOf(NotesDTO::memoDateProperty,"Date");
        col1.setOnEditCommit(
                t -> {
                    NotesDTO notesDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    notesDTO.setMemoDate(t.getNewValue());
                    membershipView.sendMessage().accept(Messages.MessageType.UPDATE,notesDTO);
                });
        col1.setMaxWidth( 1f * Integer.MAX_VALUE * 10 );   // Date
        return col1;
    }
}
