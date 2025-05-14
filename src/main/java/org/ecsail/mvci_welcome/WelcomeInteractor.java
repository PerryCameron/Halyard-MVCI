package org.ecsail.mvci_welcome;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import okhttp3.Response;
import org.ecsail.dto.StatsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

public class WelcomeInteractor {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeInteractor.class);
    private WelcomeModel welcomeModel;

public WelcomeInteractor(WelcomeModel welcomeModel) {
    this.welcomeModel = welcomeModel;

}

    public void fetchStatistics() throws Exception {
        int startYear = welcomeModel.getDefaultStartYear();
        int endYear = startYear + welcomeModel.getYearSpan();
        String endpoint = "statistics?startYear=" + startYear + "&stopYear=" + endYear;

        String jsonResponse = fetchDataFromHalyard(endpoint);
        List<StatsDTO> statsDTOS = welcomeModel.getObjectMapper().readValue(
                jsonResponse,
                new TypeReference<>() {
                }
        );
        Platform.runLater(() -> welcomeModel.setStats(new ArrayList<>(statsDTOS)));
    }

    private String fetchDataFromHalyard(String endpoint) throws Exception {
        try (Response response = welcomeModel.getHttpClient().makeRequest("halyard/" + endpoint)) {
            logger.info("Fetching data from /halyard/{}: Status {}", endpoint, response.code());
            if (response.code() == 403) {
                throw new AccessDeniedException("Access Denied: You don’t have the required permissions to access this resource.");
            } else if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new Exception("Failed to fetch data: " + response.code());
            }
        } catch (IOException e) {
            throw new Exception("Failed to fetch data: " + e.getMessage());
        }
    }

//    protected void setStatistics() {
//        int endYear = welcomeModel.getDefaultStartYear() + welcomeModel.getYearSpan();
//        HandlingTools.queryForList(() -> {
//        ArrayList<StatsDTO> statsDTOS = (ArrayList<StatsDTO>) statRepository.getStatistics(welcomeModel.getDefaultStartYear(), endYear);
//        Platform.runLater(() -> welcomeModel.setStats(statsDTOS));
//        }, welcomeModel.getMainModel(), logger);
//    }

//    protected void reloadStats() {
//        logger.info("Reloading stats...");
//        HandlingTools.queryForList(() -> {
//            int endYear = welcomeModel.getDefaultStartYear() + welcomeModel.getYearSpan();
//            if (endYear > welcomeModel.getSelectedYear()) endYear = welcomeModel.getSelectedYear();
//            ArrayList<StatsDTO> statsDTOS = (ArrayList<StatsDTO>) statRepository.getStatistics(welcomeModel.getDefaultStartYear(), endYear);
//            Platform.runLater(() -> {
//                welcomeModel.getStats().clear();
//                welcomeModel.getStats().addAll(statsDTOS);
//                welcomeModel.setRefreshCharts(!welcomeModel.isRefreshCharts());
//            });
//        }, welcomeModel.getMainModel(), logger);
//    }
//
//    protected void updateProgressBar() {
//        statRepository.deleteAllStats();
//        final double numberOfYears = Year.now().getValue() - welcomeModel.getStartYear() + 1;
//        final double increment = 100 / numberOfYears;
//        for (int i = 0; i < numberOfYears; i++) {
//            StatsDTO statsDTO = statRepository.createStatDTO(welcomeModel.getStartYear());
//            statRepository.addStatRecord(statsDTO);
//            welcomeModel.incrementStartYear();
//            double currentProgress = welcomeModel.getProgress();
//            double nextProgress = currentProgress + increment;
//            welcomeModel.setProgress(nextProgress);
//        }
//        welcomeModel.setStartYear(1970);
//    }

//    protected void setStatUpdateSucceeded() {
//        reloadStats();
//        welcomeModel.getMembershipBarChart().refreshChart();
//        welcomeModel.getMembershipStackedBarChart().refreshChart();
//        welcomeModel.setDataBaseStatisticsRefreshed(true);
//        logger.info("Finished updating Statistics");
//    }

    protected void setStatSucceeded() {
        welcomeModel.getMembershipBarChart().refreshChart();
        welcomeModel.getMembershipStackedBarChart().refreshChart();
        logger.info("Statistics have been loaded");
    }

    public void taskOnFailed(WorkerStateEvent e) {
        logger.error(e.toString());
    }

    public String getTab() {
        return welcomeModel.getTabName();
    }
}
