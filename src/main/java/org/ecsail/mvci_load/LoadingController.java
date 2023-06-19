package org.ecsail.mvci_load;

import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class LoadingController extends Controller {

    private final MainController mainController;
    private LoadingInteractor loadingInteractor;
    private LoadingModel loadingModel;
    private LoadingView loadingView;

    public LoadingController(MainController mainController) {
        this.mainController = mainController;
        loadingModel = new LoadingModel();
        loadingView = new LoadingView(loadingModel);
        loadingInteractor = new LoadingInteractor(loadingModel);
    }

    @Override
    public Region getView() {
        return loadingView.build();
    }

    public Stage getStage() {
        return loadingInteractor.getStage();
    }

    public void setOffset(double x, double y) { loadingInteractor.setOffSet(x,y); }

    public void showLoadSpinner(boolean show) {
        loadingInteractor.showLoadSpinner(show);
    }
}
