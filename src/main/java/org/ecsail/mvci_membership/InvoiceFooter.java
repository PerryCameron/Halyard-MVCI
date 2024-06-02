package org.ecsail.mvci_membership;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.MembershipIdDTO;
import org.ecsail.dto.PaymentDTO;
import org.ecsail.enums.PaymentType;
import org.ecsail.enums.Success;
import org.ecsail.static_tools.CustomTools;
import org.ecsail.widgetfx.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class InvoiceFooter implements Builder<Region> {
    private InvoiceView invoiceView;
    private InvoiceDTO invoiceDTO;
    private TableView<PaymentDTO> tableView;
    BooleanProperty renew = new SimpleBooleanProperty(false);

    public InvoiceFooter(InvoiceView invoiceView) {
        this.invoiceView = invoiceView;
        this.invoiceDTO = invoiceView.getInvoiceDTO();
    }

    @Override
    public Region build() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,0,10,0));
        vBox.getChildren().addAll(tableBox(), totalsBox());
        addPaymentsAndUpdateBalance();
        return vBox;
    }

    private Node totalsBox() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(10,0,0,0),10.0);
        hBox.getChildren().addAll(lineTotalsBox(), controlBox());
        return hBox;
    }

    private Node controlBox() {
        VBox vBox = VBoxFx.vBoxOf(90.0, 5.0, new Insets(5.0,0,0,20));
        vBox.getChildren().addAll(addSaveButton(), addCommitButton());
        if(!invoiceDTO.isSupplemental())
            vBox.getChildren().add(addRenewCheckBox());
        return vBox;
    }

    private Control addRenewCheckBox() {
        CheckBox checkBox = CheckBoxFx.CheckBoxOf("Renew", 70, renew);
        if (!invoiceDTO.isSupplemental()) {
            checkBox.setSelected(true);
            // make sure ids are populated
            loadHistoryTab();
        }
        return checkBox;
    }

    private MembershipIdDTO getMembershipID() {
        MembershipIdDTO membershipIdDTO = invoiceView.getMembershipView().getMembershipModel().getMembership().getMembershipIdDTOS()
                .stream().filter(id -> id.getFiscalYear() == invoiceDTO.getYear())
                .findFirst().orElse(null);
        if(membershipIdDTO == null)
            loadHistoryTab();
        return membershipIdDTO;
    }

    private void loadHistoryTab() {  // prevents a null ID if tab was never opened
        TabPane tabpane = invoiceView.getMembershipView().getMembershipModel().getInfoTabPane();
        CustomTools.selectTabByText("History", tabpane);
        CustomTools.selectTabByText(String.valueOf(invoiceDTO.getYear()), tabpane);
    }

    private Control addSaveButton() {
        return ButtonFx.buttonOf("Save", 70, () -> {
            invoiceView.getMembershipView().sendMessage().accept(MembershipMessage.SAVE_INVOICE);
        });
    }

    private Control addCommitButton() {
        return ButtonFx.buttonOf("Commit", 70, () -> {
            invoiceDTO.setCommitted(true);
            invoiceView.getMembershipView().sendMessage().accept(MembershipMessage.UPDATE_INVOICE);
            invoiceView.successProperty().addListener(ListenerFx.createSingleUseEnumListener(() ->
                    viewMessaging(invoiceView.successProperty().get())));
            if(!invoiceDTO.isSupplemental()) { // no need to update an ID record if invoice is a supplemental record
                MembershipIdDTO membershipIdDTO = getMembershipID();
                membershipIdDTO.setIsRenew(renew.get());
                invoiceView.getMembershipView().getMembershipModel().setSelectedMembershipId(membershipIdDTO);
                invoiceView.getMembershipView().sendMessage().accept(MembershipMessage.UPDATE_MEMBERSHIP_ID);
            }
        });
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
        vBox.getChildren().addAll(getAddButton(), getDeleteButton());
        return vBox;
    }

    private Button getDeleteButton() {
        return ButtonFx.buttonOf("Delete", 60, () -> {
            PaymentDTO paymentDTO = tableView.getSelectionModel().getSelectedItem();
            if (paymentDTO != null) // is something selected?
                invoiceView.getMembershipView().getMembershipModel().setSelectedPayment(paymentDTO);
            invoiceView.getMembershipView().sendMessage().accept(MembershipMessage.DELETE_PAYMENT);
            invoiceView.getMembershipView().getMembershipModel().getSelectedInvoice().getPaymentDTOS().remove(paymentDTO);
            addPaymentsAndUpdateBalance();
        });
    }

    private Button getAddButton() {
        return ButtonFx.buttonOf("Add", 60, () ->
                invoiceView.getMembershipView().sendMessage().accept(MembershipMessage.INSERT_PAYMENT));
    }

    private TableView<PaymentDTO> tableView() {
        this.tableView = TableViewFx.tableViewOf(PaymentDTO.class, 115);
        tableView.setItems(invoiceDTO.getPaymentDTOS());
        tableView.getColumns().addAll(Arrays.asList(column1(),column2(),column3(),column4()));
        return tableView;
    }

    private TableColumn<PaymentDTO,String> column1() {
        TableColumn<PaymentDTO, String> col1 = TableColumnFx.tableColumnOf(PaymentDTO::PaymentAmountProperty,"Amount");
        col1.setStyle("-fx-alignment: CENTER-RIGHT;");
        col1.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        col1.setOnEditCommit(
                t -> {
                    PaymentDTO paymentDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    paymentDTO.setPaymentAmount(String.valueOf(new BigDecimal(t.getNewValue()).setScale(2, RoundingMode.HALF_UP)));
                    invoiceView.getMembershipView().getMembershipModel().setSelectedPayment(paymentDTO);
                    invoiceView.getMembershipView().sendMessage().accept(MembershipMessage.UPDATE_PAYMENT);
                    addPaymentsAndUpdateBalance();
                }
        );
        return col1;
    }

    private void addPaymentsAndUpdateBalance() {
        BigDecimal payments = invoiceView.getMembershipView().getMembershipModel().getSelectedInvoice().getPaymentDTOS()
                .stream()
                .map(p -> new BigDecimal(p.getPaymentAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        invoiceDTO.setPaid(payments.toString());
        invoiceDTO.updateBalance();
    }

    private TableColumn<PaymentDTO,PaymentType> column2() {
        ObservableList<PaymentType> paymentTypeList = FXCollections.observableArrayList(PaymentType.values());
        TableColumn<PaymentDTO, PaymentType> col2 = new TableColumn<>("Type");
        col2.setStyle("-fx-alignment: CENTER;");
        col2.setCellValueFactory(param -> {
            PaymentDTO paymentDTO = param.getValue();
            String paymentCode = paymentDTO.getPaymentType();
            PaymentType paymentType = PaymentType.getByCode(paymentCode);
            return new SimpleObjectProperty<>(paymentType);
        });
        col2.setCellFactory(ComboBoxTableCell.forTableColumn(paymentTypeList));
        col2.setOnEditCommit((TableColumn.CellEditEvent<PaymentDTO, PaymentType> event) -> {
            TablePosition<PaymentDTO, PaymentType> pos = event.getTablePosition();
            PaymentType newPaymentType = event.getNewValue();
            PaymentDTO paymentDTO = event.getTableView().getItems().get(pos.getRow());
            invoiceView.getMembershipView().getMembershipModel().setSelectedPayment(paymentDTO);
            invoiceView.getMembershipView().sendMessage().accept(MembershipMessage.UPDATE_PAYMENT);
            paymentDTO.setPaymentType(newPaymentType.getCode());
        });
        col2.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        return col2;
    }

    private TableColumn<PaymentDTO,String> column3() {
        TableColumn<PaymentDTO, String> col3 = TableColumnFx.tableColumnOf(PaymentDTO::checkNumberProperty,"Check #");
        col3.setStyle("-fx-alignment: CENTER-LEFT;");
        col3.setOnEditCommit(this::updatePayment);
        col3.setMaxWidth(1f * Integer.MAX_VALUE * 35);
        return col3;
    }

    private TableColumn<PaymentDTO,String> column4() {
        TableColumn<PaymentDTO, String> col4 = TableColumnFx.tableColumnOf(PaymentDTO::paymentDateProperty,"Date");
        col4.setStyle("-fx-alignment: CENTER-LEFT;");
        col4.setOnEditCommit(this::updatePayment);
        col4.setMaxWidth(1f * Integer.MAX_VALUE * 25);
        return col4;
    }

    private void updatePayment(TableColumn.CellEditEvent<PaymentDTO, String> t) {
        PaymentDTO paymentDTO = t.getTableView().getItems().get(t.getTablePosition().getRow());
        paymentDTO.setCheckNumber(t.getNewValue());
        invoiceView.getMembershipView().getMembershipModel().setSelectedPayment(paymentDTO);
        invoiceView.getMembershipView().sendMessage().accept(MembershipMessage.UPDATE_PAYMENT);
    }
}
