package org.ecsail.pojo;

public class BoatOwnerDTO {
    private Integer msid;
    private Integer boatId;


    public BoatOwnerDTO(Integer msid, Integer boatId) {
        this.msid = msid;
        this.boatId = boatId;
    }

    public BoatOwnerDTO() {
    }

    public Integer getMsid() {
        return msid;
    }

    public void setMsid(Integer msid) {
        this.msid = msid;
    }

    public Integer getBoatId() {
        return boatId;
    }

    public void setBoatId(Integer boatId) {
        this.boatId = boatId;
    }

    @Override
    public String toString() {
        return "BoatOwnerDTO{" +
                "msid=" + msid +
                ", boat_id=" + boatId +
                '}';
    }
}


