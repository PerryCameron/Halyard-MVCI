package org.ecsail.mvci_slip;


import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class SlipController extends Controller<SlipMessage> {
    private final MainController mainController;
    private final SlipInteractor slipInteractor;
    private final SlipView slipView;

    public SlipController(MainController mc) {
        mainController = mc;
        SlipModel slipModel = new SlipModel(mainController.getMainModel());
        slipInteractor = new SlipInteractor(slipModel, mainController.getConnections());
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
        }
    }

    private void getSlipData() {
        mainController.setSpinnerOffset(50,50);
        mainController.showLoadingSpinner(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                slipInteractor.getPlacement();
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
