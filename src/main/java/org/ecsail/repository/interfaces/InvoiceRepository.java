package org.ecsail.repository.interfaces;

import org.ecsail.dto.DepositDTO;
import org.ecsail.dto.InvoiceDTO;
import org.ecsail.dto.InvoiceWithMemberInfoDTO;

import java.util.List;

public interface InvoiceRepository {
    List<InvoiceDTO> getInvoicesByMsid(int ms_id);
    List<InvoiceDTO> getAllInvoices();
    List<InvoiceWithMemberInfoDTO> getInvoicesWithMembershipInfoByDeposit(DepositDTO d);
    List<InvoiceWithMemberInfoDTO> getInvoicesWithMembershipInfoByYear(String year);
    int getBatchNumber(String year);

}
