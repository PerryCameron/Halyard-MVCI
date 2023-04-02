package org.ecsail.mvci_welcome;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.ecsail.dto.StatsDTO;

import java.util.ArrayList;

public class WelcomeModel {
    private ArrayList<StatsDTO> stats;
    private ObservableList<XYChart.Data<String,Number>> familyData = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data<String,Number>> regularData = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data<String,Number>> socialData = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data<String,Number>> lakeAssociateData = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data<String,Number>> lifeMemberData = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data<String,Number>> nonRenewData = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data<String,Number>> newMemberData = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data<String,Number>> returnMemberData = FXCollections.observableArrayList();


    public ObservableList<XYChart.Data<String, Number>> getNonRenewData() {
        return nonRenewData;
    }

    public void setNonRenewData(ObservableList<XYChart.Data<String, Number>> nonRenewData) {
        this.nonRenewData = nonRenewData;
    }

    public ObservableList<XYChart.Data<String, Number>> getNewMemberData() {
        return newMemberData;
    }

    public void setNewMemberData(ObservableList<XYChart.Data<String, Number>> newMemberData) {
        this.newMemberData = newMemberData;
    }

    public ObservableList<XYChart.Data<String, Number>> getReturnMemberData() {
        return returnMemberData;
    }

    public void setReturnMemberData(ObservableList<XYChart.Data<String, Number>> returnMemberData) {
        this.returnMemberData = returnMemberData;
    }

    public ObservableList<XYChart.Data<String, Number>> getFamilyData() {
        return familyData;
    }

    public void setFamilyData(ObservableList<XYChart.Data<String, Number>> familyData) {
        this.familyData = familyData;
    }

    public ObservableList<XYChart.Data<String, Number>> getRegularData() {
        return regularData;
    }

    public void setRegularData(ObservableList<XYChart.Data<String, Number>> regularData) {
        this.regularData = regularData;
    }

    public ObservableList<XYChart.Data<String, Number>> getSocialData() {
        return socialData;
    }

    public void setSocialData(ObservableList<XYChart.Data<String, Number>> socialData) {
        this.socialData = socialData;
    }

    public ObservableList<XYChart.Data<String, Number>> getLakeAssociateData() {
        return lakeAssociateData;
    }

    public void setLakeAssociateData(ObservableList<XYChart.Data<String, Number>> lakeAssociateData) {
        this.lakeAssociateData = lakeAssociateData;
    }

    public ObservableList<XYChart.Data<String, Number>> getLifeMemberData() {
        return lifeMemberData;
    }

    public void setLifeMemberData(ObservableList<XYChart.Data<String, Number>> lifeMemberData) {
        this.lifeMemberData = lifeMemberData;
    }

    public ArrayList<StatsDTO> getStats() {
        return stats;
    }

    public void setStats(ArrayList<StatsDTO> stats) {
        this.stats = stats;
    }
}
