package org.ecsail.mvci_main;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.scene.layout.Region;
import org.ecsail.fx.BoardPositionDTO;
import org.ecsail.fx.MembershipListDTO;
import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.interfaces.Status;
import org.ecsail.pojo.HalyardUser;
import org.ecsail.widgetfx.PaneFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
//
//    public void getLists() {
//        mainModel.setBoardPositionDTOS(FXCollections.observableArrayList());
//    }


//    public void loadCommonLists(Connections connections) {
//        BoardPositionsRepository boardPositionsRepo = new BoardPositionsRepositoryImpl(connections.getDataSource());
//        mainModel.setBoardPositionDTOS(FXCollections.observableList(boardPositionsRepo.getPositions()));
//    }

    public MainModel getMainModel() {
        return mainModel;
    }

//    public void setComplete() {
//        logger.info("primaryStage completed");
//        Platform.runLater(() -> mainModel.setReturnMessage(MainMessage.PRIMARY_STAGE_COMPLETE));
//    }

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

    public void fetchPositions() throws Exception {
        String endpoint = "positions";
        String jsonResponse = mainModel.getHttpClient().fetchDataFromGybe(endpoint);
        logger.debug("positions response: {}", jsonResponse);
        List<BoardPositionDTO> choices = mainModel.getHttpClient().getObjectMapper().readValue(
                jsonResponse,
                new TypeReference<>() {
                }
        );
        logger.info("Fetched {} positions", choices.size());
        Platform.runLater(() -> {
            mainModel.getBoardPositionDTOS().addAll(choices); // this is saying required type is MembershipListRadioDTO
            logger.info("Radio choices model updated with {} choices", mainModel.getBoardPositionDTOS().size());
        });
    }

    // fetches user information from database that correspond to the userName (email) of the person who logged in.
    public void fetchHalyardUser(String email) throws Exception {
        try {
            // Construct endpoint with URL-encoded email
            String endpoint = "halyard-user?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);
            String jsonResponse = mainModel.getHttpClient().fetchDataFromGybe(endpoint);
            logger.debug("halyard-user response: {}", jsonResponse);
            // Deserialize JSON to HalyardUserDTO
            HalyardUser halyardUser = mainModel.getHttpClient().getObjectMapper().readValue(
                    jsonResponse,
                    new TypeReference<>() {}
            );
            // Update mainModel on JavaFX Application Thread
            Platform.runLater(() -> {
                mainModel.setHalyardUser(halyardUser);
                logger.info("User: {}", mainModel.getHalyardUser().getFullName());
            });
        } catch (Exception e) {
            logger.error("Failed to fetch user for email: {}", email, e);
            throw new Exception("Failed to fetch user for email: " + email, e);
        }
    }
}
