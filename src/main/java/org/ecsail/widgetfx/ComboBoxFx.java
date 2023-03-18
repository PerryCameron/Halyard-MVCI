package org.ecsail.widgetfx;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class ComboBoxFx {

    public static <T> ComboBox<T> comboBoxOf(double width, ObservableList<T> choices) {
        ComboBox<T> comboBox = new ComboBox<>(choices);
        comboBox.setPrefWidth(width);
        return comboBox;
    }

}
