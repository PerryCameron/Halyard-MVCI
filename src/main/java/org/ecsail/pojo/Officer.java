package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ecsail.fx.OfficerFx;

import java.time.Year;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Officer {
    @JsonProperty("officerId")
    private int officerId;

    @JsonProperty("pId")
    private int pId;

    @JsonProperty("boardYear")
    private int boardYear;

    @JsonProperty("officerType")
    private String officerType;

    @JsonProperty("fiscalYear")
    private int fiscalYear;

    public Officer() {
    }

    public Officer(OfficerFx officerDTOFx) {
        this.officerId = officerDTOFx.getOfficerId();
        this.pId = officerDTOFx.getpId();
        this.boardYear = officerDTOFx.getBoardYear();
        this.officerType = officerDTOFx.getOfficerType();
        this.fiscalYear = officerDTOFx.getFiscalYear();
    }

    public Officer(int pId) {
        this.officerId = 0;
        this.pId = pId;
        this.boardYear = 0;
        this.officerType = "BM";
        this.fiscalYear = Year.now().getValue();
    }

    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getBoardYear() {
        return boardYear;
    }

    public void setBoardYear(int boardYear) {
        this.boardYear = boardYear;
    }

    public String getOfficerType() {
        return officerType;
    }

    public void setOfficerType(String officerType) {
        this.officerType = officerType;
    }

    public int getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(int fiscalYear) {
        this.fiscalYear = fiscalYear;
    }
}
