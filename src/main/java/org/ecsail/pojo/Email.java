package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ecsail.fx.EmailDTOFx;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Email {
    @JsonProperty("emailId")
    private int emailId;

    @JsonProperty("pId")
    private int pId;

    @JsonProperty("primaryUse")
    private boolean primaryUse;

    @JsonProperty("email")
    private String email;

    @JsonProperty("emailListed")
    private boolean emailListed;

    public Email() {
    }

    public Email(EmailDTOFx emailDTOFx) {
        this.emailId = emailDTOFx.getEmailId();
        this.pId = emailDTOFx.getpId();
        this.primaryUse = emailDTOFx.getIsPrimaryUse();
        this.email = emailDTOFx.getEmail();
        this.emailListed = emailDTOFx.getIsListed();
    }

    public Email(int pId) {
        this.emailId = 0;
        this.pId = pId;
        this.primaryUse = false;
        this.email = "";
        this.emailListed = true;
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

    public boolean isPrimaryUse() {
        return primaryUse;
    }

    public void setPrimaryUse(boolean primaryUse) {
        this.primaryUse = primaryUse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailListed() {
        return emailListed;
    }

    public void setEmailListed(boolean emailListed) {
        this.emailListed = emailListed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email1 = (Email) o;
        return emailId == email1.emailId && pId == email1.pId && primaryUse == email1.primaryUse && emailListed == email1.emailListed && Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailId, pId, primaryUse, email, emailListed);
    }
}
