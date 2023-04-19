package org.ecsail.mvci_loading;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.Stage;

public class LoadingModel {

    protected Stage loadingStage = new Stage();
    private SimpleDoubleProperty primaryXProperty = new SimpleDoubleProperty();
    private SimpleDoubleProperty primaryYProperty = new SimpleDoubleProperty();


    
    protected SimpleDoubleProperty primaryXPropertyProperty() {
        return primaryXProperty;
    }
    protected SimpleDoubleProperty primaryYPropertyProperty() {
        return primaryYProperty;
    }
    protected Stage getLoadingStage() {
        return loadingStage;
    }
}
