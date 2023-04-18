package org.ecsail.mvci_loading;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;

public class LoadingModel {

    Stage loadingStage = new Stage();

    SimpleBooleanProperty show = new SimpleBooleanProperty(false);

    public Stage getLoadingStage() {
        return loadingStage;
    }

    public void setLoadingStage(Stage loadingStage) {
        this.loadingStage = loadingStage;
    }

    public boolean isShow() {
        return show.get();
    }

    public SimpleBooleanProperty showProperty() {
        return show;
    }

    public void setShow(boolean show) {
        this.show.set(show);
    }

}
