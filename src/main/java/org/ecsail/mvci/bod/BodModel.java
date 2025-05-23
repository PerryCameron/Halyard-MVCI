package org.ecsail.mvci.bod;


import org.ecsail.mvci.main.MainModel;

public class BodModel {
    private final MainModel mainModel;





    public BodModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    public MainModel getMainModel() {
        return mainModel;
    }
}
