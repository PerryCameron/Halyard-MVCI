package org.ecsail.mvci_deposit;

import org.ecsail.connection.Connections;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.repository.implementations.MembershipRepositoryImpl;
import org.ecsail.repository.implementations.SlipRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DepositInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(DepositInteractor.class);
    private final SlipRepositoryImpl slipRepo;
    private final MembershipRepositoryImpl memRepo;
    private final DepositModel depositModel;

    public DepositInteractor(DepositModel depositModel, Connections connections) {
        this.depositModel = depositModel;
        this.slipRepo = new SlipRepositoryImpl(connections.getDataSource());
        this.memRepo = new MembershipRepositoryImpl(connections.getDataSource());
    }

    public MembershipListDTO getMembershipList() {
        return null;
    }
}
