package org.ecsail.mvci_new_membership;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.connection.Connections;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.SlipInfoDTO;
import org.ecsail.dto.SlipStructureDTO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.mvci_slip.SlipModel;
import org.ecsail.repository.implementations.MembershipRepositoryImpl;
import org.ecsail.repository.implementations.SlipRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewMembershipInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(NewMembershipInteractor.class);
    private final NewMembershipModel newMembershipModel;
    private final SlipRepositoryImpl slipRepo;
    private final MembershipRepositoryImpl memRepo;

    public NewMembershipInteractor(NewMembershipModel newMembershipModel, Connections connections) {
        this.newMembershipModel = newMembershipModel;
        this.slipRepo = new SlipRepositoryImpl(connections.getDataSource());
        this.memRepo = new MembershipRepositoryImpl(connections.getDataSource());
    }

    public MembershipListDTO getMembershipList() {
        return null;
    }
}
