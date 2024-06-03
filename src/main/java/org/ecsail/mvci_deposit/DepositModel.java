package org.ecsail.mvci_deposit;


import org.ecsail.mvci_main.MainModel;

public class DepositModel {
    private final MainModel mainModel;





    public DepositModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    public MainModel getMainModel() {
        return mainModel;
    }
}
