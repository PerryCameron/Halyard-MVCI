package org.ecsail.mvci.main;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.scene.layout.Region;
import org.ecsail.exceptions.GlobalDataFetchException;
import org.ecsail.fx.DbRosterSettingsDTO;
import org.ecsail.fx.MembershipListDTO;
import org.ecsail.fileio.FileIO;
import org.ecsail.fx.SlipStructureDTO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.interfaces.Status;
import org.ecsail.mvci.membership.MembershipMessage;
import org.ecsail.static_tools.HalyardPaths;
import org.ecsail.widgetfx.PaneFx;
import org.ecsail.wrappers.DirectoryResponse;
import org.ecsail.wrappers.GlobalDataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MainInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(MainInteractor.class);
    private final MainModel mainModel;
    public MainInteractor(MainModel mainModel) {
        this.mainModel = mainModel;
        FileIO.checkPath(SCRIPTS_FOLDER);
        FileIO.checkPath(LOG_FOLDER);
    }

    public Region returnController(String tabName, MainController mainController) {
        return null;
    }

    public void setStatus(String status) {
        Platform.runLater(() -> mainModel.statusLabelProperty().set(status));
    }

    public void setComplete() {
        try  {
            Platform.runLater(() -> mainModel.returnMessageProperty().set(MainMessage.PRIMARY_STAGE_COMPLETE));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void printMembershipList(MembershipListDTO membershipListDTO) {
        logger.info(membershipListDTO.toString());
    }

    public MainModel getMainModel() {
        return mainModel;
    }

    public void setChangeStatus(Status.light status) {
        // changing this property causes a listener to react which changes the lights
        mainModel.setLightStatusProperty(status);
    }

    public boolean tabIsNotOpen(int msId) {  // find if tab is open
        if (PaneFx.tabIsOpen(msId, mainModel.getMainTabPane())) {
            Platform.runLater(() -> {
                mainModel.setMsId(msId);
                mainModel.setReturnMessage(MainMessage.SELECT_TAB);
            });
            return false;
        }
        return true;
    }

    public void logout() {
        try {
            mainModel.getHttpClient().logout();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void setConnectError(boolean b) {
        mainModel.setConnectError(b);
    }

    public GlobalDataResponse fetchGlobalData(String email) throws GlobalDataFetchException {
        try {
            // Construct endpoint with URL-encoded email
            String endpoint = "global-data?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);
            String jsonResponse = mainModel.getHttpClient().fetchDataFromGybe(endpoint);
            logger.debug("global-data response: {}", jsonResponse);
            // Deserialize JSON to GlobalDataResponse
            GlobalDataResponse globalDataResponse = mainModel.getHttpClient().getObjectMapper().readValue(
                    jsonResponse,
                    new TypeReference<>() {
                    }
            );
            if (globalDataResponse == null) {
                throw new GlobalDataFetchException("Received null response from server");
            }
            if (globalDataResponse.isSuccess()) {
                // Validate response fields
                if (globalDataResponse.getUser() == null || globalDataResponse.getBoardPositions() == null) {
                    throw new GlobalDataFetchException("Invalid response: user or board positions are null");
                }
                return globalDataResponse;
            } else {
                throw new GlobalDataFetchException("Failed to fetch global data: " + globalDataResponse.getMessage());
            }
        } catch (IOException e) {
            logger.error("Failed to fetch global data for email: {}", email, e);
            throw new GlobalDataFetchException("Failed to fetch global data for email: " + email, e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setGlobalData(GlobalDataResponse response) {
        if (response == null) {
            logger.error("Cannot set global data: response is null");
            return;
        }
        Platform.runLater(() -> {
            if (response.getUser() != null) {
                mainModel.setHalyardUser(response.getUser());
            } else {
                logger.warn("No user data in global data response");
            }
            if (response.getBoardPositions() != null) {
                mainModel.getBoardPositionDTOS().clear();
                mainModel.getBoardPositionDTOS().addAll(response.getBoardPositions());
                logger.debug("Updated board positions: {} items", response.getBoardPositions().size());
            } else {
                logger.warn("No board positions in global data response");
            }
        });
    }

    public void closeTabByMsId(int msId) {
        mainModel.deleteMembershipProperty().set(msId);
    }

    public ExecutorService getExecutorService() {
        return mainModel.getExecutor();
    }

    public MainMessage getDirectory() {
        try {
            String endpoint = "directory-rest";
            String jsonResponse = mainModel.getHttpClient().fetchDataFromGybe(endpoint);
            DirectoryResponse directoryResponse = mainModel.getHttpClient().getObjectMapper().readValue(
                    jsonResponse,
                    DirectoryResponse.class
            );

            // Check if the response is successful and has data
            if (directoryResponse.isSuccess() && directoryResponse.getDirectory() != null) {
                // Convert byte[] to PDF file
                byte[] pdfBytes = directoryResponse.getDirectory();
                String outputPath = HalyardPaths.ECSC_HOME + "/directory.pdf"; // Specify your desired file path
                File pdfFile = new File(outputPath);
                try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
                    fos.write(pdfBytes);
                    logger.info("PDF saved successfully to {}", outputPath);
                } catch (IOException e) {
                    return setFailMessage("Error saving PDF file: ",0, e.getMessage());
                }
                // open folder
                try {
                    if (Desktop.isDesktopSupported() && pdfFile.exists()) {
                        Desktop desktop = Desktop.getDesktop();
                        File parentDir = pdfFile.getParentFile(); // Get the directory containing the file
                        if (parentDir != null) {
                            desktop.open(parentDir); // Open the folder
                            logger.info("Opened folder: {}", parentDir.getAbsolutePath());
                        } else {
                            desktop.open(new File(".")); // Open current directory if no parent
                            logger.info("Opened current directory");
                        }
                    } else {
                        return setFailMessage("Error opening folder: ",0, "Desktop is not supported or file does not exist");
                    }
                } catch (IOException e) {
                    return setFailMessage("Error opening folder: ",0, e.getMessage());
                }
            } else {
                return setFailMessage("No PDF data found or request failed",0, directoryResponse.getMessage());
            }
        } catch (Exception e) {
            return setFailMessage("Error fetching directory: ",0, e.getMessage());
        }
        return MainMessage.SUCCESS;
    }

    public void setSuccess() {
        getMainModel().toggleRxSuccess();
    }

    public void setFailure() {
        getMainModel().toggleRxFail();
    }

    public String[] getFailMessage() {
        return mainModel.errorMessageProperty().get().split(":");
    }

    public MainMessage setFailMessage(String title, int id, String message) {
        mainModel.errorMessageProperty().set(title + ":" + id + ":" + message);
        return MainMessage.FAIL;
    }

    public void logError(String s) {
        logger.error(s);
    }
}
