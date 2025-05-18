package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Invoice {
    @JsonProperty("invoiceId")
    private int invoiceId;

    @JsonProperty("fiscalYear")
    private int fiscalYear;

    @JsonProperty("paid")
    private double paid;

    @JsonProperty("total")
    private double total;

    @JsonProperty("credit")
    private double credit;

    @JsonProperty("balance")
    private double balance;

    @JsonProperty("batch")
    private int batch;

    @JsonProperty("committed")
    private int committed;

    @JsonProperty("closed")
    private int closed;

    @JsonProperty("supplemental")
    private int supplemental;

    @JsonProperty("maxCredit")
    private double maxCredit;

    public Invoice() {
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(int fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public int getCommitted() {
        return committed;
    }

    public void setCommitted(int committed) {
        this.committed = committed;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public int getSupplemental() {
        return supplemental;
    }

    public void setSupplemental(int supplemental) {
        this.supplemental = supplemental;
    }

    public double getMaxCredit() {
        return maxCredit;
    }

    public void setMaxCredit(double maxCredit) {
        this.maxCredit = maxCredit;
    }
}