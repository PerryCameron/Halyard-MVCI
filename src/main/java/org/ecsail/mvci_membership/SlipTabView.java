package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.interfaces.SlipRelation;
import org.ecsail.widgetfx.VBoxFx;

import java.util.Objects;

public class SlipTabView implements Builder<Tab>, SlipRelation {


    private final MembershipView membershipView;
    private final MembershipModel membershipModel;

    public SlipTabView(MembershipView membershipView) {
        this.membershipView = membershipView;
        this.membershipModel = membershipView.getMembershipModel();
    }

    @Override
    public Tab build() {
        Tab tab = new Tab();
        tab.setText("Slip");
        VBox vBox = VBoxFx.vBoxOf(new Insets(5,5,5,5)); // makes outer border
        vBox.setId("custom-tap-pane-frame");
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(setSlipImage());
        borderPane.setId("box-background-light");
        vBox.getChildren().add(borderPane);
        tab.setContent(vBox);
        return tab;
    }

    private Node setSlipImage() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(0,0,0,0));
        Image slipImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/slips/" + getSlip() + "_icon.png")));
        ImageView imageView = new ImageView(slipImage);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(240);
        vBox.getChildren().add(imageView);
        return vBox;
    }

    private String getSlip() {
        switch (membershipModel.getSlipRelationStatus()) {
            case noSlip -> { return "none"; }
            case owner, subLeaser, ownAndSublease -> { return membershipModel.getSlip().getSlipNumber(); }
        }
        return "none";
    }
}
