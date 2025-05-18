package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {
    @JsonProperty("pId")
    private int pId;

    @JsonProperty("msId")
    private int msId;

    @JsonProperty("memberType")
    private int memberType;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("birthday")
    private String birthday;

    @JsonProperty("occupation")
    private String occupation;

    @JsonProperty("business")
    private String business;

    @JsonProperty("active")
    private int active;

    @JsonProperty("nickName")
    private String nickName;

    @JsonProperty("oldMsid")
    private int oldMsid;

    @JsonProperty("emails")
    private List<Email> emails;

    @JsonProperty("phones")
    private List<Phone> phones;

    @JsonProperty("awards")
    private List<Award> awards;

    @JsonProperty("officers")
    private List<Officer> officers;

    public Person() {
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getMsId() {
        return msId;
    }

    public void setMsId(int msId) {
        this.msId = msId;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getOldMsid() {
        return oldMsid;
    }

    public void setOldMsid(int oldMsid) {
        this.oldMsid = oldMsid;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Award> getAwards() {
        return awards;
    }

    public void setAwards(List<Award> awards) {
        this.awards = awards;
    }

    public List<Officer> getOfficers() {
        return officers;
    }

    public void setOfficers(List<Officer> officers) {
        this.officers = officers;
    }
}
