package org.ecsail.dto;

public class SlipInfoDTO {
    int ownerId;
    String ownerFirstName;
    String ownerLastName;
    int ownerMsid;
    String slipNumber;
    String altText;
    int subleaserId;
    int subleaserMsid;
    String subleaserFirstName;
    String subleaserLastName;

    public SlipInfoDTO(int ownerId, String ownerFirstName, String ownerLastName, int ownerMsid, String slipNumber, String altText, int subleaserId, int subleaserMsid, String subleaserFirstName, String subleaserLastName) {
        this.ownerId = ownerId;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.ownerMsid = ownerMsid;
        this.slipNumber = slipNumber;
        this.altText = altText;
        this.subleaserId = subleaserId;
        this.subleaserMsid = subleaserMsid;
        this.subleaserFirstName = subleaserFirstName;
        this.subleaserLastName = subleaserLastName;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

    public int getOwnerMsid() {
        return ownerMsid;
    }

    public void setOwnerMsid(int ownerMsid) {
        this.ownerMsid = ownerMsid;
    }

    public String getSlipNumber() {
        return slipNumber;
    }

    public void setSlipNumber(String slipNumber) {
        this.slipNumber = slipNumber;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public int getSubleaserId() {
        return subleaserId;
    }

    public void setSubleaserId(int subleaserId) {
        this.subleaserId = subleaserId;
    }

    public int getSubleaserMsid() {
        return subleaserMsid;
    }

    public void setSubleaserMsid(int subleaserMsid) {
        this.subleaserMsid = subleaserMsid;
    }

    public String getSubleaserFirstName() {
        return subleaserFirstName;
    }

    public void setSubleaserFirstName(String subleaserFirstName) {
        this.subleaserFirstName = subleaserFirstName;
    }

    public String getSubleaserLastName() {
        return subleaserLastName;
    }

    public void setSubleaserLastName(String subleaserLastName) {
        this.subleaserLastName = subleaserLastName;
    }

    @Override
    public String toString() {
        return "SlipInfoDTO{" +
                "ownerId=" + ownerId +
                ", ownerFirstName='" + ownerFirstName + '\'' +
                ", ownerLastName='" + ownerLastName + '\'' +
                ", ownerMsid=" + ownerMsid +
                ", slipNumber='" + slipNumber + '\'' +
                ", altText='" + altText + '\'' +
                ", subleaserId=" + subleaserId +
                ", subleaserMsid=" + subleaserMsid +
                ", subleaserFirstName='" + subleaserFirstName + '\'' +
                ", subleaserLastName='" + subleaserLastName + '\'' +
                '}';
    }
}

