package org.ecsail.static_tools;

import org.ecsail.mvci_main.MainModel;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.function.Supplier;

public class HandlingTools {
    public static void queryForList(Runnable task, MainModel model, Logger logger) {
        try {
            task.run();
            retrievedFromIndicator(true, model);
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            retrievedFromIndicator(false, model);
            logger.error("DataAccessException: " + dae);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            retrievedFromIndicator(false, model);
            logger.error("NullPointerException: " + npe);
        } catch (Exception e) {
            e.printStackTrace();
            retrievedFromIndicator(false, model);
            logger.error("Exception: " + e);
        }
    }

    public static boolean executeQuery(Supplier<Integer> operation, MainModel model, Logger logger) {
        try {
            int rowsUpdated = operation.get();
            savedToIndicator(rowsUpdated == 1, model);
            return true;
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            savedToIndicator(false, model);
            logger.error("An exception occurred", dae);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            savedToIndicator(false, model);
            logger.error("An exception occurred", npe);
        } catch (Exception e) {
            e.printStackTrace();
            savedToIndicator(false, model);
            logger.error("An exception occurred", e);
        }
        return false;
    }

    public static boolean executeExistsQuery(Supplier<Boolean> operation, MainModel model, Logger logger) {
        try {
            boolean operationSuccess = operation.get();
            savedToIndicator(operationSuccess, model);
            return operationSuccess;
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            savedToIndicator(false, model);
            logger.error("An exception occurred", dae);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            savedToIndicator(false, model);
            logger.error("An exception occurred", npe);
        } catch (Exception e) {
            e.printStackTrace();
            savedToIndicator(false, model);
            logger.error("An exception occurred", e);
        }
        return false;
    }

    public static boolean executeBatchQuery(Supplier<int[]> operation, MainModel model, Logger logger) {
        try {
            int[] rowsUpdated = operation.get();
            savedToIndicator(Arrays.stream(rowsUpdated).allMatch(value -> value == 1), model);
            logger.info("Successfully updated " + rowsUpdated.length + " rows");
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            savedToIndicator(false, model);
            logger.error("An exception occurred", e);
        } catch (NullPointerException e) {
            e.printStackTrace();
            savedToIndicator(false, model);
            logger.error("An exception occurred", e);
        } catch (Exception e) {
            e.printStackTrace();
            savedToIndicator(false, model);
            logger.error("An exception occurred", e);
        }
        return false;
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
