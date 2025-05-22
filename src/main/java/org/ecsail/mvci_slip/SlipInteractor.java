package org.ecsail.mvci_slip;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.ecsail.fx.SlipInfoDTO;
import org.ecsail.fx.SlipStructureDTO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SlipInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(SlipInteractor.class);
    private final SlipModel slipModel;
//    private final SlipRepositoryImpl slipRepo;
//    private final MembershipRepositoryImpl memRepo;

    public SlipInteractor(SlipModel slipModel) {
        this.slipModel = slipModel;

    }
//    public SlipInteractor(SlipModel slipModel, Connections connections) {
//        this.slipModel = slipModel;
//        this.slipRepo = new SlipRepositoryImpl(connections.getDataSource());
//        this.memRepo = new MembershipRepositoryImpl(connections.getDataSource());
//    }

//    public void getSlipInfo() {
//        try {
//            logger.info("Getting slip info from data base");
//            ObservableList<SlipInfoDTO> updatedRoster
//                    = FXCollections.observableArrayList(slipRepo.getSlipInfo());
//            updateSlipInfo(updatedRoster);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void getSlipInfo() throws Exception {
        String endpoint = "slip/info";
        String jsonResponse = slipModel.getHttpClient().fetchDataFromGybe(endpoint);
        logger.debug("slip info response: {}", jsonResponse);
        List<SlipInfoDTO> slipInfo = slipModel.getHttpClient().getObjectMapper().readValue(
                jsonResponse,
                new TypeReference<>() {
                }
        );
        logger.info("Fetched {} positions", slipInfo.size());
        Platform.runLater(() -> {
            slipModel.getSlipInfoDTOS().addAll(slipInfo); // this is saying required type is MembershipListRadioDTO
            logger.info("Radio choices model updated with {} choices", slipModel.getSlipInfoDTOS().size());
        });
    }

    public void getSlipStructure() throws Exception {
        String endpoint = "slip/structure";
        String jsonResponse = slipModel.getHttpClient().fetchDataFromGybe(endpoint);
        logger.debug("slip info response: {}", jsonResponse);
        List<SlipStructureDTO> slipStructure = slipModel.getHttpClient().getObjectMapper().readValue(
                jsonResponse,
                new TypeReference<>() {
                }
        );
        logger.info("Fetched {} positions", slipStructure.size());
        Platform.runLater(() -> {
            slipModel.getSlipStructureDTOS().addAll(slipStructure); // this is saying required type is MembershipListRadioDTO
            logger.info("Radio choices model updated with {} choices", slipModel.getSlipStructureDTOS().size());
        });
    }

    private void updateSlipInfo(ObservableList<SlipInfoDTO> slipInfo) {
        Platform.runLater(() -> {
            logger.info("Adding slip Info to model");
            slipModel.getSlipInfoDTOS().setAll(slipInfo);
        });
    }

//    public void getSlipStructure() {
//        try {
//            logger.info("Getting slip info from data base");
//            ObservableList<SlipStructureDTO> slipStructureInfo
//                    = FXCollections.observableArrayList(slipRepo.getSlipStructure());
//            updateSlipStructureInfo(slipStructureInfo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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

//    public MembershipListDTO getMembershipList() {
//        return memRepo.getMembershipByMsId(slipModel.getSelectedMsId());
//    }
}
