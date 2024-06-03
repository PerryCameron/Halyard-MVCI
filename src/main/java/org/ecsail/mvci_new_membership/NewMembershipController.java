package org.ecsail.mvci_new_membership;


import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_main.MainController;

public class NewMembershipController extends Controller<NewMembershipMessage> {
    private final MainController mainController;
    private final NewMembershipInteractor newMembershipInteractor;
    private final NewMembershipView newMembershipView;

    public NewMembershipController(MainController mc) {
        mainController = mc;
        NewMembershipModel newMembershipModel = new NewMembershipModel(mainController.getMainModel());
        newMembershipInteractor = new NewMembershipInteractor(newMembershipModel, mainController.getConnections());
        action(NewMembershipMessage.CREATE_MEMBERSHIP); // moved this last, we will see if it works
        newMembershipView = new NewMembershipView(newMembershipModel, this::action);
    }

    @Override
    public Region getView() {
        return newMembershipView.build();
    }

    @Override
    public void action(NewMembershipMessage action) {
        switch (action) {
            case CREATE_MEMBERSHIP -> createMembership();
            case LAUNCH_TAB -> launchTab();
        }
    }

    private void launchTab() {
        mainController.openMembershipMVCI(newMembershipInteractor.getMembershipList());
    }

    private void createMembership() {
        mainController.setSpinnerOffset(50,50);
        mainController.showLoadingSpinner(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                newMembershipInteractor.getNextAvailableId();
                newMembershipInteractor.createMembershipObject();
                newMembershipInteractor.createPrimaryMember();
                newMembershipInteractor.updateMembership();
                newMembershipInteractor.createMembershipIdRow();
                newMembershipInteractor.createMemoToDocument();
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            mainController.showLoadingSpinner(false);
            newMembershipInteractor.showSuccess();
            launchTab();
        });
        new Thread(task).start();
    }
}
