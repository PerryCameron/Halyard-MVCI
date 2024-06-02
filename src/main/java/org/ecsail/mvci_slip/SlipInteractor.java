package org.ecsail.mvci_slip;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.connection.Connections;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.SlipInfoDTO;
import org.ecsail.dto.SlipStructureDTO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.repository.implementations.MembershipRepositoryImpl;
import org.ecsail.repository.implementations.SlipRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlipInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(SlipInteractor.class);
    private final SlipModel slipModel;
    private final SlipRepositoryImpl slipRepo;
    private final MembershipRepositoryImpl memRepo;

    public SlipInteractor(SlipModel slipModel, Connections connections) {
        this.slipModel = slipModel;
        this.slipRepo = new SlipRepositoryImpl(connections.getDataSource());
        this.memRepo = new MembershipRepositoryImpl(connections.getDataSource());
    }

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

    public MembershipListDTO getMembershipList() {
        return memRepo.getMembershipByMsId(slipModel.getSelectedMsId());
    }
}
