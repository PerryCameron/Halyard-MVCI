package org.ecsail.mvci_loading;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadingInteractor {
    private static final Logger logger = LoggerFactory.getLogger(LoadingInteractor.class);
    private final LoadingModel loadingModel;

    public LoadingInteractor(LoadingModel loadingModel) {
        this.loadingModel = loadingModel;
    }

    public void showLoadSpinner(boolean show) {
        Platform.runLater(() -> {
            logger.info("Loading Spinner set: " + show);
            loadingModel.setShow(show); });
    }

    public void logInfo(String log) {
        logger.info(log);
    }
}
