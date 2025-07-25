package org.ecsail.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.pojo.Membership;

import java.util.Objects;

public class MembershipFx {
    private final SimpleIntegerProperty mid;
    private final SimpleIntegerProperty fiscalYear;
    private final SimpleIntegerProperty msId;
    private final SimpleIntegerProperty membershipId;
    private final SimpleBooleanProperty renew;
    private final SimpleStringProperty memType;
    private final SimpleBooleanProperty selected;
    private final SimpleBooleanProperty lateRenew;
    private final SimpleIntegerProperty pId;
    private final SimpleStringProperty joinDate;
    private final SimpleStringProperty address;
    private final SimpleStringProperty city;
    private final SimpleStringProperty state;
    private final SimpleStringProperty zip;
    private final ObjectProperty<SlipFx> slip = new SimpleObjectProperty<>();
    private ObservableList<PersonFx> people = FXCollections.observableArrayList();
    private ObservableList<BoatFx> boats = FXCollections.observableArrayList();
    private ObservableList<InvoiceFx> invoices = FXCollections.observableArrayList();
    private ObservableList<MembershipIdFx> membershipIds = FXCollections.observableArrayList();
    private ObservableList<NotesFx> memos = FXCollections.observableArrayList();

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

    public ObjectProperty<SlipFx> slipProperty() {
        return slip;
    }

    public ObservableList<PersonFx> getPeople() {
        return people;
    }

    public ObservableList<BoatFx> getBoats() {
        return boats;
    }

    public ObservableList<InvoiceFx> getInvoices() {
        return invoices;
    }

    public ObservableList<MembershipIdFx> getMembershipIds() {
        return membershipIds;
    }

    public ObservableList<NotesFx> getMemos() {
        return memos;
    }

    public void setPeople(ObservableList<PersonFx> people) {
        this.people = people;
    }

    public void setBoats(ObservableList<BoatFx> boats) {
        this.boats = boats;
    }

    public void setInvoices(ObservableList<InvoiceFx> invoices) {
        this.invoices = invoices;
    }

    public void setMembershipIds(ObservableList<MembershipIdFx> membershipIds) {
        this.membershipIds = membershipIds;
    }

    public void setMemos(ObservableList<NotesFx> memos) {
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

    public SlipFx getSlip() {
        return slip.get();
    }

    public void setSlip(SlipFx slip) {
        this.slip.set(slip);
    }

    public BoatFx getBoatById(int id) {
        return boats.stream()
                .filter(boatDTOFx -> Objects.equals(boatDTOFx.getBoatId(), id))
                .findFirst()
                .orElse(null);
    }
}