package org.ecsail.mvci_main;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
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
import org.ecsail.mvci_roster.RosterController;
import org.ecsail.mvci_welcome.WelcomeController;

public class MainController extends Controller implements Status {

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

    private void action(MainMessage action) {
        switch (action) {
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
        loadingController = new LoadingController(this);
        loadingController.getStage().setScene(new Scene(loadingController.getView(), Color.TRANSPARENT));
    }

    public void openMembershipMVCI(MembershipListDTO ml) {
        //check for tab open with this msid
        if(mainInteractor.tabAlreadyOpen(ml.getMsId()))
            mainInteractor.selectTabByUserData(ml);
        else
        // else open new tab
        mainView.addTab("Mem " + ml.getMembershipId(), new MembershipController(this, ml).getView(), ml.getMsId());
    }

    public void openBoatMVCI(BoatListDTO bl) {
        mainView.addTab("Boat " + bl.getBoatId(), new BoatController(this, bl).getView(), 0);
    }

    public void openTab(String tabName) {
        Region region = null;
        switch (tabName) {
            case "People" -> System.out.println("Displaying people list");
            case "Slips" -> System.out.println("Displaying slips list");
            case "Board of Directors" -> System.out.println("Displaying board of directors list");
            case "Create New Membership" -> System.out.println("Creating new membership");
            case "Deposits" -> System.out.println("Displaying deposits");
            case "Rosters" -> region = new RosterController(this).getView();
            case "Boats" -> region = new BoatListController(this).getView();
            case "Notes" -> System.out.println("Displaying notes");
            case "Jotform" -> System.out.println("Opening Jotform");
            default -> System.out.println("Invalid input");
        }
        mainView.addTab(tabName, region,0);
    }

    public void openWelcomeMVCI() {
        mainView.closeTabs();
        mainView.addTab("Welcome",new WelcomeController(this).getView(),0);
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
        loadingController.setOffset(x,y);
    }

    public Connections getConnections() {
        return connectController.getConnectInteractor().getConnections();
    }

    public MainModel getMainModel() {
        return mainInteractor.getMainModel();
    }

    private void closeAllConnections() {
        Platform.runLater(connectController.closeConnection());
    }

    @Override
    public Region getView() { return mainView.build(); }

    public void setStatus(String status) { mainInteractor.setStatus(status); }

    public void setChangeStatus(Status.light status) { mainInteractor.setChangeStatus(status); }
}
