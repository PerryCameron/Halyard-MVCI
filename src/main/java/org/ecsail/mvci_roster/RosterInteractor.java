package org.ecsail.mvci_roster;

import javafx.collections.FXCollections;
import org.ecsail.connection.Connections;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.repository.implementations.MembershipRepositoryImpl;
import org.ecsail.repository.implementations.SettingsRepositoryImpl;
import org.ecsail.repository.interfaces.MembershipRepository;
import org.ecsail.repository.interfaces.SettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

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

    public void changeState() {
        rosterModel.getRosters().clear();
        Method method;
        try {
            method = membershipRepo.getClass().getMethod(rosterModel.getSelectedRadioBox().getMethod(),Integer.class);
            rosterModel.getRosters().setAll((List<MembershipListDTO>) method.invoke(membershipRepo, rosterModel.getSelectedYear()));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if(!parent.textField.getText().equals("")) fillTableView(parent.textField.getText());
        updateRecords(rosterModel.getRosters().size());
        rosterModel.getRosters().sort(Comparator.comparing(MembershipListDTO::getMembershipId));
    }

    public void getRadioChoices() {
        try {
        rosterModel.getRadioChoices().addAll(FXCollections.observableArrayList(settingsRepo.getRadioChoices()));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void getRadioChoicesSize() {
        System.out.println("Radio choices size=" + rosterModel.getRadioChoices().size());
    }
}
