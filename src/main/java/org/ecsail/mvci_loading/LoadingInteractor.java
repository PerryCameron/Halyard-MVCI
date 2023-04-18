package org.ecsail.mvci_loading;

public class LoadingInteractor {
    private final LoadingModel loadingModel;

    public LoadingInteractor(LoadingModel loadingModel) {
        this.loadingModel = loadingModel;
    }

    public void showLoadSpinner(boolean show) {
        loadingModel.setShow(show);
    }
}
