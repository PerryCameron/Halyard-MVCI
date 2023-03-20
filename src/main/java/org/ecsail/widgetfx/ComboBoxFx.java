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


