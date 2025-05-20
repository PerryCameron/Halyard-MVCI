package org.ecsail.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ecsail.dto.BoatDTOFx;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoatDTO {
    @JsonProperty("boatId")
    private int boatId;

    @JsonProperty("manufacturer")
    private String manufacturer;

    @JsonProperty("manufactureYear")
    private String manufactureYear;

    @JsonProperty("registrationNum")
    private String registrationNum;

    @JsonProperty("model")
    private String model;

    @JsonProperty("boatName")
    private String boatName;

    @JsonProperty("sailNumber")
    private String sailNumber;

    @JsonProperty("hasTrailer")
    private boolean hasTrailer;

    @JsonProperty("length")
    private String length;

    @JsonProperty("weight")
    private String weight;

    @JsonProperty("keel")
    private String keel;

    @JsonProperty("phrf")
    private String phrf;

    @JsonProperty("draft")
    private String draft;

    @JsonProperty("beam")
    private String beam;

    @JsonProperty("lwl")
    private String lwl;

    @JsonProperty("aux")
    private boolean aux;

    // Getters and setters


    public BoatDTO(BoatDTOFx selectedBoat) {
        this.boatId = selectedBoat.getBoatId();
        this.manufacturer = selectedBoat.getManufacturer();
        this.manufactureYear = selectedBoat.getManufactureYear();
        this.registrationNum = selectedBoat.getRegistrationNum();
        this.model = selectedBoat.getModel();
        this.boatName = selectedBoat.getBoatName();
        this.sailNumber = selectedBoat.getSailNumber();
        this.hasTrailer = selectedBoat.isHasTrailer();
        this.length = selectedBoat.getLoa();
        this.weight = selectedBoat.getDisplacement();
        this.keel = selectedBoat.getKeel();
        this.phrf = selectedBoat.getPhrf();
        this.draft = selectedBoat.getDraft();
        this.beam = selectedBoat.getBeam();
        this.lwl = selectedBoat.getLwl();
        this.aux = selectedBoat.isAux();
    }

    public BoatDTO() {
    }


    public int getBoatId() {
        return boatId;
    }

    public void setBoatId(int boatId) {
        this.boatId = boatId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(String manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public String getRegistrationNum() {
        return registrationNum;
    }

    public void setRegistrationNum(String registrationNum) {
        this.registrationNum = registrationNum;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBoatName() {
        return boatName;
    }

    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }

    public String getSailNumber() {
        return sailNumber;
    }

    public void setSailNumber(String sailNumber) {
        this.sailNumber = sailNumber;
    }

    public boolean isHasTrailer() {
        return hasTrailer;
    }

    public void setHasTrailer(boolean hasTrailer) {
        this.hasTrailer = hasTrailer;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getKeel() {
        return keel;
    }

    public void setKeel(String keel) {
        this.keel = keel;
    }

    public String getPhrf() {
        return phrf;
    }

    public void setPhrf(String phrf) {
        this.phrf = phrf;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    public String getBeam() {
        return beam;
    }

    public void setBeam(String beam) {
        this.beam = beam;
    }

    public String getLwl() {
        return lwl;
    }

    public void setLwl(String lwl) {
        this.lwl = lwl;
    }

    public boolean isAux() {
        return aux;
    }

    public void setAux(boolean aux) {
        this.aux = aux;
    }
}