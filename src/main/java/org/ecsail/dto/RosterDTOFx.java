package org.ecsail.dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RosterDTOFx {
    private IntegerProperty id = new SimpleIntegerProperty();
    private IntegerProperty msId = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty joinDate = new SimpleStringProperty();
    private StringProperty type = new SimpleStringProperty();
    private StringProperty slip = new SimpleStringProperty();
    private StringProperty city = new SimpleStringProperty();
    private IntegerProperty  subleasedTo = new SimpleIntegerProperty();


    public RosterDTOFx(int id, int msId, String name, String joinDate, String type, String slip, String city, int subleasedTo) {
        this.id.set(id);
        this.msId.set(msId);
        this.name.set(name);
        this.joinDate.set(joinDate);
        this.type.set(type);
        this.slip.set(slip);
        this.city.set(city);
        this.subleasedTo.set(subleasedTo);
    }

    public RosterDTOFx(RosterDTO rosterDTO) {
        this.id.set(rosterDTO.getId());
        this.msId.set(rosterDTO.getMsId());
        this.name.set(rosterDTO.getName());
        this.joinDate.set(rosterDTO.getJoinDate());
        this.type.set(rosterDTO.getType());
        this.slip.set(rosterDTO.getSlip());
        this.city.set(rosterDTO.getCity());
        this.subleasedTo.set(rosterDTO.getSubleasedTo());
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getMsId() {
        return msId.get();
    }

    public IntegerProperty msIdProperty() {
        return msId;
    }

    public void setMsId(int msId) {
        this.msId.set(msId);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getJoinDate() {
        return joinDate.get();
    }

    public StringProperty joinDateProperty() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate.set(joinDate);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getSlip() {
        return slip.get();
    }

    public StringProperty slipProperty() {
        return slip;
    }

    public void setSlip(String slip) {
        this.slip.set(slip);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public int getSubleasedTo() {
        return subleasedTo.get();
    }

    public IntegerProperty subleasedToProperty() {
        return subleasedTo;
    }

    public void setSubleasedTo(int subleasedTo) {
        this.subleasedTo.set(subleasedTo);
    }
}