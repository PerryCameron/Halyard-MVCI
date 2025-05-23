package org.ecsail.mvci.bod;


import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci.main.MainController;


public class BodController extends Controller<BodMessage> {
    private final MainController mainController;
    private final BodInteractor bodInteractor;
    private final BodView bodView;

    public BodController(MainController mc) {
        mainController = mc;
        BodModel BodModel = new BodModel(mainController.getMainModel());
//        bodInteractor = new BodInteractor(BodModel, mainController.getConnections());
        bodInteractor = new BodInteractor(BodModel);
        action(BodMessage.GET_DATA); // moved this last, we will see if it works
        bodView = new BodView(BodModel, this::action);
    }

    @Override
    public Region getView() {
        return bodView.build();
    }

    @Override
    public void action(BodMessage action) {
        switch (action) {
            case GET_DATA -> getSlipData();
            case LAUNCH_TAB -> launchTab();
        }
    }

    private void launchTab() {
//        mainController.openMembershipMVCI(bodInteractor.getMembershipList());
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
