package org.ecsail.mvci_membership;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.PaymentDTO;
import org.ecsail.widgetfx.TableViewFx;

import java.util.ArrayList;
import java.util.List;

public class InvoiceFooter implements Builder<Region> {

    private final InvoiceView invoiceView;
    private final InvoiceDTO invoiceDTO;

    public InvoiceFooter(InvoiceView invoiceView) {
        this.invoiceView = invoiceView;
        this.invoiceDTO = invoiceView.getInvoiceDTO();
    }

    @Override
    public Region build() {
        VBox vBox = new VBox();

        return vBox;
    }

    private TableView<PaymentDTO> createTableView() {
        TableView<PaymentDTO> tableView = TableViewFx.tableViewOf(PaymentDTO.class, 115);
//        tableView.setItems();
        List<TableColumn<PaymentDTO, ?>> columns = new ArrayList<>();
        return tableView;
    }
}
