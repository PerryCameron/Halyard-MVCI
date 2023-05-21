package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.BoatDTO;
import org.ecsail.interfaces.Messages;
import org.ecsail.widgetfx.*;

import java.util.Comparator;

public class BoatTabView implements Builder<Tab>, Messages {
    private final MembershipView membershipView;
    private TableColumn<BoatDTO, String> column1;

    public BoatTabView(MembershipView membershipView) {
        this.membershipView = membershipView;
    }

    @Override
    public Tab build() {
        Tab tab = new Tab("Boats");
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,5,5)); // makes outer border
        vBox.setId("custom-tap-pane-frame");
        HBox.setHgrow(vBox, Priority.ALWAYS);
        vBox.getChildren().add(createTableViewAndButtonsBox());
        tab.setContent(vBox);
        return tab;
    }

    private Node createTableViewAndButtonsBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5),"box-background-light",false);
        hBox.setPrefHeight(200);
        hBox.getChildren().addAll(getTableView(), getButtonControls());
        return hBox;
    }

    private Node getButtonControls() {
        VBox vBox = VBoxFx.vBoxOf(5.0, new Insets(10,5,5,10));
        vBox.getChildren().addAll(createButton1(),createButton2(),createButton3());
        return vBox;
    }

    private Node createButton3() {
        Button button = ButtonFx.buttonOf("View", 60);
        button.setOnAction(event -> {
            // TODO
        });
        return button;
    }

    private Node createButton2() {
        Button button = ButtonFx.buttonOf("Delete", 60);
        button.setOnAction(event -> {
            int selectedIndex = membershipView.getMembershipModel().getBoatTableView().getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) { // TODO add error checking and prompt
                BoatDTO boatDTO = membershipView.getMembershipModel().getMembership().getBoatDTOS().get(selectedIndex);
                membershipView.sendMessage().accept(MessageType.DELETE, boatDTO);
                // can't I remove it from the list iteslf?
                membershipView.getMembershipModel().getBoatTableView().getItems().remove(boatDTO);
            }
        });
        return button;
    }

    // TODO need to complete this correctly, it curretly doesn't assign boat_id, it does create the object and row
    // into the TableView
    private Node createButton1() {
        Button button = ButtonFx.buttonOf("Add", 60);
        button.setOnAction(event -> {
            BoatDTO boatDTO = new BoatDTO(membershipView.getMembershipModel().getMembership().getMsId());
            membershipView.getMembershipModel().getMembership().getBoatDTOS().add(boatDTO);
            membershipView.getMembershipModel().getMembership().getBoatDTOS().sort(Comparator.comparing(BoatDTO::getBoatId));
            membershipView.getMembershipModel().getBoatTableView().layout();
            membershipView.getMembershipModel().getBoatTableView().edit(0, column1);
            membershipView.sendMessage().accept(MessageType.INSERT, boatDTO);
        });
        return button;
    }

    private Node getTableView() {
        VBox vBox = new VBox();
        HBox.setHgrow(vBox,Priority.ALWAYS);
        TableView<BoatDTO> tableView = TableViewFx.tableViewOf(BoatDTO.class);
        membershipView.getMembershipModel().setBoatTableView(tableView);
        tableView.setItems(membershipView.getMembershipModel().getMembership().getBoatDTOS());
        tableView.getColumns().addAll(col1(),col2(),col3(),col4(),col5(),col7(),col8(),col9(),col10());
        vBox.getChildren().add(tableView);
        return vBox;
    }

    private TableColumn<BoatDTO,String> col10() {
        TableColumn<BoatDTO, String> col10 = TableColumnFx.tableColumnOf(BoatDTO::displacementProperty,"Displacement");
        col10.setOnEditCommit(t -> editAndUpdateCell(t,"setDisplacement"));
        col10.setMaxWidth(1f * Integer.MAX_VALUE * 10);  // Trailer
        return col10;
    }

    private TableColumn<BoatDTO,String> col9() {
        TableColumn<BoatDTO, String> col9 = TableColumnFx.tableColumnOf(BoatDTO::draftProperty,"Draft");
        col9.setOnEditCommit(t -> editAndUpdateCell(t,"setDraft"));
        col9.setMaxWidth(1f * Integer.MAX_VALUE * 10);   // Weight
        return col9;
    }

    private TableColumn<BoatDTO,String> col8() {
        TableColumn<BoatDTO, String> col8 = TableColumnFx.tableColumnOf(BoatDTO::loaProperty,"LOA");
        col8.setOnEditCommit(t -> editAndUpdateCell(t,"setLoa"));
        col8.setMaxWidth(1f * Integer.MAX_VALUE * 10);   // Length
        return col8;
    }

    private TableColumn<BoatDTO,String> col7() {
        final TableColumn<BoatDTO, String> col7 = TableColumnFx.tableColumnOf(BoatDTO::lwlProperty,"LWL");
        col7.setOnEditCommit(t -> editAndUpdateCell(t,"setLwl"));
        col7.setMaxWidth(1f * Integer.MAX_VALUE * 5);   // PHRF
        return col7;
    }

    private TableColumn<BoatDTO, String> col5() {
        TableColumn<BoatDTO, String> col5 = TableColumnFx.tableColumnOf(BoatDTO::registrationNumProperty, "Registration");
        col5.setOnEditCommit(t -> editAndUpdateCell(t,"setRegistrationNum"));
        col5.setMaxWidth(1f * Integer.MAX_VALUE * 10);  // Registration
        return col5;
    }

    private TableColumn<BoatDTO, String> col4() {
        final TableColumn<BoatDTO, String> col4 = TableColumnFx.tableColumnOf(BoatDTO::modelProperty, "Model");
        col4.setOnEditCommit(t -> editAndUpdateCell(t,"setModel"));
        col4.setMaxWidth(1f * Integer.MAX_VALUE * 20);  // Model
        return col4;
    }

    private TableColumn<BoatDTO, String> col3() {
        TableColumn<BoatDTO, String> col3 = TableColumnFx.tableColumnOf(BoatDTO::manufactureYearProperty, "Year");
        col3.setOnEditCommit(t -> editAndUpdateCell(t,"setManufactureYear"));
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 5);   // Year
        return col3;
    }

    private TableColumn<BoatDTO, String> col2() {
        TableColumn<BoatDTO, String> col2 = TableColumnFx.tableColumnOf(BoatDTO::manufacturerProperty, "Manufacturer");
        col2.setOnEditCommit(t -> editAndUpdateCell(t,"setManufacturer"));
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 15);  // Manufacturer
        return col2;
    }

    private TableColumn<BoatDTO, String> col1() {
        TableColumn<BoatDTO, String> col1 = TableColumnFx.tableColumnOf(BoatDTO::boatNameProperty, "Boat Name");
        column1 = col1;
        col1.setOnEditCommit(t -> editAndUpdateCell(t,"setBoatName"));
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 15);  // Boat Name
        return col1;
    }

    private void editAndUpdateCell(TableColumn.CellEditEvent<BoatDTO, String> t, String type) {
        BoatDTO boatDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
        switch (type) {
            case "setBoatName" -> boatDTO.setBoatName(t.getNewValue());
            case "setManufacturer" -> boatDTO.setManufacturer(t.getNewValue());
            case "setManufactureYear" -> boatDTO.setManufactureYear(t.getNewValue());
            case "setModel" -> boatDTO.setModel(t.getNewValue());
            case "setRegistrationNum" -> boatDTO.setRegistrationNum(t.getNewValue());
            case "setLwl" -> boatDTO.setLwl(t.getNewValue());
            case "setLoa" -> boatDTO.setLoa(t.getNewValue());
            case "setDraft" -> boatDTO.setDraft(t.getNewValue());
            case "setDisplacement" -> boatDTO.setDisplacement(t.getNewValue());
        }
        membershipView.sendMessage().accept(MessageType.UPDATE, boatDTO);
    }


}
