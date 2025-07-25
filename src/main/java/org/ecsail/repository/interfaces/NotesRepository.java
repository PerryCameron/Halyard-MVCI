package org.ecsail.repository.interfaces;



import org.ecsail.fx.InvoiceWithMemberInfoDTO;
import org.ecsail.fx.Memo2Fx;
import org.ecsail.fx.NoteFx;

import java.util.List;

public interface NotesRepository {

    List<NoteFx> getMemosByMsId(int ms_id);
    List<NoteFx> getMemosByBoatId(int boat_id);
    NoteFx getMemoByInvoiceIdAndCategory(InvoiceWithMemberInfoDTO invoice, String category);
    List<Memo2Fx> getAllMemosForTabNotes(String year, String category);
    int insertNote(NoteFx notesDTO);
    int update(NoteFx notesDTO);
    int delete(NoteFx notesDTO);

    int deleteNotes(int msId);
}
