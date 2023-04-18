package org.ecsail.mvci_roster;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class RosterController extends Controller {
    MainController mainController;
    RosterInteractor rosterInteractor;
    RosterView rosterView;

    public RosterController(MainController mc) {
        mainController = mc;
        RosterModel rosterModel = new RosterModel();
        rosterInteractor = new RosterInteractor(rosterModel,mainController.getConnections());
        rosterView = new RosterView(rosterModel, this::changeState, this::search, this::exportRoster, this::launchTab);
        getRosterData();
    }

    private void launchTab(MembershipListDTO membershipListDTO) {
        mainController.openMembershipMVCI(membershipListDTO);
    }

    private void exportRoster() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                rosterInteractor.exportRoster();
                return null;
            }
        };
        new Thread(task).start();
    }

    private void search() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                rosterInteractor.fillTableView();
                return null;
            }
        };
        new Thread(task).start();
    }

    private void changeState() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                rosterInteractor.changeListState();
                return null;
            }
        };
//        task.setOnSucceeded(e -> rosterInteractor.setRosterToTableview());
        new Thread(task).start();
    }

    private void getRosterData() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                rosterInteractor.getSelectedRoster();
                rosterInteractor.getRadioChoices();
                rosterInteractor.getRosterSettings();
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            rosterInteractor.setListsLoaded(true);
            rosterInteractor.setRosterToTableview();
            rosterView.setRadioListener(); // set last, so it doesn't fire, when radios are created.
        });
        new Thread(task).start();
    }

    @Override
    public Region getView() { return rosterView.build(); }
}
