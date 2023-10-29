package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.ecsail.dto.DbInvoiceDTO;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.InvoiceItemDTO;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.LabelFx;
import org.ecsail.widgetfx.VBoxFx;

import java.math.BigDecimal;
import java.util.ArrayList;

public class InvoiceItemGroup extends HBox {
    private final InvoiceDTO invoiceDTO;
    private final InvoiceView invoiceView;
    private final DbInvoiceDTO dbInvoiceDTO;
    private final InvoiceItemDTO invoiceItemDTO;
    private final ArrayList<InvoiceItemDTO> subItems = new ArrayList<>();

    public InvoiceItemGroup(InvoiceView invoiceView, DbInvoiceDTO dbInvoiceDTO) {
        this.invoiceView = invoiceView;
        this.invoiceDTO = invoiceView.getInvoiceDTO();
        this.dbInvoiceDTO = dbInvoiceDTO;
        this.invoiceItemDTO = getInvoiceItem();
        this.getStyleClass().add("standard-box");
        VBox vBox = VBoxFx.vBoxOf(5.0, new Insets(5, 0, 3, 0));
        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER_RIGHT, 120.0, 5);  // width should match vbox5 in invoiceItemRow
        hBox.getChildren().add(LabelFx.labelOf(invoiceItemDTO.getValue(), "standard-black-label", invoiceItemDTO.valueProperty()));
        getChildren().addAll(vBox, hBox);
        invoiceDTO.getInvoiceItemDTOS().stream()
                .filter(i -> i.getCategoryItem().equals(dbInvoiceDTO.getFieldName()))
                .forEach(i -> {
                    subItems.add(i);
                    vBox.getChildren().add(new InvoiceItemRow(i, this));
                });
    }

    private InvoiceItemDTO getInvoiceItem() {
        return invoiceDTO.getInvoiceItemDTOS()
                .stream()
                .filter(i -> dbInvoiceDTO.getFieldName().equals(i.getFieldName()))
                .findFirst()
                .orElse(null);
    }

    public void updateGroupValues() {
        invoiceItemDTO.setValue(updateGroupTotal());
        invoiceItemDTO.setQty(updateGroupQty());
    }

    private String updateGroupTotal() {
        BigDecimal total = subItems.stream()
                .map(i -> new BigDecimal(i.getValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.toString();
    }

    private int updateGroupQty() {
        return subItems.stream()
                .mapToInt(InvoiceItemDTO::getQty)
                .sum();
    }

    public InvoiceDTO getInvoiceDTO() {
        return invoiceDTO;
    }

    public DbInvoiceDTO getDbInvoiceDTO() {
        return dbInvoiceDTO;
    }

    public InvoiceView getInvoiceView() {
        return invoiceView;
    }

    public InvoiceItemDTO getInvoiceItemDTO() {
        return invoiceItemDTO;
    }
}
