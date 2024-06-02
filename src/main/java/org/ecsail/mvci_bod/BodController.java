package org.ecsail.mvci_bod;


import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class BodController extends Controller<BodMessage> {
    private final MainController mainController;
    private final BodInteractor BodInteractor;
    private final BodView BodView;

    public BodController(MainController mc) {
        mainController = mc;
        BodModel BodModel = new BodModel(mainController.getMainModel());
        BodInteractor = new BodInteractor(BodModel, mainController.getConnections());
        action(BodMessage.GET_DATA); // moved this last, we will see if it works
        BodView = new BodView(BodModel, this::action);
    }

    @Override
    public Region getView() {
        return BodView.build();
    }

    @Override
    public void action(BodMessage action) {
        switch (action) {
            case GET_DATA -> getSlipData();
            case LAUNCH_TAB -> launchTab();
        }
    }

    private void launchTab() {
        mainController.openMembershipMVCI(BodInteractor.getMembershipList());
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
