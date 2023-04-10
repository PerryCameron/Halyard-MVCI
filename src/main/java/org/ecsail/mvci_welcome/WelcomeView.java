package org.ecsail.mvci_welcome;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Builder;
import org.ecsail.interfaces.ChartConstants;
import org.ecsail.widgetfx.ButtonFx;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.VBoxFx;

import java.time.Year;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class WelcomeView implements Builder<Region>, ChartConstants {
    WelcomeModel welcomeModel;
    Runnable reloadStats;
    Runnable updateStats;
    Consumer openTab;

    public WelcomeView(WelcomeModel wm, Runnable rs, Runnable us, Consumer<String> o) {
        this.welcomeModel = wm;
        this.reloadStats = rs;
        this.updateStats = us;
        this.openTab = o;
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
        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER, new Insets(5,0,5,0),15.0);
        Node hBoxStart = HBoxFx.hBoxOf(5, Pos.CENTER_LEFT, new Label("Start"),createStartYearComboBox());
        Node hBoxStop = HBoxFx.hBoxOf(5, Pos.CENTER_LEFT, new Label("Year Span"), createYearSpanComboBox());
        Node hBoxTop = HBoxFx.hBoxOf(5, Pos.CENTER_LEFT, new Label("Bottom"),createChartSelectionComboBox());
        hBox.getChildren().addAll(hBoxStart,hBoxStop,hBoxTop,createRefreshButton());
        return hBox;
    }

    private Node createRefreshButton() {
        var button = new Button("Refresh Data");
        button.setOnAction((event)-> {
            Stage stage = new Stage();
            stage.setTitle("Updating Statistics");
            stage.setScene(new Scene(new DialogProgressIndicator(welcomeModel).build()));
            stage.getScene().getStylesheets().add("css/dark/custom_dialogue.css");
            stage.show();
            updateStats.run();
            welcomeModel.dataBaseStatisticsRefreshedProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue) stage.close();
                welcomeModel.setDataBaseStatisticsRefreshed(false);
            });
        });
        return button;
    }

    private Node createChartSelectionComboBox() {
        var comboBox = new ComboBox<String>();
        comboBox.getItems().addAll("Non-Renew","New","Return");
        comboBox.setValue("Non-Renew");
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
        var comboBox = new ComboBox<Integer>();
        IntStream.rangeClosed(10, Year.now().getValue() - 1)
                .forEach(comboBox.getItems()::add);
        comboBox.setValue(welcomeModel.getYearSpan());
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            welcomeModel.setYearSpan(newValue);
            reloadStats.run();
            welcomeModel.getMembershipBarChart().refreshChart();
            welcomeModel.getMembershipStackedBarChart().refreshChart();
        });
        return  comboBox;
    }

    private Node createStartYearComboBox() {
        var comboBox = new ComboBox<Integer>();
        comboBox.setValue(welcomeModel.getDefaultStartYear());
        IntStream.rangeClosed(1969, Year.now().getValue() - 10)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(comboBox.getItems()::add);
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            System.out.println("combo box ");
            welcomeModel.setDefaultStartYear(newValue);
            reloadStats.run();
            welcomeModel.getMembershipBarChart().refreshChart();
            welcomeModel.getMembershipStackedBarChart().refreshChart();
        });
        return comboBox;
    }

    private Node addCharts() {
        VBox vBox = new VBox();
        welcomeModel.setMembershipStackedBarChart(new MembershipStackedBarChart(welcomeModel));
        welcomeModel.setMembershipBarChart(new MembershipBarChart(welcomeModel));
        vBox.getChildren().addAll(welcomeModel.getMembershipStackedBarChart(),welcomeModel.getMembershipBarChart());
        return vBox;
    }

    private Node setUpRightPane() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(15,10,0,0), 400.0,350.0,10.0);
        String[] categories = {"People","Slips","Board of Directors","Create New Membership","Deposits",
                "Rosters","Boats","Notes","Jotform"};
        Arrays.stream(categories).forEach(category -> vBox.getChildren().add(newBigButton(category)));
        return vBox;
    }

    private Node newBigButton(String tab) {
        Button button = ButtonFx.bigButton(tab);
        button.setOnAction((event) -> openTab.accept(tab));
        return button;
    }
}
