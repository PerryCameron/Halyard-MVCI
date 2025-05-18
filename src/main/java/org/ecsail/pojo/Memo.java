package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Memo {
    @JsonProperty("memoId")
    private int memoId;

    @JsonProperty("msId")
    private int msId;

    @JsonProperty("memoDate")
    private String memoDate;

    @JsonProperty("memo")
    private String memo;

    @JsonProperty("invoiceId")
    private Integer invoiceId; // Nullable

    @JsonProperty("category")
    private String category;

    @JsonProperty("boatId")
    private Integer boatId; // Nullable

    public Memo() {
    }

    public int getMemoId() {
        return memoId;
    }

    public void setMemoId(int memoId) {
        this.memoId = memoId;
    }

    public int getMsId() {
        return msId;
    }

    public void setMsId(int msId) {
        this.msId = msId;
    }

    public String getMemoDate() {
        return memoDate;
    }

    public void setMemoDate(String memoDate) {
        this.memoDate = memoDate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getBoatId() {
        return boatId;
    }

    public void setBoatId(Integer boatId) {
        this.boatId = boatId;
    }
}
