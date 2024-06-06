package org.ecsail.mvci_deposit;


import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class DepositController extends Controller<DepositMessage> {
    private final MainController mainController;

    private final DepositInteractor depositInteractor;
    private final DepositView depositView;

    public DepositController(MainController mc) {
        mainController = mc;
        DepositModel depositModel = new DepositModel(mainController.getMainModel());
        depositInteractor = new DepositInteractor(depositModel, mainController.getConnections());
        action(DepositMessage.GET_DATA); // moved this last, we will see if it works
        depositView = new DepositView(depositModel, this::action);
    }

    @Override
    public Region getView() {
        return depositView.build();
    }

    @Override
    public void action(DepositMessage action) {
        switch (action) {
            case GET_DATA -> getSlipData();
            case LAUNCH_TAB -> launchTab();
        }
    }

    private void launchTab() {
        mainController.openMembershipMVCI(depositInteractor.getMembershipList());
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
