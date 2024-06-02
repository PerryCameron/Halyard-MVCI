package org.ecsail.mvci_new_membership;


import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;
import org.ecsail.mvci_slip.SlipMessage;

public class NewMembershipController extends Controller<NewMembershipMessage> {
    private final MainController mainController;
    private final NewMembershipInteractor newMembershipInteractor;
    private final NewMembershipView newMembershipView;

    public NewMembershipController(MainController mc) {
        mainController = mc;
        NewMembershipModel newMembershipModel = new NewMembershipModel(mainController.getMainModel());
        newMembershipInteractor = new NewMembershipInteractor(newMembershipModel, mainController.getConnections());
        action(NewMembershipMessage.GET_DATA); // moved this last, we will see if it works
        newMembershipView = new NewMembershipView(newMembershipModel, this::action);
    }

    @Override
    public Region getView() {
        return newMembershipView.build();
    }

    @Override
    public void action(NewMembershipMessage action) {
        switch (action) {
            case GET_DATA -> getSlipData();
            case LAUNCH_TAB -> launchTab();
        }
    }

    private void launchTab() {
        mainController.openMembershipMVCI(newMembershipInteractor.getMembershipList());
    }

    private void getSlipData() {
        mainController.setSpinnerOffset(50,50);
        mainController.showLoadingSpinner(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
//                slipInteractor.getSlipInfo();
//                slipInteractor.getSlipStructure();
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            mainController.showLoadingSpinner(false);
//            slipInteractor.setListsLoaded();
        });
        new Thread(task).start();
    }
}
