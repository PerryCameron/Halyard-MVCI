package org.ecsail.dto;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class InvoiceDTO {
    private final IntegerProperty id;
    private final IntegerProperty msId;
    private final IntegerProperty year;
    private final StringProperty paid;
    private final StringProperty total;
    private final StringProperty credit;
    private final StringProperty balance;
    private final IntegerProperty batch;
    private final BooleanProperty committed;
    private final BooleanProperty closed;
    private final BooleanProperty supplemental;
    private final StringProperty maxCredit;
    private final BooleanProperty listLoaded = new SimpleBooleanProperty(false);
    private final BooleanProperty feesLoaded = new SimpleBooleanProperty(false);
    private ObservableList<DbInvoiceDTO> dbInvoiceDTOS = FXCollections.observableArrayList();
    private ObservableList<PaymentDTO> paymentDTOS = FXCollections.observableArrayList();
    private ObservableList<FeeDTO> feeDTOS = FXCollections.observableArrayList();
    private ObservableList<InvoiceItemDTO> invoiceItemDTOS = FXCollections.observableArrayList();

    public InvoiceDTO(Integer id, Integer msId, Integer year, String paid, String total, String credit, String balance,
                      Integer batch, Boolean committed, Boolean closed, Boolean supplemental, String maxCredit) {
        this.id = new SimpleIntegerProperty(id);
        this.msId = new SimpleIntegerProperty(msId);
        this.year = new SimpleIntegerProperty(year);
        this.paid = new SimpleStringProperty(paid);
        this.total = new SimpleStringProperty(total);
        this.credit = new SimpleStringProperty(credit);
        this.balance = new SimpleStringProperty(balance);
        this.batch = new SimpleIntegerProperty(batch);
        this.committed = new SimpleBooleanProperty(committed);
        this.closed = new SimpleBooleanProperty(closed);
        this.supplemental = new SimpleBooleanProperty(supplemental);
        this.maxCredit = new SimpleStringProperty(maxCredit);
    }

    public InvoiceDTO(Integer msId, Integer year) {
//        this.id = new SimpleIntegerProperty(SqlSelect.getNextAvailablePrimaryKey("invoice","id"));
        this.id = new SimpleIntegerProperty(0);
        this.msId = new SimpleIntegerProperty(msId);
        this.year = new SimpleIntegerProperty(year);
        this.paid = new SimpleStringProperty("0.00");
        this.total = new SimpleStringProperty("0.00");
        this.credit = new SimpleStringProperty("0.00");
        this.balance = new SimpleStringProperty("0.00");
        this.batch = new SimpleIntegerProperty(0);
        this.committed = new SimpleBooleanProperty(false);
        this.closed = new SimpleBooleanProperty(false);
        this.supplemental = new SimpleBooleanProperty(false);
        this.maxCredit = new SimpleStringProperty("0.00");
    }

    public InvoiceDTO() {  // used for mock invoice
        this.id = new SimpleIntegerProperty(0);
        this.msId = new SimpleIntegerProperty(0);
        this.year = new SimpleIntegerProperty(0);
        this.paid = new SimpleStringProperty("0.00");
        this.total = new SimpleStringProperty("0.00");
        this.credit = new SimpleStringProperty("0.00");
        this.balance = new SimpleStringProperty("0.00");
        this.batch = new SimpleIntegerProperty(0);
        this.committed = new SimpleBooleanProperty(false);
        this.closed = new SimpleBooleanProperty(false);
        this.supplemental = new SimpleBooleanProperty(false);
        this.maxCredit = new SimpleStringProperty("");
    }

    public void showItems() {
        invoiceItemDTOS.stream().forEach(each -> {
            System.out.printf("%-20s = %-10s | Qty = %-5d%n",
                    each.getFieldName(),
                    each.getValue(),
                    each.getQty());
                }
        );
        System.out.println("------------------------");
        System.out.println("Total Fees:   " + getTotal());
        System.out.println("Total Credit: " + getCredit());
        System.out.println("Payment:      " + getPaid());
        System.out.println("Balance:      " + getBalance());
    }

    public void showObjects() {
        System.out.println("Invoice ID= " + this.getId());
        System.out.println("InvoiceItem size= " + getInvoiceItemDTOS().size());
        System.out.println("DbInvoice size= " + getDbInvoiceDTOS().size());
        System.out.println("PaymentDTO size= " + getPaymentDTOS().size());
    }

    public void updateBalance() {
        BigDecimal zero = BigDecimal.ZERO;

        BigDecimal totalCredit = invoiceItemDTOS.stream()
                .filter(InvoiceItemDTO::isCredit)
                .map(i -> new BigDecimal(i.getValue()))
                .reduce(zero, BigDecimal::add);

        BigDecimal totalFees = invoiceItemDTOS.stream()
                .filter(i -> !i.isCredit() && i.getCategoryItem().equals("none"))
                .map(i -> new BigDecimal(i.getValue()))
                .reduce(zero, BigDecimal::add);

        BigDecimal totalPayments = paymentDTOS.stream()
                .map(p -> new BigDecimal(p.getPaymentAmount()))
                .reduce(zero, BigDecimal::add);

        BigDecimal balance = totalFees.subtract(totalCredit).subtract(totalPayments);

        setCredit(totalCredit.toString());
        setTotal(totalFees.toString());
        setPaid(totalPayments.toString());
        setBalance(balance.toString());
    }


    public ObservableList<PaymentDTO> getPaymentDTOS() {
        return paymentDTOS;
    }
    public void setPaymentDTOS(ObservableList<PaymentDTO> paymentDTOS) {
        this.paymentDTOS = paymentDTOS;
    }
    public final IntegerProperty idProperty() {
        return this.id;
    }
    public final int getId() {
        return this.idProperty().get();
    }
    public final void setId(final int id) {
        this.idProperty().set(id);
    }
    public final IntegerProperty msIdProperty() {
        return this.msId;
    }
    public final int getMsId() {
        return this.msIdProperty().get();
    }
    public final void setMsId(final int msId) {
        this.msIdProperty().set(msId);
    }
    public final IntegerProperty yearProperty() {
        return this.year;
    }
    public final int getYear() {
        return this.yearProperty().get();
    }
    public final void setYear(final int year) {
        this.yearProperty().set(year);
    }
    public final StringProperty paidProperty() {
        return this.paid;
    }
    public final String getPaid() {
        return this.paidProperty().get();
    }
    public final void setPaid(final String paid) {
        this.paidProperty().set(paid);
    }
    public final StringProperty totalProperty() {
        return this.total;
    }
    public final String getTotal() {
        return this.totalProperty().get();
    }
    public final void setTotal(final String total) {
        this.totalProperty().set(total);
    }
    public final StringProperty creditProperty() {
        return this.credit;
    }
    public final String getCredit() {
        return this.creditProperty().get();
    }
    public final void setCredit(final String credit) {
        this.creditProperty().set(credit);
    }
    public final StringProperty balanceProperty() {
        return this.balance;
    }
    public final String getBalance() {
        return this.balanceProperty().get();
    }
    public final void setBalance(final String balance) {
        this.balanceProperty().set(balance);
    }
    public final IntegerProperty batchProperty() {
        return this.batch;
    }
    public final int getBatch() {
        return this.batchProperty().get();
    }
    public final void setBatch(final int batch) {
        this.batchProperty().set(batch);
    }
    public final BooleanProperty committedProperty() {
        return this.committed;
    }
    public final boolean isCommitted() {
        return this.committedProperty().get();
    }
    public final void setCommitted(final boolean committed) {
        this.committedProperty().set(committed);
    }
    public final BooleanProperty closedProperty() {
        return this.closed;
    }
    public final boolean isClosed() {
        return this.closedProperty().get();
    }
    public final void setClosed(final boolean closed) {
        this.closedProperty().set(closed);
    }
    public final BooleanProperty supplementalProperty() {
        return this.supplemental;
    }
    public final boolean isSupplemental() {
        return this.supplementalProperty().get();
    }
    public final void setSupplemental(final boolean supplemental) {
        this.supplementalProperty().set(supplemental);
    }
    public final StringProperty maxCreditProperty() {
        return this.maxCredit;
    }
    public final String getMaxCredit() {
        return this.maxCreditProperty().get();
    }
    public final void setMaxCredit(final String maxCredit) {
        this.maxCreditProperty().set(maxCredit);
    }

    public ObservableList<DbInvoiceDTO> getDbInvoiceDTOS() {
        return dbInvoiceDTOS;
    }

    public void setDbInvoiceDTOS(ObservableList<DbInvoiceDTO> dbInvoiceDTOS) {
        this.dbInvoiceDTOS = dbInvoiceDTOS;
    }

    public boolean isListLoaded() {
        return listLoaded.get();
    }
    public BooleanProperty listLoadedProperty() {
        return listLoaded;
    }
    public void setListLoaded(boolean listLoaded) {
        this.listLoaded.set(listLoaded);
    }
    public ObservableList<FeeDTO> getFeeDTOS() {
        return feeDTOS;
    }
    public void setFeeDTOS(ObservableList<FeeDTO> feeDTOS) {
        this.feeDTOS = feeDTOS;
    }
    public ObservableList<InvoiceItemDTO> getInvoiceItemDTOS() {
        return invoiceItemDTOS;
    }
    public void setInvoiceItemDTOS(ObservableList<InvoiceItemDTO> invoiceItemDTOS) {
        this.invoiceItemDTOS = invoiceItemDTOS;
    }

    public boolean isFeesLoaded() {
        return feesLoaded.get();
    }

    public BooleanProperty feesLoadedProperty() {
        return feesLoaded;
    }

    public void setFeesLoaded(boolean feesLoaded) {
        this.feesLoaded.set(feesLoaded);
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
                "id=" + id +
                ", msId=" + msId +
                ", year=" + year +
                ", paid=" + paid +
                ", total=" + total +
                ", credit=" + credit +
                ", balance=" + balance +
                ", batch=" + batch +
                ", committed=" + committed +
                ", closed=" + closed +
                ", supplemental=" + supplemental +
                ", maxCredit=" + maxCredit +
                ", listLoaded=" + listLoaded +
                ", feesLoaded=" + feesLoaded +
                ", dbInvoiceDTOS=" + dbInvoiceDTOS +
                ", paymentDTOS=" + paymentDTOS +
                ", feeDTOS=" + feeDTOS +
                ", invoiceItemDTOS=" + invoiceItemDTOS +
                '}';
    }

    public String toFullInvoiceString() {
        StringBuilder sb = new StringBuilder();
            sb.append("InvoiceDTO{");
            sb.append("id=").append(id.get());
            sb.append(", msId=").append(msId.get());
            sb.append(", year=").append(year.get());
            sb.append(", paid=").append(paid.get());
            sb.append(", total=").append(total.get());
            sb.append(", credit=").append(credit.get());
            sb.append(", balance=").append(balance.get());
            sb.append(", batch=").append(batch.get());
            sb.append(", committed=").append(committed.get());
            sb.append(", closed=").append(closed.get());
            sb.append(", supplemental=").append(supplemental.get());
            sb.append(", maxCredit=").append(maxCredit.get());
            sb.append('}');
            sb.append("\n");

            AtomicInteger count = new AtomicInteger(1);
            if (getInvoiceItemDTOS() != null) {
                getInvoiceItemDTOS().forEach(each -> sb.append(String.format(
                        "%2d) InvoiceItemDTO{id=%d, invoiceId=%d, msId=%d, year=%d, fieldName=%-25s, credit=%-6b, value=%-6s, qty=%d, category=%-8b, categoryItem=%-20s}%n",
                        count.getAndIncrement(), each.idProperty().get(), each.invoiceIdProperty().get(), each.msIdProperty().get(), each.yearProperty().get(),
                        each.fieldNameProperty().get(), each.creditProperty().get(), each.valueProperty().get(), each.qtyProperty().get(),
                        each.categoryProperty().get(), each.categoryItemProperty().get()
                )));
            }
        return sb.toString();
    }
}
