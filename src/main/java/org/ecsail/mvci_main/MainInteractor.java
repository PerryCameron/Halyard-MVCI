package org.ecsail.mvci_main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import org.ecsail.connection.Connections;
import org.ecsail.dto.BoardPositionDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.mvci_connect.ConnectController;
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
}
