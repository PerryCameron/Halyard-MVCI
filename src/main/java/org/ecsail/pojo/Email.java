package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ecsail.dto.EmailDTOFx;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Email {
    @JsonProperty("emailId")
    private int emailId;

    @JsonProperty("pId")
    private int pId;

    @JsonProperty("primaryUse")
    private int primaryUse;

    @JsonProperty("email")
    private String email;

    @JsonProperty("emailListed")
    private int emailListed;

    public Email() {
    }

    public Email(EmailDTOFx emailDTOFx) {
        this.emailId = emailDTOFx.getEmailId();
        this.pId = emailDTOFx.getpId();
        this.primaryUse = emailDTOFx.getIsPrimaryUse() ? 1 : 0;
        this.email = emailDTOFx.getEmail();
        this.emailListed = emailDTOFx.getIsListed() ? 1 : 0;
    }

    public int getEmailId() {
        return emailId;
    }

    public void setEmailId(int emailId) {
        this.emailId = emailId;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getPrimaryUse() {
        return primaryUse;
    }

    public void setPrimaryUse(int primaryUse) {
        this.primaryUse = primaryUse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEmailListed() {
        return emailListed;
    }

    public void setEmailListed(int emailListed) {
        this.emailListed = emailListed;
    }
}
