package org.ecsail.dto;

public class DockPlacementDTO {
    int id;
    String dockName;
    int webX;
    int webY;
    int appX;
    int appY;

    public DockPlacementDTO(int id, String dockName, int appX, int appY) {
        this.id = id;
        this.dockName = dockName;
        this.webX = 0;
        this.webY = 0;
        this.appX = appX;
        this.appY = appY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDockName() {
        return dockName;
    }

    public void setDockName(String dockName) {
        this.dockName = dockName;
    }

    public int getWebX() {
        return webX;
    }

    public void setWebX(int webX) {
        this.webX = webX;
    }

    public int getWebY() {
        return webY;
    }

    public void setWebY(int webY) {
        this.webY = webY;
    }

    public int getAppX() {
        return appX;
    }

    public void setAppX(int appX) {
        this.appX = appX;
    }

    public int getAppY() {
        return appY;
    }

    public void setAppY(int appY) {
        this.appY = appY;
    }
}
