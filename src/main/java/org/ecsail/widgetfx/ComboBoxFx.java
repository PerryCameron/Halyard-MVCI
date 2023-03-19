package org.ecsail.widgetfx;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.ecsail.dto.LoginDTO;

public class ComboBoxFx {

    public static <T> ComboBox<T> comboBoxOf(double width, ObservableList<T> list) {
        ComboBox<T> comboBox = new ComboBox<>(list);
        comboBox.setPrefWidth(width);
        comboBox.setItems(list);
        return comboBox;
    }



}

//    ComboBox<String> comboBox = new ComboBox<>();
//    ObservableList<String> observableList = FXCollections.observableArrayList("Item 1", "Item 2", "Item 3");
//
//comboBox.itemsProperty().bind(observableList); // bind the ComboBox's items to the ObservableList

