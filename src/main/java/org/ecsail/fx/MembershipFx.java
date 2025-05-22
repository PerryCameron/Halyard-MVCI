package org.ecsail.fx;

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
    private List<NotesFx> memos = FXCollections.observableArrayList();

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

    public List<NotesFx> getMemos() {
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

    public void setMemos(List<NotesFx> memos) {
        this.memos = memos;
    }

    public void setMid(int mid) {
        this.mid.set(mid);
    }

    public void setFiscalYear(int fiscalYear) {
        this.fiscalYear.set(fiscalYear);
    }

    public void setMsId(int msId) {
        this.msId.set(msId);
    }

    public void setMembershipId(int membershipId) {
        this.membershipId.set(membershipId);
    }

    public boolean isRenew() {
        return renew.get();
    }

    public SimpleBooleanProperty renewProperty() {
        return renew;
    }

    public void setRenew(boolean renew) {
        this.renew.set(renew);
    }

    public void setMemType(String memType) {
        this.memType.set(memType);
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public boolean isLateRenew() {
        return lateRenew.get();
    }

    public SimpleBooleanProperty lateRenewProperty() {
        return lateRenew;
    }

    public void setLateRenew(boolean lateRenew) {
        this.lateRenew.set(lateRenew);
    }

    public void setpId(int pId) {
        this.pId.set(pId);
    }

    public void setJoinDate(String joinDate) {
        this.joinDate.set(joinDate);
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public void setZip(String zip) {
        this.zip.set(zip);
    }

    public SlipDTOFx getSlip() {
        return slip.get();
    }

    public void setSlip(SlipDTOFx slip) {
        this.slip.set(slip);
    }
}