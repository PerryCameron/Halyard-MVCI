package org.ecsail.mvci_main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.layout.Region;
import org.ecsail.connection.Connections;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.interfaces.Status;
import org.ecsail.repository.implementations.BoardPositionsRepositoryImpl;
import org.ecsail.repository.interfaces.BoardPositionsRepository;
import org.ecsail.widgetfx.PaneFx;
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
        Platform.runLater(() -> mainModel.setReturnMessage(MainMessage.PRIMARY_STAGE_COMPLETE));
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
