package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ecsail.dto.OfficerFx;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfficerDTO {
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

    public OfficerDTO() {
    }

    public OfficerDTO(OfficerFx officerDTOFx) {
        this.officerId = officerDTOFx.getOfficerId();
        this.pId = officerDTOFx.getpId();
        this.boardYear = Integer.parseInt(officerDTOFx.getBoardYear());
        this.officerType = officerDTOFx.getOfficerType();
        this.fiscalYear = Integer.parseInt(officerDTOFx.getFiscalYear());
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
