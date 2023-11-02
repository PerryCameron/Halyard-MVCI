package org.ecsail.mvci_load;

import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.ecsail.interfaces.Controller;

public class LoadingController extends Controller<LoadMessage> {

    private LoadingInteractor loadingInteractor;
    private LoadingModel loadingModel;
    private LoadingView loadingView;

    public LoadingController() {
        loadingModel = new LoadingModel();
        loadingView = new LoadingView(loadingModel);
        loadingInteractor = new LoadingInteractor(loadingModel);
    }

    @Override
    public Region getView() {
        return loadingView.build();
    }

    @Override
    public void action(LoadMessage actionEnum) {
        // No messages to receive here
    }

    public Stage getStage() {
        return loadingInteractor.getStage();
    }

    public void setOffset(double x, double y) { loadingInteractor.setOffSet(x,y); }

    public void showLoadSpinner(boolean show) {
        loadingInteractor.showLoadSpinner(show);
    }
}
