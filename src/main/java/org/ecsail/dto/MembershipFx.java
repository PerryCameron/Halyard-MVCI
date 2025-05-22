package org.ecsail.dto;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.ecsail.pojo.Membership;
import org.ecsail.pojo.Slip;

import java.util.List;

public class MembershipFx {
    private SimpleIntegerProperty mid;
    private SimpleIntegerProperty fiscalYear;
    private SimpleIntegerProperty msId;
    private SimpleIntegerProperty membershipId;
    private SimpleBooleanProperty renew;
    private SimpleStringProperty memType;
    private SimpleBooleanProperty selected;
    private SimpleBooleanProperty lateRenew;
    private SimpleIntegerProperty pId;
    private SimpleStringProperty joinDate;
    private SimpleStringProperty address;
    private SimpleStringProperty city;
    private SimpleStringProperty state;
    private SimpleStringProperty zip;
    private ObjectProperty<SlipDTOFx> slip = new SimpleObjectProperty<>();
    private List<PersonFx> people = FXCollections.observableArrayList();
    private List<BoatDTOFx> boats = FXCollections.observableArrayList();
    private List<InvoiceDTOFx> invoices = FXCollections.observableArrayList();
    private List<MembershipIdDTOFx> membershipIds = FXCollections.observableArrayList();
    private List<NotesDTOFx> memos = FXCollections.observableArrayList();

    public MembershipFx(Integer mid, Integer fiscalYear, Integer msId, Integer membershipId,
                        Boolean  renew, String memType, Boolean  selected, Boolean lateRenew,
                        Integer pId, String joinDate, String address, String city,
                        String state, String zip, Slip slip) {
        this.mid = new SimpleIntegerProperty(mid);
        this.fiscalYear = new SimpleIntegerProperty(fiscalYear);
        this.msId = new SimpleIntegerProperty(msId);
        this.membershipId = new SimpleIntegerProperty(membershipId);
        this.renew = new SimpleBooleanProperty(renew);
        this.memType = new SimpleStringProperty(memType);
        this.selected = new SimpleBooleanProperty(selected);
        this.lateRenew = new SimpleBooleanProperty(lateRenew);
        this.pId = new SimpleIntegerProperty(pId);
        this.joinDate = new SimpleStringProperty(joinDate);
        this.address = new SimpleStringProperty(address);
        this.city = new SimpleStringProperty(city);
        this.state = new SimpleStringProperty(state);
        this.zip = new SimpleStringProperty(zip);
    }

    public MembershipFx(Membership membership) {
        this.mid = new SimpleIntegerProperty(membership.getMid());
        this.fiscalYear = new SimpleIntegerProperty(membership.getFiscalYear());
        this.msId = new SimpleIntegerProperty(membership.getMsId());
        this.membershipId = new SimpleIntegerProperty(membership.getMembershipId());
        this.renew = new SimpleBooleanProperty(membership.getRenew() == 1);
        this.memType = new SimpleStringProperty(membership.getMemType());
        this.selected = new SimpleBooleanProperty(membership.getSelected() == 1);
        this.lateRenew = new SimpleBooleanProperty(membership.getLateRenew() == 1);
        this.pId = new SimpleIntegerProperty(membership.getpId());
        this.joinDate = new SimpleStringProperty(membership.getJoinDate());
        this.address = new SimpleStringProperty(membership.getAddress());
        this.city = new SimpleStringProperty(membership.getCity());
        this.state = new SimpleStringProperty(membership.getState());
        this.zip = new SimpleStringProperty(membership.getZip());
    }

    public int getMid() {
        return mid.get();
    }

    public SimpleIntegerProperty midProperty() {
        return mid;
    }

    public int getFiscalYear() {
        return fiscalYear.get();
    }

    public SimpleIntegerProperty fiscalYearProperty() {
        return fiscalYear;
    }

    public int getMsId() {
        return msId.get();
    }

    public SimpleIntegerProperty msIdProperty() {
        return msId;
    }

    public int getMembershipId() {
        return membershipId.get();
    }

    public SimpleIntegerProperty membershipIdProperty() {
        return membershipId;
    }


    public String getMemType() {
        return memType.get();
    }

    public SimpleStringProperty memTypeProperty() {
        return memType;
    }

    public int getpId() {
        return pId.get();
    }

    public SimpleIntegerProperty pIdProperty() {
        return pId;
    }

    public String getJoinDate() {
        return joinDate.get();
    }

    public SimpleStringProperty joinDateProperty() {
        return joinDate;
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public String getCity() {
        return city.get();
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }

    public String getState() {
        return state.get();
    }

    public SimpleStringProperty stateProperty() {
        return state;
    }

    public String getZip() {
        return zip.get();
    }

    public SimpleStringProperty zipProperty() {
        return zip;
    }

    public ObjectProperty<SlipDTOFx> slipProperty() {
        return slip;
    }

    public List<PersonFx> getPeople() {
        return people;
    }

    public List<BoatDTOFx> getBoats() {
        return boats;
    }

    public List<InvoiceDTOFx> getInvoices() {
        return invoices;
    }

    public List<MembershipIdDTOFx> getMembershipIds() {
        return membershipIds;
    }

    public List<NotesDTOFx> getMemos() {
        return memos;
    }

    public void setPeople(List<PersonFx> people) {
        this.people = people;
    }

    public void setBoats(List<BoatDTOFx> boats) {
        this.boats = boats;
    }

    public void setInvoices(List<InvoiceDTOFx> invoices) {
        this.invoices = invoices;
    }

    public void setMembershipIds(List<MembershipIdDTOFx> membershipIds) {
        this.membershipIds = membershipIds;
    }

    public void setMemos(List<NotesDTOFx> memos) {
        this.memos = memos;
    }
}