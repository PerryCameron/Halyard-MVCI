package org.ecsail.mvci.deposit;


import org.ecsail.mvci.main.MainModel;

public class DepositModel {
    private final MainModel mainModel;





    public DepositModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    public MainModel getMainModel() {
        return mainModel;
    }
}
