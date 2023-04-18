package org.ecsail.mvci_loading;

import org.ecsail.mvci_main.MainController;

public class LoadingController {

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

    public LoadingController getView() {
        loadingView.createStage(loadingView.build());
        System.out.println("Stage LoadingView created");
        return this;
    }

    public void showLoadSpinner(boolean show) {
        loadingInteractor.showLoadSpinner(show);
    }

}
