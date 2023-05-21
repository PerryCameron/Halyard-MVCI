package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.Collection;

public class BoatTabView implements Builder<Tab> {
    private final MembershipView membershipView;

    public BoatTabView(MembershipView membershipView) {
        this.membershipView = membershipView;


    }

    @Override
    public Tab build() {
        Tab tab = new Tab("Boats");
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,5,5)); // makes outer border
        vBox.setId("custom-tap-pane-frame");
        HBox.setHgrow(vBox, Priority.ALWAYS);
        vBox.getChildren().add(createTableViewAndButtonsBox());
        tab.setContent(vBox);
        return tab;
    }

    private Node createTableViewAndButtonsBox() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(getTableView(), getButtonControls());
        return hBox;
    }

    private Node getButtonControls() {
        VBox vBox = new VBox();
        return vBox;
    }

    private Node getTableView() {
        VBox vBox = new VBox();
        return vBox;
    }
}
