package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ecsail.fx.AwardFx;
import org.ecsail.fx.PersonFx;

import java.time.Year;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Award {
    @JsonProperty("awardId")
    private int awardId;

    @JsonProperty("pId")
    private int pId;

    @JsonProperty("awardYear")
    private String awardYear;   // TODO this should be integer

    @JsonProperty("awardType")
    private String awardType;

    public Award() {
    }

    public Award(PersonFx personFx) {
        this.awardId = 0;
        this.pId = personFx.getpId();
        this.awardYear = Year.now().toString();
        this.awardType = "SA";
    }

    public Award(AwardFx awardDTOFx) {
        this.awardId = awardDTOFx.getAwardId();
        this.pId = awardDTOFx.getpId();
        this.awardYear = awardDTOFx.getAwardYear();
        this.awardType = awardDTOFx.getAwardType();
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

    @Override
    public String toString() {
        return "Award{" +
                "awardId=" + awardId +
                ", pId=" + pId +
                ", awardYear='" + awardYear + '\'' +
                ", awardType='" + awardType + '\'' +
                '}';
    }
}
