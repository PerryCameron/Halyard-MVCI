package org.ecsail.mvci_main;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.ecsail.connection.Connections;
import org.ecsail.dto.BoatListDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.interfaces.Controller;
import org.ecsail.interfaces.Status;
import org.ecsail.mvci_boat.BoatController;
import org.ecsail.mvci_boatlist.BoatListController;
import org.ecsail.mvci_connect.ConnectController;
import org.ecsail.mvci_load.LoadingController;
import org.ecsail.mvci_membership.MembershipController;
import org.ecsail.mvci_new_membership.NewMembershipController;
import org.ecsail.mvci_roster.RosterController;
import org.ecsail.mvci_slip.SlipController;
import org.ecsail.mvci_welcome.WelcomeController;

public class MainController extends Controller<MainMessage> implements Status {

    private final MainInteractor mainInteractor;
    private final MainView mainView;
    private ConnectController connectController;
    private LoadingController loadingController;

    public MainController() {
        MainModel mainModel = new MainModel();
        mainInteractor = new MainInteractor(mainModel);
        mainView = new MainView(mainModel, this::action);
        mainInteractor.setComplete();
    }
    @Override
    public void action(MainMessage action) {
        switch (action) {
            case CLOSE_ALL_CONNECTIONS_AND_EXIT -> connectController.closeConnection();
            case CLOSE_ALL_CONNECTIONS -> closeAllConnections();
            case CREATE_CONNECT_CONTROLLER -> createConnectController();
            case BACKUP_DATABASE -> backUpDatabase();
        }
    }

    public void backUpDatabase() {
//        Database.BackUp(getConnections().getDataSource());
    }

    public void createConnectController() {
        connectController = new ConnectController(this);
        connectController.getStage().setScene(new Scene(connectController.getView()));
        connectController.getStage().getScene().getStylesheets().add("css/dark/dark.css");
        connectController.setStageHeightListener();
    }

    public void createLoadingController() {
        loadingController = new LoadingController();
        loadingController.getStage().setScene(new Scene(loadingController.getView(), Color.TRANSPARENT));
    }

    public void openMembershipMVCI(MembershipListDTO m) {
        if (mainInteractor.tabIsNotOpen(m.getMsId()))
            mainView.addNewTab("Mem " + m.getMembershipId(), new MembershipController(this, m).getView(), m.getMsId());
    }

    public void openBoatMVCI(BoatListDTO b) {
        mainView.addNewTab("Boat " + b.getBoatId(), new BoatController(this, b).getView(), b.getBoatId());
    }

    public void openTab(String tabName) {
        switch (tabName) {
            case "People" -> System.out.println("Displaying people list");
            case "Slips" -> openSlipsTab(tabName);
            case "Board of Directors" -> System.out.println("Displaying board of directors list");
            case "Create New Membership" -> openNewMembershipTab(tabName);
            case "Deposits" -> System.out.println("Displaying deposits");
            case "Rosters" -> openRosterTab(tabName);
            case "Boats" -> openBoatListTab(tabName);
            case "Notes" -> System.out.println("Displaying notes");
            case "Jotform" -> System.out.println("Opening Jotform");
            default -> System.out.println("Invalid input");
        }

    }

    private void openNewMembershipTab(String tabName) {
        if (mainInteractor.tabIsNotOpen(-5))
            mainView.addNewTab(tabName, new NewMembershipController(this).getView(), -5);
    }

    private void openSlipsTab(String tabName) {
        if (mainInteractor.tabIsNotOpen(-4))
            mainView.addNewTab(tabName, new SlipController(this).getView(), -4);
    }

    private void openBoatListTab(String tabName) {
        if (mainInteractor.tabIsNotOpen(-3))
            mainView.addNewTab(tabName, new BoatListController(this).getView(), -3);
    }

    private void openRosterTab(String tabName) {
        if (mainInteractor.tabIsNotOpen(-2))
            mainView.addNewTab(tabName, new RosterController(this).getView(), -2);
    }

    public void openWelcomeMVCI() {
        mainView.closeTabs();
        mainView.addNewTab("Welcome", new WelcomeController(this).getView(), -1);
    }

    public void loadCommonLists() {
        Task<Boolean> connectTask = new Task<>() {
            @Override
            protected Boolean call() {
                mainInteractor.loadCommonLists(getConnections());
                return null;
            }
        };
        Thread thread = new Thread(connectTask);
        thread.start();
    }

    public void showLoadingSpinner(boolean isVisible) {
        loadingController.showLoadSpinner(isVisible);
    }

    public void setSpinnerOffset(double x, double y) {
        loadingController.setOffset(x, y);
    }

    public Connections getConnections() {
        return connectController.getConnectInteractor().getConnections();
    }

    public MainModel getMainModel() {
        return mainInteractor.getMainModel();
    }

    private void closeAllConnections() {
        connectController.closeConnection();
        Platform.runLater(() -> {
            mainInteractor.getMainModel().getMainTabPane().getTabs().clear();
            getMainModel().getMainTabPane().getTabs().add(new Tab("Log in"));
            createConnectController();
        });
    }

    @Override
    public Region getView() {
        return mainView.build();
    }

    public void setStatus(String status) {
        mainInteractor.setStatus(status);
    }
}
