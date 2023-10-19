package org.ecsail.mvci_membership;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.widgetfx.*;


public class InvoiceView implements Builder<Tab> {
    private final MembershipView membershipView;
    private InvoiceDTO invoiceDTO;

    public InvoiceView(MembershipView membershipView) {
        this.membershipView = membershipView;
        this.invoiceDTO = membershipView.getMembershipModel().getSelectedInvoice();
    }

    @Override
    public Tab build() {
        Tab tab = new Tab();
        tab.setText(String.valueOf(invoiceDTO.getYear()));
        invoiceDTO.listLoadedProperty().addListener(getDataLoadedListener(tab));
        return tab;
    }

    private ChangeListener<Boolean> getDataLoadedListener(Tab tab) {
        return ListenerFx.createSingleUseListener(invoiceDTO.listLoadedProperty(), () -> {
            if(invoiceDTO.isCommitted()) tab.setContent(showCommittedInvoice());
            else tab.setContent(showEditableInvoice());
        });
    }

    private Node showEditableInvoice() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2)); // makes outer border
        vBox.getStyleClass().add("standard-box");
        return vBox;
    }

    private Node showCommittedInvoice() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2)); // makes outer border
        vBox.getStyleClass().add("standard-box");
        vBox.getChildren().addAll(HBoxFx.customHBoxHeader(),RegionFx.regionHeightOf(10.0));
        invoiceDTO.getItemDTOS().stream()
                .filter(item -> !item.getValue().equals("0.00"))
                .map(HBoxFx::customHBox)
                .forEach(vBox.getChildren()::add);
        Separator separator = new Separator(Orientation.HORIZONTAL);
        vBox.getChildren().addAll(RegionFx.regionHeightOf(20.0),separator);
        HBox hBox = HBoxFx.hBoxOf(new Insets(10,15,5,0),10);
        VBox buttonBox = VBoxFx.vBoxOf(10.0, new Insets(25,5,5,20));
        buttonBox.getChildren().addAll(
                ButtonFx.buttonOf("Add Note",100, () -> System.out.println("Add Note")),
                ButtonFx.buttonOf("Edit",100, () -> System.out.println("Edit")));
        hBox.getChildren().addAll(HBoxFx.customHBox(invoiceDTO),buttonBox);
        vBox.getChildren().addAll(hBox, VBoxFx.customVBox(invoiceDTO));
        return vBox;
    }


}
