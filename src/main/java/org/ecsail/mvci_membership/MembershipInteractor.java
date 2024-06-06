package org.ecsail.mvci_membership;

import javafx.application.Platform;
import org.ecsail.connection.Connections;
import org.ecsail.interfaces.SlipUser;
import org.ecsail.pdf.PDF_Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MembershipInteractor implements SlipUser {
    private final MembershipModel membershipModel;
    private static final Logger logger = LoggerFactory.getLogger(MembershipInteractor.class);
    private final DataBaseService dataBaseService;

    public MembershipInteractor(MembershipModel membershipModel, Connections connections) {
        this.membershipModel = membershipModel;
        this.dataBaseService = new DataBaseService(connections.getDataSource(), membershipModel);
    }

    protected void setListsLoaded() {
        Platform.runLater(() -> {
            logger.info("Data for MembershipView is now loaded");
            membershipModel.setReturnMessage(MembershipMessage.DATA_LOAD_SUCCEED);
        });
    }

    public DataBaseService getDataBaseService() {
        return dataBaseService;
    }

    public void uploadMemberPhoto() {
        System.out.println("uploading photo");
    }

    public void loadInvoices() {
    }

    public void printEnvelope() {
            try {   /// probably should send membershipModel instead of membership ID
                new PDF_Envelope(true, membershipModel, dataBaseService);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
    }
}
