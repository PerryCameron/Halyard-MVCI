package org.ecsail.mvci_slip;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.layout.Region;
import org.ecsail.connection.Connections;
import org.ecsail.dto.DockPlacementDTO;
import org.ecsail.dto.MembershipListDTO;
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

public class SlipInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(SlipInteractor.class);
    private final SlipModel slipModel;
    private final SlipRepositoryImpl slipRepo;

    public SlipInteractor(SlipModel slipModel, Connections connections) {
        this.slipModel = slipModel;
        this.slipRepo = new SlipRepositoryImpl(connections.getDataSource());
    }

    protected void getPlacement() {
        slipModel.getDockPlacement().add(new DockPlacementDTO(1,"A", 140, 40));
        slipModel.getDockPlacement().add(new DockPlacementDTO(2,"B", 140, 40));
        slipModel.getDockPlacement().add(new DockPlacementDTO(3,"C", 140, 40));
        slipModel.getDockPlacement().add(new DockPlacementDTO(4,"D", 140, 40));
        slipModel.getDockPlacement().add(new DockPlacementDTO(5,"F", 140, 40));
    }

    public void getSlipInfo() {
    }

    public void getSlipStructure() {
    }

    public void setListsLoaded() {
    }
}
