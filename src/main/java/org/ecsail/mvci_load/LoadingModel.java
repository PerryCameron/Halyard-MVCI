package org.ecsail.mvci_load;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.stage.Stage;

public class LoadingModel {

    protected Stage loadingStage = new Stage();
    private SimpleDoubleProperty primaryXProperty = new SimpleDoubleProperty();
    private SimpleDoubleProperty primaryYProperty = new SimpleDoubleProperty();
    private SimpleDoubleProperty offsetX = new SimpleDoubleProperty();
    private SimpleDoubleProperty offsetY = new SimpleDoubleProperty();


    public double getOffsetX() {
        return offsetX.get();
    }

    public SimpleDoubleProperty offsetXProperty() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX.set(offsetX);
    }

    public void setOffsets(double x, double y) {
        this.offsetX.set(x);
        this.offsetY.set(y);
    }

    public double getOffsetY() {
        return offsetY.get();
    }

    public SimpleDoubleProperty offsetYProperty() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY.set(offsetY);
    }

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
