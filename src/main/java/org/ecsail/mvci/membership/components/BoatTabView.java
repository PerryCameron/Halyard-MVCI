package org.ecsail.mvci.membership.components;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.fx.BoatFx;
import org.ecsail.mvci.membership.MembershipMessage;
import org.ecsail.mvci.membership.MembershipModel;
import org.ecsail.mvci.membership.MembershipView;
import org.ecsail.widgetfx.*;

import java.util.Arrays;

public class BoatTabView implements Builder<Tab> {
    private final MembershipView membershipView;
    private final MembershipModel membershipModel;
    private TableColumn<BoatFx, String> column1;

    public BoatTabView(MembershipView membershipView) {
        this.membershipView = membershipView;
        this.membershipModel = membershipView.getMembershipModel();
    }

    @Override
    public Tab build() {
        return TabFx.tabOf("Boats", createTableViewAndButtonsBox());
    }

    private Node createTableViewAndButtonsBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5),"box-background-light",true);
        hBox.getChildren().addAll(getTableView(), getButtonControls());
        return hBox;
    }

    private Node getButtonControls() {
        VBox vBox = VBoxFx.vBoxOf(5.0, new Insets(10,5,5,10));
        vBox.getChildren().addAll(createButton("Add"),createButton("Delete"),createButton("View"));
        return vBox;
    }

    private void deleteBoat() {
        String[] strings = {
                "Delete Boat",
                "Are you sure you want to delete this boat?",
                "Missing Selection",
                "You need to select a boat first"};
        if (DialogueFx.verifyAction(strings, membershipModel.getSelectedBoat()))
            membershipView.sendMessage().accept(MembershipMessage.DELETE_BOAT);
    }

    private Node createButton(String type) {
        Button button = ButtonFx.buttonOf(type, 60);
        switch (type) {
            case "Add" -> button.setOnAction(event -> membershipView.sendMessage().accept(MembershipMessage.INSERT_BOAT));
            case "Delete" -> button.setOnAction(event -> deleteBoat());
            case "View" -> button.setOnAction(event -> System.out.println("View"));
        }
        return button;
    }

    private Node getTableView() {
        TableView<BoatFx> tableView = TableViewFx.tableViewOf(200,true);
        membershipView.getMembershipModel().setBoatTableView(tableView);
        tableView.setItems(membershipModel.membershipProperty().get().getBoats());
        tableView.getColumns().addAll(Arrays.asList(col1(),col2(),col3(),col4(),col5(),col7(),col8(),col9(),col10()));
        TableView.TableViewSelectionModel<BoatFx> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) membershipModel.setSelectedBoat(newSelection);
        });
        return tableView;
    }

    private TableColumn<BoatFx,String> col10() {
        TableColumn<BoatFx, String> col10 = TableColumnFx.editableStringTableColumn(BoatFx::displacementProperty,"Displacement");
        col10.setOnEditCommit(t -> editAndUpdateCell(t,"setDisplacement"));
        col10.setMaxWidth(1f * Integer.MAX_VALUE * 10);  // Trailer
        return col10;
    }

    private TableColumn<BoatFx,String> col9() {
        TableColumn<BoatFx, String> col9 = TableColumnFx.editableStringTableColumn(BoatFx::draftProperty,"Draft");
        col9.setOnEditCommit(t -> editAndUpdateCell(t,"setDraft"));
        col9.setMaxWidth(1f * Integer.MAX_VALUE * 10);   // Weight
        return col9;
    }

    private TableColumn<BoatFx,String> col8() {
        TableColumn<BoatFx, String> col8 = TableColumnFx.editableStringTableColumn(BoatFx::loaProperty,"LOA");
        col8.setOnEditCommit(t -> editAndUpdateCell(t,"setLoa"));
        col8.setMaxWidth(1f * Integer.MAX_VALUE * 10);   // Length
        return col8;
    }

    private TableColumn<BoatFx,String> col7() {
        final TableColumn<BoatFx, String> col7 = TableColumnFx.editableStringTableColumn(BoatFx::lwlProperty,"LWL");
        col7.setOnEditCommit(t -> editAndUpdateCell(t,"setLwl"));
        col7.setMaxWidth(1f * Integer.MAX_VALUE * 5);   // PHRF
        return col7;
    }

    private TableColumn<BoatFx, String> col5() {
        TableColumn<BoatFx, String> col5 = TableColumnFx.editableStringTableColumn(BoatFx::registrationNumProperty, "Registration");
        col5.setOnEditCommit(t -> editAndUpdateCell(t,"setRegistrationNum"));
        col5.setMaxWidth(1f * Integer.MAX_VALUE * 10);  // Registration
        return col5;
    }

    private TableColumn<BoatFx, String> col4() {
        final TableColumn<BoatFx, String> col4 = TableColumnFx.editableStringTableColumn(BoatFx::modelProperty, "Model");
        col4.setOnEditCommit(t -> editAndUpdateCell(t,"setModel"));
        col4.setMaxWidth(1f * Integer.MAX_VALUE * 20);  // Model
        return col4;
    }

    private TableColumn<BoatFx, String> col3() {
        TableColumn<BoatFx, String> col3 = TableColumnFx.editableStringTableColumn(BoatFx::manufactureYearProperty, "Year");
        col3.setOnEditCommit(t -> editAndUpdateCell(t,"setManufactureYear"));
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 5);   // Year
        return col3;
    }

    private TableColumn<BoatFx, String> col2() {
        TableColumn<BoatFx, String> col2 = TableColumnFx.editableStringTableColumn(BoatFx::manufacturerProperty, "Manufacturer");
        col2.setOnEditCommit(t -> editAndUpdateCell(t,"setManufacturer"));
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 15);  // Manufacturer
        return col2;
    }

    private TableColumn<BoatFx, String> col1() {
        TableColumn<BoatFx, String> col1 = TableColumnFx.editableStringTableColumn(BoatFx::boatNameProperty, "Boat Name");
        column1 = col1;
        col1.setOnEditCommit(t -> editAndUpdateCell(t,"setBoatName"));
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 15);  // Boat Name
        return col1;
    }

    private void editAndUpdateCell(TableColumn.CellEditEvent<BoatFx, String> t, String type) {
        BoatFx boatDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
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
        membershipView.sendMessage().accept(MembershipMessage.UPDATE_BOAT);
    }


}
