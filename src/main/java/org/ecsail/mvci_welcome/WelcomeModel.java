package org.ecsail.mvci_welcome;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.ecsail.dto.StatsDTO;
import org.ecsail.mvci_main.MainModel;

import java.time.Year;
import java.util.ArrayList;

public class WelcomeModel {
    private final MainModel mainModel;
    private ArrayList<StatsDTO> stats;
    private final ObservableList<XYChart.Data<String, Number>> familyData = FXCollections.observableArrayList();
    private final ObservableList<XYChart.Data<String, Number>> regularData = FXCollections.observableArrayList();
    private final ObservableList<XYChart.Data<String, Number>> socialData = FXCollections.observableArrayList();
    private final ObservableList<XYChart.Data<String, Number>> lakeAssociateData = FXCollections.observableArrayList();
    private final ObservableList<XYChart.Data<String, Number>> lifeMemberData = FXCollections.observableArrayList();
    private final ObservableList<XYChart.Data<String, Number>> nonRenewData = FXCollections.observableArrayList();
    private final ObservableList<XYChart.Data<String, Number>> newMemberData = FXCollections.observableArrayList();
    private final ObservableList<XYChart.Data<String, Number>> returnMemberData = FXCollections.observableArrayList();
    private final IntegerProperty selectedYear = new SimpleIntegerProperty(Year.now().getValue());
    private final IntegerProperty defaultStartYear = new SimpleIntegerProperty(Year.now().getValue() - 20);
    private final IntegerProperty yearSpan = new SimpleIntegerProperty(21);
    private final IntegerProperty chartSet = new SimpleIntegerProperty(1);
    private final BooleanProperty dataBaseStatisticsRefreshed = new SimpleBooleanProperty(false);
    private final ObjectProperty<MembershipBarChart> membershipBarChart = new SimpleObjectProperty<>();
    private final ObjectProperty<MembershipStackedBarChart> membershipStackedBarChart = new SimpleObjectProperty<>();
    private final BooleanProperty refreshCharts = new SimpleBooleanProperty(false);
    private final StringProperty tabName = new SimpleStringProperty();
    private final DoubleProperty progress = new SimpleDoubleProperty(0);
    private final IntegerProperty startYear = new SimpleIntegerProperty(1970);

    public WelcomeModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    public MainModel getMainModel() {
        return mainModel;
    }

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress.set(progress);
    }

    public int getStartYear() {
        return startYear.get();
    }

    public void setStartYear(int startYear) {
        this.startYear.set(startYear);
    }

    public void incrementStartYear() {
        startYear.set(startYear.get() + 1);
    }

    public IntegerProperty selectedYearProperty() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear.set(selectedYear);
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

    public MembershipBarChart getMembershipBarChart() {
        return membershipBarChart.get();
    }

    public void setMembershipBarChart(MembershipBarChart membershipBarChart) {
        this.membershipBarChart.set(membershipBarChart);
    }

    public MembershipStackedBarChart getMembershipStackedBarChart() {
        return membershipStackedBarChart.get();
    }

    public void setMembershipStackedBarChart(MembershipStackedBarChart membershipStackedBarChart) {
        this.membershipStackedBarChart.set(membershipStackedBarChart);
    }

    public int getChartSet() {
        return chartSet.get();
    }

    public void setChartSet(int chartSet) {
        this.chartSet.set(chartSet);
    }

    public int getSelectedYear() {
        return selectedYear.get();
    }

    public int getDefaultStartYear() {
        return defaultStartYear.get();
    }

    public void setDefaultStartYear(int defaultStartYear) {
        this.defaultStartYear.set(defaultStartYear);
    }

    public int getYearSpan() {
        return yearSpan.get();
    }

    public void setYearSpan(int yearSpan) {
        this.yearSpan.set(yearSpan);
    }

    public ObservableList<XYChart.Data<String, Number>> getNonRenewData() {
        return nonRenewData;
    }

    public ObservableList<XYChart.Data<String, Number>> getNewMemberData() {
        return newMemberData;
    }

    public ObservableList<XYChart.Data<String, Number>> getReturnMemberData() {
        return returnMemberData;
    }

    public ObservableList<XYChart.Data<String, Number>> getFamilyData() {
        return familyData;
    }

    public ObservableList<XYChart.Data<String, Number>> getRegularData() {
        return regularData;
    }

    public ObservableList<XYChart.Data<String, Number>> getSocialData() {
        return socialData;
    }

    public ObservableList<XYChart.Data<String, Number>> getLakeAssociateData() {
        return lakeAssociateData;
    }

    public ObservableList<XYChart.Data<String, Number>> getLifeMemberData() {
        return lifeMemberData;
    }

    public ArrayList<StatsDTO> getStats() {
        return stats;
    }

    public void setStats(ArrayList<StatsDTO> stats) {
        this.stats = stats;
    }

    public String getTabName() {
        return tabName.get();
    }

    public StringProperty tabNameProperty() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName.set(tabName);
    }

    public boolean isRefreshCharts() {
        return refreshCharts.get();
    }

    public BooleanProperty refreshChartsProperty() {
        return refreshCharts;
    }

    public void setRefreshCharts(boolean refreshCharts) {
        this.refreshCharts.set(refreshCharts);
    }
}
