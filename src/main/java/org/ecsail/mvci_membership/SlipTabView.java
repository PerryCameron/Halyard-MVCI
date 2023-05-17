package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.ecsail.interfaces.SlipRelation;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.TextFx;
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
        borderPane.setLeft(setSlipInfo());
        borderPane.setCenter(setSlipImage());
        borderPane.setId("box-background-light");
        vBox.getChildren().add(borderPane);
        tab.setContent(vBox);
        return tab;
    }

    private Node setSlipInfo() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(10,20,0,10));
        vBox.getChildren().add(setSlipNumber());
        return vBox;
    }

    private Node setSlipNumber() {
        HBox hBox = HBoxFx.hBoxOf(10, Pos.CENTER);
        Text label = TextFx.textOf("Slip Number:","text-white");
        Text slip = TextFx.textOf(membershipModel.getSlip().getSlipNumber(),"text-bold");
        hBox.getChildren().addAll(label,slip);
        return hBox;
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
            case owner, subLeaser, ownAndSublease -> { return membershipModel.getSlip().getSlipNumber(); }
            default -> { return "none"; }
        }
    }
}
