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
import org.ecsail.custom.CustomDatePicker;
import org.ecsail.dto.MembershipIdDTO;
import org.ecsail.enums.MembershipType;
import org.ecsail.widgetfx.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

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
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2),"custom-tap-pane-frame",true); // makes outer border
        vBox.getChildren().add(innerVBox());
        tab.setContent(vBox);
        return tab;
    }

    private Node innerVBox() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,5,5),"box-background-light",true);
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
            if (!isFocused){
                datePicker.updateValue();
                LocalDate date = datePicker.getValue();
                membershipModel.getMembership().setJoinDate(date.toString());
                membershipView.sendMessage()
                        .accept(MembershipMessage.UPDATE_MEMBERSHIP_LIST);
                membershipView.getMembershipModel().getMembership().setJoinDate(date.toString());
            }
        });
        hBox.getChildren().addAll(TextFx.textOf("Join Date","text-white"), datePicker, createButtonBox());
        return hBox;
    }

    private Node createButtonBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(0,0,0,50), 5.0);
        hBox.getChildren().addAll(createAddButton(),addDeleteButton());
        return hBox;
    }

    private Node addDeleteButton() {
        Button button = new Button("Delete");
        button.setOnAction(event -> {

        });
        return button;
    }

    private Node createAddButton() {
        Button button = new Button("Add");
        button.setOnAction(event -> {

        });
        return button;
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
        TableView tableView = TableViewFx.tableViewOf(MembershipIdDTO.class);
        membershipModel.getMembership().getMembershipIdDTOS()
                .sort(Comparator.comparing(MembershipIdDTO::getFiscal_Year).reversed());
        tableView.setItems(membershipView.getMembershipModel().getMembership().getMembershipIdDTOS());
        tableView.getColumns().addAll(col1(),col2(),col3(),col4(),col5());
        return tableView;
    }

    private TableColumn<MembershipIdDTO, Boolean> col5() {
        TableColumn<MembershipIdDTO, Boolean> col5 = new TableColumn<>("Renew Late");
        col5.setCellValueFactory(
                param -> {
                    MembershipIdDTO membershipIdDTO = param.getValue();
                    SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(membershipIdDTO.getIsLateRenew());
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
                    SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(membershipIdDTO.getIsRenew());
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

    private TableColumn<MembershipIdDTO,MembershipType> col3() {
        ObservableList<MembershipType> MembershipTypeList = FXCollections.observableArrayList(MembershipType.values());
        TableColumn<MembershipIdDTO, MembershipType> col3 = new TableColumn<>("Mem Type");
        col3.setCellValueFactory(
                param -> {
                    MembershipIdDTO thisId = param.getValue();
                    String membershipCode = thisId.getMem_type();
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
            membershipIdDTO.setMem_type(newMembershipType.getCode());
            membershipModel.setSelectedMembershipId(membershipIdDTO);
            membershipView.sendMessage().accept(MembershipMessage.UPDATE_MEMBERSHIP_ID);
        });
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 25);   // Mem Type
        return col3;
    }

    private TableColumn<MembershipIdDTO,String> col2() {
        TableColumn<MembershipIdDTO, String> col2 = TableColumnFx.tableColumnOf(MembershipIdDTO::membership_idProperty, "Mem ID");
        col2.setOnEditCommit(t -> {
            MembershipIdDTO membershipIdDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
            membershipIdDTO.setMembership_id(t.getNewValue());
            membershipModel.setSelectedMembershipId(membershipIdDTO);
            membershipView.sendMessage().accept(MembershipMessage.UPDATE_MEMBERSHIP_ID);          });
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 15);  // Mem Id
        return col2;
    }

    private TableColumn<MembershipIdDTO,String> col1() {
        TableColumn<MembershipIdDTO, String> col1 = TableColumnFx.tableColumnOf(MembershipIdDTO::fiscal_YearProperty,"Year");
        col1.setOnEditCommit(t -> {
            MembershipIdDTO membershipIdDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
            membershipIdDTO.setFiscal_Year(t.getNewValue());
            membershipModel.setSelectedMembershipId(membershipIdDTO);
            membershipView.sendMessage().accept(MembershipMessage.UPDATE_MEMBERSHIP_ID);          });
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 25);   // Year
        return col1;
    }
}
