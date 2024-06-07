package org.ecsail.repository.interfaces;



import org.ecsail.dto.InvoiceWithMemberInfoDTO;
import org.ecsail.dto.Memo2DTO;
import org.ecsail.dto.NotesDTO;

import java.util.List;

public interface NotesRepository {

    List<NotesDTO> getMemosByMsId(int ms_id);
    List<NotesDTO> getMemosByBoatId(int boat_id);
    NotesDTO getMemoByInvoiceIdAndCategory(InvoiceWithMemberInfoDTO invoice, String category);
    List<Memo2DTO> getAllMemosForTabNotes(String year, String category);
    int insertNote(NotesDTO notesDTO);
    int update(NotesDTO notesDTO);
    int delete(NotesDTO notesDTO);

    int deleteNotes(int msId);
}
