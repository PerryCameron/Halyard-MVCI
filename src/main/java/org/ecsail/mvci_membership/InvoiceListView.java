package org.ecsail.mvci_membership;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.MembershipIdDTO;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.TableViewFx;
import org.ecsail.widgetfx.VBoxFx;

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

    private Node addTable() {
        TableView<InvoiceDTO> tableView = TableViewFx.tableViewOf(InvoiceDTO.class);
//        membershipModel.getMembership().getMembershipIdDTOS()
//                .sort(Comparator.comparing(InvoiceDTO::getFiscalYear).reversed());
        tableView.getColumns().addAll(col1(),col2(),col3(),col4(),col5());
        TableView.TableViewSelectionModel<InvoiceDTO> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) membershipView.getMembershipModel().setSelectedInvoice(newSelection);
        });
        membershipView.getMembershipModel().setInvoiceListTableView(tableView);
        return tableView;
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

    private Node createRecordAddBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(2,2,2,2), 20);
        return hBox;
    }
}
