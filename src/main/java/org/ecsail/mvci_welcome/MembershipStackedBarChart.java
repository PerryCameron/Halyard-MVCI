package org.ecsail.mvci_welcome;

import javafx.collections.FXCollections;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import org.ecsail.dto.StatsDTO;

import java.util.Arrays;

public class MembershipStackedBarChart extends StackedBarChart<String,Number> {

	private final WelcomeModel welcomeModel;
	Series<String,Number> seriesFamily = new Series<>();
	Series<String,Number> seriesRegular = new Series<>();
	Series<String,Number> seriesSocial = new Series<>();
	Series<String,Number> seriesLakeAssociate = new Series<>();
	Series<String,Number> seriesLifeMember = new Series<>();

	public MembershipStackedBarChart(WelcomeModel welcomeModel) {
		super(new CategoryAxis(),new NumberAxis());
		this.welcomeModel = welcomeModel;
	        setTitle("Active Memberships By Year");
			getXAxis().setAutoRanging(true);
			setNames();
			addData();
			setAnimated(false);
		getData().addAll(Arrays.asList(seriesFamily,seriesRegular,seriesSocial,seriesLakeAssociate,seriesLifeMember));
	}

	public void setNames() {
		seriesFamily.setName("Family");
		seriesRegular.setName("Regular");
		seriesSocial.setName("Social");
		seriesLakeAssociate.setName("Lake Associate");
		seriesLifeMember.setName("Life Member");
	}

	public void addData() {
		for (StatsDTO s: welcomeModel.getStats()) {
			welcomeModel.getFamilyData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getFamily()));
			welcomeModel.getRegularData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getRegular()));
			welcomeModel.getSocialData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getSocial()));
			welcomeModel.getLakeAssociateData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getLakeAssociates()));
			welcomeModel.getLifeMemberData().add(new Data<>(String.valueOf(s.getFiscalYear()), s.getLifeMembers()));
		}
		setData();
	}

	private void setData() {
		seriesFamily.setData(welcomeModel.getFamilyData());
		seriesRegular.setData(welcomeModel.getRegularData());
		seriesSocial.setData(welcomeModel.getSocialData());
		seriesLakeAssociate.setData(welcomeModel.getLakeAssociateData());
		seriesLifeMember.setData(welcomeModel.getLifeMemberData());
	}

	public void clearData() {
			welcomeModel.getFamilyData().clear();
			welcomeModel.getRegularData().clear();
			welcomeModel.getSocialData().clear();
			welcomeModel.getLakeAssociateData().clear();
			welcomeModel.getLifeMemberData().clear();
	}

	public void refreshChart() {
		clearData();
		addData();
		setData(FXCollections.observableArrayList(seriesFamily,seriesRegular,seriesSocial,seriesLakeAssociate,seriesLifeMember));
	}
}
