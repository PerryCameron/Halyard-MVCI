package org.ecsail.mvci_bod;

import org.ecsail.connection.Connections;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.repository.implementations.MembershipRepositoryImpl;
import org.ecsail.repository.implementations.SlipRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BodInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(BodInteractor.class);
    private final BodModel BodModel;
    private final SlipRepositoryImpl slipRepo;
    private final MembershipRepositoryImpl memRepo;

    public BodInteractor(BodModel BodModel, Connections connections) {
        this.BodModel = BodModel;
        this.slipRepo = new SlipRepositoryImpl(connections.getDataSource());
        this.memRepo = new MembershipRepositoryImpl(connections.getDataSource());
    }

    public MembershipListDTO getMembershipList() {
        return null;
    }
}
