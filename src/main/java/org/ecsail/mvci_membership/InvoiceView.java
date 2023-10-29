package org.ecsail.mvci_membership;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.DbInvoiceDTO;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.widgetfx.*;

import java.util.Comparator;

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
        ScrollPane scrollPane = PaneFx.scrollPaneOf();
        scrollPane.getStyleClass().add("scroll-pane-border");
        ChangeListener<Boolean> dataLoadedListener =
                ListenerFx.createSingleUseListener(invoiceDTO.listLoadedProperty(), () -> {  // data is loaded
            if (invoiceDTO.isCommitted()) tab.setContent(showCommittedInvoice()); // is committed no need to load more data
            else if (invoiceDTO.getFeeDTOS().isEmpty()) { // invoice is not committed and (feeDTO and DbInvoiceDTO) are not populated
                invoiceDTO.feesLoadedProperty().addListener(
                        ListenerFx.createSingleUseListener(invoiceDTO.feesLoadedProperty(), () ->
                                scrollPane.setContent(showEditableInvoice())));
                membershipView.sendMessage().accept(MembershipMessage.LOAD_FEES);
            } else { // is not committed but data is already loaded (feeDTO and DbInvoiceDTO)
                scrollPane.setContent(showEditableInvoice());
            }
        });
        tab.setContent(scrollPane);
        invoiceDTO.listLoadedProperty().addListener(dataLoadedListener);
        return tab;
    }

    private Node showEditableInvoice() {
        VBox vBox = VBoxFx.vBoxOf(5.0,new Insets(10,0,0,15)); // makes outer border
        vBox.getChildren().add(HBoxFx.customHBoxHeader(false));
        invoiceDTO.getDbInvoiceDTOS().sort(Comparator.comparing(DbInvoiceDTO::getOrder).reversed());
        for (DbInvoiceDTO dbInvoiceDTO : invoiceDTO.getDbInvoiceDTOS()) {
            if(dbInvoiceDTO.isItemized()) {
                TitledPane titledPane = new TitledPane(dbInvoiceDTO.getFieldName(), new InvoiceItemGroup(this, dbInvoiceDTO));
                titledPane.getStyleClass().add("custom-title-pane");
                titledPane.setExpanded(false);
                vBox.getChildren().add(titledPane);
            } else
            vBox.getChildren().add(new InvoiceItemRow(invoiceDTO, dbInvoiceDTO, this));
        }
        vBox.getChildren().add(new InvoiceFooter(this).build());
        return vBox;
    }

    private Node showCommittedInvoice() {
        VBox vBox = VBoxFx.vBoxOf(new Insets(10,15,0,15)); // makes outer border
        vBox.getStyleClass().add("standard-bordered-box");
        vBox.getChildren().addAll(HBoxFx.customHBoxHeader(true),RegionFx.regionHeightOf(10.0));
        invoiceDTO.getInvoiceItemDTOS().stream()
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

    public InvoiceDTO getInvoiceDTO() {
        return invoiceDTO;
    }

    public MembershipView getMembershipView() {
        return membershipView;
    }
}
