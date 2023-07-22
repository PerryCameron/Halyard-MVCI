package org.ecsail.dto;

import javafx.beans.property.*;

import java.time.LocalDate;

public class NotesDTO {

    private IntegerProperty memoId;
    private IntegerProperty msId;
    private ObjectProperty<LocalDate> memoDate;
    private StringProperty memo;
    private IntegerProperty invoiceId;
    private StringProperty category;
    private IntegerProperty boatId;

    public NotesDTO(Integer memoId, Integer msId, LocalDate memoDate,
                    String memo, Integer invoiceId, String category, int boatId) {
        super();
        this.memoId = new SimpleIntegerProperty(memoId);
        this.msId = new SimpleIntegerProperty(msId);
        this.memoDate = new SimpleObjectProperty(memoDate);
        this.memo = new SimpleStringProperty(memo);
        this.invoiceId = new SimpleIntegerProperty(invoiceId);
        this.category = new SimpleStringProperty(category);
        this.boatId = new SimpleIntegerProperty(boatId);
    }

    public NotesDTO(int boatId, String type) {
        super();
        this.memoId = new SimpleIntegerProperty(0);
        this.msId = new SimpleIntegerProperty(0);
        this.memoDate = new SimpleObjectProperty(LocalDate.now());
        this.memo = new SimpleStringProperty("");
        this.invoiceId = new SimpleIntegerProperty(0);
        this.category = new SimpleStringProperty(type);
        this.boatId = new SimpleIntegerProperty(boatId);
    }

    public NotesDTO(String type, int msId) {
        super();
        this.memoId = new SimpleIntegerProperty(0);
        this.msId = new SimpleIntegerProperty(msId);
        this.memoDate = new SimpleObjectProperty(LocalDate.now());
        this.memo = new SimpleStringProperty("");
        this.invoiceId = new SimpleIntegerProperty(0);
        this.category = new SimpleStringProperty(type);
        this.boatId = new SimpleIntegerProperty(0);
    }

    public final IntegerProperty memoIdProperty() {
        return this.memoId;
    }

    public final int getMemoId() {
        return this.memoIdProperty().get();
    }

    public final void setMemoId(final int memoId) {
        this.memoIdProperty().set(memoId);
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

    public ObjectProperty<LocalDate> memoDateProperty() {
        return memoDate;
    }

    public void setMemoDate(LocalDate date) {
        this.memoDate.set(date);
    }

    public LocalDate getMemoDate() {
        return memoDate.get();
    }

    public final StringProperty memoProperty() {
        return this.memo;
    }

    public final String getMemo() {
        return this.memoProperty().get();
    }

    public final void setMemo(final String memo) {
        this.memoProperty().set(memo);
    }

    public final IntegerProperty invoiceIdProperty() {
        return this.invoiceId;
    }

    public final int getInvoiceId() {
        return this.invoiceIdProperty().get();
    }

    public final void setInvoiceId(final int invoiceId) {
        this.invoiceIdProperty().set(invoiceId);
    }

    public final StringProperty categoryProperty() {
        return this.category;
    }

    public final String getCategory() {
        return this.categoryProperty().get();
    }

    public final void setCategory(final String category) {
        this.categoryProperty().set(category);
    }

    public final IntegerProperty boatIdProperty() {
        return this.boatId;
    }

    public final int getBoatId() {
        return this.boatIdProperty().get();
    }

    public final void setBoatId(final int boatId) {
        this.boatIdProperty().set(boatId);
    }

    @Override
    public String toString() {
        return "NotesDTO{" +
                "memoId=" + memoId +
                ", msId=" + msId +
                ", memoDate=" + memoDate +
                ", memo=" + memo +
                ", invoiceId=" + invoiceId +
                ", category=" + category +
                ", boatId=" + boatId +
                '}';
    }
}
