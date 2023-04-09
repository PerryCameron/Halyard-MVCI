package org.ecsail.mvci_welcome;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import org.ecsail.dto.StatsDTO;
import org.ecsail.interfaces.ChartConstants;

public class MembershipBarChart extends BarChart<String,Number> implements ChartConstants {
    private final WelcomeModel welcomeModel;
    Series<String,Number> seriesData = new Series<>();

    protected MembershipBarChart(WelcomeModel welcomeModel) {
        super(new CategoryAxis(),new NumberAxis());
        this.welcomeModel = welcomeModel;
        setLegendVisible(false);
        setAnimated(false);
        getYAxis().setPrefWidth(30);
        setTitle("Non-Renewed Memberships");
        getXAxis().setLabel("Years");
//    addData();
//    setSeriesData();
//    getData().add(seriesData);
    }

    private void addData() {
        for (StatsDTO s: welcomeModel.getStats()) {
            switch (welcomeModel.getChartSet()) {
                case NON_RENEW ->
                        welcomeModel.getNonRenewData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getNonRenewMemberships()));
                case NEW_MEMBER ->
                        welcomeModel.getNewMemberData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getNewMemberships()));
                case RETURN_MEMBER ->
                        welcomeModel.getReturnMemberData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getReturnMemberships()));
            }
        }
    }

    private void setSeriesData() {
        switch (welcomeModel.getChartSet()) {
            case NON_RENEW -> {
                seriesData.setData(welcomeModel.getNonRenewData());
                setTitle("Non-Renewed Memberships");
            }
            case NEW_MEMBER -> {
                seriesData.setData(welcomeModel.getNewMemberData());
                setTitle("New Memberships");
                changeSeriesColor("#860061");
            }
            case RETURN_MEMBER -> {
                seriesData.setData(welcomeModel.getReturnMemberData());
                setTitle("Return Memberships");
                changeSeriesColor("#22bad9");
            }
        }
    }

    private void clearSeries() {
        seriesData.getData().clear();
    }

    protected void changeData(int change) {
        welcomeModel.setChartSet(change);
        clearSeries();
        addData();
        setSeriesData();
        setData(FXCollections.singletonObservableList(seriesData));
    }

    private void changeSeriesColor(String color) {
        for(int i = 0; i < welcomeModel.getStats().size(); i++) {
            for(Node n:lookupAll(".data"+i+".chart-bar")) {
               n.setStyle("-fx-bar-fill: "+color+";");
            }
        }
    }

    protected void refreshChart() {
        changeData(welcomeModel.getChartSet());
    }
}
