package org.ecsail.mvci_welcome;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class MembershipStackedBarChart extends StackedBarChart<String,Number> {

	private final WelcomeModel welcomeModel;
	private final Series<String,Number> seriesFamily = new Series<>();
	private final Series<String,Number> seriesRegular = new Series<>();
	private final Series<String,Number> seriesSocial = new Series<>();
	private final Series<String,Number> seriesLakeAssociate = new Series<>();
	private final Series<String,Number> seriesLifeMember = new Series<>();
	private static final Logger logger = LoggerFactory.getLogger(MembershipStackedBarChart.class);


	public MembershipStackedBarChart(WelcomeModel welcomeModel) {
		super(new CategoryAxis(),new NumberAxis());
		this.welcomeModel = welcomeModel;
	        setTitle("Active Memberships By Year");
			getXAxis().setAutoRanging(true);
			getYAxis().setPrefWidth(30);
			setNames();
			getLegend().getStyleClass().add("legend-small");
			setAnimated(false);
		getData().addAll(Arrays.asList(seriesFamily,seriesRegular,seriesSocial,seriesLakeAssociate,seriesLifeMember));
	}

	private void setNames() {
		seriesFamily.setName("Family");
		seriesRegular.setName("Regular");
		seriesSocial.setName("Social");
		seriesLakeAssociate.setName("Lake Associate");
		seriesLifeMember.setName("Life Member");
	}

	private void addData() {
		if(welcomeModel.getStats() != null) {
			welcomeModel.getStats().forEach(s -> {
				welcomeModel.getFamilyData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getFamily()));
				welcomeModel.getRegularData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getRegular()));
				welcomeModel.getSocialData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getSocial()));
				welcomeModel.getLakeAssociateData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getLakeAssociates()));
				welcomeModel.getLifeMemberData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getLifeMembers()));
			});
			setData();
		} else logger.warn("There are no stats available");

	}

	private void setData() {
		seriesFamily.setData(welcomeModel.getFamilyData());
		seriesRegular.setData(welcomeModel.getRegularData());
		seriesSocial.setData(welcomeModel.getSocialData());
		seriesLakeAssociate.setData(welcomeModel.getLakeAssociateData());
		seriesLifeMember.setData(welcomeModel.getLifeMemberData());
	}

	private void clearData() {
			welcomeModel.getFamilyData().clear();
			welcomeModel.getRegularData().clear();
			welcomeModel.getSocialData().clear();
			welcomeModel.getLakeAssociateData().clear();
			welcomeModel.getLifeMemberData().clear();
	}

	protected void refreshChart() {
		clearData();
		addData();
		setData();
	}
}
