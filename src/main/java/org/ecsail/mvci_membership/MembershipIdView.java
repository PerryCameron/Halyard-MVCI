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
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.MembershipIdDTO;
import org.ecsail.enums.MembershipType;
import org.ecsail.interfaces.Messages;
import org.ecsail.widgetfx.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class MembershipIdView implements Builder<Tab> {
    private final MembershipView membershipView;

    public MembershipIdView(MembershipView membershipView) {
        this.membershipView = membershipView;
    }

    @Override
    public Tab build() {
        Tab tab = new Tab();
        tab.setText("History");
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,5,5),"custom-tap-pane-frame",true); // makes outer border
        vBox.getChildren().add(innerVBox());
        tab.setContent(vBox);
        return tab;
    }

    private Node innerVBox() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,5,5),"box-background-light",true);
        vBox.setSpacing(10);
        vBox.getChildren().addAll(createTopControls(), addTable());
        return vBox;
    }

    private Node createTopControls() {
        HBox hBox = HBoxFx.hBoxOf(10, Pos.CENTER_LEFT);
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(getDate());
        hBox.getChildren().addAll(TextFx.textOf("Join Date","text-white"), datePicker, createButtonBox());
        return hBox;
    }

    private Node createButtonBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(0,0,0,50), 5.0);
        Button button1 = new Button("Add");
        Button button2 = new Button("Delete");
        hBox.getChildren().addAll(button1,button2);
        return hBox;
    }

    private LocalDate getDate() {
        LocalDate date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (membershipView.getMembershipModel().getMembership().getJoinDate() != null)
            date = LocalDate.parse(membershipView.getMembershipModel().getMembership().getJoinDate(), formatter);
        else date = LocalDate.parse("1900-01-01", formatter);
        return date;
    }

    private Node addTable() {
        TableView tableView = TableViewFx.tableViewOf(MembershipIdDTO.class);
        membershipView.getMembershipModel().getMembership().getMembershipIdDTOS()
                .sort(Comparator.comparing(MembershipIdDTO::getFiscal_Year).reversed());
        tableView.setItems(membershipView.getMembershipModel().getMembership().getMembershipIdDTOS());
        tableView.getColumns().addAll(col1(),col2(),col3(),col4(),col5());
        return tableView;
    }

    private TableColumn<MembershipIdDTO, Boolean> col5() {
        TableColumn<MembershipIdDTO, Boolean> col5 = new TableColumn<>("Renew Late");
        col5.setCellValueFactory(
                param -> {
                    MembershipIdDTO id = param.getValue();
                    SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(id.isLateRenew());
                    booleanProp.addListener((observable, oldValue, newValue) -> {
                        id.setIsLateRenew(newValue);
                            membershipView.sendMessage().accept(Messages.MessageType.UPDATE, id);
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
                    MembershipIdDTO id = param.getValue();
                    SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(id.isRenew());
                    booleanProp.addListener((observable, oldValue, newValue) -> {
                        id.setIsRenew(newValue);
                        membershipView.sendMessage().accept(Messages.MessageType.UPDATE, id);
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
            MembershipIdDTO id = event.getTableView().getItems().get(row);
            id.setMem_type(newMembershipType.getCode());
            membershipView.sendMessage().accept(Messages.MessageType.UPDATE, id);
        });
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 25);   // Mem Type
        return col3;
    }

    private TableColumn<MembershipIdDTO,String> col2() {
        TableColumn<MembershipIdDTO, String> col2 = TableColumnFx.tableColumnOf(MembershipIdDTO::membership_idProperty, "Mem ID");
        col2.setOnEditCommit(t -> {
            MembershipIdDTO id = t.getTableView().getItems().get(t.getTablePosition().getRow());
            id.setMembership_id(t.getNewValue());
            membershipView.sendMessage().accept(Messages.MessageType.UPDATE, id);
        });
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 15);  // Mem Id
        return col2;
    }

    private TableColumn<MembershipIdDTO,String> col1() {
        TableColumn<MembershipIdDTO, String> col1 = TableColumnFx.tableColumnOf(MembershipIdDTO::fiscal_YearProperty,"Year");
        col1.setOnEditCommit(t -> {
            MembershipIdDTO id = t.getTableView().getItems().get(t.getTablePosition().getRow());
            id.setFiscal_Year(t.getNewValue());
            membershipView.sendMessage().accept(Messages.MessageType.UPDATE, id);
        });
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 25);   // Year
        return col1;
    }
}
