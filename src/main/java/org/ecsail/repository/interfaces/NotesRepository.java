package org.ecsail.repository.interfaces;



import org.ecsail.fx.InvoiceWithMemberInfoDTO;
import org.ecsail.fx.Memo2DTOFx;
import org.ecsail.fx.NotesDTOFx;

import java.util.List;

public interface NotesRepository {

    List<NotesDTOFx> getMemosByMsId(int ms_id);
    List<NotesDTOFx> getMemosByBoatId(int boat_id);
    NotesDTOFx getMemoByInvoiceIdAndCategory(InvoiceWithMemberInfoDTO invoice, String category);
    List<Memo2DTOFx> getAllMemosForTabNotes(String year, String category);
    int insertNote(NotesDTOFx notesDTO);
    int update(NotesDTOFx notesDTO);
    int delete(NotesDTOFx notesDTO);

    int deleteNotes(int msId);
}
