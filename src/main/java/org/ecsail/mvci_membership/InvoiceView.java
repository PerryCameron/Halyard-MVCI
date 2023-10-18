package org.ecsail.mvci_membership;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.InvoiceItemDTO;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.VBoxFx;

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
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2),"custom-tap-pane-frame",false); // makes outer border
        return vBox;
    }

    private Node showCommittedInvoice() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2),"custom-tap-pane-frame",false); // makes outer border
        vBox.getChildren().add(HBoxFx.customHBoxHeader());
        for(InvoiceItemDTO item: invoiceDTO.getItemDTOS()) {
            if(!item.getValue().equals("0.00"))
            vBox.getChildren().add(HBoxFx.customHBox(item));
        }
        return vBox;
    }
}
