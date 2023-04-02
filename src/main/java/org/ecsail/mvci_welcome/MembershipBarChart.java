package org.ecsail.mvci_welcome;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import org.ecsail.dto.StatsDTO;

public class MembershipBarChart extends BarChart<String,Number> {
    private final WelcomeModel welcomeModel;
    int set;

    Series seriesData = new Series();

    public MembershipBarChart(Axis xAxis, Axis yAxis, WelcomeModel welcomeModel, int set) {
        super(xAxis, yAxis);
        this.set = set;
        this.welcomeModel = welcomeModel;
        setLegendVisible(false);
        setAnimated(false);
        setTitle("Non-Renewed Memberships");
        xAxis.setLabel("Years");

    addData();
    setSeriesData();
    getData().addAll(seriesData);
    }

    private void addData() {
        for (StatsDTO s: welcomeModel.getStats()) {
            switch (set) {
                case 1:
                    welcomeModel.getNonRenewData().add(new Data<String,Number>(String.valueOf(s.getFiscalYear()),s.getNonRenewMemberships()));
                    break;
                case 2:
                    welcomeModel.getNewMemberData().add(new Data<String,Number>(String.valueOf(s.getFiscalYear()),s.getNewMemberships()));
                    break;
                case 3:
                    welcomeModel.getReturnMemberData().add(new Data<String,Number>(String.valueOf(s.getFiscalYear()),s.getReturnMemberships()));
            }
        }
    }

    private void setSeriesData() {
        switch (set) {
            case 1:
                seriesData.setData(welcomeModel.getNonRenewData());
                setTitle("Non-Renewed Memberships");

                break;
            case 2:
                seriesData.setData(welcomeModel.getNewMemberData());
                setTitle("New Memberships");
                changeSeriesColor("#860061");
                break;
            case 3:
                seriesData.setData(welcomeModel.getReturnMemberData());
                setTitle("Return Memberships");
                changeSeriesColor("#22bad9");
        }
    }

    private void clearSeries() {
        seriesData.getData().clear();
    }

    public void changeData(int change) {
        this.set = change;
        clearSeries();
        addData();
        setSeriesData();
        setData(FXCollections.singletonObservableList(seriesData));
    }

    public void changeSeriesColor(String color) {
        for(int i = 0; i < welcomeModel.getStats().size(); i++) {
            for(Node n:lookupAll(".data"+i+".chart-bar")) {
               n.setStyle("-fx-bar-fill: "+color+";");
            }
        }
    }

    public void refreshChart() {
        changeData(set);
    }

}
