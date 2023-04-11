package org.ecsail.mvci_roster;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ecsail.connection.Connections;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.dto.MembershipListRadioDTO;
import org.ecsail.mvci_connect.ConnectInteractor;
import org.ecsail.repository.implementations.MembershipRepositoryImpl;
import org.ecsail.repository.implementations.SettingsRepositoryImpl;
import org.ecsail.repository.interfaces.MembershipRepository;
import org.ecsail.repository.interfaces.SettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;

public class RosterInteractor {

    private static final Logger logger = LoggerFactory.getLogger(RosterInteractor.class);
    private RosterModel rosterModel;
    private MembershipRepository membershipRepo;
    private SettingsRepository settingsRepo;

    public RosterInteractor(RosterModel rm, Connections connections) {
        rosterModel = rm;
        membershipRepo = new MembershipRepositoryImpl(connections.getDataSource());
        settingsRepo = new SettingsRepositoryImpl(connections.getDataSource());
    }

    public void getSelectedRoster() {
        try {
            rosterModel.setRosters(FXCollections.observableArrayList(membershipRepo.getAllRoster(rosterModel.getSelectedYear())));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    public void setRosterToTableview() {
        rosterModel.getRosterTableView().setItems(rosterModel.getRosters());
        rosterModel.getRosters().sort(Comparator.comparing(MembershipListDTO::getMembershipId));
        updateRecords(rosterModel.getRosters().size());
    }

    private void updateRecords(int number) { rosterModel.setNumberOfRecords(String.valueOf(number)); }

    public void changeYear() {
        System.out.println(rosterModel.getSelectedYear());
    }

    public void getRadioChoices() {
        try {
        rosterModel.getRadioChoices().addAll(FXCollections.observableArrayList(settingsRepo.getRadioChoices()));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        System.out.println("Radio list added here");
    }

    public void getRadioChoicesSize() {
        System.out.println("Radio choices size=" + rosterModel.getRadioChoices().size());
    }
}
