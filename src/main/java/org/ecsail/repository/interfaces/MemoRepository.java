package org.ecsail.repository.interfaces;



import org.ecsail.dto.InvoiceWithMemberInfoDTO;
import org.ecsail.dto.Memo2DTO;
import org.ecsail.dto.MemoDTO;

import java.util.List;

public interface MemoRepository {

    List<MemoDTO> getMemosByMsId(int ms_id);
    List<MemoDTO> getMemosByBoatId(int boat_id);
    MemoDTO getMemoByInvoiceIdAndCategory(InvoiceWithMemberInfoDTO invoice, String category);
    List<Memo2DTO> getAllMemosForTabNotes(String year, String category);

}
