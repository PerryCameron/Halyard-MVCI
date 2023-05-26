package org.ecsail.mvci_main;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.ecsail.connection.Connections;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.interfaces.Status;
import org.ecsail.repository.implementations.BoardPositionsRepositoryImpl;
import org.ecsail.repository.interfaces.BoardPositionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Platform.runLater(() -> {
            mainModel.statusLabelProperty().set(status);
        });
    }

    public void printMembershipList(MembershipListDTO membershipListDTO) {
        logger.info(membershipListDTO.toString());
    }
    public void getLists() {
        mainModel.setBoardPositionDTOS(FXCollections.observableArrayList());
    }
    public void loadCommonLists(Connections connections) {
        BoardPositionsRepository boardPositionsRepo = new BoardPositionsRepositoryImpl(connections.getDataSource());
        mainModel.setBoardPositionDTOS(FXCollections.observableList(boardPositionsRepo.getPositions()));
    }
    public MainModel getMainModel() {
        return mainModel;
    }
    public void setComplete() {
        logger.info("primaryStage completed");
        Platform.runLater(() -> mainModel.setPrimaryStageComplete(true));
    }

    public void setChangeStatus(Status.light status) {
        // changing this property causes a listener to react which changes the lights
        mainModel.setLightStatusProperty(status);
    }

//    public void setChangeStatus(Status.light status) {
//        Platform.runLater(() -> {
//            switch (status) {
//                case RX_RED -> setBlink(Color.RED, "receive");
//                case TX_RED -> setBlink(Color.RED, "transmit");
//                case RX_GREEN -> setBlink(Color.GREEN, "receive");
//                case TX_GREEN -> setBlink(Color.GREEN, "transmit");
//            }
//        });
//    }


}
