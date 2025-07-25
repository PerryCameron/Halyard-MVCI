package org.ecsail.repository.interfaces;



import org.ecsail.fx.InvoiceWithMemberInfoDTO;
import org.ecsail.fx.Memo2Fx;
import org.ecsail.fx.NotesFx;

import java.util.List;

public interface NotesRepository {

    List<NotesFx> getMemosByMsId(int ms_id);
    List<NotesFx> getMemosByBoatId(int boat_id);
    NotesFx getMemoByInvoiceIdAndCategory(InvoiceWithMemberInfoDTO invoice, String category);
    List<Memo2Fx> getAllMemosForTabNotes(String year, String category);
    int insertNote(NotesFx notesDTO);
    int update(NotesFx notesDTO);
    int delete(NotesFx notesDTO);

    int deleteNotes(int msId);
}
