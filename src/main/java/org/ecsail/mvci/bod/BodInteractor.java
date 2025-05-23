package org.ecsail.mvci.bod;

import org.ecsail.fx.MembershipListDTO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BodInteractor implements ConfigFilePaths {

    private static final Logger logger = LoggerFactory.getLogger(BodInteractor.class);
    private final BodModel BodModel;
//    private final SlipRepositoryImpl slipRepo;
//    private final MembershipRepositoryImpl memRepo;

//    public BodInteractor(BodModel BodModel, Connections connections) {
//        this.BodModel = BodModel;
//        this.slipRepo = new SlipRepositoryImpl(connections.getDataSource());
//        this.memRepo = new MembershipRepositoryImpl(connections.getDataSource());
//    }


    public BodInteractor(BodModel BodModel) {
        this.BodModel = BodModel;
    }

    public MembershipListDTO getMembershipList() {
        return null;
    }
}
