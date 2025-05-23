package org.ecsail.mvci.welcome;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.interfaces.ChartConstants;
import org.ecsail.widgetfx.*;

import java.time.Year;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class WelcomeView implements Builder<Region>, ChartConstants {
    WelcomeModel welcomeModel;
    private final Consumer<WelcomeMessage> action;

    public WelcomeView(WelcomeModel welcomeModel, Consumer<WelcomeMessage> action) {
        this.welcomeModel = welcomeModel;
        this.action = action;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();
        borderPane.setRight(setUpRightPane());
        borderPane.setCenter(setUpCharts());
        return borderPane;
    }

    private Node setUpCharts() {
        VBox vBox = new VBox();
        vBox.getChildren().addAll(addCharts(), addChartControls());
        return vBox;
    }

    private Node addChartControls() {
        HBox hBox = HBoxFx.hBoxOf(new Insets(5, 0, 5, 0), Pos.CENTER, 15.0);
        Node hBoxStart = HBoxFx.hBoxOf(5, Pos.CENTER_LEFT, new Label("Start"), createStartYearComboBox());
        Node hBoxStop = HBoxFx.hBoxOf(5, Pos.CENTER_LEFT, new Label("Year Span"), createYearSpanComboBox());
        Node hBoxTop = HBoxFx.hBoxOf(5, Pos.CENTER_LEFT, new Label("Bottom"), createChartSelectionComboBox());
        hBox.getChildren().addAll(hBoxStart, hBoxStop, hBoxTop, createRefreshButton());
        return hBox;
    }

    private Node createRefreshButton() {
        var button = new Button("Refresh Data");
        button.setOnAction((event) -> {
            action.accept(WelcomeMessage.UPDATE_STATS);
        });
        return button;
    }

    private Node createChartSelectionComboBox() {
        var comboBox = new ComboBox<String>();
        comboBox.getItems().addAll("Non-Renew", "New", "Return");
        comboBox.setValue("New");
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            switch (newValue) {
                case "Non-Renew" -> welcomeModel.getMembershipBarChart().changeData(NON_RENEW);
                case "New" -> welcomeModel.getMembershipBarChart().changeData(NEW_MEMBER);
                case "Return" -> welcomeModel.getMembershipBarChart().changeData(RETURN_MEMBER);
            }
        });
        return comboBox;
    }

    private Node createYearSpanComboBox() {
        ComboBox<Integer> comboBox = new ComboBox<>();
        int maxSpan = Year.now().getValue() - 1970 + 1;  // +1 to include the current year in the span
        IntStream.rangeClosed(21, maxSpan)
                .forEach(comboBox.getItems()::add);
        comboBox.setValue(welcomeModel.getYearSpan());
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            System.out.println("setting year span to: " + newValue);
            welcomeModel.setYearSpan(newValue);
            ChangeListener<Boolean> dataLoadedListener = ListenerFx.addSingleFireBooleanListener(welcomeModel.refreshChartsProperty(), () -> {
                refreshCharts();
            });
            welcomeModel.refreshChartsProperty().addListener(dataLoadedListener);
            action.accept(WelcomeMessage.RELOAD_STATS);
        });
        return comboBox;
    }

    private Node createStartYearComboBox() {
        ComboBox<Integer> comboBox = new ComboBox<>();
        comboBox.setValue(welcomeModel.getDefaultStartYear());
        IntStream.rangeClosed(1969, Year.now().getValue() - 10)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(comboBox.getItems()::add);
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            welcomeModel.setDefaultStartYear(newValue);
            ChangeListener<Boolean> dataLoadedListener = ListenerFx.addSingleFireBooleanListener(welcomeModel.refreshChartsProperty(), () -> {
                refreshCharts();
            });
            welcomeModel.refreshChartsProperty().addListener(dataLoadedListener);
            action.accept(WelcomeMessage.RELOAD_STATS);
        });
        return comboBox;
    }

    private Node addCharts() {
        VBox vBox = new VBox();
        welcomeModel.setMembershipStackedBarChart(new MembershipStackedBarChart(welcomeModel));
        welcomeModel.setMembershipBarChart(new MembershipBarChart(welcomeModel));
        vBox.getChildren().addAll(welcomeModel.getMembershipStackedBarChart(), welcomeModel.getMembershipBarChart());
        return vBox;
    }

    private Node setUpRightPane() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(15, 10, 0, 0), 400.0, 350.0, 10.0);
        String[] categories = {"People", "Slips", "Board of Directors", "Create New Membership", "Deposits",
                "Rosters", "Boats", "Notes", "Jotform"};
        Arrays.stream(categories).forEach(category -> vBox.getChildren().add(newBigButton(category)));
        return vBox;
    }

    private Node newBigButton(String category) {
        Button button = ButtonFx.bigButton(category);
        button.setOnAction((event) -> {
            welcomeModel.setTabName(category);
            action.accept(WelcomeMessage.OPEN_TAB);
        });
        return button;
    }

    private void refreshCharts() {
        welcomeModel.getMembershipBarChart().refreshChart();
        welcomeModel.getMembershipStackedBarChart().refreshChart();
    }
}
