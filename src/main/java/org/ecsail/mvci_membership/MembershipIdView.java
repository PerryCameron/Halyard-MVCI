package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.widgetfx.VBoxFx;

public class MembershipIdView implements Builder<Tab> {
    private final MembershipView membershipView;

    public MembershipIdView(MembershipView membershipView) {
        this.membershipView = membershipView;
    }

    @Override
    public Tab build() {
        Tab tab = new Tab();
        tab.setText("History");
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,5,5),"custom-tap-pane-frame",false); // makes outer border
        BorderPane borderPane = new BorderPane();
//        borderPane.setLeft(setSlipInfo());
//        borderPane.setCenter(setSlipImage());
        borderPane.setId("box-background-light");
        VBox.setVgrow(borderPane, Priority.ALWAYS); // causes slip tab to grow to fit vertical space
        vBox.getChildren().add(borderPane);
        tab.setContent(vBox);
        return tab;
    }
}
