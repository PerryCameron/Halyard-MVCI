package org.ecsail.mvci_new_membership;


import org.ecsail.mvci_main.MainModel;

public class NewMembershipModel {
    private final MainModel mainModel;





    public NewMembershipModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    public MainModel getMainModel() {
        return mainModel;
    }
}
