package org.ecsail.mvci_membership;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.util.Builder;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.MembershipIdDTO;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.TableViewFx;
import org.ecsail.widgetfx.VBoxFx;

import java.time.Year;
import java.util.Comparator;

public class InvoiceListView implements Builder<Tab> {
    private final MembershipView membershipView;

    public InvoiceListView(MembershipView membershipView) {
        this.membershipView = membershipView;
    }

    @Override
    public Tab build() {
        Tab tab = new Tab();
        tab.setText("Invoices");
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2),"custom-tap-pane-frame",false); // makes outer border
        BorderPane borderPane = new BorderPane();
        borderPane.setId("box-background-light");
        borderPane.setTop(createRecordAddBox());
        borderPane.setCenter(addTable());
        VBox.setVgrow(borderPane, Priority.ALWAYS); // causes slip tab to grow to fit vertical space
        vBox.getChildren().add(borderPane);
        tab.setContent(vBox);
        // Create a listener for tab selection change
        ListenerFx.createSingleUseTabListener(tab, () -> membershipView.sendMessage().accept(MembershipMessage.LOAD_INVOICES) );
        return tab;
    }

    private Node createRecordAddBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(7,2,5,10), 80);
        hBox.getChildren().addAll(createInvoiceAdder(), getButton("Delete"));
        return hBox;
    }

    private Node createInvoiceAdder() {
        HBox hBox = HBoxFx.hBoxOf(5, Pos.CENTER_LEFT);
        Label label = new Label("Create new Fiscal Year Record:");
        hBox.getChildren().addAll(label, getYear(),getButton("Add"));
        return hBox;
    }

    private Node getButton(String type) {
        Button button = new Button(type);
        return button;
    }

    private Node getYear() {
        ComboBox comboBox = new ComboBox<Integer>();
        comboBox.setPrefWidth(80);
        for (int i = Integer.parseInt(Year.now().toString()) + 1; i > 1969; i--) comboBox.getItems().add(i);
        comboBox.setValue(membershipView.getMembershipModel().getMembership().getSelectedYear()); // sets year for combo box to record year
        return comboBox;
    }


    private Node addTable() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5));
        TableView<InvoiceDTO> tableView = TableViewFx.tableViewOf(InvoiceDTO.class);
        tableView.getColumns().addAll(col1(),col2(),col3(),col4(),col5());
        TableView.TableViewSelectionModel<InvoiceDTO> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) membershipView.getMembershipModel().setSelectedInvoice(newSelection);
        });
        membershipView.getMembershipModel().setInvoiceListTableView(tableView);
        hBox.getChildren().add(tableView);
        return hBox;
    }

    private TableColumn<InvoiceDTO,Integer> col1() { //
        TableColumn col = new TableColumn<InvoiceDTO, Integer>("Year");
        col.setCellValueFactory(new PropertyValueFactory<>("year"));
        col.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );
        return col;
    }

    private TableColumn<InvoiceDTO,Integer> col2() { //
        TableColumn col = new TableColumn<InvoiceDTO, Integer>("Fees");
        col.setCellValueFactory(new PropertyValueFactory<>("total"));
        col.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );
        col.setStyle( "-fx-alignment: CENTER-RIGHT;");
        return col;
    }

    private TableColumn<InvoiceDTO,Integer> col3() { //
        TableColumn col = new TableColumn<InvoiceDTO, Integer>("Credit");
        col.setCellValueFactory(new PropertyValueFactory<>("credit"));
        col.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );
        col.setStyle( "-fx-alignment: CENTER-RIGHT;");
        return col;
    }

    private TableColumn<InvoiceDTO,Integer> col4() { //
        TableColumn col = new TableColumn<InvoiceDTO, Integer>("Paid");
        col.setCellValueFactory(new PropertyValueFactory<>("paid"));
        col.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );
        col.setStyle( "-fx-alignment: CENTER-RIGHT;");
        return col;
    }

    private TableColumn<InvoiceDTO,Integer> col5() { //
        TableColumn col = new TableColumn<InvoiceDTO, Integer>("Balance");
        col.setCellValueFactory(new PropertyValueFactory<>("balance"));
        col.setMaxWidth( 1f * Integer.MAX_VALUE * 20 );
        col.setStyle( "-fx-alignment: CENTER-RIGHT;");
        return col;
    }


}
