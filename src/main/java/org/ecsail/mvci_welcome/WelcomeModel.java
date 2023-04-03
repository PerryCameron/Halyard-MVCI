package org.ecsail.mvci_welcome;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.ecsail.dto.StatsDTO;

import java.time.Year;
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
    private IntegerProperty selectedYear = new SimpleIntegerProperty();
    private IntegerProperty defaultStartYear = new SimpleIntegerProperty(Year.now().getValue());
    private IntegerProperty defaultYearSpan = new SimpleIntegerProperty(20);
    private IntegerProperty yearSpan = new SimpleIntegerProperty();
    private IntegerProperty chartSet = new SimpleIntegerProperty(1);
    private BooleanProperty dataBaseStatisticsRefreshed = new SimpleBooleanProperty(false);
    private ObjectProperty<MembershipBarChart> membershipBarChart = new SimpleObjectProperty<>();
    private ObjectProperty<MembershipStackedBarChart> membershipStackedBarChart = new SimpleObjectProperty<>();


    public MembershipBarChart getMembershipBarChart() {
        return membershipBarChart.get();
    }

    public ObjectProperty<MembershipBarChart> membershipBarChartProperty() {
        return membershipBarChart;
    }

    public void setMembershipBarChart(MembershipBarChart membershipBarChart) {
        this.membershipBarChart.set(membershipBarChart);
    }

    public MembershipStackedBarChart getMembershipStackedBarChart() {
        return membershipStackedBarChart.get();
    }

    public ObjectProperty<MembershipStackedBarChart> membershipStackedBarChartProperty() {
        return membershipStackedBarChart;
    }

    public void setMembershipStackedBarChart(MembershipStackedBarChart membershipStackedBarChart) {
        this.membershipStackedBarChart.set(membershipStackedBarChart);
    }

    public int getChartSet() {
        return chartSet.get();
    }

    public IntegerProperty chartSetProperty() {
        return chartSet;
    }

    public void setChartSet(int chartSet) {
        this.chartSet.set(chartSet);
    }

    public int getSelectedYear() {
        return selectedYear.get();
    }

    public IntegerProperty selectedYearProperty() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear.set(selectedYear);
    }

    public int getDefaultStartYear() {
        return defaultStartYear.get();
    }

    public IntegerProperty defaultStartYearProperty() {
        return defaultStartYear;
    }

    public void setDefaultStartYear(int defaultStartYear) {
        this.defaultStartYear.set(defaultStartYear);
    }

    public int getDefaultYearSpan() {
        return defaultYearSpan.get();
    }

    public IntegerProperty defaultYearSpanProperty() {
        return defaultYearSpan;
    }

    public void setDefaultYearSpan(int defaultYearSpan) {
        this.defaultYearSpan.set(defaultYearSpan);
    }

    public int getYearSpan() {
        return yearSpan.get();
    }

    public IntegerProperty yearSpanProperty() {
        return yearSpan;
    }

    public void setYearSpan(int yearSpan) {
        this.yearSpan.set(yearSpan);
    }

    public boolean isDataBaseStatisticsRefreshed() {
        return dataBaseStatisticsRefreshed.get();
    }

    public BooleanProperty dataBaseStatisticsRefreshedProperty() {
        return dataBaseStatisticsRefreshed;
    }

    public void setDataBaseStatisticsRefreshed(boolean dataBaseStatisticsRefreshed) {
        this.dataBaseStatisticsRefreshed.set(dataBaseStatisticsRefreshed);
    }

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
