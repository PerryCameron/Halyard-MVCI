package org.ecsail.mvci_bod;


import org.ecsail.mvci_main.MainModel;

public class BodModel {
    private final MainModel mainModel;





    public BodModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    public MainModel getMainModel() {
        return mainModel;
    }
}
