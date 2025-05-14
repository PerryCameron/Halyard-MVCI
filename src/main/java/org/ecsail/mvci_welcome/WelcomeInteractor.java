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

import static org.ecsail.interfaces.ChartConstants.NEW_MEMBER;

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
        logger.debug("Statistics response: {}", jsonResponse); // Log the raw JSON response
        List<StatsDTO> statsDTOS = welcomeModel.getObjectMapper().readValue(
                jsonResponse,
                new TypeReference<>() {
                }
        );
        logger.info("Fetched {} statistics records", statsDTOS.size()); // Log the number of records
        Platform.runLater(() -> {
            welcomeModel.setStats(new ArrayList<>(statsDTOS));
            logger.info("Statistics model updated with {} records", welcomeModel.getStats().size());
        });
    }

    private String fetchDataFromHalyard(String endpoint) throws Exception {
        try (Response response = welcomeModel.getHttpClient().makeRequest("halyard/" + endpoint)) {
            logger.info("Fetching data from /halyard/{}: Status {}", endpoint, response.code());
            String contentType = response.header("Content-Type", "");
            if (contentType.contains("text/html")) {
                logger.warn("Received HTML response, likely a redirect to login page. Session may be invalid.");
                throw new Exception("Session invalid: Server redirected to login page. Please log in again.");
            }
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


    public void reloadStats() {
        logger.info("Reloading stats...");
        try {
            fetchStatistics();
        } catch (Exception e) {
            logger.error("Failed to reload stats", e);
        }
        Platform.runLater(() -> {
            welcomeModel.setRefreshCharts(!welcomeModel.isRefreshCharts());
        });
    }

    public void triggerInitialChartUpdate() {
        welcomeModel.getMembershipBarChart().changeData(NEW_MEMBER);
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

    public WelcomeModel getWelcomeModel() {
        return welcomeModel;
    }

    public void logout() {
        try {
            logger.warn("Logging out and clearing session from " + welcomeModel.getHttpClient().getServerUrl());
            welcomeModel.getHttpClient().logout();
        } catch (IOException ex) {
            logger.warn("Failed to clear session during redirect to login", ex);
        }
    }

}
