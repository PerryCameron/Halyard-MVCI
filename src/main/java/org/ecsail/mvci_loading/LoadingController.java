package org.ecsail.mvci_loading;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.ecsail.BaseApplication;
import org.ecsail.mvci_main.MainController;

public class LoadingController {

    private Stage loadingStage;
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
        this.loadingStage = new Stage();
        loadingStage.initOwner(BaseApplication.primaryStage);
        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.initStyle(StageStyle.TRANSPARENT);
        double centerXPosition = BaseApplication.primaryStage.getX() + BaseApplication.primaryStage.getWidth() / 2d;
        double centerYPosition = BaseApplication.primaryStage.getY() + BaseApplication.primaryStage.getHeight() / 2d;
//        loadingStage.setOnShown(windowEvent -> {
//        BaseApplication.loadingStage.setX(centerXPosition - BaseApplication.loadingStage.getWidth() / 2d);
//        BaseApplication.loadingStage.setY(centerYPosition - BaseApplication.loadingStage.getHeight() / 2d);
//        });
        loadingStage.setScene(new Scene(loadingView.build()));
        loadingInteractor.logInfo("Loading Stage Set");
        return this;
    }

    public void showLoadSpinner(boolean show) {
        if(show) loadingStage.show();
        else loadingStage.hide();
    }
}
