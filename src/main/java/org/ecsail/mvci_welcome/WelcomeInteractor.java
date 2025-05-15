package org.ecsail.mvci_welcome;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import okhttp3.Response;
import org.ecsail.dto.StatsDTO;
import org.ecsail.widgetfx.DialogueFx;
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

        String jsonResponse = welcomeModel.getHttpClient().fetchDataFromHalyard(endpoint);
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

    protected String recompileStatsAndFetchData() throws Exception {
        int endYear = welcomeModel.getDefaultStartYear() + welcomeModel.getYearSpan();
        String endpoint = "refresh-statistics?startYear=" + welcomeModel.getDefaultStartYear() + "&stopYear=" + endYear;
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
            logger.warn("Logging out and clearing session from {}", welcomeModel.getHttpClient().getServerUrl());
            welcomeModel.getHttpClient().logout();
        } catch (IOException ex) {
            logger.warn("Failed to clear session during redirect to login", ex);
        }
    }

    public void setStatUpdateSucceeded(String jsonResponse) throws JsonProcessingException {
        logger.debug("Statistics response: {}", jsonResponse); // Log the raw JSON response
        List<StatsDTO> statsDTOS = welcomeModel.getObjectMapper().readValue(
                jsonResponse,
                new TypeReference<>() {
                }
        );
        logger.info("Fetched {} updated statistics records", statsDTOS.size()); // Log the number of records
        if(statsDTOS.size() > 0) DialogueFx.infoAlert("Success!", "The statistics have been successfully updated." );
        Platform.runLater(() -> {
            welcomeModel.setStats(new ArrayList<>(statsDTOS));
            logger.info("Statistics model updated with {} updated records", welcomeModel.getStats().size());
        });
    }
}
