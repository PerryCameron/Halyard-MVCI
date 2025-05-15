package org.ecsail.repository.interfaces;



import org.ecsail.dto.InvoiceWithMemberInfoDTO;
import org.ecsail.dto.Memo2DTO;
import org.ecsail.dto.NotesDTOFx;

import java.util.List;

public interface NotesRepository {

    List<NotesDTOFx> getMemosByMsId(int ms_id);
    List<NotesDTOFx> getMemosByBoatId(int boat_id);
    NotesDTOFx getMemoByInvoiceIdAndCategory(InvoiceWithMemberInfoDTO invoice, String category);
    List<Memo2DTO> getAllMemosForTabNotes(String year, String category);
    int insertNote(NotesDTOFx notesDTO);
    int update(NotesDTOFx notesDTO);
    int delete(NotesDTOFx notesDTO);

    int deleteNotes(int msId);
}
