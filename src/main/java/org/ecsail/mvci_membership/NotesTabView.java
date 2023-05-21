package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.widgetfx.VBoxFx;

public class NotesTabView implements Builder<Tab> {
    private final MembershipView membershipView;

    public NotesTabView(MembershipView membershipView) {
        this.membershipView = membershipView;
    }

    @Override
    public Tab build() {
        Tab tab = new Tab("Notes");
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,5,5)); // makes outer border
        vBox.setId("custom-tap-pane-frame");
        HBox.setHgrow(vBox, Priority.ALWAYS);
        tab.setContent(vBox);
        return tab;
    }
}
