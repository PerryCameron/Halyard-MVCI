package org.ecsail.dto;

public class BoatPhotosDTO {
    int id;
    int boatId;
    String uploadDate;
    String filename;
    int fileNumber;
    boolean isDefault;

    public BoatPhotosDTO(int id, int boatId, String uploadDate, String filename, int fileNumber, boolean isDefault) {
        this.id = id;
        this.boatId = boatId;
        this.uploadDate = uploadDate;
        this.filename = filename;
        this.fileNumber = fileNumber;
        this.isDefault = isDefault;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBoatId() {
        return boatId;
    }

    public void setBoatId(int boatId) {
        this.boatId = boatId;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

}
