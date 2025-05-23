package org.ecsail.mvci.main;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.ecsail.BaseApplication;
import org.ecsail.fx.BoatListDTO;
import org.ecsail.fx.RosterFx;
import org.ecsail.interfaces.Controller;
import org.ecsail.interfaces.Status;
import org.ecsail.mvci.boat.BoatController;
import org.ecsail.mvci.boatlist.BoatListController;
import org.ecsail.mvci.bod.BodController;
import org.ecsail.mvci.connect.ConnectController;
import org.ecsail.mvci.deposit.DepositController;
import org.ecsail.mvci.load.LoadingController;
import org.ecsail.mvci.membership.MembershipController;
import org.ecsail.mvci.newmembership.NewMembershipController;
import org.ecsail.mvci.roster.RosterController;
import org.ecsail.mvci.slip.SlipController;
import org.ecsail.mvci.welcome.WelcomeController;
import org.ecsail.widgetfx.DialogueFx;
import org.ecsail.wrappers.GlobalDataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;

public class MainController extends Controller<MainMessage> implements Status {

    private final MainInteractor mainInteractor;
    private final MainView mainView;
    private ConnectController connectController;
    private LoadingController loadingController;
    private RosterController rosterController;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);


    public MainController() {
        MainModel mainModel = new MainModel();
        mainInteractor = new MainInteractor(mainModel);
        mainView = new MainView(mainModel, this::action);
        mainInteractor.setComplete();
    }

    @Override
    public void action(MainMessage action) {
        switch (action) {
            case CLOSE_ALL_CONNECTIONS_AND_EXIT -> closeAllConnectionsAndExit();
            case CLOSE_ALL_CONNECTIONS -> closeAllConnections();
            case CREATE_CONNECT_CONTROLLER -> createConnectController();
            case SHOW_LOG -> showDebugLog();
            case STOP_SPINNER -> showLoadingSpinner(false);
            case SET_CONNECT_ERROR_FALSE -> mainInteractor.setConnectError(false);
//            case SET_CONNECT_ERROR_TRUE -> mainInteractor.setConnectError(true);
        }
    }

    public void getGlobalData() {
        String email = connectController.getLogin().userProperty().get();
        if (email == null || email.isBlank()) {
            logger.error("Cannot fetch global data: email is empty");
            DialogueFx.errorAlert("Error", "No user email provided");
            return;
        }
        Task<GlobalDataResponse> fetchGlobalDataTask = new Task<>() {
            @Override
            protected GlobalDataResponse call() throws Exception {
                return mainInteractor.fetchGlobalData(email);
            }
        };
        fetchGlobalDataTask.setOnSucceeded(e -> {
            GlobalDataResponse response = fetchGlobalDataTask.getValue();
            if (response != null) {
                mainInteractor.setGlobalData(response);
            }
        });
        fetchGlobalDataTask.setOnFailed(e -> {
            Throwable exception = fetchGlobalDataTask.getException();
            logger.error("Failed to fetch global data for email: {}, error: {}", email, exception.getMessage());
            Platform.runLater(() -> DialogueFx.errorAlert("Error", "Failed to fetch global data: " + exception.getMessage()));
        });
        Thread thread = new Thread(fetchGlobalDataTask);
        thread.setDaemon(true); // Ensure thread doesn't prevent app shutdown
        thread.start();
    }

    private void showDebugLog() {
        Desktop desktop = Desktop.getDesktop(); // Gui_Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()
        // Open the document
        try {
            desktop.open(BaseApplication.outputFile);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void createConnectController() {
        connectController = new ConnectController(this);
        connectController.getStage().setScene(new Scene(connectController.getView()));  // line 74
        connectController.getStage().getScene().getStylesheets().add("css/dark/dark.css");
        connectController.setStageHeightListener();
    }

    public void createLoadingController() {
        loadingController = new LoadingController();
        loadingController.getStage().setScene(new Scene(loadingController.getView(), Color.TRANSPARENT));
    }

    public void openMembershipMVCI(RosterFx rosterDTOFx, int selectedYear) {
        if (mainInteractor.tabIsNotOpen(rosterDTOFx.getMsId()))
            mainView.addNewTab("Mem " + rosterDTOFx.getId(), new MembershipController(this, rosterDTOFx, selectedYear).getView(), rosterDTOFx.getMsId(),true);
    }

    public void openBoatMVCI(BoatListDTO b) {
        mainView.addNewTab("Boat " + b.getBoatId(), new BoatController(this, b).getView(), b.getBoatId(),true);
    }

    public void closeMembershipMVCI(RosterFx rosterDTOFx, int selectedYear) {

    }

    public void openTab(String tabName) {
        switch (tabName) {
            case "People" -> System.out.println("Displaying people list");
            case "Slips" -> openSlipsTab(tabName);
            case "Board of Directors" -> openBodTab(tabName);
            case "Create New Membership" -> openNewMembershipTab(tabName);
            case "Deposits" -> openDepositsTab(tabName);
            case "Rosters" -> openRosterTab(tabName);
            case "Boats" -> openBoatListTab(tabName);
            case "Notes" -> System.out.println("Displaying notes");
            case "Jotform" -> System.out.println("Opening Jotform");
            default -> System.out.println("Invalid input");
        }

    }

    private void openBodTab(String tabName) {
        if (mainInteractor.tabIsNotOpen(-7))
            mainView.addNewTab(tabName, new BodController(this).getView(), -7, true);
    }

    private void openDepositsTab(String tabName) {
        if (mainInteractor.tabIsNotOpen(-6))
            mainView.addNewTab(tabName, new DepositController(this).getView(), -6, true);
    }

    private void openNewMembershipTab(String tabName) {
        if (mainInteractor.tabIsNotOpen(-5))
            mainView.addNewTab(tabName, new NewMembershipController(this).getView(), -5, true);
    }

    private void openSlipsTab(String tabName) {
        if (mainInteractor.tabIsNotOpen(-4))
            mainView.addNewTab(tabName, new SlipController(this).getView(), -4, true);
    }

    private void openBoatListTab(String tabName) {
        if (mainInteractor.tabIsNotOpen(-3))
            mainView.addNewTab(tabName, new BoatListController(this).getView(), -3, true);
    }

    private void openRosterTab(String tabName) {
        if (mainInteractor.tabIsNotOpen(-2))
            this.rosterController = new RosterController(this);
            mainView.addNewTab(tabName, rosterController.getView(), -2, false);
    }

    public void openWelcomeMVCI() {
        mainView.closeTabs();
        mainView.addNewTab("Welcome", new WelcomeController(this).getView(), -1, false);
    }

    public void closeTabByMsId(int msId) {
        mainInteractor.closeTabByMsId(msId);
    }

    public void showLoadingSpinner(boolean isVisible) {
        loadingController.showLoadSpinner(isVisible);
    }

    public void setSpinnerOffset(double x, double y) {
        loadingController.setOffset(x, y);
    }

    public MainModel getMainModel() {
        return mainInteractor.getMainModel();
    }

    private void closeAllConnections() {
        mainInteractor.logout();
        Platform.runLater(() -> {
            mainInteractor.getMainModel().getMainTabPane().getTabs().clear();
            getMainModel().getMainTabPane().getTabs().add(new Tab("Log in"));
            createConnectController();
        });
    }

    private void closeAllConnectionsAndExit() {
        mainInteractor.logout();
        Platform.runLater(() -> {
            mainInteractor.getMainModel().getMainTabPane().getTabs().clear();
            System.exit(0);
        });
    }

    @Override
    public Region getView() {
        return mainView.build();
    }

    public void setStatus(String status) {
        mainInteractor.setStatus(status);
    }

    public void showLoginDialog() {
//        connectController.getView();
        logger.warn("logged out");
    }

    public ConnectController getConnectController() {
        return connectController;
    }

    public RosterController getRosterController() {
        return rosterController;
    }
}
