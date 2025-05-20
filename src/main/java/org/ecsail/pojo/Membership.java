package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Membership {
    @JsonProperty("mid")
    private int mid;

    @JsonProperty("fiscalYear")
    private int fiscalYear;

    @JsonProperty("msId")
    private int msId;

    @JsonProperty("membershipId")
    private int membershipId;

    @JsonProperty("renew")  // this was supposed to be a boolean
    private int renew;

    @JsonProperty("memType")
    private String memType;

    @JsonProperty("selected") // this was suppoed to be a boolean
    private int selected;

    @JsonProperty("lateRenew")
    private int lateRenew;   // this was supposed to be a boolean

    @JsonProperty("pId")
    private int pId;

    @JsonProperty("joinDate")
    private String joinDate;

    @JsonProperty("address")
    private String address;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("zip")
    private String zip;

    @JsonProperty("slip")
    private Slip slip;

    @JsonProperty("people")
    private List<Person> people;

    @JsonProperty("boats")
    private List<BoatDTO> boats;

    @JsonProperty("invoices")
    private List<Invoice> invoices;

    @JsonProperty("membership_ids")
    private List<MembershipId> membershipIds;

    @JsonProperty("memos")
    private List<Note> memos;

    public Membership() {
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(int fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public int getMsId() {
        return msId;
    }

    public void setMsId(int msId) {
        this.msId = msId;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
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

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Slip getSlip() {
        return slip;
    }

    public void setSlip(Slip slip) {
        this.slip = slip;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public List<BoatDTO> getBoats() {
        return boats;
    }

    public void setBoats(List<BoatDTO> boats) {
        this.boats = boats;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public List<MembershipId> getMembershipIds() {
        return membershipIds;
    }

    public void setMembershipIds(List<MembershipId> membershipIds) {
        this.membershipIds = membershipIds;
    }

    public List<Note> getMemos() {
        return memos;
    }

    public void setMemos(List<Note> memos) {
        this.memos = memos;
    }
}
