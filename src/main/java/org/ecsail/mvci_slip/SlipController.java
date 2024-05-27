package org.ecsail.mvci_slip;


import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_connect.ConnectController;
import org.ecsail.mvci_load.LoadingController;

public class SlipController extends Controller<SlipMessage> {

    private final SlipInteractor slipInteractor;
    private final SlipView slipView;
    private ConnectController connectController;
    private LoadingController loadingController;

    public SlipController() {
        SlipModel slipModel = new SlipModel();
        slipInteractor = new SlipInteractor(slipModel);
        slipView = new SlipView(slipModel, this::action);
    }

    @Override
    public Region getView() {
        return null;
    }

    @Override
    public void action(SlipMessage action) {
        switch (action) {

        }
    }


}
