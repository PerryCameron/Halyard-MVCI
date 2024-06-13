package org.ecsail.mvci_membership;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import javafx.util.converter.IntegerStringConverter;
import org.ecsail.custom.CustomDatePicker;
import org.ecsail.custom.CustomIntegerTableCell;
import org.ecsail.dto.MembershipIdDTO;
import org.ecsail.enums.MembershipType;
import org.ecsail.widgetfx.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MembershipIdView implements Builder<Tab> {
    private final MembershipView membershipView;
    private final MembershipModel membershipModel;

    public MembershipIdView(MembershipView membershipView) {
        this.membershipView = membershipView;
        this.membershipModel = membershipView.getMembershipModel();
    }

    @Override
    public Tab build() {
        Tab tab = new Tab();
        tab.setText("History");
        VBox vBox = VBoxFx.vBoxOf(new Insets(2, 2, 2, 2), "custom-tap-pane-frame", true); // makes outer border
        vBox.getChildren().add(innerVBox());
        tab.setContent(vBox);
        ListenerFx.addSingleFireTabListener(tab, () -> membershipView.sendMessage().accept(MembershipMessage.SELECT_IDS));
        return tab;
    }

    private Node innerVBox() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(5, 5, 5, 5), "box-background-light", true);
        vBox.setSpacing(10);
        VBox.setVgrow(vBox, Priority.ALWAYS);
        vBox.getChildren().addAll(createTopControls(), addTable());
        return vBox;
    }

    private Node createTopControls() {
        HBox hBox = HBoxFx.hBoxOf(10, Pos.CENTER_LEFT);
        CustomDatePicker datePicker = new CustomDatePicker();
        datePicker.setValue(getDate());
        datePicker.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (!isFocused) {
                datePicker.updateValue();
                LocalDate date = datePicker.getValue();
                membershipModel.getMembership().setJoinDate(date.toString());
                membershipView.sendMessage()
                        .accept(MembershipMessage.UPDATE_MEMBERSHIP_LIST);
            }
        });
        hBox.getChildren().addAll(TextFx.textOf("Join Date", "text-white"), datePicker, createButtonBox());
        return hBox;
    }

    private Node createButtonBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(0, 0, 0, 50), 5.0);
        hBox.getChildren().addAll(createButton("Add"), createButton("Delete"));
        return hBox;
    }

    private Node createButton(String text) {
        Button button = ButtonFx.buttonOf(text, 60);
        switch (text) {
            case "Delete" -> button.setOnAction(event -> deleteMembershipId());
            case "Add" ->
                    button.setOnAction(event -> membershipView.sendMessage().accept(MembershipMessage.INSERT_MEMBERSHIP_ID));
        }
        return button;
    }

    private void deleteMembershipId() {
        String[] strings = {
                "Delete Membership ID",
                "Are you sure you want to delete this ID number?",
                "Missing Selection",
                "You need to select an ID first"};
        if (DialogueFx.verifyAction(strings, membershipModel.getSelectedMembershipId()))
            membershipView.sendMessage().accept(MembershipMessage.DELETE_MEMBERSHIP_ID);
    }

    private LocalDate getDate() {
        LocalDate date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (membershipModel.getMembership().getJoinDate() != null)
            date = LocalDate.parse(membershipView.getMembershipModel().getMembership().getJoinDate(), formatter);
        else date = LocalDate.parse("1900-01-01", formatter);
        return date;
    }

    private Node addTable() {
        TableView<MembershipIdDTO> tableView = TableViewFx.tableViewOf(MembershipIdDTO.class);
        tableView.getColumns().addAll(col1(), col2(), col3(), col4(), col5());
        TableView.TableViewSelectionModel<MembershipIdDTO> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) membershipModel.setSelectedMembershipId(newSelection);
        });
        membershipModel.setIdTableView(tableView);
        return tableView;
    }

    private TableColumn<MembershipIdDTO, Boolean> col5() {
        TableColumn<MembershipIdDTO, Boolean> col5 = new TableColumn<>("Renew Late");
        col5.setCellValueFactory(
                param -> {
                    MembershipIdDTO membershipIdDTO = param.getValue();
                    SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(membershipIdDTO.isLateRenew());
                    booleanProp.addListener((observable, oldValue, newValue) -> {
                        membershipIdDTO.setIsLateRenew(newValue);
                        membershipModel.setSelectedMembershipId(membershipIdDTO);
                        membershipView.sendMessage().accept(MembershipMessage.UPDATE_MEMBERSHIP_ID);
                    });
                    return booleanProp;
                });
        col5.setCellFactory(p -> {
            CheckBoxTableCell<MembershipIdDTO, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        col5.setMaxWidth(1f * Integer.MAX_VALUE * 20);   // Renew Late
        return col5;
    }

    private TableColumn<MembershipIdDTO, Boolean> col4() {
        TableColumn<MembershipIdDTO, Boolean> col4 = new TableColumn<>("Renewed");
        col4.setCellValueFactory(
                param -> {
                    MembershipIdDTO membershipIdDTO = param.getValue();
                    SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(membershipIdDTO.isRenew());
                    booleanProp.addListener((observable, oldValue, newValue) -> {
                        membershipIdDTO.setIsRenew(newValue);
                        membershipModel.setSelectedMembershipId(membershipIdDTO);
                        membershipView.sendMessage().accept(MembershipMessage.UPDATE_MEMBERSHIP_ID);
                    });
                    return booleanProp;
                });
        col4.setCellFactory(p -> {
            CheckBoxTableCell<MembershipIdDTO, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        col4.setMaxWidth(1f * Integer.MAX_VALUE * 15);   // Renewed
        return col4;
    }

    private TableColumn<MembershipIdDTO, MembershipType> col3() {
        ObservableList<MembershipType> MembershipTypeList = FXCollections.observableArrayList(MembershipType.values());
        TableColumn<MembershipIdDTO, MembershipType> col3 = new TableColumn<>("Mem Type");
        col3.setCellValueFactory(
                param -> {
                    MembershipIdDTO thisId = param.getValue();
                    String membershipCode = thisId.getMemType();
                    /// careful with capitals
                    MembershipType membershipType = MembershipType.getByCode(membershipCode);
                    return new SimpleObjectProperty<>(membershipType);
                });
        col3.setCellFactory(ComboBoxTableCell.forTableColumn(MembershipTypeList));
        col3.setOnEditCommit((TableColumn.CellEditEvent<MembershipIdDTO, MembershipType> event) -> {
            TablePosition<MembershipIdDTO, MembershipType> pos = event.getTablePosition();
            MembershipType newMembershipType = event.getNewValue();
            int row = pos.getRow();
            MembershipIdDTO membershipIdDTO = event.getTableView().getItems().get(row);
            membershipIdDTO.setMemType(newMembershipType.getCode());
            membershipModel.setSelectedMembershipId(membershipIdDTO);
            membershipView.sendMessage().accept(MembershipMessage.UPDATE_MEMBERSHIP_ID);
        });
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 25);   // Mem Type
        return col3;
    }

    private TableColumn<MembershipIdDTO, Integer> col2() {
        TableColumn<MembershipIdDTO, Integer> col2 = TableColumnFx.editableIntegerTableColumn(MembershipIdDTO::membershipIdProperty, "Mem ID");
        col2.setEditable(true);  // Ensure the TableColumn is editable

        col2.setCellFactory(tc -> new CustomIntegerTableCell<>(new IntegerStringConverter()));
        col2.setOnEditCommit(t -> {
            MembershipIdDTO membershipIdDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
            membershipIdDTO.setMembershipId(t.getNewValue());
            membershipModel.setSelectedMembershipId(membershipIdDTO);
            membershipView.sendMessage().accept(MembershipMessage.UPDATE_MEMBERSHIP_ID);
        });
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 15);  // Mem Id
        return col2;
    }

    private TableColumn<MembershipIdDTO, Integer> col1() {
        TableColumn<MembershipIdDTO, Integer> col1 = TableColumnFx.editableIntegerTableColumn(MembershipIdDTO::fiscalYearProperty, "Year");
        col1.setEditable(true);  // Ensure the TableColumn is editable

        col1.setCellFactory(tc -> new CustomIntegerTableCell<>(new IntegerStringConverter()));
        col1.setOnEditCommit(t -> {
            MembershipIdDTO membershipIdDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
            membershipIdDTO.setFiscalYear(t.getNewValue());
            membershipModel.setSelectedMembershipId(membershipIdDTO);
            membershipView.sendMessage().accept(MembershipMessage.UPDATE_MEMBERSHIP_ID);
        });

        col1.setMaxWidth(1f * Integer.MAX_VALUE * 25);  // Year
        return col1;
    }
}
