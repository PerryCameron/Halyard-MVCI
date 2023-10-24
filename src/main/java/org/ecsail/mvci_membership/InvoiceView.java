package org.ecsail.mvci_membership;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
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

    private ChangeListener<Boolean> getDataLoadedListener(Tab tab) { // used on load to determine what is displayed
        return ListenerFx.createSingleUseListener(invoiceDTO.listLoadedProperty(), () -> {  // data is loaded
            if (invoiceDTO.isCommitted()) tab.setContent(showCommittedInvoice()); // is committed no need to load more data
            else if (invoiceDTO.getFeeDTOS().isEmpty()) { // invoice is not committed and (feeDTO and DbInvoiceDTO) are not populated
                invoiceDTO.feesLoadedProperty().addListener(
                        ListenerFx.createSingleUseListener(invoiceDTO.feesLoadedProperty(), () ->
                    tab.setContent(showEditableInvoice())));
                membershipView.sendMessage().accept(MembershipMessage.LOAD_FEES);
            } else { // is not committed but data is already loaded (feeDTO and DbInvoiceDTO)
                tab.setContent(showEditableInvoice());
            }
        });
    }

    private Node showEditableInvoice() {
        ScrollPane scrollPane = PaneFx.scrollPaneOf();
        VBox vBox = VBoxFx.vBoxOf(new Insets(2,2,2,2)); // makes outer border
        scrollPane.setContent(vBox);
        vBox.getStyleClass().add("standard-box");
        vBox.getChildren().addAll(HBoxFx.customHBoxHeader(false),RegionFx.regionHeightOf(10.0));
        return vBox;
    }

    private Node showCommittedInvoice() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(10,0,0,0)); // makes outer border
        vBox.getStyleClass().add("standard-box");
        vBox.getChildren().addAll(HBoxFx.customHBoxHeader(true),RegionFx.regionHeightOf(10.0));
        invoiceDTO.getItemDTOS().stream()
                .filter(item -> !item.getValue().equals("0.00"))
                .map(HBoxFx::customHBox)
                .forEach(vBox.getChildren()::add);
        Separator separator = new Separator(Orientation.HORIZONTAL);
        vBox.getChildren().addAll(RegionFx.regionHeightOf(20.0),separator);
        HBox hBox = HBoxFx.hBoxOf(new Insets(10,15,5,0),10);
        VBox buttonBox = VBoxFx.vBoxOf(10.0, new Insets(25,5,5,20));
        buttonBox.getChildren().addAll(
                ButtonFx.buttonOf("Add Note",100, () ->
                        membershipView.sendMessage().accept(MembershipMessage.INSERT_INVOICE_NOTE)),
                ButtonFx.buttonOf("Edit",100, () -> System.out.println("Edit")));
        hBox.getChildren().addAll(HBoxFx.customHBox(invoiceDTO),buttonBox);
        vBox.getChildren().addAll(hBox, VBoxFx.customVBox(invoiceDTO));
        return vBox;
    }
}
