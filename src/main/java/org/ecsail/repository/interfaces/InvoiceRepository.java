package org.ecsail.repository.interfaces;

import org.ecsail.fx.*;

import java.util.List;
import java.util.Set;

public interface InvoiceRepository {
    List<InvoiceFx> getInvoicesByMsid(int ms_id);
    List<InvoiceFx> getAllInvoices();
    List<InvoiceWithMemberInfoDTO> getInvoicesWithMembershipInfoByDeposit(DepositDTO d);
    List<InvoiceWithMemberInfoDTO> getInvoicesWithMembershipInfoByYear(String year);
    List<InvoiceItemDTO> getInvoiceItemsByInvoiceId(int id);
    List<PaymentDTO> getPaymentByInvoiceId(int id);
    int insert(PaymentDTO paymentDTO);
    int insert(InvoiceFx invoiceDTO);
    int getBatchNumber(String year);
    List<FeeDTO> getFeesFromYear(int year);
    List<DbInvoiceDTO> getDbInvoiceByYear(int year);
    int update(InvoiceFx invoiceDTO);
    int[] updateBatch(InvoiceFx invoiceDTO);
    int[] insertBatch(InvoiceFx invoiceDTO);
    int update(PaymentDTO paymentDTO);
    int delete(PaymentDTO paymentDTO);
    boolean exists(MembershipListDTO membershipListDTO, int year);
    Set<FeeDTO> getRelatedFeesAsInvoiceItems(DbInvoiceDTO dbInvoiceDTO);

    int deletePaymentsByInvoiceID(InvoiceFx invoiceDTO);

    int deleteItemsByInvoiceID(InvoiceFx invoiceDTO);
    int delete(InvoiceFx invoiceDTO);

    int deletePaymentByInvoiceID(int invoiceId);

    int deleteInvoiceItemByInvoiceID(int invoiceId);

    int deleteInvoiceByID(int invoiceId);

    int[] deleteAllPaymentsAndInvoicesByMsId(int msId);
}
