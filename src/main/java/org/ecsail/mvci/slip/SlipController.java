package org.ecsail.mvci.slip;


import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci.main.MainController;

public class SlipController extends Controller<SlipMessage> {
    private final MainController mainController;
    private final SlipInteractor slipInteractor;
    private final SlipView slipView;

    public SlipController(MainController mc) {
        mainController = mc;
        SlipModel slipModel = new SlipModel(mainController.getMainModel());
        slipInteractor = new SlipInteractor(slipModel);
        action(SlipMessage.GET_DATA); // moved this last, we will see if it works
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
//            case LAUNCH_TAB -> launchTab();
        }
    }

//    private void launchTab() {
//        mainController.openMembershipMVCI(slipInteractor.getMembershipList());
//    }

    private void getSlipData() {
        mainController.setSpinnerOffset(50,50);
        mainController.showLoadingSpinner(true);
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    slipInteractor.getSlipInfo();
                    slipInteractor.getSlipStructure();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            return true;
            }
        };
        task.setOnSucceeded(e -> {
            mainController.showLoadingSpinner(false);
            slipInteractor.setListsLoaded();
        });
        new Thread(task).start();
    }
}
