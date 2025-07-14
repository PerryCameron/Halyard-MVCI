package org.ecsail.fx;

public class PictureDTO {
    private int id;
    private int pid;
    private boolean defaultPicture;
    private byte[] picture;

    // Default constructor
    public PictureDTO() {
    }

    // Constructor with all fields
    public PictureDTO(int id, int pid, boolean defaultPicture, byte[] picture) {
        this.id = id;
        this.pid = pid;
        this.defaultPicture = defaultPicture;
        this.picture = picture;
    }

    public PictureDTO(int pid, byte[] picture) {
        this.id = 0;
        this.pid = pid;
        this.defaultPicture = true;
        this.picture = picture;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public boolean isDefaultPicture() {
        return defaultPicture;
    }

    public void setDefaultPicture(boolean defaultPicture) {
        this.defaultPicture = defaultPicture;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    // toString
    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", pid=" + pid +
                ", defaultPicture=" + defaultPicture +
                ", picture=" + (picture != null ? "byte[" + picture.length + "]" : "null") +
                '}';
    }

    // equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PictureDTO picture1 = (PictureDTO) o;
        return id == picture1.id &&
                pid == picture1.pid &&
                defaultPicture == picture1.defaultPicture &&
                java.util.Arrays.equals(picture, picture1.picture);
    }

    // hashCode
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + pid;
        result = 31 * result + (defaultPicture ? 1 : 0);
        result = 31 * result + java.util.Arrays.hashCode(picture);
        return result;
    }
}
