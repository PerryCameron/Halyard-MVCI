package org.ecsail.mvci_roster;

import javafx.collections.FXCollections;
import org.ecsail.connection.Connections;
import org.ecsail.repository.implementations.MembershipRepositoryImpl;
import org.ecsail.repository.interfaces.MembershipRepository;

public class RosterInteractor {

    private RosterModel rosterModel;
    private MembershipRepository membershipRepo;

    public RosterInteractor(RosterModel rm, Connections connections) {
        rosterModel = rm;
        membershipRepo = new MembershipRepositoryImpl(connections.getDataSource());
    }

    public void getSelectedRoster() {
        rosterModel.setRosters(FXCollections.observableArrayList(membershipRepo.getAllRoster(rosterModel.getSelectedYear())));
    }
    public void setRosterToTableview() {
        rosterModel.getRosterTableView().setItems(rosterModel.getRosters());
    }
}
