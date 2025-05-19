package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.TabFx;
import org.ecsail.widgetfx.VBoxFx;

public class AttachmentsTabView implements Builder<Tab> {
    private final MembershipView membershipView;

    public AttachmentsTabView(MembershipView membershipView) {
        this.membershipView = membershipView;
    }

    @Override
    public Tab build() {
        return TabFx.tabOf("Attachments", addTempNode());
    }

    private Node addTempNode() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2), "custom-tap-pane-frame", true); // makes outer border
        HBox hBox = HBoxFx.hBoxOf(new Insets(5,5,5,5),"box-background-light",true);
        vBox.getChildren().add(hBox);
        return vBox;
    }
}
