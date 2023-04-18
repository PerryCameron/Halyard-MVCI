package org.ecsail.mvci_loading;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Builder;
import org.ecsail.BaseApplication;

public class LoadingView implements Builder<Region> {

    private final LoadingModel loadingModel;

    public LoadingView(LoadingModel loadingModel) {
        this.loadingModel = loadingModel;
    }

    @Override
    public Region build() {
        VBox loadingVBox = new VBox(createProcessIndicator());
        loadingVBox.setAlignment(Pos.CENTER);
        loadingVBox.setBackground(Background.EMPTY);
        loadingVBox.setPadding(Insets.EMPTY);
        setShowListener();
        return loadingVBox;
    }

    private void setShowListener() {
        loadingModel.showProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) BaseApplication.loadingStage.show();
            else BaseApplication.loadingStage.hide();
        });
    }

    public void createStage(Region region) {
        BaseApplication.loadingStage = new Stage();
        BaseApplication.loadingStage.initOwner(BaseApplication.primaryStage);
        BaseApplication.loadingStage.initModality(Modality.APPLICATION_MODAL);
        BaseApplication.loadingStage.initStyle(StageStyle.TRANSPARENT);
//        BaseApplication.loadingStage.setScene(new Scene(createVbox(), Color.TRANSPARENT));
        BaseApplication.loadingStage.setOnShown(windowEvent -> {
//        BaseApplication.loadingStage.setX(centerXPosition - BaseApplication.loadingStage.getWidth() / 2d);
//        BaseApplication.loadingStage.setY(centerYPosition - BaseApplication.loadingStage.getHeight() / 2d);
        });
    }

    private Control createProcessIndicator() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(100, 100);
        return progressIndicator;
    }
}
