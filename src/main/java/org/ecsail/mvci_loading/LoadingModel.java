package org.ecsail.mvci_loading;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.Stage;

public class LoadingModel {

    protected Stage loadingStage = new Stage();
    private SimpleDoubleProperty primaryXProperty = new SimpleDoubleProperty();
    private SimpleDoubleProperty primaryYProperty = new SimpleDoubleProperty();



    public double getPrimaryXProperty() {
        return primaryXProperty.get();
    }

    public SimpleDoubleProperty primaryXPropertyProperty() {
        return primaryXProperty;
    }

    public void setPrimaryXProperty(double primaryXProperty) {
        this.primaryXProperty.set(primaryXProperty);
    }

    public double getPrimaryYProperty() {
        return primaryYProperty.get();
    }

    public SimpleDoubleProperty primaryYPropertyProperty() {
        return primaryYProperty;
    }

    public void setPrimaryYProperty(double primaryYProperty) {
        this.primaryYProperty.set(primaryYProperty);
    }

    public Stage getLoadingStage() {
        return loadingStage;
    }

    public void setLoadingStage(Stage loadingStage) {
        this.loadingStage = loadingStage;
    }


}
