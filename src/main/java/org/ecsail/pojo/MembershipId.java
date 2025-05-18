package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MembershipId {
    @JsonProperty("mId")
    private int mId;

    @JsonProperty("fiscalYear")
    private int fiscalYear;

    @JsonProperty("membershipId")
    private int membershipId;

    @JsonProperty("msId")
    private int msId;

    @JsonProperty("renew")
    private int renew;

    @JsonProperty("memType")
    private String memType;

    @JsonProperty("selected")
    private int selected;

    @JsonProperty("lateRenew")
    private int lateRenew;

    public MembershipId() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(int fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public int getMsId() {
        return msId;
    }

    public void setMsId(int msId) {
        this.msId = msId;
    }

    public int getRenew() {
        return renew;
    }

    public void setRenew(int renew) {
        this.renew = renew;
    }

    public String getMemType() {
        return memType;
    }

    public void setMemType(String memType) {
        this.memType = memType;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getLateRenew() {
        return lateRenew;
    }

    public void setLateRenew(int lateRenew) {
        this.lateRenew = lateRenew;
    }
}
