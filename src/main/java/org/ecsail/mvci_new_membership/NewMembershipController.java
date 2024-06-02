package org.ecsail.mvci_new_membership;


import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;
import org.ecsail.mvci_slip.SlipInteractor;
import org.ecsail.mvci_slip.SlipMessage;
import org.ecsail.mvci_slip.SlipModel;
import org.ecsail.mvci_slip.SlipView;

public class NewMembershipController extends Controller<SlipMessage> {
    private final MainController mainController;
    private final SlipInteractor slipInteractor;
    private final SlipView slipView;

    public NewMembershipController(MainController mc) {
        mainController = mc;
        SlipModel slipModel = new SlipModel(mainController.getMainModel());
        slipInteractor = new SlipInteractor(slipModel, mainController.getConnections());
        System.out.println("getting DATA");
        action(SlipMessage.GET_DATA); // moved this last, we will see if it works
        System.out.println("Getting slipView");
        slipView = new SlipView(slipModel, this::action);
    }

    @Override
    public Region getView() {
        return slipView.build();
    }

    @Override
    public void action(SlipMessage action) {
        switch (action) {
            case GET_DATA -> getSlipData();
            case LAUNCH_TAB -> launchTab();
        }
    }

    private void launchTab() {
        mainController.openMembershipMVCI(slipInteractor.getMembershipList());
    }

    private void getSlipData() {
        mainController.setSpinnerOffset(50,50);
        mainController.showLoadingSpinner(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                slipInteractor.getSlipInfo();
                slipInteractor.getSlipStructure();
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            mainController.showLoadingSpinner(false);
            slipInteractor.setListsLoaded();
        });
        new Thread(task).start();
    }
}
