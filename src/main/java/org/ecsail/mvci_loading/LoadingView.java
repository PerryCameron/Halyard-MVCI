package org.ecsail.mvci_loading;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

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
        return loadingVBox;
    }

    private Control createProcessIndicator() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(100, 100);
        return progressIndicator;
    }
}
