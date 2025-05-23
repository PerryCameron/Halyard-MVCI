package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ecsail.fx.PhoneFx;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private int phoneListed;

    public Phone() {
    }

    public Phone(PhoneFx phoneDTOFx) {
        this.phoneId = phoneDTOFx.getPhoneId();
        this.pId = phoneDTOFx.getpId();
        this.phone = phoneDTOFx.getPhone();
        this.phoneType = phoneDTOFx.getPhoneType();
        this.phoneListed = phoneDTOFx.getPhoneListed() ? 1 : 0;
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

    public int getPhoneListed() {
        return phoneListed;
    }

    public void setPhoneListed(int phoneListed) {
        this.phoneListed = phoneListed;
    }
}