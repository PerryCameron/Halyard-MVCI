package org.ecsail.mvci_membership;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.ecsail.dto.DbInvoiceDTO;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.InvoiceItemDTO;
import org.ecsail.widgetfx.HBoxFx;
import org.ecsail.widgetfx.VBoxFx;

import java.util.ArrayList;

public class InvoiceItemGroup extends HBox {
    private InvoiceDTO invoiceDTO;
    private DbInvoiceDTO dbInvoiceDTO;
    private final ArrayList<InvoiceItemDTO> subItems = new ArrayList<>();
    public InvoiceItemGroup(InvoiceDTO invoiceDTO, DbInvoiceDTO dbInvoiceDTO) {
        this.invoiceDTO = invoiceDTO;
        this.dbInvoiceDTO = dbInvoiceDTO;
//        setSpacing(5);
        VBox vBox = VBoxFx.vBoxOf(5.0, new Insets(5,0,3,0));
        HBox hBox = HBoxFx.hBoxOf(Pos.CENTER_RIGHT,70.0, 5);
        hBox.getChildren().add(new Label("0.00"));
        getChildren().addAll(vBox, hBox);
        for(InvoiceItemDTO i: invoiceDTO.getInvoiceItemDTOS()) {
            if(i.getCategory().equals(dbInvoiceDTO.getFieldName())) {
                subItems.add(i);
                vBox.getChildren().add(new InvoiceItemRow(i, this));
            }
        }
    }

    public InvoiceDTO getInvoiceDTO() {
        return invoiceDTO;
    }

    public void setInvoiceDTO(InvoiceDTO invoiceDTO) {
        this.invoiceDTO = invoiceDTO;
    }

    public DbInvoiceDTO getDbInvoiceDTO() {
        return dbInvoiceDTO;
    }

    public void setDbInvoiceDTO(DbInvoiceDTO dbInvoiceDTO) {
        this.dbInvoiceDTO = dbInvoiceDTO;
    }
}
