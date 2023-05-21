package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.VBoxFx;

public class AddressTabView implements Builder<Tab> {
    private final MembershipView membershipView;

    public AddressTabView(MembershipView membershipView) {
        this.membershipView = membershipView;
    }

    @Override
    public Tab build() {
        Tab tab = new Tab("Address");
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,5,5), "custom-tap-pane-frame", true); // makes outer border
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5),"box-background-light",true);
        vBox.getChildren().add(hBox);
        tab.setContent(vBox);
        return tab;
    }
}
