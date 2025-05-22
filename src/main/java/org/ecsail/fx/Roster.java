package org.ecsail.fx;

public class Roster {
    private int id;
    private int msId;
    private String name;
    private String joinDate;
    private String type;
    private String slip;
    private String city;
    private int  subleasedTo;


    public Roster(int id, int msId, String name, String joinDate, String type, String slip, String city, int subleasedTo) {
        this.id = id;
        this.msId = msId;
        this.name = name;
        this.joinDate = joinDate;
        this.type = type;
        this.slip = slip;
        this.city = city;
        this.subleasedTo = subleasedTo;
    }

    public Roster() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMsId() {
        return msId;
    }

    public void setMsId(int msId) {
        this.msId = msId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlip() {
        return slip;
    }

    public void setSlip(String slip) {
        this.slip = slip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getSubleasedTo() {
        return subleasedTo;
    }

    public void setSubleasedTo(int subleasedTo) {
        this.subleasedTo = subleasedTo;
    }
}