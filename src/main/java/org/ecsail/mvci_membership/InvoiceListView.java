package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.static_tools.CustomTools;
import org.ecsail.widgetfx.*;

import java.time.Year;
import java.util.Comparator;

import static org.ecsail.widgetfx.ListenerFx.addSingleFireObjectListener;

public class InvoiceListView implements Builder<Tab> {
    private final MembershipView membershipView;
    private final MembershipModel membershipModel;
    public InvoiceListView(MembershipView membershipView) {
        this.membershipView = membershipView;
        this.membershipModel = membershipView.getMembershipModel();
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
        ListenerFx.addSingleFireTabListener(tab, () -> membershipView.sendMessage().accept(MembershipMessage.SELECT_INVOICES));
        return tab;
    }

    private Node createRecordAddBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(7,2,5,10), 80);
        hBox.getChildren().addAll(createInvoiceAdder(), createDeleteButton());
        return hBox;
    }

    private Node createInvoiceAdder() {
        HBox hBox = HBoxFx.hBoxOf(5, Pos.CENTER_LEFT);
        Label label = new Label("Create new Fiscal Year Record:");
        hBox.getChildren().addAll(label, getYear(),createAddButton());
        return hBox;
    }

    private Control createAddButton() {
        return ButtonFx.buttonOf("Add", 60, () -> {
            addSingleFireObjectListener(membershipModel.selectedInvoiceProperty(), () -> {
                CustomTools.removeExistingTabAndCreateNew(membershipView);
                membershipModel.getMembership().getInvoiceDTOS().sort(Comparator.comparing(InvoiceDTO::getYear).reversed());
            });
            membershipView.sendMessage().accept(MembershipMessage.INSERT_INVOICE);
            // TODO open tab
        });
    }

    private Node createDeleteButton() {
        return ButtonFx.buttonOf("Delete", 60, () -> {
            membershipView.sendMessage().accept(MembershipMessage.DELETE_INVOICE);
            // TODO close tab if open somehow
        });
    }

    private Node getYear() {
        ComboBox<Integer> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(80);
        int currentYear = Year.now().getValue();
        for (int year = currentYear + 1; year > 1969; year--) comboBox.getItems().add(year);
        int selectedYear = membershipView.getMembershipModel().getMembership().getSelectedYear();
        comboBox.setValue(selectedYear); // sets year for combo box to record year
        membershipView.getMembershipModel().setSelectedInvoiceCreateYear(selectedYear); // sets initial for combo-box
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            membershipView.getMembershipModel().setSelectedInvoiceCreateYear(newValue);
        });
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
        tableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                CustomTools.removeExistingTabAndCreateNew(membershipView); // also used in InvoiceFooter
            }
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
