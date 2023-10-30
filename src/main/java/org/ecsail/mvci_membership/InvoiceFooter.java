package org.ecsail.mvci_membership;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.PaymentDTO;
import org.ecsail.enums.PaymentType;
import org.ecsail.enums.Success;
import org.ecsail.static_tools.CustomTools;
import org.ecsail.static_tools.DateTools;
import org.ecsail.widgetfx.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InvoiceFooter implements Builder<Region> {
    private final InvoiceView invoiceView;
    private final InvoiceDTO invoiceDTO;
    private TableView<PaymentDTO> tableView;

    public InvoiceFooter(InvoiceView invoiceView) {
        this.invoiceView = invoiceView;
        this.invoiceDTO = invoiceView.getInvoiceDTO();
    }

    @Override
    public Region build() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,0,10,0));
        vBox.getChildren().addAll(tableBox(), totalsBox());
        return vBox;
    }

    private Node totalsBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(10,0,0,0),10.0);
        hBox.getChildren().addAll(lineTotalsBox(), controlBox());
        return hBox;
    }

    private Node controlBox() {
        VBox vBox = VBoxFx.vBoxOf(90.0, 10.0, new Insets(10,0,0,20));
        vBox.getChildren().addAll(new CheckBox("Renew"),
                ButtonFx.buttonOf("Commit", 70, () -> {
                    invoiceDTO.setCommitted(true);
                    invoiceDTO.showItems();
                    invoiceView.getMembershipView().sendMessage().accept(MembershipMessage.UPDATE_INVOICE);
                    invoiceView.successProperty().addListener(ListenerFx.createOneTimeEnumListener(() ->
                            viewMessaging(invoiceView.successProperty().get())));
                })
        );
        return vBox;
    }

    private void viewMessaging(Success success) { // when database updates, this makes UI reflect.
            switch (success) {
                case YES -> Platform.runLater(this::showCommittedView);
                case NO -> System.out.println("We didn't succeed in updating invoice");
            }
    }

    private void showCommittedView() {  // this is pretty much identical to
        CustomTools.removeExistingTabAndCreateNew(invoiceView.getMembershipView()); // also used in InvoiceListView
        invoiceView.getMembershipView().getMembershipModel().setInvoiceSaved(Success.NULL);
    }

    private Node lineTotalsBox() {
        VBox vBox = new VBox(5);
        vBox.getChildren().add(lineTotal("Total Fees:", invoiceDTO.totalProperty()));
        vBox.getChildren().add(lineTotal("Total Credit:", invoiceDTO.creditProperty()));
        vBox.getChildren().add(lineTotal("Payment:", invoiceDTO.paidProperty()));
        vBox.getChildren().add(lineTotal("Balance:", invoiceDTO.balanceProperty()));
        return vBox;
    }

    private Node lineTotal(String text, StringProperty stringProperty) {
        HBox hBox = new HBox();
        Label label = new Label(text);
        Label value = new Label("0.00");
        value.getStyleClass().add("standard-black-label");
        value.textProperty().bind(stringProperty);
        VBox vBox1 = VBoxFx.vBoxOf(200.0, Pos.CENTER_LEFT);
        vBox1.getChildren().add(label);
        VBox vBox2 = VBoxFx.vBoxOf(160.0, Pos.CENTER_RIGHT);
        vBox2.getChildren().add(value);
        hBox.getChildren().addAll(vBox1,vBox2);
        return hBox;
    }

    private Node tableBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(10,5,0,0),10.0);
        hBox.getChildren().addAll(tableView(), buttonBox());
        return hBox;
    }

    private Node buttonBox() {
        VBox vBox = VBoxFx.vBoxOf(5.0, new Insets(0,5,0,0));
        vBox.getChildren().addAll(
                ButtonFx.buttonOf("Add", 60, () -> {
                    PaymentDTO paymentDTO = new PaymentDTO(0, invoiceDTO.getId(), null, "CH", DateTools.getDate(), "0", 1);
                    // adds to database and updates pay_id
                    System.out.println("Insert payment into database");
                    invoiceDTO.getPaymentDTOS().add(paymentDTO); // let's add it to our GUI
                }),
                ButtonFx.buttonOf("Delete", 60, () -> {
                    int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
                    if (selectedIndex >= 0) // is something selected?
                        System.out.println("Delete Payment Entry from database");
                    tableView.getItems().remove(selectedIndex); // remove it from our GUI
//                    BigDecimal totalPaidAmount = new BigDecimal(SqlPayment.getTotalAmount(parent.invoice.getId()));
//                    parent.totalPaymentText.setText(String.valueOf(totalPaidAmount.setScale(2)));
//                    parent.invoice.setPaid(String.valueOf(totalPaidAmount.setScale(2)));
//                    updateTotals();
                })
        );
        return vBox;
    }

    private TableView<PaymentDTO> tableView() {
        this.tableView = TableViewFx.tableViewOf(PaymentDTO.class, 115);
        System.out.println("PaymentDTOs size is=" + invoiceDTO.getPaymentDTOS().size());
        tableView.setItems(invoiceDTO.getPaymentDTOS());
        List<TableColumn<PaymentDTO, ?>> columns = new ArrayList<>();
        columns.add(column1());
        columns.add(column2());
        columns.add(column3());
        columns.add(column4());
        tableView.getColumns().addAll(columns);
        return tableView;
    }

    private TableColumn<PaymentDTO,String> column1() {
        TableColumn<PaymentDTO, String> col1 = TableColumnFx.tableColumnOf(PaymentDTO::PaymentAmountProperty,"Amount");
        col1.setStyle("-fx-alignment: CENTER-RIGHT;");
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        col1.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setPaymentAmount(String.valueOf(new BigDecimal(t.getNewValue()).setScale(2)));
//                    var pay_id = t.getTableView().getItems().get(t.getTablePosition().getRow()).getPay_id();
//                    BigDecimal amount = new BigDecimal(t.getNewValue());
//                    SqlUpdate.updatePayment(pay_id, "amount", String.valueOf(amount.setScale(2)));
//                    // This adds all the amounts together
//                    BigDecimal totalPaidAmount = new BigDecimal(SqlPayment.getTotalAmount(parent.getInvoice().getId())).setScale(2);
//                    String totalAmountPaid = String.valueOf(totalPaidAmount.setScale(2));
//                    parent.parent.totalPaymentText.setText(totalAmountPaid);
//                    parent.getInvoice().setPaid(totalAmountPaid);
//                    parent.updateTotals();
                }
        );
        return col1;
    }

    private TableColumn<PaymentDTO,PaymentType> column2() {
        ObservableList<PaymentType> paymentTypeList = FXCollections.observableArrayList(PaymentType.values());
        TableColumn<PaymentDTO, PaymentType> col2 = new TableColumn<>("Type");
        col2.setStyle("-fx-alignment: CENTER;");
        col2.setCellValueFactory(param -> {
            var thisPayment = param.getValue();
            var paymentCode = thisPayment.getPaymentType();
            var paymentType = PaymentType.getByCode(paymentCode);
            return new SimpleObjectProperty<>(paymentType);
        });
        col2.setCellFactory(ComboBoxTableCell.forTableColumn(paymentTypeList));
        col2.setOnEditCommit((TableColumn.CellEditEvent<PaymentDTO, PaymentType> event) -> {
            var pos = event.getTablePosition();
            var newPaymentType = event.getNewValue();
            var row = pos.getRow();
            var thisPayment = event.getTableView().getItems().get(row);
//            SqlUpdate.updatePayment(thisPayment.getPay_id(), "payment_type", newPaymentType.getCode());
            // need to update paid from here
            thisPayment.setPaymentType(newPaymentType.getCode());
        });
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        return col2;
    }

    private TableColumn<PaymentDTO,String> column3() {
        TableColumn<PaymentDTO, String> col3 = TableColumnFx.tableColumnOf(PaymentDTO::checkNumberProperty,"Check #");
        col3.setStyle("-fx-alignment: CENTER-LEFT;");
        col3.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setCheckNumber(t.getNewValue());
                    var pay_id = t.getTableView().getItems().get(t.getTablePosition().getRow()).getPay_id();
//                    SqlUpdate.updatePayment(pay_id, "CHECK_NUMBER", t.getNewValue());
                }
        );
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 35);
        return col3;
    }

    private TableColumn<PaymentDTO,String> column4() {
        TableColumn<PaymentDTO, String> col4 = TableColumnFx.tableColumnOf(PaymentDTO::paymentDateProperty,"Date");
        col4.setStyle("-fx-alignment: CENTER-LEFT;");
        col4.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setPaymentDate(t.getNewValue());
                    var pay_id = t.getTableView().getItems().get(t.getTablePosition().getRow()).getPay_id();
//                    SqlUpdate.updatePayment(pay_id, "payment_date", t.getNewValue());
                    //	SqlUpdate.updatePhone("phone", phone_id, t.getNewValue());
                }
        );
        col4.setMaxWidth(1f * Integer.MAX_VALUE * 25);
        return col4;
    }

}
