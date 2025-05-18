package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Slip {
    @JsonProperty("slipId")
    private int slipId;

    @JsonProperty("slipNum")
    private String slipNum;

    @JsonProperty("subleasedTo")
    private String subleasedTo;

    @JsonProperty("altText")
    private String altText;

    public Slip() {
    }

    public int getSlipId() {
        return slipId;
    }

    public void setSlipId(int slipId) {
        this.slipId = slipId;
    }

    public String getSlipNum() {
        return slipNum;
    }

    public void setSlipNum(String slipNum) {
        this.slipNum = slipNum;
    }

    public String getSubleasedTo() {
        return subleasedTo;
    }

    public void setSubleasedTo(String subleasedTo) {
        this.subleasedTo = subleasedTo;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }
}