package org.ecsail.mvci_loading;

import javafx.beans.property.SimpleBooleanProperty;

public class LoadingModel {

    SimpleBooleanProperty show = new SimpleBooleanProperty(false);

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
