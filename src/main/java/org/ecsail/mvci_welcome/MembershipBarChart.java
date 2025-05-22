package org.ecsail.mvci_welcome;

import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import org.ecsail.fx.StatsDTO;
import org.ecsail.interfaces.ChartConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class MembershipBarChart extends BarChart<String,Number> implements ChartConstants {
    private final WelcomeModel welcomeModel;
    Series<String,Number> seriesData = new Series<>();
    private static final Logger logger = LoggerFactory.getLogger(MembershipBarChart.class);


    protected MembershipBarChart(WelcomeModel welcomeModel) {
        super(new CategoryAxis(),new NumberAxis());
        this.welcomeModel = welcomeModel;
        setLegendVisible(false);
        setAnimated(false);
        getYAxis().setPrefWidth(30);
        setTitle("Non-Renewed Memberships");
        getXAxis().setLabel("Years");
    }

    private void addData() {
        if(welcomeModel.getStats() != null) {
            for (StatsDTO s : welcomeModel.getStats()) {
                switch (welcomeModel.getChartSet()) {
                    case NON_RENEW ->
                            welcomeModel.getNonRenewData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getNonRenewMemberships()));
                    case NEW_MEMBER ->
                            welcomeModel.getNewMemberData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getNewMemberships()));
                    case RETURN_MEMBER ->
                            welcomeModel.getReturnMemberData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getReturnMemberships()));
                }
            }
        } else logger.warn("There are no statistics available");
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
        IntStream.range(0, welcomeModel.getStats().size())
                .forEach(i -> lookupAll(".data"+i+".chart-bar")
                        .forEach(n -> n.setStyle("-fx-bar-fill: "+color+";")));
    }

    protected void refreshChart() {
        changeData(welcomeModel.getChartSet());
    }
}
