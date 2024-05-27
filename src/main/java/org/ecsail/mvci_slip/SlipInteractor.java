package org.ecsail.mvci_slip;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.layout.Region;
import org.ecsail.connection.Connections;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.fileio.FileIO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.interfaces.Status;
import org.ecsail.mvci_main.MainController;
import org.ecsail.mvci_main.MainMessage;
import org.ecsail.mvci_main.MainModel;
import org.ecsail.repository.implementations.BoardPositionsRepositoryImpl;
import org.ecsail.repository.interfaces.BoardPositionsRepository;
import org.ecsail.widgetfx.PaneFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlipInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(SlipInteractor.class);
    private final SlipModel slipModel;
    public SlipInteractor(SlipModel slipModel) {
        this.slipModel = slipModel;
    }

}
