package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ecsail.fx.PersonFx;
import org.ecsail.fx.PhoneFx;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore membershipTypes and other unknown fields
public class Phone {
    @JsonProperty("phoneId")
    private int phoneId;

    @JsonProperty("pId")
    private int pId;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("phoneType")
    private String phoneType;

    @JsonProperty("phoneListed")
    private boolean phoneListed;

    public Phone() {
    }

    public Phone(PhoneFx phoneDTOFx) {
        this.phoneId = phoneDTOFx.getPhoneId();
        this.pId = phoneDTOFx.getpId();
        this.phone = phoneDTOFx.getPhone();
        this.phoneType = phoneDTOFx.getPhoneType();
        this.phoneListed = phoneDTOFx.getPhoneListed();
    }

    public Phone(PersonFx person) {
        this.phoneId = 0;
        this.pId = person.getpId();
        this.phone = "";
        this.phoneType = "C";
        this.phoneListed = true;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public boolean isPhoneListed() {
        return phoneListed;
    }

    public void setPhoneListed(boolean phoneListed) {
        this.phoneListed = phoneListed;
    }
}