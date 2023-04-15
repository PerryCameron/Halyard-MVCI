package org.ecsail.mvci_main;


import javafx.application.Platform;
import javafx.scene.layout.Region;
import org.ecsail.connection.Connections;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.interfaces.Controller;
import org.ecsail.mvci_connect.ConnectController;
import org.ecsail.mvci_membership.MembershipController;
import org.ecsail.mvci_roster.RosterController;
import org.ecsail.mvci_welcome.WelcomeController;

public class MainController extends Controller {

    private final MainInteractor mainInteractor;
    private final MainView mainView;
    private final ConnectController connectController;
    public MainController() {
        MainModel mainModel = new MainModel();
        mainInteractor = new MainInteractor(mainModel);
        mainView = new MainView(mainModel, this::closeAllConnections);
        connectController = new ConnectController(this).getView();
    }

    public void openMembershipMVCI(MembershipListDTO ml) {
        mainView.addTab("Mem " + ml.getMembershipId(), new MembershipController(this, ml).getView());
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
            case "Boats" -> System.out.println("Displaying boats");
            case "Notes" -> System.out.println("Displaying notes");
            case "Jotform" -> System.out.println("Opening Jotform");
            default -> System.out.println("Invalid input");
        }
        mainView.addTab(tabName, region);
    }

    public void openWelcomeMVCI() {
        mainView.closeTabs();
        mainView.addTab("Welcome",new WelcomeController(this).getView());
    }

    public Connections getConnections() {
        return connectController.getConnectInteractor().getConnections();
    }

    private void closeAllConnections() {
        Platform.runLater(connectController.closeConnection());
    }

    @Override
    public Region getView() { return mainView.build(); }

    public void setStatus(String status) { mainInteractor.setStatus(status); }
}
