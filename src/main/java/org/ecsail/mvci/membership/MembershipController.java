package org.ecsail.mvci.membership;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.fx.RosterFx;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci.main.MainController;
import org.ecsail.mvci.membership.mvci.person.PersonMessage;
import org.ecsail.widgetfx.DialogueFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import static org.ecsail.mvci.membership.MembershipMessage.*;

public class MembershipController extends Controller<MembershipMessage> {
    private static final Logger logger = LoggerFactory.getLogger(MembershipController.class);
    MembershipInteractor membershipInteractor;
    MembershipView membershipView;
    MainController mainController;

    public MembershipController(MainController mc, RosterFx rosterDTOFxDTO, int selectedYear) {
        this.mainController = mc;
        MembershipModel membershipModel = new MembershipModel(rosterDTOFxDTO, selectedYear, mainController.getMainModel());
        this.membershipInteractor = new MembershipInteractor(membershipModel);
        action(GET_DATA);
        setExecutorService();
        membershipView = new MembershipView(membershipModel, this::action);
    }

    @Override
    public void action(MembershipMessage type) {
        switch (type) {
            case GET_DATA -> runSpinner(membershipInteractor::getMembership, 50, 50, true);
            case UPDATE_BOAT -> runTask(membershipInteractor::updateBoat);
            case UPDATE_NOTE -> runTask(membershipInteractor::updateNotes);
            case INSERT_BOAT -> runTask(membershipInteractor::insertBoat);
            case DELETE_MEMBERSHIP -> runTask(membershipInteractor::deleteMembership);
            case FAIL -> membershipInteractor.signalFail();
            case SUCCESS -> membershipInteractor.signalSuccess();
            case PRINT_ENVELOPE -> runTask(membershipInteractor::printEnvelope);
        }
    }

    private void runTask(Supplier<MembershipMessage> method) {
        Task<MembershipMessage> task = new Task<>() {
            @Override
            protected MembershipMessage call() {
                return method.get();
            }
        };
        task.setOnSucceeded(e -> {
            if (task.getValue() == SUCCESS)
                membershipInteractor.signalSuccess();
            else logFailure();
        });
        task.setOnFailed(e -> {
            logFailure();
        });
        getExecutorService().submit(task);
    }

    private void runSpinner(Supplier<MembershipMessage> method, double x, double y, boolean runSpinner) {
        mainController.setSpinnerOffset(x, y);
        if (runSpinner) mainController.showLoadingSpinner(true);
        Task<MembershipMessage> task = new Task<>() {
            @Override
            protected MembershipMessage call() {
                return method.get();
            }
        };
        task.setOnSucceeded(e -> {
            if (task.getValue() != null)
                switch (task.getValue()) {
                    case DELETE_MEMBERSHIP_FROM_DATABASE_SUCCEED -> {
                        membershipInteractor.removeMembershipFromList(mainController.getRosterController().getRosterTableView());
                        mainController.closeTabByMsId(membershipInteractor.getMsId());
                    }
                    case GET_DATA_SUCCESS -> membershipInteractor.setDataLoaded();
                    case FAIL -> membershipInteractor.signalFail();
                    case SUCCESS -> membershipInteractor.signalSuccess();
                    default -> {
                    }
                }
            if (runSpinner) mainController.showLoadingSpinner(false);
            else
                logger.warn("task was null");
        });
        task.setOnFailed(e -> {
            logger.error("Failed to perform task");
            if (runSpinner) mainController.showLoadingSpinner(false);
        });
        getExecutorService().submit(task);
    }

    public ExecutorService getExecutorService() {
        return mainController.getExecutorService();
    }

    public void setExecutorService() {
        membershipInteractor.setExecutorService(mainController.getExecutorService());
    }

    private void logFailure() {
        String[] message = membershipInteractor.getFailMessage();
        if (message[1].equals("0")) {
            membershipInteractor.logError(message[0] + ": " + message[2]);
        } else {
            membershipInteractor.logError(message[0] + " with ID: " + message[1] + message[2]);
        }
        DialogueFx.errorAlert(message[0], message[2]);
        membershipView.sendMessage().accept(MembershipMessage.FAIL);
    }

    @Override
    public Region getView() {
        return membershipView.build();
    }
}
