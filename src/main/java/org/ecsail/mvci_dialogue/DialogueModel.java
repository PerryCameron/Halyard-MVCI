package org.ecsail.mvci_dialogue;


import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.ecsail.dto.BoardPositionDTO;
import org.ecsail.interfaces.Status;


public class DialogueModel {

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

}
