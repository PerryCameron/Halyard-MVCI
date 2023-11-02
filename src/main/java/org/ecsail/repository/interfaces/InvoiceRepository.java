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
    int insert(PaymentDTO paymentDTO);
    int getBatchNumber(String year);
    List<FeeDTO> getFeesFromYear(int year);
    List<DbInvoiceDTO> getDbInvoiceByYear(int year);
    int update(InvoiceDTO invoiceDTO);
    int[] updateBatch(InvoiceDTO invoiceDTO);
    int update(PaymentDTO paymentDTO);
    int delete(PaymentDTO paymentDTO);
    boolean exists(MembershipListDTO membershipListDTO, int year);
}
