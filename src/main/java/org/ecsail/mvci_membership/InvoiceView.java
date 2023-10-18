package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.InvoiceItemDTO;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.ListenerFx;
import org.ecsail.widgetfx.TableViewFx;
import org.ecsail.widgetfx.VBoxFx;

import java.time.Year;

public class InvoiceView implements Builder<Tab> {
    private final MembershipView membershipView;
    private final InvoiceDTO invoiceDTO;
    public InvoiceView(MembershipView membershipView) {
        this.membershipView = membershipView;
        this.invoiceDTO = membershipView.getMembershipModel().getSelectedInvoice();
    }

    @Override
    public Tab build() {
        Tab tab = new Tab();
        tab.setText(String.valueOf(invoiceDTO.getYear()));
        if(invoiceDTO.isCommitted()) tab.setContent(showInvoice());
        else tab.setContent(showEditableInvoice());
        return tab;
    }

    private Node showEditableInvoice() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2),"custom-tap-pane-frame",false); // makes outer border
        return vBox;
    }

    private Node showInvoice() {
        System.out.println("showInvoice() dto=" + invoiceDTO.getItemDTOS().size());
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2),"custom-tap-pane-frame",false); // makes outer border
        vBox.getChildren().add(HBoxFx.customHBoxHeader());
        for(InvoiceItemDTO item: invoiceDTO.getItemDTOS()) {
            System.out.println("adding" + item.getFieldName());
            vBox.getChildren().add(HBoxFx.customHBox(item));
        }
        return vBox;
    }
}
