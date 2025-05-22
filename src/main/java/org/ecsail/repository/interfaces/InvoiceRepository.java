package org.ecsail.repository.interfaces;

import org.ecsail.fx.*;

import java.util.List;
import java.util.Set;

public interface InvoiceRepository {
    List<InvoiceDTOFx> getInvoicesByMsid(int ms_id);
    List<InvoiceDTOFx> getAllInvoices();
    List<InvoiceWithMemberInfoDTO> getInvoicesWithMembershipInfoByDeposit(DepositDTO d);
    List<InvoiceWithMemberInfoDTO> getInvoicesWithMembershipInfoByYear(String year);
    List<InvoiceItemDTO> getInvoiceItemsByInvoiceId(int id);
    List<PaymentDTO> getPaymentByInvoiceId(int id);
    int insert(PaymentDTO paymentDTO);
    int insert(InvoiceDTOFx invoiceDTO);
    int getBatchNumber(String year);
    List<FeeDTO> getFeesFromYear(int year);
    List<DbInvoiceDTO> getDbInvoiceByYear(int year);
    int update(InvoiceDTOFx invoiceDTO);
    int[] updateBatch(InvoiceDTOFx invoiceDTO);
    int[] insertBatch(InvoiceDTOFx invoiceDTO);
    int update(PaymentDTO paymentDTO);
    int delete(PaymentDTO paymentDTO);
    boolean exists(MembershipListDTO membershipListDTO, int year);
    Set<FeeDTO> getRelatedFeesAsInvoiceItems(DbInvoiceDTO dbInvoiceDTO);

    int deletePaymentsByInvoiceID(InvoiceDTOFx invoiceDTO);

    int deleteItemsByInvoiceID(InvoiceDTOFx invoiceDTO);
    int delete(InvoiceDTOFx invoiceDTO);

    int deletePaymentByInvoiceID(int invoiceId);

    int deleteInvoiceItemByInvoiceID(int invoiceId);

    int deleteInvoiceByID(int invoiceId);

    int[] deleteAllPaymentsAndInvoicesByMsId(int msId);
}
