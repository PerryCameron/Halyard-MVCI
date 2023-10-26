package org.ecsail.mvci_membership;

import javafx.scene.layout.VBox;
import org.ecsail.dto.DbInvoiceDTO;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.InvoiceItemDTO;

import java.util.ArrayList;

public class InvoiceItemGroup extends VBox {
    private InvoiceDTO invoiceDTO;
    private DbInvoiceDTO dbInvoiceDTO;
    private final ArrayList<InvoiceItemDTO> subItems = new ArrayList<>();
    public InvoiceItemGroup(InvoiceDTO invoiceDTO, DbInvoiceDTO dbInvoiceDTO) {
        this.invoiceDTO = invoiceDTO;
        this.dbInvoiceDTO = dbInvoiceDTO;
        for(InvoiceItemDTO i: invoiceDTO.getInvoiceItemDTOS()) {
            if(i.getCategory().equals(dbInvoiceDTO.getFieldName())) {
                subItems.add(i);
                getChildren().add(new InvoiceItemRow(i, this));
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
