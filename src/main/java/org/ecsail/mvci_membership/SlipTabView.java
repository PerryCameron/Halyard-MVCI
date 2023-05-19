package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
        VBox vBox = VBoxFx.vBoxOf(15.0, new Insets(10,20,0,10));
        ToggleGroup tg = new ToggleGroup();
        vBox.getChildren().add(setSlipNumber());
        vBox.getChildren().add(setSubleaseInfo());
        vBox.getChildren().add(newRadioButton(tg, "Sublease Slip"));
        vBox.getChildren().add(newRadioButton(tg, "Reassign Slip"));
        vBox.getChildren().add(newRadioButton(tg, "Release Sublease"));
        return vBox;
    }

    private Node newRadioButton(ToggleGroup tg, String rbType) {
        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER_LEFT,new Insets(0,0,0,0));
        RadioButton radioButton = new RadioButton(rbType);
        radioButton.setToggleGroup(tg);
        hBox.getChildren().add(radioButton);
        return hBox;
    }

    private Node setSubleaseInfo() {
        HBox hBox = HBoxFx.hBoxOf(10, Pos.CENTER_LEFT);
        Text label = TextFx.textOf("","text-white",membershipModel.subleaseProperty());
        Text membership = TextFx.textOf("","text-blue", membershipModel.membershipIdProperty());
        hBox.getChildren().addAll(label, membership);
        setIfSubleased();
        membershipModel.slipRelationStatusProperty().addListener(observable -> { setIfSubleased(); });
        return hBox;
    }

    private Node setSlipNumber() {
        HBox hBox = HBoxFx.hBoxOf(10, Pos.CENTER_LEFT);
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

    private void setIfSubleased() {
        switch (membershipModel.getSlipRelationStatus()) {
            case subLeaser -> membershipModel.setSublease("Subleasing From: ");
            case ownAndSublease -> membershipModel.setSublease("Subleasing To: ");
            default -> membershipModel.setSublease("");
        }
    }
}
