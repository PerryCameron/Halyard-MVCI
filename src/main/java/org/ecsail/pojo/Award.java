package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Award {
    @JsonProperty("awardId")
    private int awardId;

    @JsonProperty("pId")
    private int pId;

    @JsonProperty("awardYear")
    private String awardYear;

    @JsonProperty("awardType")
    private String awardType;

    public Award() {
    }

    public int getAwardId() {
        return awardId;
    }

    public void setAwardId(int awardId) {
        this.awardId = awardId;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getAwardYear() {
        return awardYear;
    }

    public void setAwardYear(String awardYear) {
        this.awardYear = awardYear;
    }

    public String getAwardType() {
        return awardType;
    }

    public void setAwardType(String awardType) {
        this.awardType = awardType;
    }
}
