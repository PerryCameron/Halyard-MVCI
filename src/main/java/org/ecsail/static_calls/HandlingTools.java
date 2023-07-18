package org.ecsail.static_calls;

import org.ecsail.mvci_main.MainModel;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.slf4j.Logger;
import java.util.function.Supplier;

public class HandlingTools {
    public static void queryForList(Runnable task, MainModel model, Logger logger) {
        try {
            task.run();
            retrievedFromIndicator(true, model);
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            retrievedFromIndicator(false, model);
            logger.error("DataAccessException: " + dae.getMessage());
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            retrievedFromIndicator(false, model);
            logger.error("NullPointerException: " + npe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            retrievedFromIndicator(false, model);
            logger.error("Exception: " + e.getMessage());
        }
    }

    public static void executeQuery(Supplier<Integer> operation, MainModel model, Logger logger) {
        try {
            int rowsUpdated = operation.get();
            savedToIndicator(rowsUpdated == 1, model);
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            savedToIndicator(false, model);
            logger.error(dae.getMessage());
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            savedToIndicator(false, model);
            logger.error(npe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            savedToIndicator(false, model);
            logger.error(e.getMessage());
        }
    }

    public static void savedToIndicator(boolean returnOk, MainModel model) { // updates status lights
        if(returnOk) model.getLightAnimationMap().get("receiveSuccess").playFromStart();
        else model.getLightAnimationMap().get("receiveError").playFromStart();
    }

    public static void retrievedFromIndicator(boolean returnOk, MainModel model) { // updates status lights
        if(returnOk) model.getLightAnimationMap().get("transmitSuccess").playFromStart();
        else model.getLightAnimationMap().get("transmitError").playFromStart();
    }
}
