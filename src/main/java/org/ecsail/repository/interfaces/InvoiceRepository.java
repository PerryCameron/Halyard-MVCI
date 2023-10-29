package org.ecsail.repository.interfaces;

import org.ecsail.dto.*;

import java.util.List;

public interface InvoiceRepository {
    List<InvoiceDTO> getInvoicesByMsid(int ms_id);
    List<InvoiceDTO> getAllInvoices();
    List<InvoiceWithMemberInfoDTO> getInvoicesWithMembershipInfoByDeposit(DepositDTO d);
    List<InvoiceWithMemberInfoDTO> getInvoicesWithMembershipInfoByYear(String year);
    List<InvoiceItemDTO> getInvoiceItemsByInvoiceId(int id);
    List<PaymentDTO> getPaymentByInvoiceId(int id);
    int getBatchNumber(String year);
    List<FeeDTO> getFeesFromYear(int year);
    List<DbInvoiceDTO> getDbInvoiceByYear(int year);
    int update(InvoiceItemDTO invoiceItemDTO);
    int update(InvoiceDTO invoiceDTO);

}
