package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import javafx.util.Callback;
import org.ecsail.dto.NotesDTO;
import org.ecsail.mvci_boat.BoatMessage;
import org.ecsail.widgetfx.*;

import java.time.LocalDate;

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
        tableView.getColumns().addAll(col1(),col2(),col3());  // Add col1 back in TODO
        membershipView.getMembershipModel().setNotesTableView(tableView);
        return tableView;
    }

    private TableColumn<NotesDTO,String> col3() {
        TableColumn<NotesDTO, String> col3 = TableColumnFx.tableColumnOf(NotesDTO::memoProperty,"Note");
        col3.setPrefWidth(740);
        col3.setOnEditCommit(t -> {
                    NotesDTO notesDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    notesDTO.setMemo(t.getNewValue());
                    membershipModel.setSelectedNote(notesDTO);
                    membershipView.sendMessage().accept(MembershipMessage.UPDATE_NOTE);
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

private TableColumn<NotesDTO, LocalDate> col1() {
    TableColumn<NotesDTO, LocalDate> col1 = new TableColumn<>("Date");
    col1.setCellValueFactory(cellData -> cellData.getValue().memoDateProperty());
    col1.setCellFactory(CallBackFX.createDatePickerCellFactory(notesDTO -> {
            membershipModel.setSelectedNote(notesDTO);
            membershipView.sendMessage().accept(MembershipMessage.UPDATE_NOTE);
    }));
    col1.setMaxWidth(1f * Integer.MAX_VALUE * 15);   // Date
    return col1;
}

//    private Callback<TableColumn<NotesDTO, LocalDate>, TableCell<NotesDTO, LocalDate>> createDatePickerCellFactory() {
//        return param -> new TableCell<>() {
//            private final DatePicker datePicker = new DatePicker();
//            { // this is called: instance initializer block
//                datePicker.setOnAction(event -> {
//                    commitEdit(datePicker.getValue());
//                    NotesDTO notesDTO = null;
//                    if(this.getTableRow() != null)
//                    notesDTO = this.getTableRow().getItem();
//                    // Check if notesDTO is not null before calling methods on it
//                    if(notesDTO != null) {
//                        notesDTO.setMemoDate(datePicker.getValue());
//                        membershipView.sendMessage().accept(MembershipMessage.UPDATE,notesDTO);
//                    }
//                });
//            }
//
//            @Override
//            protected void updateItem(LocalDate item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    datePicker.setValue(item);
//                    setGraphic(datePicker);
//                }
//            }
//        };
//    }
    
}
