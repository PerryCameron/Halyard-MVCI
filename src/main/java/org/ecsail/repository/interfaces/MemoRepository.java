package org.ecsail.repository.interfaces;

import com.ecsail.dto.Memo2DTO;
import com.ecsail.dto.MemoDTO;
import com.ecsail.views.tabs.deposits.InvoiceWithMemberInfoDTO;

import java.util.List;

public interface MemoRepository {

    List<MemoDTO> getMemosByMsId(int ms_id);
    List<MemoDTO> getMemosByBoatId(int boat_id);
    MemoDTO getMemoByInvoiceIdAndCategory(InvoiceWithMemberInfoDTO invoice, String category);
    List<Memo2DTO> getAllMemosForTabNotes(String year, String category);

}
