package org.ecsail.mvci_slip;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import org.ecsail.connection.Connections;
import org.ecsail.dto.DockPlacementDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.SlipInfoDTO;
import org.ecsail.dto.SlipStructureDTO;
import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.interfaces.Status;
import org.ecsail.mvci_main.MainController;
import org.ecsail.mvci_main.MainMessage;
import org.ecsail.mvci_main.MainModel;
import org.ecsail.repository.implementations.BoardPositionsRepositoryImpl;
import org.ecsail.repository.implementations.SlipRepositoryImpl;
import org.ecsail.repository.interfaces.BoardPositionsRepository;
import org.ecsail.widgetfx.PaneFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SlipInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(SlipInteractor.class);
    private final SlipModel slipModel;
    private final SlipRepositoryImpl slipRepo;

    public SlipInteractor(SlipModel slipModel, Connections connections) {
        this.slipModel = slipModel;
        this.slipRepo = new SlipRepositoryImpl(connections.getDataSource());
    }

//    protected void getPlacement() {
//        Platform.runLater(() -> {
//        slipModel.getDockPlacement().add(new DockPlacementDTO(1,"A", 140, 40));
//        slipModel.getDockPlacement().add(new DockPlacementDTO(2,"B", 140, 40));
//        slipModel.getDockPlacement().add(new DockPlacementDTO(3,"C", 140, 40));
//        slipModel.getDockPlacement().add(new DockPlacementDTO(4,"D", 140, 40));
//        slipModel.getDockPlacement().add(new DockPlacementDTO(5,"F", 140, 40));
//        });
//    }

    public void getSlipInfo() {
        try {
            logger.info("Getting slip info from data base");
            ObservableList<SlipInfoDTO> updatedRoster
                    = FXCollections.observableArrayList(slipRepo.getSlipInfo());
            updateSlipInfo(updatedRoster);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSlipInfo(ObservableList<SlipInfoDTO> slipInfo) {
        Platform.runLater(() -> {
            logger.info("Adding slip Info to model");
            slipModel.getSlipInfoDTOS().setAll(slipInfo);
        });
    }

    public void getSlipStructure() {
        try {
            logger.info("Getting slip info from data base");
            ObservableList<SlipStructureDTO> slipStructureInfo
                    = FXCollections.observableArrayList(slipRepo.getSlipStructure());
            updateSlipStructureInfo(slipStructureInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSlipStructureInfo(ObservableList<SlipStructureDTO> slipStructureInfo) {
        Platform.runLater(() -> {
            logger.info("Adding slip structure info to model");
            slipModel.getSlipStructureDTOS().setAll(slipStructureInfo);
        });
    }

    public void setListsLoaded() {
        System.out.println("slipStructure: " + slipModel.getSlipStructureDTOS().size());
        System.out.println("slipInfo: " + slipModel.getSlipInfoDTOS().size());
        slipModel.setListsLoaded(true);
    }
}
